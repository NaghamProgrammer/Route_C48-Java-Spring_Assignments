package masrdelivery.observer;

import masrdelivery.model.Order;
import masrdelivery.model.OrderStatus;

public class StatisticsRecalculator implements OrderStatusListener {
    @Override public void onStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        System.out.println("  [stats] recalculating platform statistics after order #" + order.getId());
    }
}
