package masrdelivery.service;

import masrdelivery.model.District;
import masrdelivery.model.Restaurant;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Part D - the search facility. Each filter method just narrows a
 * shared Predicate<Restaurant>; the caller composes whichever
 * combination of criteria it needs and apply() never has to be
 * touched to support a new one.
 */
public final class RestaurantSearch {
    private Predicate<Restaurant> predicate = r -> true;

    public RestaurantSearch byDistrict(District district) {
        if (district != null) predicate = predicate.and(r -> r.getDistrict() == district);
        return this;
    }

    public RestaurantSearch byCuisine(String cuisine) {
        if (cuisine != null && !cuisine.isBlank())
            predicate = predicate.and(r -> r.getCuisines().stream().anyMatch(c -> c.equalsIgnoreCase(cuisine)));
        return this;
    }

    public RestaurantSearch minRating(Double minRating) {
        if (minRating != null) predicate = predicate.and(r -> r.getRating() >= minRating);
        return this;
    }

    public RestaurantSearch maxPrice(Double priceCeiling) {
        if (priceCeiling != null)
            predicate = predicate.and(r -> r.getMenu().values().stream()
                    .anyMatch(item -> item.referencePrice().raw().doubleValue() <= priceCeiling));
        return this;
    }

    public RestaurantSearch openOnly() { predicate = predicate.and(Restaurant::isOpen); return this; }

    public RestaurantSearch nameOrCuisineContains(String text) {
        if (text != null && !text.isBlank()) {
            String needle = text.toLowerCase();
            predicate = predicate.and(r -> r.getName().toLowerCase().contains(needle)
                    || r.getCuisines().stream().anyMatch(c -> c.toLowerCase().contains(needle)));
        }
        return this;
    }

    public RestaurantSearch custom(Predicate<Restaurant> extra) { predicate = predicate.and(extra); return this; }

    /** Part C.4 - highest rating first, ties broken alphabetically by name. */
    public List<Restaurant> apply(List<Restaurant> restaurants) {
        return restaurants.stream().filter(predicate)
                .sorted(Comparator.comparingDouble(Restaurant::getRating).reversed()
                        .thenComparing(Restaurant::getName))
                .collect(Collectors.toList());
    }
}
