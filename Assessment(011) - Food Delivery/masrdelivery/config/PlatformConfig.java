package masrdelivery.config;

import masrdelivery.model.Money;


 // a single, globally reachable configuration, loaded exactly once. Implemented as an enum singleton rather than a lazy-holder
 // double-checked-locking class: enum instances are constructed exactly once by the classloader (thread-safe), cannot be re-created by reflection the way a plain singleton can,
 // and singleton is a global, which makes it harder to substitute a different configuration in a unit test than passing config through constructors (dependency injection) would.
 // That cost is acceptable here because the whole application is one console process with one configuration for its entire lifetime

public enum PlatformConfig {
    INSTANCE;

    private final Money baseDeliveryFee = Money.of(15.0);
    private final Money perKmBeyondFirst3 = Money.of(3.0);
    private final double freeKm = 3.0;
    private final double serviceFeeRate = 0.10;
    private final int adminPinAttempts = 3;
    private final String dataFilePath = "data/masrdelivery.json"; // simulated location for console demo

    public Money getBaseDeliveryFee() { return baseDeliveryFee; }
    public Money getPerKmBeyondFirst3() { return perKmBeyondFirst3; }
    public double getFreeKm() { return freeKm; }
    public double getServiceFeeRate() { return serviceFeeRate; }
    public int getAdminPinAttempts() { return adminPinAttempts; }
    public String getDataFilePath() { return dataFilePath; }
}
