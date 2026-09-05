public class CartItem {
    private Product product;
    int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }


    public double calculateSubtotal(){
        return product.getPrice() * quantity;
    }

    //used by Order.addItem() when the product is already in the cart
    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    @Override
    public String toString() {
        return product.getName() + " x" + quantity + " = " + calculateSubtotal();
    }

}
