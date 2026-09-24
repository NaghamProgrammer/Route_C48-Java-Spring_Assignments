package masrdelivery.promotion;

import masrdelivery.model.District;
import masrdelivery.model.Money;

public final class PromotionContext {
    private final Money subtotal;
    private final District deliveryDistrict;
    private final boolean firstTimeCustomer;

    public PromotionContext(Money subtotal, District deliveryDistrict, boolean firstTimeCustomer) {
        this.subtotal = subtotal;
        this.deliveryDistrict = deliveryDistrict;
        this.firstTimeCustomer = firstTimeCustomer;
    }

    public Money getSubtotal() { return subtotal; }
    public District getDeliveryDistrict() { return deliveryDistrict; }
    public boolean isFirstTimeCustomer() { return firstTimeCustomer; }
}
