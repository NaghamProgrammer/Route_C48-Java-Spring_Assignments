package masrdelivery.observer;

import masrdelivery.model.Order;
import masrdelivery.model.OrderStatus;

public class CustomerNotifier implements OrderStatusListener {
    @Override public void onStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        System.out.println("  [notify-customer] Order #" + order.getId() + " for " + order.getCustomer().getName()
                + " is now " + newStatus);
    }
}
