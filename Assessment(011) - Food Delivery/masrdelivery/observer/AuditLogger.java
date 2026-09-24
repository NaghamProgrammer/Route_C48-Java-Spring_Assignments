package masrdelivery.observer;

import masrdelivery.model.Order;
import masrdelivery.model.OrderStatus;

import java.time.LocalDateTime;

public class AuditLogger implements OrderStatusListener {
    @Override public void onStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        System.out.println("  [audit] " + LocalDateTime.now() + " order#" + order.getId()
                + " " + oldStatus + " -> " + newStatus);
    }
}
