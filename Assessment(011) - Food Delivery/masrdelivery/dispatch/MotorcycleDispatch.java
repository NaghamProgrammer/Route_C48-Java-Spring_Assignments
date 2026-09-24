package masrdelivery.dispatch;

public class MotorcycleDispatch implements DispatchBehavior {
    @Override public double rangeKm() { return 25; }
    @Override public double averageSpeedKmh() { return 35; }
    @Override public int maxOrderItems() { return 15; }
    @Override public String vehicleLabel() { return "Motorcycle"; }
}
