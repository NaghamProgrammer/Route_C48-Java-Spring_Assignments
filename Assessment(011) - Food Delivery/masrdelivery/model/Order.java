package masrdelivery.model;

import masrdelivery.exceptions.ValidationException;
import masrdelivery.observer.OrderStatusListener;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private final int id;
    private final Customer customer;
    private final Restaurant restaurant;
    private final Address deliveryAddress;
    private final List<LineItem> lineItems;
    private final String promotionCode; // nullable
    private final String notes;
    private final LocalDateTime placedAt;

    private OrderStatus status = OrderStatus.PLACED;
    private Rider assignedRider;
    private LocalDateTime readyAt;
    private LocalDateTime deliveredAt;

    private Money subtotal;
    private Money deliveryFee;
    private Money serviceFee;
    private Money promotionDiscount = Money.ZERO;
    private Money total;

    private final List<OrderStatusListener> listeners = new ArrayList<>(); // Part G.4 - observer subject

    public Order(int id, Customer customer, Restaurant restaurant, Address deliveryAddress,
                 List<LineItem> lineItems, String promotionCode, String notes, LocalDateTime placedAt) {
        if (lineItems == null || lineItems.isEmpty())
            throw new ValidationException("An order must contain at least one line item");
        this.id = id;
        this.customer = customer;
        this.restaurant = restaurant;
        this.deliveryAddress = deliveryAddress;
        this.lineItems = List.copyOf(lineItems);
        this.promotionCode = promotionCode;
        this.notes = notes;
        this.placedAt = placedAt;
    }

    public void addListener(OrderStatusListener listener) { listeners.add(listener); }

    public void transitionTo(OrderStatus target) {
        status.assertCanTransitionTo(target);
        OrderStatus old = status;
        status = target;
        if (target == OrderStatus.READY) readyAt = LocalDateTime.now();
        if (target == OrderStatus.DELIVERED) deliveredAt = LocalDateTime.now();
        for (OrderStatusListener l : listeners) l.onStatusChanged(this, old, target);
    }

    public long deliveryDurationMinutes() {
        if (deliveredAt == null) return -1;
        return Duration.between(placedAt, deliveredAt).toMinutes();
    }

    public void applyPricing(Money subtotal, Money deliveryFee, Money serviceFee, Money promotionDiscount, Money total) {
        this.subtotal = subtotal;
        this.deliveryFee = deliveryFee;
        this.serviceFee = serviceFee;
        this.promotionDiscount = promotionDiscount;
        this.total = total;
    }

    public void assignRider(Rider rider) { this.assignedRider = rider; }

    public int getId() { return id; }
    public Customer getCustomer() { return customer; }
    public Restaurant getRestaurant() { return restaurant; }
    public Address getDeliveryAddress() { return deliveryAddress; }
    public List<LineItem> getLineItems() { return lineItems; } // already immutable via List.copyOf
    public String getPromotionCode() { return promotionCode; }
    public String getNotes() { return notes; }
    public LocalDateTime getPlacedAt() { return placedAt; }
    public LocalDateTime getReadyAt() { return readyAt; }
    public OrderStatus getStatus() { return status; }
    public Rider getAssignedRider() { return assignedRider; }
    public Money getSubtotal() { return subtotal; }
    public Money getDeliveryFee() { return deliveryFee; }
    public Money getServiceFee() { return serviceFee; }
    public Money getPromotionDiscount() { return promotionDiscount; }
    public Money getTotal() { return total; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        return id == ((Order) o).id;
    }
    @Override public int hashCode() { return Integer.hashCode(id); }
    @Override public String toString() { return "Order#" + id + " [" + status + "] total=" + total; }
}
