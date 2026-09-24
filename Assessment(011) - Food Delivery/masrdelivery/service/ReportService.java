package masrdelivery.service;

import masrdelivery.model.Customer;
import masrdelivery.model.District;
import masrdelivery.model.LineItem;
import masrdelivery.model.Money;
import masrdelivery.model.Order;
import masrdelivery.model.OrderStatus;
import masrdelivery.model.Restaurant;
import masrdelivery.model.Rider;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/** Part D - every report is a declarative stream pipeline over the live data, no accumulation loops. */
public class ReportService {
    private final Collection<Order> orders;
    private final Collection<Restaurant> restaurants;
    private final Collection<Customer> customers;
    private final Collection<Rider> riders;

    public ReportService(Collection<Order> orders, Collection<Restaurant> restaurants,
                          Collection<Customer> customers, Collection<Rider> riders) {
        this.orders = orders;
        this.restaurants = restaurants;
        this.customers = customers;
        this.riders = riders;
    }

    /** 1. Total revenue for a date range. */
    public Money totalRevenue(LocalDateTime from, LocalDateTime to) {
        return orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .filter(o -> !o.getPlacedAt().isBefore(from) && !o.getPlacedAt().isAfter(to))
                .map(Order::getTotal)
                .reduce(Money.ZERO, Money::add);
    }

    /** 2. Top five restaurants by revenue for a given month. */
    public List<Map.Entry<Restaurant, Money>> topRestaurantsByRevenue(YearMonth month) {
        Map<Restaurant, Money> revenueByRestaurant = orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .filter(o -> YearMonth.from(o.getPlacedAt()).equals(month))
                .collect(Collectors.groupingBy(Order::getRestaurant,
                        Collectors.reducing(Money.ZERO, Order::getTotal, Money::add)));
        return revenueByRestaurant.entrySet().stream()
                .sorted(Map.Entry.<Restaurant, Money>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    /** 3. Average order value per district. */
    public Map<District, Double> averageOrderValuePerDistrict() {
        return orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .collect(Collectors.groupingBy(o -> o.getDeliveryAddress().getDistrict(),
                        Collectors.averagingDouble(o -> o.getTotal().raw().doubleValue())));
    }

    /** 4. Restaurants with rating above 4.5 and at least 20 completed orders. */
    public List<Restaurant> topRatedActiveRestaurants() {
        return restaurants.stream()
                .filter(r -> r.getRating() > 4.5 && r.getCompletedOrders() >= 20)
                .collect(Collectors.toList());
    }

    /** 5. Count of orders grouped by current status. */
    public Map<OrderStatus, Long> orderCountsByStatus() {
        return orders.stream().collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()));
    }

    /** 6. Each rider's completed deliveries and average delivery duration, sorted by deliveries descending. */
    public List<Rider> ridersByDeliveriesDescending() {
        return riders.stream()
                .sorted(Comparator.comparingInt(Rider::getCompletedDeliveries).reversed())
                .collect(Collectors.toList());
    }

    /** 7. Most frequently ordered menu item platform-wide; empty when there are no orders at all. */
    public Optional<Map.Entry<String, Long>> mostFrequentMenuItem() {
        return orders.stream()
                .flatMap(o -> o.getLineItems().stream())
                .collect(Collectors.groupingBy(li -> li.getMenuItem().getName(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue());
    }

    /** 8. A customer's full order history, newest first. */
    public List<Order> customerHistory(Customer customer) {
        return orders.stream()
                .filter(o -> o.getCustomer().equals(customer))
                .sorted(Comparator.comparing(Order::getPlacedAt).reversed())
                .collect(Collectors.toList());
    }

    public Money customerTotalSpent(Customer customer) {
        return orders.stream()
                .filter(o -> o.getCustomer().equals(customer) && o.getStatus() == OrderStatus.DELIVERED)
                .map(Order::getTotal)
                .reduce(Money.ZERO, Money::add);
    }

    /** 9. The peak ordering hour of the day across all orders. */
    public Optional<Integer> peakOrderingHour() {
        return orders.stream()
                .collect(Collectors.groupingBy(o -> o.getPlacedAt().getHour(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    /** 10. Every customer who has not ordered in the last 30 days. */
    public List<Customer> customersInactiveSince30Days(LocalDateTime now) {
        Map<Customer, LocalDateTime> lastOrderByCustomer = orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomer,
                        Collectors.mapping(Order::getPlacedAt, Collectors.maxBy(Comparator.naturalOrder()))))
                .entrySet().stream()
                .filter(e -> e.getValue().isPresent())
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));

        return customers.stream()
                .filter(c -> {
                    LocalDateTime last = lastOrderByCustomer.get(c);
                    return last == null || last.isBefore(now.minusDays(30));
                })
                .collect(Collectors.toList());
    }
}
