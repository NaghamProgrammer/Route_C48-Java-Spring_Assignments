package masrdelivery.promotion;

import masrdelivery.model.District;
import masrdelivery.model.Money;

import java.time.LocalDate;

public class FixedAmountOffPromotion extends Promotion {
    private final Money amountOff;

    public FixedAmountOffPromotion(String code, Money amountOff, LocalDate expiry, Money minimumSubtotal,
                                    District district, boolean firstTimeOnly) {
        super(code, expiry, minimumSubtotal, district, firstTimeOnly);
        this.amountOff = amountOff;
    }

    @Override public Money subtotalDiscount(Money subtotal) { return amountOff.min(subtotal); }
}
