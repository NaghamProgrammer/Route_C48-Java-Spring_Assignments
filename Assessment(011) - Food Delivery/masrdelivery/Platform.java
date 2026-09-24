package masrdelivery;

import masrdelivery.builder.OrderBuilder;
import masrdelivery.exceptions.ItemUnavailableException;
import masrdelivery.exceptions.NotFoundException;
import masrdelivery.exceptions.PromotionException;
import masrdelivery.exceptions.RestaurantClosedException;
import masrdelivery.exceptions.RiderUnavailableException;
import masrdelivery.exceptions.StockShortageException;
import masrdelivery.model.Customer;
import masrdelivery.model.LineItem;
import masrdelivery.model.MenuItem;
import masrdelivery.model.Order;
import masrdelivery.model.OrderStatus;
import masrdelivery.model.Restaurant;
import masrdelivery.model.Rider;
import masrdelivery.observer.AuditLogger;
import masrdelivery.observer.CustomerNotifier;
import masrdelivery.observer.RiderDashboardUpdater;
import masrdelivery.observer.StatisticsRecalculator;
import masrdelivery.promotion.Promotion;
import masrdelivery.repository.InMemoryRepository;
import masrdelivery.service.DispatchQueue;
import masrdelivery.service.PricingService;
import masrdelivery.service.ReportService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// Facade tying repositories, pricing, dispatch, and reporting together.
// This is the single place Main talks to

public class Platform {
    private final InMemoryRepository<Restaurant, Integer> restaurants = new InMemoryRepository<>("Restaurant");
    private final InMemoryRepository<Customer, Integer> customers = new InMemoryRepository<>("Customer");
    private final InMemoryRepository<Rider, Integer> riders = new InMemoryRepository<>("Rider");
    private final InMemoryRepository<Order, Integer> orders = new InMemoryRepository<>("Order");
    private final Map<String, Promotion> promotions = new HashMap<>();
    private final DispatchQueue dispatchQueue = new DispatchQueue();

    private int nextOrderId = 1000;

    // ---- restaurants ----
    public void addRestaurant(Restaurant r) { restaurants.add(r, r.getId()); }
    public void removeRestaurant(int id) { restaurants.remove(id); }
    public Restaurant getRestaurant(int id) { return restaurants.getById(id); }
    public List<Restaurant> allRestaurants() { return new ArrayList<>(restaurants.findAll()); }
    public void removeOutOfStockAcrossPlatform() { restaurants.findAll().forEach(Restaurant::removeOutOfStockItems); }

    // ---- customers ----
    public void addCustomer(Customer c) { customers.add(c, c.getId()); }
    public Customer getCustomer(int id) { return customers.getById(id); }
    public List<Customer> allCustomers() { return new ArrayList<>(customers.findAll()); }

    // ---- riders ----
    public void addRider(Rider r) { riders.add(r, r.getId()); }
    public Rider getRider(int id) { return riders.getById(id); }
    public List<Rider> allRiders() { return new ArrayList<>(riders.findAll()); }

    // ---- promotions ----
    public void addPromotion(Promotion p) { promotions.put(p.getCode(), p); }
    public Promotion getPromotion(String code) {
        if (code == null || code.isBlank()) return null;
        Promotion p = promotions.get(code.toUpperCase());
        if (p == null) throw new PromotionException("No such promotion code: " + code);
        return p;
    }

    // ---- orders ----
    public OrderBuilder newOrderBuilder(Customer customer, Restaurant restaurant) {
        return new OrderBuilder(nextOrderId++, customer, restaurant);
    }

    /**
     * Validates everything, prices the order, and only then reserves stock -
     * so a failure at any step leaves no partial state behind (atomic reject,
     * per Part H's "Place order" requirement).
     */
    public Order finalizeOrder(OrderBuilder builder) {
        Order order = builder.build();
        Restaurant restaurant = order.getRestaurant();
        if (!restaurant.isOpen())
            throw new RestaurantClosedException("Restaurant " + restaurant.getName() + " is closed");

        for (LineItem li : order.getLineItems()) {
            MenuItem item = restaurant.getMenuItem(li.getMenuItem().getId());
            if (!item.isAvailable()) throw new ItemUnavailableException(item.getName() + " is currently unavailable");
            int neededUnits = (int) Math.ceil(li.getAmount());
            if (restaurant.getStock(item.getId()) < neededUnits)
                throw new StockShortageException("Not enough stock for " + item.getName());
        }

        boolean firstTime = order.getCustomer().getCompletedOrders() == 0;
        Promotion promo = getPromotion(order.getPromotionCode());
        PricingService.Breakdown breakdown = PricingService.price(
                order.getLineItems(), restaurant.getDistrict(), order.getDeliveryAddress().getDistrict(),
                order.getCustomer().getTier(), promo, firstTime, LocalDate.now());
        order.applyPricing(breakdown.subtotal, breakdown.deliveryFee, breakdown.serviceFee,
                breakdown.promotionDiscount, breakdown.total);

        // Nothing mutated above; only now, after every check passed, do we touch stock.
        for (LineItem li : order.getLineItems()) {
            restaurant.decreaseStock(li.getMenuItem().getId(), (int) Math.ceil(li.getAmount()));
        }

        order.addListener(new CustomerNotifier());
        order.addListener(new RiderDashboardUpdater());
        order.addListener(new AuditLogger());
        order.addListener(new StatisticsRecalculator());

        orders.add(order, order.getId());
        return order;
    }

    public Order getOrder(int id) { return orders.getById(id); }
    public List<Order> allOrders() { return new ArrayList<>(orders.findAll()); }

    public void payFromWallet(Order order) { order.getCustomer().debit(order.getTotal()); }

    public void cancelOrder(Order order) {
        order.transitionTo(OrderStatus.CANCELLED);
        order.getCustomer().credit(order.getTotal());
    }

    public void acceptOrder(Order order) { order.transitionTo(OrderStatus.ACCEPTED); }
    public void startPreparing(Order order) { order.transitionTo(OrderStatus.PREPARING); }

    public void markReady(Order order) {
        order.transitionTo(OrderStatus.READY);
        dispatchQueue.enqueue(order);
    }

    /** Part C.5 dispatch order + Part A.3 single-active-order rule, both enforced here. */
    public Order dispatchNextReadyOrder() {
        if (dispatchQueue.isEmpty()) throw new NotFoundException("No orders are currently ready for dispatch");
        Rider rider = riders.findAll().stream().filter(Rider::isAvailable).findFirst()
                .orElseThrow(() -> new RiderUnavailableException("No riders are currently available"));
        Order order = dispatchQueue.pollNext();
        rider.assignOrder();
        order.assignRider(rider);
        order.transitionTo(OrderStatus.ASSIGNED);
        return order;
    }

    public void markPickedUp(Order order) { order.transitionTo(OrderStatus.OUT_FOR_DELIVERY); }

    public void markDelivered(Order order) {
        order.transitionTo(OrderStatus.DELIVERED);
        long minutes = Math.max(order.deliveryDurationMinutes(), 0);
        order.getAssignedRider().completeDelivery(minutes);
        order.getCustomer().recordCompletedOrder();
        order.getRestaurant().recordCompletedOrder();
    }

    public ReportService reports() {
        return new ReportService(orders.findAll(), restaurants.findAll(), customers.findAll(), riders.findAll());
    }
}
