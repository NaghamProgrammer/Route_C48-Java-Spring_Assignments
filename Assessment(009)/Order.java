import java.util.ArrayList;
import java.util.List;
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
            System.out.println("Cannot modify order #" + orderId + " — it is already " + orderStatus + ".");
            return false;
        }
        if (product == null || quantity <= 0) {
            System.out.println("Invalid product or quantity.");
            return false;
        }

        for (CartItem item : items) {
            if (item.getProduct().getId() == product.getId()) {
                item.increaseQuantity(quantity);
                calculateTotal();
                return true;
            }
        }

        items.add(new CartItem(product, quantity));
        calculateTotal();
        return true;
    }

    public boolean removeItem(int productId) {
        if (orderStatus != OrderStatus.pending) {
            System.out.println("Cannot modify order #" + orderId + "  it is already " + orderStatus);
            return false;
        }

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getProduct().getId() == productId) {
                items.remove(i);
                calculateTotal();
                return true;
            }
        }

        System.out.println("Product " + productId + " is not in order #" + orderId + ".");
        return false;
    }


    private void calculateTotal() {
        double sum = 0.0;
        for (CartItem item : items) {
            sum += item.calculateSubtotal();
        }
        this.total = sum;
    }


    public void displayOrder() {
        System.out.println("Order #" + orderId + "  |  Customer: " + customerName + "  |  Status: " + orderStatus);
        if (items.isEmpty()) {
            System.out.println("no items yet");
        } else {
            for (CartItem item : items) {
                System.out.println(item.getProduct().getName()
                        + "  x" + item.getQuantity()
                        + "  subtotal: " + item.calculateSubtotal());
            }
        }
        System.out.println("Total: " + total);
    }
}




