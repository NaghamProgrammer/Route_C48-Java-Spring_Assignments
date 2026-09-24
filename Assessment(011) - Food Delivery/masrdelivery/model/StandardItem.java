package masrdelivery.model;

import masrdelivery.exceptions.ValidationException;

public class StandardItem extends MenuItem {
    private final Money price;

    public StandardItem(int id, int restaurantId, String name, String category, int prepMinutes, Money price) {
        super(id, restaurantId, name, category, prepMinutes);
        if (price.isZero() || price.isNegative()) throw new ValidationException("Price must be greater than zero");
        this.price = price;
    }

    @Override public Money priceFor(double count) {
        if (count <= 0) throw new ValidationException("Quantity must be greater than zero");
        return price.multiply(count);
    }
    @Override public String kind() { return "Standard"; }
    @Override public Money referencePrice() { return price; }
}
