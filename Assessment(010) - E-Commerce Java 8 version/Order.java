import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Order {
    private int orderId;
    private String customerName;
    private List<CartItem> items;
    private double total;
    private OrderStatus orderStatus;


    public Order(int orderId, String customerName, double total, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.items = new ArrayList<>();
        this.total = 0.0;
        this.orderStatus = OrderStatus.pending;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }


    public int getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public boolean addItem(Product product, int quantity) {
        if (orderStatus != OrderStatus.pending) {
            System.out.println("Cannot modify order #" + orderId + " — it is already " + orderStatus);
            return false;
        }
        if (product == null || quantity <= 0) {
            System.out.println("Invalid product or quantity");
            return false;
        }

        Optional<CartItem> existing = items.stream()
                .filter(item -> item.getProduct().getId() == product.getId())
                .findFirst();

        if (existing.isPresent()) {
            existing.get().increaseQuantity(quantity);
        } else {
            items.add(new CartItem(product, quantity));
        }
        calculateTotal();
        return true;
    }

    public boolean removeItem(int productId) {
        if (orderStatus != OrderStatus.pending) {
            System.out.println("Cannot modify order #" + orderId + "  it is already " + orderStatus);
            return false;
        }

        boolean removed = items.removeIf(item -> item.getProduct().getId() == productId);
        if (removed) {
            calculateTotal();
        } else {
            System.out.println("Product " + productId + " is not in order #" + orderId);
        }
        return removed;
    }

    private void calculateTotal() {
        this.total = items.stream()
                .mapToDouble(CartItem::calculateSubtotal)
                .sum();
    }

    public void displayOrder() {
        System.out.println("Order #" + orderId + "  |  Customer: " + customerName + "  |  Status: " + orderStatus);
        if (items.isEmpty()) {
            System.out.println("no items yet");
        } else {
            items.forEach(item -> System.out.println(item.getProduct().getName()
                    + "  x" + item.getQuantity()
                    + "  subtotal: " + item.calculateSubtotal()));
        }
        System.out.println("Total: " + total);
    }
}