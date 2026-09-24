package masrdelivery.model;

//tier is always derived from completed order count, never set by hand.
public enum LoyaltyTier {
    BRONZE, SILVER, GOLD;

    public static LoyaltyTier fromCompletedOrders(int completedOrders) {
        if (completedOrders >= 30) return GOLD;
        if (completedOrders >= 10) return SILVER;
        return BRONZE;
    }

    // Multiplies the delivery fee: Silver = 10% off, Gold = fully waived, Bronze = no change.
    public double deliveryFeeMultiplier() {
        switch (this) {
            case SILVER: return 0.90;
            case GOLD:   return 0.0;
            default:     return 1.0;
        }
    }
}
