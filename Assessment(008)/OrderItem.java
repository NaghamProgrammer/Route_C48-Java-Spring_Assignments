public class OrderItem {

    private MenuItem item;
    private int quantity;

    public OrderItem(MenuItem item, int quantity) {
        if (item == null) {
            throw new IllegalArgumentException("item must not be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        this.item = item;
        this.quantity = quantity;
    }


    public MenuItem getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }


    public double calculateSubtotal(){
        return item.getPrice() * quantity;
    }


    @Override
    public String toString() {
        return "Name: " + item.getName() + ", Price: " + item.getPrice() + ", Quantity: " + quantity + ", Subtotal: " + calculateSubtotal();
    }
}