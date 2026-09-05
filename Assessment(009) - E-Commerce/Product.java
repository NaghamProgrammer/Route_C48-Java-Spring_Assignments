public class Product implements Comparable<Product>{
    private int id;
    private String name;
    private double price;
    private String category;
    private int stockQuantity;


    public Product(int id, String name, double price, String category, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.stockQuantity = stockQuantity;
    }


    //setters for stockQuantity only
    // because on business logic level it doesn't make sense to change name or category after construction
    //and changing the od or price can be a dangerous operation
    public void setStockQuantity(int tockQuantity) {
        this.stockQuantity = tockQuantity;
    }


    //getters for everything because admins might need such info exposed
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    @Override
    public int compareTo(Product other) {
        return Double.compare(this.price, other.price);
    }

    @Override
    public String toString() {
        return "id: " + id
                + ", name: " + name
                + ", price: " + price
                + ", category: " + category
                + ", quantity in stock: " + stockQuantity;
    }


}
