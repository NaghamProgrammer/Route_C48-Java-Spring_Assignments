package masrdelivery.dispatch;


// strategy interface for how a rider's vehicle affects
//dispatch (range, speed, how large an order it can carry). Adding a
//new vehicle (electric scooter) means writing one new class, nothing
//in Rider or the dispatch service changes.

public interface DispatchBehavior {
    double rangeKm();
    double averageSpeedKmh();
    int maxOrderItems();
    String vehicleLabel();
}
