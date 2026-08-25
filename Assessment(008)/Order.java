import java.util.ArrayList;

public class Order {
    private int orderId;
    private String customerName;
    private ArrayList<OrderItem> items;
    private double total;
    private OrderStatus status;


    public Order(int orderId, String customerName) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.items = new ArrayList<>();
        this.total = 0.0;
        this.status = OrderStatus.PENDING;
    }


    public int getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public ArrayList<OrderItem> getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }

    public OrderStatus getStatus() {
        return status;
    }



    //a method to update the order status -> a setter for status
    public void setStatus(OrderStatus status) {
        this.status = status;
    }


    public void addItem(MenuItem item , int quantity) {
        items.add(new OrderItem(item, quantity));
        calculateTotal();
    }

    public boolean removeItem(int menuItemId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItem().getId() == menuItemId) {
                items.remove(i);
                calculateTotal();
                return true;
            }
        }
        return false;
    }

    public double calculateTotal() {
        total = 0.0;
        for (OrderItem oi : items) {
            total += oi.calculateSubtotal();
        }
        return total;
    }




    public void displayOrder() {
        System.out.println("Order ID: " + orderId);
        System.out.println("Customer: " + customerName);
        System.out.println("Status: " + status);

        if (items.isEmpty()) {
            System.out.println("Items: (no items yet)");
        } else {
            System.out.println("Items:");
            for (OrderItem oi : items) {System.out.println("   " + oi);}
        }

        System.out.print("Total: " + total);
    }
}
