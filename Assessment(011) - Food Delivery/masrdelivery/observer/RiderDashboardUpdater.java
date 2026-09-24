package masrdelivery.observer;

import masrdelivery.model.Order;
import masrdelivery.model.OrderStatus;

public class RiderDashboardUpdater implements OrderStatusListener {
    @Override public void onStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        System.out.println("  [rider-dashboard] Order #" + order.getId() + " " + oldStatus + " -> " + newStatus);
    }
}
