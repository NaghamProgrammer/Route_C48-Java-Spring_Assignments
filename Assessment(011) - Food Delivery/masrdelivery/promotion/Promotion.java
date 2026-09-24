package masrdelivery.promotion;

import masrdelivery.exceptions.PromotionException;
import masrdelivery.model.District;
import masrdelivery.model.Money;

import java.time.LocalDate;


//strategy for pricing a promotion. PricingService calls
// subtotalDiscount() polymorphically and never switches on a promotion
// "type", a new promotion kind is a new subclass, nothing else changes.

public abstract class Promotion {
    private final String code;
    private final LocalDate expiry;              // nullable = never expires
    private final Money minimumSubtotal;          // nullable
    private final District restrictedToDistrict;  // nullable
    private final boolean firstTimeOnly;

    protected Promotion(String code, LocalDate expiry, Money minimumSubtotal,
                         District restrictedToDistrict, boolean firstTimeOnly) {
        this.code = code.toUpperCase();
        this.expiry = expiry;
        this.minimumSubtotal = minimumSubtotal;
        this.restrictedToDistrict = restrictedToDistrict;
        this.firstTimeOnly = firstTimeOnly;
    }

    public String getCode() { return code; }

    //Shared conditions (Part B): min subtotal, expiry, district, first-time-only
    public final void validate(PromotionContext ctx, LocalDate today) {
        if (expiry != null && today.isAfter(expiry)) {
            throw new PromotionException("Promotion " + code + " expired on " + expiry);
        }
        if (minimumSubtotal != null && ctx.getSubtotal().compareTo(minimumSubtotal) < 0) {
            throw new PromotionException("Promotion " + code + " requires a minimum subtotal of " + minimumSubtotal);
        }
        if (restrictedToDistrict != null && ctx.getDeliveryDistrict() != restrictedToDistrict) {
            throw new PromotionException("Promotion " + code + " only applies to deliveries in " + restrictedToDistrict);
        }
        if (firstTimeOnly && !ctx.isFirstTimeCustomer()) {
            throw new PromotionException("Promotion " + code + " is only valid for first-time customers");
        }
    }

    //Discount applied to the subtotal only (never to fees) (Part B.4)
    public abstract Money subtotalDiscount(Money subtotal);

    //Whether this promotion also waives the delivery fee entirely
    public boolean waivesDelivery() { return false; }
}
