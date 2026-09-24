package masrdelivery.dispatch;

public class CarDispatch implements DispatchBehavior {
    @Override public double rangeKm() { return 40; }
    @Override public double averageSpeedKmh() { return 30; }
    @Override public int maxOrderItems() { return 40; }
    @Override public String vehicleLabel() { return "Car"; }
}
