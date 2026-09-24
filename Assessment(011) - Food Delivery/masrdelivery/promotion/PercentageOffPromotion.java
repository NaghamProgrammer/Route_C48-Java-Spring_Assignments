package masrdelivery.promotion;

import masrdelivery.model.District;
import masrdelivery.model.Money;

import java.time.LocalDate;

public class PercentageOffPromotion extends Promotion {
    private final double percentage; // e.g. 0.20 = 20% off
    private final Money cap;         // nullable = no cap

    public PercentageOffPromotion(String code, double percentage, Money cap, LocalDate expiry,
                                   Money minimumSubtotal, District district, boolean firstTimeOnly) {
        super(code, expiry, minimumSubtotal, district, firstTimeOnly);
        this.percentage = percentage;
        this.cap = cap;
    }

    @Override public Money subtotalDiscount(Money subtotal) {
        Money raw = subtotal.multiply(percentage);
        return cap == null ? raw : raw.min(cap);
    }
}
