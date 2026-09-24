package masrdelivery.model;

import masrdelivery.exceptions.ValidationException;

import java.util.List;

public class ComboItem extends MenuItem {
    private final List<MenuItem> components;
    private final double discountFraction;

    public ComboItem(int id, int restaurantId, String name, String category, int prepMinutes,
                      List<MenuItem> components, double discountFraction) {
        super(id, restaurantId, name, category, prepMinutes);
        if (components == null || components.isEmpty())
            throw new ValidationException("A combo needs at least one component item");
        if (discountFraction < 0 || discountFraction >= 1)
            throw new ValidationException("Discount fraction must be within [0,1)");
        this.components = List.copyOf(components);
        this.discountFraction = discountFraction;
    }

    private Money bundlePrice() {
        Money sum = Money.ZERO;
        for (MenuItem m : components) sum = sum.add(m.referencePrice());
        return sum.multiply(1 - discountFraction);
    }

    @Override public Money priceFor(double count) {
        if (count <= 0) throw new ValidationException("Quantity must be greater than zero");
        return bundlePrice().multiply(count);
    }
    @Override public String kind() { return "Combo"; }
    @Override public Money referencePrice() { return bundlePrice(); }
}
