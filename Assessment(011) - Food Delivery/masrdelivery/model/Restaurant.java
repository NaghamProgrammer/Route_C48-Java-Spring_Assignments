package masrdelivery.model;

import masrdelivery.exceptions.DuplicateIdException;
import masrdelivery.exceptions.NotFoundException;
import masrdelivery.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Restaurant {
    private final int id;
    private String name;
    private District district;
    private final Set<String> cuisines = new LinkedHashSet<>();
    private double rating;
    private boolean open;

    // Part C.2 - LinkedHashMap so the menu always prints in the order items were added
    private final Map<Integer, MenuItem> menu = new LinkedHashMap<>();
    private final Map<Integer, Integer> stock = new HashMap<>();
    private int completedOrders = 0;

    public Restaurant(int id, String name, District district, Collection<String> cuisines, double rating, boolean open) {
        this.id = id;
        this.name = name;
        this.district = district;
        setRating(rating);
        this.cuisines.addAll(cuisines);
        this.open = open;
    }

    public void setRating(double rating) {
        if (rating < 0.0 || rating > 5.0) throw new ValidationException("Rating must be between 0.0 and 5.0");
        this.rating = rating;
    }

    public void addMenuItem(MenuItem item, int initialStock) {
        if (menu.containsKey(item.getId()))
            throw new DuplicateIdException("Menu item id " + item.getId() + " already exists at " + name);
        menu.put(item.getId(), item);
        stock.put(item.getId(), initialStock);
    }

    public void removeMenuItem(int itemId) {
        if (menu.remove(itemId) == null) throw new NotFoundException("Menu item " + itemId + " not found at " + name);
        stock.remove(itemId);
    }

    public MenuItem getMenuItem(int itemId) {
        MenuItem item = menu.get(itemId);
        if (item == null) throw new NotFoundException("Menu item " + itemId + " not found at " + name);
        return item;
    }

    public Map<Integer, MenuItem> getMenu() { return Collections.unmodifiableMap(menu); } // Part C.8

    public int getStock(int itemId) { return stock.getOrDefault(itemId, 0); }

    public void setStock(int itemId, int quantity) {
        if (quantity < 0) throw new ValidationException("Stock cannot be negative");
        stock.put(itemId, quantity);
    }

    public void decreaseStock(int itemId, int by) { stock.merge(itemId, -by, Integer::sum); }

    public void removeOutOfStockItems() {
        List<Integer> zero = new ArrayList<>();
        for (Map.Entry<Integer, Integer> e : stock.entrySet()) if (e.getValue() <= 0) zero.add(e.getKey());
        for (int id : zero) removeMenuItem(id);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public District getDistrict() { return district; }
    public Set<String> getCuisines() { return Collections.unmodifiableSet(cuisines); }
    public double getRating() { return rating; }
    public boolean isOpen() { return open; }
    public void setOpen(boolean open) { this.open = open; }
    public int getCompletedOrders() { return completedOrders; }
    public void recordCompletedOrder() { completedOrders++; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurant)) return false;
        return id == ((Restaurant) o).id;
    }
    @Override public int hashCode() { return Integer.hashCode(id); }
    @Override public String toString() {
        return "Restaurant#" + id + " " + name + " (" + district + ", " + String.format("%.1f", rating) + "*, "
                + (open ? "open" : "closed") + ")";
    }
}
