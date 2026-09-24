package masrdelivery.observer;

import masrdelivery.model.Order;
import masrdelivery.model.OrderStatus;

//observer of order status changes. Adding a fourth reaction is one new implementation
public interface OrderStatusListener {
    void onStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus);
}
