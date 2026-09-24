package masrdelivery.dispatch;

public class BicycleDispatch implements DispatchBehavior {
    @Override public double rangeKm() { return 8; }
    @Override public double averageSpeedKmh() { return 15; }
    @Override public int maxOrderItems() { return 6; }
    @Override public String vehicleLabel() { return "Bicycle"; }
}
