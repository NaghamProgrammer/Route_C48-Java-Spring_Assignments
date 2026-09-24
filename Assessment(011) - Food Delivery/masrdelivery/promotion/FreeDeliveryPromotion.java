package masrdelivery.promotion;

import masrdelivery.model.District;
import masrdelivery.model.Money;

import java.time.LocalDate;

public class FreeDeliveryPromotion extends Promotion {
    public FreeDeliveryPromotion(String code, LocalDate expiry, Money minimumSubtotal,
                                  District district, boolean firstTimeOnly) {
        super(code, expiry, minimumSubtotal, district, firstTimeOnly);
    }

    @Override public Money subtotalDiscount(Money subtotal) { return Money.ZERO; }
    @Override public boolean waivesDelivery() { return true; }
}
