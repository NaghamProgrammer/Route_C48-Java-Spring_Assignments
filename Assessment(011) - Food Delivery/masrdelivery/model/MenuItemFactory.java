package masrdelivery.model;

import masrdelivery.exceptions.ValidationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


//centralises the "type field -> concrete class" decision in
//exactly one place. Loading menu items from a data file (or, here, from
//seed data) never branches on a type string anywhere else; adding a new
// kind later means registering one more Creator, not scattering an
// if/else or switch across the codebase.

public final class MenuItemFactory {

    @FunctionalInterface
    public interface Creator { MenuItem create(Map<String, Object> data); }

    private static final Map<String, Creator> CREATORS = new HashMap<>();

    static {
        register("standard", data -> new StandardItem(
                (int) data.get("id"), (int) data.get("restaurantId"), (String) data.get("name"),
                (String) data.get("category"), (int) data.get("prepMinutes"),
                Money.of((double) data.get("price"))));

        register("combo", data -> new ComboItem(
                (int) data.get("id"), (int) data.get("restaurantId"), (String) data.get("name"),
                (String) data.get("category"), (int) data.get("prepMinutes"),
                (List<MenuItem>) data.get("components"), (double) data.get("discountFraction")));

        register("weighted", data -> new WeightedItem(
                (int) data.get("id"), (int) data.get("restaurantId"), (String) data.get("name"),
                (String) data.get("category"), (int) data.get("prepMinutes"),
                Money.of((double) data.get("pricePerKg"))));
    }

    private MenuItemFactory() {}

    public static void register(String type, Creator creator) { CREATORS.put(type.toLowerCase(), creator); }

    public static MenuItem create(String type, Map<String, Object> data) {
        Creator creator = CREATORS.get(type == null ? "" : type.toLowerCase());
        if (creator == null) throw new ValidationException("Unknown menu item type: " + type);
        return creator.create(data);
    }
}
