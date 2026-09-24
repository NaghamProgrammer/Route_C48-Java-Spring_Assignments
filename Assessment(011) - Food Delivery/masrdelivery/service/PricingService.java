package masrdelivery.service;

import masrdelivery.config.PlatformConfig;
import masrdelivery.model.District;
import masrdelivery.model.LineItem;
import masrdelivery.model.LoyaltyTier;
import masrdelivery.model.Money;
import masrdelivery.promotion.Promotion;
import masrdelivery.promotion.PromotionContext;

import java.time.LocalDate;
import java.util.List;

/** Part B - implements the five pricing steps, in order, exactly as specified. */
public final class PricingService {
    private PricingService() {}

    public static final class Breakdown {
        public final Money subtotal, deliveryFee, serviceFee, promotionDiscount, total;
        Breakdown(Money subtotal, Money deliveryFee, Money serviceFee, Money promotionDiscount, Money total) {
            this.subtotal = subtotal;
            this.deliveryFee = deliveryFee;
            this.serviceFee = serviceFee;
            this.promotionDiscount = promotionDiscount;
            this.total = total;
        }
    }

    public static Breakdown price(List<LineItem> lineItems, District restaurantDistrict, District deliveryDistrict,
                                   LoyaltyTier tier, Promotion promotion, boolean firstTimeCustomer, LocalDate today) {
        PlatformConfig cfg = PlatformConfig.INSTANCE;

        // 1. Subtotal - sum of every line item (each item type prices itself polymorphically)
        Money subtotal = Money.ZERO;
        for (LineItem li : lineItems) subtotal = subtotal.add(li.lineTotal());

        // 2. Delivery fee - 15 EGP base + 3 EGP/km beyond the first 3km, then the loyalty benefit
        double distance = restaurantDistrict.distanceTo(deliveryDistrict);
        double billableKm = Math.max(0, distance - cfg.getFreeKm());
        Money deliveryFee = cfg.getBaseDeliveryFee().add(cfg.getPerKmBeyondFirst3().multiply(billableKm));
        deliveryFee = deliveryFee.multiply(tier.deliveryFeeMultiplier());

        // 3. Service fee - 10% of the subtotal (Money rounds to the nearest piastre automatically)
        Money serviceFee = subtotal.multiply(cfg.getServiceFeeRate());

        // 4. Promotion - applied to the subtotal only, never to the fees
        Money promotionDiscount = Money.ZERO;
        if (promotion != null) {
            PromotionContext ctx = new PromotionContext(subtotal, deliveryDistrict, firstTimeCustomer);
            promotion.validate(ctx, today);
            promotionDiscount = promotion.subtotalDiscount(subtotal);
            if (promotion.waivesDelivery()) deliveryFee = Money.ZERO;
        }

        // 5. Total - subtotal + delivery fee + service fee - promotion discount, floored at zero
        Money total = subtotal.add(deliveryFee).add(serviceFee).subtract(promotionDiscount);

        return new Breakdown(subtotal, deliveryFee, serviceFee, promotionDiscount, total);
    }
}
