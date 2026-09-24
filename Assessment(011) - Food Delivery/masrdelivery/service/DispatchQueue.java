package masrdelivery.service;

import masrdelivery.model.LoyaltyTier;
import masrdelivery.model.Order;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Part C.5 - a priority queue of READY-but-unassigned orders: Gold
 * customers' orders always sort ahead of every Bronze/Silver order,
 * and within the same priority band the order waiting longest (earlier
 * readyAt) comes first.
 */
public class DispatchQueue {
    private final PriorityQueue<Order> readyOrders = new PriorityQueue<>(
            Comparator.<Order>comparingInt(o -> o.getCustomer().getTier() == LoyaltyTier.GOLD ? 0 : 1)
                    .thenComparing(Order::getReadyAt));

    public void enqueue(Order order) { readyOrders.add(order); }
    public Order pollNext() { return readyOrders.poll(); }
    public boolean isEmpty() { return readyOrders.isEmpty(); }
    public int size() { return readyOrders.size(); }
}
