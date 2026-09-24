package masrdelivery.model;

import java.util.Objects;


// menu items come in several priced kinds. Pricing is
// pushed down into an abstract method so that a brand-new kind (say,
// a subscription box) can be added later as one more subclass without
// touching PricingService or any code that totals an order.

public abstract class MenuItem {
    private final int id;
    private final int restaurantId;
    private String name;
    private String category;
    private int prepMinutes;
    private boolean available;

    protected MenuItem(int id, int restaurantId, String name, String category, int prepMinutes) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.name = name;
        this.category = category;
        this.prepMinutes = prepMinutes;
        this.available = true;
    }

    //amount = item count for Standard/Combo, kilograms for Weighted
    public abstract Money priceFor(double amount);

    //Human label used in menu listings, e.g. "Standard", "Combo", "Weighted (per kg)"
    public abstract String kind();

    // The price shown next to the item in the menu (unit price, bundle price, or price/kg)
    public abstract Money referencePrice();

    public int getId() { return id; }
    public int getRestaurantId() { return restaurantId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getPrepMinutes() { return prepMinutes; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override public boolean equals(Object o) { //unique within its restaurant
        if (this == o) return true;
        if (!(o instanceof MenuItem)) return false;
        MenuItem m = (MenuItem) o;
        return id == m.id && restaurantId == m.restaurantId;
    }
    @Override public int hashCode() { return Objects.hash(restaurantId, id); }
    @Override public String toString() { return id + ". " + name + " (" + kind() + ", " + referencePrice() + ")"
            + (available ? "" : " [unavailable]"); }
}
