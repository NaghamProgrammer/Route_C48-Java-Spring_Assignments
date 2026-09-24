package masrdelivery.model;

public final class LineItem {
    private final MenuItem menuItem;
    private final double amount; // count for Standard/Combo, kilograms for Weighted

    public LineItem(MenuItem menuItem, double amount) {
        this.menuItem = menuItem;
        this.amount = amount;
    }

    public MenuItem getMenuItem() { return menuItem; }
    public double getAmount() { return amount; }
    public Money lineTotal() { return menuItem.priceFor(amount); }

    @Override public String toString() { return menuItem.getName() + " x" + amount + " = " + lineTotal(); }
}
