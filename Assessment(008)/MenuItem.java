public class MenuItem {

    private int id;
    private String name;
    private double price;
    private String category;


    public MenuItem(int id, String name, double price, String category) {
        if (id <= 0) {
            throw new IllegalArgumentException("id must be positive");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("price must be positive");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be empty");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("category must not be empty");
        }
        this.id = id;
        this.name = name.trim();
        this.price = price;
        this.category = category.trim();
    }


    //no setter for id, price
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be empty");
        }
        this.name = name.trim();
    }

    public void setCategory(String category) {
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("category must not be empty");
        }
        this.category = category.trim();
    }



    //getters for everything
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


    @Override
    public String toString() {
        return "id: " + id + ", name: " + name + ", price: " + price + ", category: " + category;
    }
}