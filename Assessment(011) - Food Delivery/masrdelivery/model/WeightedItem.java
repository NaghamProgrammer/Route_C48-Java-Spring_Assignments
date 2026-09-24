package masrdelivery.model;

import masrdelivery.exceptions.ValidationException;

public class WeightedItem extends MenuItem {
    private final Money pricePerKg;

    public WeightedItem(int id, int restaurantId, String name, String category, int prepMinutes, Money pricePerKg) {
        super(id, restaurantId, name, category, prepMinutes);
        if (pricePerKg.isZero() || pricePerKg.isNegative())
            throw new ValidationException("Price per kg must be greater than zero");
        this.pricePerKg = pricePerKg;
    }

    @Override public Money priceFor(double kilograms) {
        if (kilograms <= 0) throw new ValidationException("Weight (kg) must be greater than zero");
        return pricePerKg.multiply(kilograms);
    }
    @Override public String kind() { return "Weighted (per kg)"; }
    @Override public Money referencePrice() { return pricePerKg; }
}
