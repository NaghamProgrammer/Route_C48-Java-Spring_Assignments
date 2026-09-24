package masrdelivery;

import masrdelivery.builder.OrderBuilder;
import masrdelivery.config.PlatformConfig;
import masrdelivery.dispatch.BicycleDispatch;
import masrdelivery.dispatch.CarDispatch;
import masrdelivery.dispatch.MotorcycleDispatch;
import masrdelivery.exceptions.PlatformException;
import masrdelivery.model.Address;
import masrdelivery.model.ComboItem;
import masrdelivery.model.Customer;
import masrdelivery.model.District;
import masrdelivery.model.LoyaltyTier;
import masrdelivery.model.MenuItem;
import masrdelivery.model.Money;
import masrdelivery.model.Order;
import masrdelivery.model.OrderStatus;
import masrdelivery.model.Restaurant;
import masrdelivery.model.Rider;
import masrdelivery.model.StandardItem;
import masrdelivery.model.WeightedItem;
import masrdelivery.promotion.PercentageOffPromotion;
import masrdelivery.promotion.Promotion;
import masrdelivery.service.RestaurantSearch;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static Platform platform = new Platform();

    // for testing purposes only
    static final String ADMIN_PIN = "123";
    static final int MAX_PIN_ATTEMPTS = 3;

    static int nextCustomerId = 6000;

    static final List<String> MAIN_MENU_OPTIONS = Arrays.asList(
            "Customer",
            "Restaurant",
            "Rider",
            "Admin & Reports",
            "Exit"
    );

    static final List<String> CUSTOMER_MENU_OPTIONS = Arrays.asList(
            "Browse Restaurants",
            "Search Restaurants",
            "View Menu",
            "Place Order",
            "Pay Order From Wallet",
            "Track Order",
            "Cancel Order",
            "Order History",
            "Back to Main Menu"
    );

    static final List<String> RESTAURANT_MENU_OPTIONS = Arrays.asList(
            "Accept Pending Order",
            "Mark Order Preparing",
            "Mark Order Ready",
            "Toggle Item Availability",
            "Add Menu Item",
            "Remove Menu Item",
            "Adjust Stock",
            "View Today's Orders And Revenue",
            "Back to Main Menu"
    );

    static final List<String> RIDER_MENU_OPTIONS = Arrays.asList(
            "Go On Duty",
            "Go Off Duty",
            "View Assigned Order",
            "Mark Picked Up",
            "Mark Delivered",
            "View My Delivery Stats",
            "Back to Main Menu"
    );

    static final List<String> ADMIN_MENU_OPTIONS = Arrays.asList(
            "Add Restaurant",
            "Remove Restaurant",
            "Remove Out-of-Stock Items (platform-wide)",
            "Create Promotion",
            "Dispatch Next Ready Order To A Rider",
            "Run Reports",
            "View Platform-Wide Statistics",
            "Back to Main Menu"
    );

    static final List<String> REPORT_OPTIONS = Arrays.asList(
            "Total Revenue For A Date Range",
            "Top 5 Restaurants By Revenue For A Month",
            "Average Order Value Per District",
            "Restaurants Rated Above 4.5 With 20+ Completed Orders",
            "Order Counts By Status",
            "Riders By Completed Deliveries",
            "Most Frequently Ordered Menu Item",
            "A Customer's Order History And Total Spent",
            "Peak Ordering Hour",
            "Customers Inactive For 30+ Days",
            "Back"
    );

    public static void main(String[] args) {
        seedData();

        boolean running = true;
        while (running) {
            byte choice = readMenuChoice("MASR DELIVERY - Main Menu", MAIN_MENU_OPTIONS);
            switch (choice) {
                case 1: runCustomerMenu(); break;
                case 2: runRestaurantMenu(); break;
                case 3: runRiderMenu(); break;
                case 4:
                    if (verifyAdminPin()) runAdminMenu();
                    break;
                case 5:
                    running = false;
                    System.out.println("\nThank you for using Masr Delivery");
                    break;
            }
        }
        sc.close();
    }

    // ============================== CUSTOMER AREA ==============================

    static void runCustomerMenu() {
        Customer customer = selectOrRegisterCustomer();
        if (customer == null) return;

        boolean inMenu = true;
        while (inMenu) {
            byte choice = readMenuChoice("Customer Menu - " + customer.getName(), CUSTOMER_MENU_OPTIONS);
            switch (choice) {
                case 1: safe(() -> browseRestaurants()); break;
                case 2: safe(() -> searchRestaurants(customer)); break;
                case 3: safe(() -> viewMenu()); break;
                case 4: safe(() -> placeOrder(customer)); break;
                case 5: safe(() -> payFromWallet()); break;
                case 6: safe(() -> trackOrder()); break;
                case 7: safe(() -> cancelOrder()); break;
                case 8: safe(() -> orderHistory(customer)); break;
                case 9: inMenu = false; break;
            }
        }
    }

    static Customer selectOrRegisterCustomer() {
        int id = readNonNegativeInt("Enter your customer ID (0 to register as a new customer): ");
        if (id == 0) {
            try {
                int newId = nextCustomerId++;
                String name = readLine("Name: ");
                String mobile = readLine("Mobile (11 digits, starting 010/011/012/015): ");
                Customer c = new Customer(newId, name, mobile);
                District district = readDistrict("Address district");
                String detail = readLine("Address detail (building, street): ");
                c.addAddress(new Address(district, detail));
                platform.addCustomer(c);
                System.out.println("Registered! Your customer ID is " + newId);
                return c;
            } catch (PlatformException e) {
                System.out.println("Error: " + e.getMessage());
                return null;
            }
        }
        try {
            return platform.getCustomer(id);
        } catch (PlatformException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    static void browseRestaurants() {
        District district = readOptionalDistrict("Filter by district (blank for any): ");
        List<Restaurant> results = new RestaurantSearch().byDistrict(district).openOnly()
                .apply(platform.allRestaurants());
        printRestaurants(results);
    }

    static void searchRestaurants(Customer customer) {
        String text = readLine("Search (name or cuisine): ");
        customer.recordSearch(text);
        List<Restaurant> results = new RestaurantSearch().nameOrCuisineContains(text).apply(platform.allRestaurants());
        if (results.isEmpty()) System.out.println("Nothing found for \"" + text + "\"");
        else printRestaurants(results);
    }

    static void printRestaurants(List<Restaurant> results) {
        if (results.isEmpty()) { System.out.println("No restaurants match."); return; }
        for (Restaurant r : results) System.out.println(r);
    }

    static void viewMenu() {
        Restaurant r = platform.getRestaurant(readPositiveInt("Restaurant ID: "));
        System.out.println(r.getName() + " menu (in the order items were added):");
        for (MenuItem item : r.getMenu().values()) {
            System.out.println("  " + item + " | stock: " + r.getStock(item.getId()));
        }
    }

    static void placeOrder(Customer customer) {
        Restaurant restaurant = platform.getRestaurant(readPositiveInt("Restaurant ID: "));
        OrderBuilder builder = platform.newOrderBuilder(customer, restaurant);

        boolean addingItems = true;
        while (addingItems) {
            int itemId = readPositiveInt("Menu item ID to add: ");
            MenuItem item = restaurant.getMenuItem(itemId);
            double amount = (item instanceof WeightedItem)
                    ? readPositiveDouble("Weight in kg: ")
                    : readPositiveInt("Quantity: ");
            builder.addLineItem(item, amount);
            addingItems = readYesNo("Add another item? (y/n): ");
        }

        List<Address> addresses = customer.getAddresses();
        for (int i = 0; i < addresses.size(); i++) System.out.println((i + 1) + ". " + addresses.get(i));
        int addrChoice = readIntInRange("Delivery address number: ", 1, addresses.size());
        builder.deliverTo(addresses.get(addrChoice - 1));

        String promo = readLine("Promotion code (blank for none): ");
        if (!promo.isBlank()) builder.withPromotion(promo);
        builder.withNotes(readLine("Delivery notes (optional): "));

        Order order = platform.finalizeOrder(builder);
        System.out.println("\nOrder #" + order.getId() + " placed.");
        System.out.println("Subtotal:  " + order.getSubtotal());
        System.out.println("Delivery:  " + order.getDeliveryFee());
        System.out.println("Service:   " + order.getServiceFee());
        System.out.println("Promo:    -" + order.getPromotionDiscount());
        System.out.println("TOTAL:     " + order.getTotal());
    }

    static void payFromWallet() {
        Order order = platform.getOrder(readPositiveInt("Order ID: "));
        platform.payFromWallet(order);
        System.out.println("Paid. New wallet balance: " + order.getCustomer().getWallet());
    }

    static void trackOrder() {
        Order order = platform.getOrder(readPositiveInt("Order ID: "));
        long minutesSincePlaced = java.time.Duration.between(order.getPlacedAt(), LocalDateTime.now()).toMinutes();
        System.out.println("Status: " + order.getStatus() + " | placed " + minutesSincePlaced + " minute(s) ago");
    }

    static void cancelOrder() {
        Order order = platform.getOrder(readPositiveInt("Order ID: "));
        platform.cancelOrder(order);
        System.out.println("Order #" + order.getId() + " cancelled and refunded to wallet.");
    }

    static void orderHistory(Customer customer) {
        List<Order> history = platform.reports().customerHistory(customer);
        if (history.isEmpty()) { System.out.println("No orders yet."); return; }
        for (Order o : history) System.out.println("  " + o + " placed " + o.getPlacedAt());
        System.out.println("Lifetime total spent: " + platform.reports().customerTotalSpent(customer));
    }

    // ============================== RESTAURANT AREA ==============================

    static void runRestaurantMenu() {
        Restaurant restaurant;
        try {
            restaurant = platform.getRestaurant(readPositiveInt("Restaurant ID: "));
        } catch (PlatformException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        boolean inMenu = true;
        while (inMenu) {
            byte choice = readMenuChoice("Restaurant Menu - " + restaurant.getName(), RESTAURANT_MENU_OPTIONS);
            switch (choice) {
                case 1: safe(() -> platform.acceptOrder(platform.getOrder(readPositiveInt("Order ID: ")))); break;
                case 2: safe(() -> platform.startPreparing(platform.getOrder(readPositiveInt("Order ID: ")))); break;
                case 3: safe(() -> platform.markReady(platform.getOrder(readPositiveInt("Order ID: ")))); break;
                case 4: safe(() -> toggleAvailability(restaurant)); break;
                case 5: safe(() -> addMenuItem(restaurant)); break;
                case 6: safe(() -> restaurant.removeMenuItem(readPositiveInt("Menu item ID to remove: "))); break;
                case 7: safe(() -> adjustStock(restaurant)); break;
                case 8: safe(() -> todaysOrdersAndRevenue(restaurant)); break;
                case 9: inMenu = false; break;
            }
        }
    }

    static void toggleAvailability(Restaurant restaurant) {
        MenuItem item = restaurant.getMenuItem(readPositiveInt("Menu item ID: "));
        item.setAvailable(!item.isAvailable());
        System.out.println(item.getName() + " is now " + (item.isAvailable() ? "available" : "unavailable"));
    }

    static void addMenuItem(Restaurant restaurant) {
        int id = readPositiveInt("New item ID: ");
        String name = readLine("Name: ");
        String category = readLine("Category: ");
        int prep = readPositiveInt("Prep time (minutes): ");
        System.out.println("1. Standard  2. Weighted (per kg)");
        int kind = readIntInRange("Kind: ", 1, 2);
        MenuItem item = (kind == 1)
                ? new StandardItem(id, restaurant.getId(), name, category, prep, Money.of(readPositiveDouble("Price (EGP): ")))
                : new WeightedItem(id, restaurant.getId(), name, category, prep, Money.of(readPositiveDouble("Price per kg (EGP): ")));
        int stock = readNonNegativeInt("Initial stock: ");
        restaurant.addMenuItem(item, stock);
        System.out.println("Added " + item);
    }

    static void adjustStock(Restaurant restaurant) {
        int id = readPositiveInt("Menu item ID: ");
        restaurant.getMenuItem(id); // validates existence
        int newStock = readNonNegativeInt("New stock quantity: ");
        restaurant.setStock(id, newStock);
        System.out.println("Stock updated.");
    }

    static void todaysOrdersAndRevenue(Restaurant restaurant) {
        LocalDate today = LocalDate.now();
        List<Order> todays = platform.allOrders().stream()
                .filter(o -> o.getRestaurant().equals(restaurant))
                .filter(o -> o.getPlacedAt().toLocalDate().equals(today))
                .collect(java.util.stream.Collectors.toList());
        Money revenue = todays.stream().filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .map(Order::getTotal).reduce(Money.ZERO, Money::add);
        System.out.println(todays.size() + " order(s) today, revenue so far: " + revenue);
        todays.forEach(o -> System.out.println("  " + o));
    }

    // ============================== RIDER AREA ==============================

    static void runRiderMenu() {
        Rider rider;
        try {
            rider = platform.getRider(readPositiveInt("Rider ID: "));
        } catch (PlatformException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        boolean inMenu = true;
        while (inMenu) {
            byte choice = readMenuChoice("Rider Menu - " + rider.getName(), RIDER_MENU_OPTIONS);
            switch (choice) {
                case 1: rider.setOnDuty(true); System.out.println("You are now on duty."); break;
                case 2: rider.setOnDuty(false); System.out.println("You are now off duty."); break;
                case 3: safe(() -> viewAssignedOrder(rider)); break;
                case 4: safe(() -> platform.markPickedUp(findAssignedOrder(rider))); break;
                case 5: safe(() -> platform.markDelivered(findAssignedOrder(rider))); break;
                case 6: System.out.println("Completed deliveries: " + rider.getCompletedDeliveries()
                        + " | avg duration: " + String.format("%.1f", rider.averageDeliveryMinutes()) + " min"); break;
                case 7: inMenu = false; break;
            }
        }
    }

    static Order findAssignedOrder(Rider rider) {
        return platform.allOrders().stream()
                .filter(o -> rider.equals(o.getAssignedRider())
                        && (o.getStatus() == OrderStatus.ASSIGNED || o.getStatus() == OrderStatus.OUT_FOR_DELIVERY))
                .findFirst()
                .orElseThrow(() -> new masrdelivery.exceptions.NotFoundException("No active order assigned to you"));
    }

    static void viewAssignedOrder(Rider rider) {
        Order order = findAssignedOrder(rider);
        System.out.println(order + " -> " + order.getDeliveryAddress());
    }

    // ============================== ADMIN AREA ==============================

    static boolean verifyAdminPin() {
        for (int attempt = 1; attempt <= MAX_PIN_ATTEMPTS; attempt++) {
            String pin = readLine("Enter admin PIN (for testing purposes admin pin is 123): ");
            if (pin.equals(ADMIN_PIN)) return true;
            System.out.println("Incorrect PIN. Attempts remaining: " + (MAX_PIN_ATTEMPTS - attempt));
        }
        System.out.println("Too many incorrect attempts. Returning to main menu.");
        return false;
    }

    static void runAdminMenu() {
        boolean inMenu = true;
        while (inMenu) {
            byte choice = readMenuChoice("Admin Menu", ADMIN_MENU_OPTIONS);
            switch (choice) {
                case 1: safe(() -> addRestaurant()); break;
                case 2: safe(() -> platform.removeRestaurant(readPositiveInt("Restaurant ID to remove: "))); break;
                case 3: platform.removeOutOfStockAcrossPlatform(); System.out.println("Done."); break;
                case 4: safe(() -> createPromotion()); break;
                case 5: safe(() -> dispatchNext()); break;
                case 6: runReportsMenu(); break;
                case 7: platformStatistics(); break;
                case 8: inMenu = false; break;
            }
        }
    }

    static void addRestaurant() {
        int id = readPositiveInt("Restaurant ID: ");
        String name = readLine("Name: ");
        District district = readDistrict("District");
        String cuisinesLine = readLine("Cuisines (comma separated): ");
        List<String> cuisines = Arrays.asList(cuisinesLine.split("\\s*,\\s*"));
        double rating = readRating("Initial rating (0.0-5.0): ");
        Restaurant r = new Restaurant(id, name, district, cuisines, rating, true);
        platform.addRestaurant(r);
        System.out.println("Added " + r);
    }

    static double readRating(String prompt) {
        while (true) {
            double v = readDouble(prompt);
            if (v >= 0.0 && v <= 5.0) return v;
            System.out.println("Rating must be between 0.0 and 5.0");
        }
    }

    static void createPromotion() {
        String code = readLine("Promotion code: ");
        System.out.println("1. Percentage off  2. Fixed amount off  3. Free delivery");
        int kind = readIntInRange("Kind: ", 1, 3);
        LocalDate expiry = readYesNo("Does it expire? (y/n): ") ? readDate("Expiry date (yyyy-mm-dd): ") : null;
        Money minSubtotal = readYesNo("Minimum subtotal condition? (y/n): ") ? Money.of(readPositiveDouble("Minimum subtotal: ")) : null;
        District district = readYesNo("Restrict to one district? (y/n): ") ? readDistrict("District") : null;
        boolean firstTimeOnly = readYesNo("First-time customers only? (y/n): ");

        Promotion promo;
        switch (kind) {
            case 1: {
                double pct = readPositiveDouble("Percentage off (e.g. 0.20 for 20%): ");
                boolean hasCap = readYesNo("Cap the discount? (y/n): ");
                Money cap = hasCap ? Money.of(readPositiveDouble("Cap amount (EGP): ")) : null;
                promo = new PercentageOffPromotion(code, pct, cap, expiry, minSubtotal, district, firstTimeOnly);
                break;
            }
            case 2: {
                Money amount = Money.of(readPositiveDouble("Fixed amount off (EGP): "));
                promo = new masrdelivery.promotion.FixedAmountOffPromotion(code, amount, expiry, minSubtotal, district, firstTimeOnly);
                break;
            }
            default:
                promo = new masrdelivery.promotion.FreeDeliveryPromotion(code, expiry, minSubtotal, district, firstTimeOnly);
        }
        platform.addPromotion(promo);
        System.out.println("Promotion " + promo.getCode() + " created.");
    }

    static void dispatchNext() {
        Order order = platform.dispatchNextReadyOrder();
        System.out.println("Order #" + order.getId() + " assigned to " + order.getAssignedRider());
    }

    static void platformStatistics() {
        System.out.println("Restaurants: " + platform.allRestaurants().size());
        System.out.println("Customers:   " + platform.allCustomers().size());
        System.out.println("Riders:      " + platform.allRiders().size());
        System.out.println("Orders:      " + platform.allOrders().size());
    }

    static void runReportsMenu() {
        boolean inMenu = true;
        while (inMenu) {
            byte choice = readMenuChoice("Reports", REPORT_OPTIONS);
            switch (choice) {
                case 1: safe(() -> report1()); break;
                case 2: safe(() -> report2()); break;
                case 3: safe(() -> report3()); break;
                case 4: safe(() -> report4()); break;
                case 5: safe(() -> report5()); break;
                case 6: safe(() -> report6()); break;
                case 7: safe(() -> report7()); break;
                case 8: safe(() -> report8()); break;
                case 9: safe(() -> report9()); break;
                case 10: safe(() -> report10()); break;
                case 11: inMenu = false; break;
            }
        }
    }

    static void report1() {
        LocalDate from = readDate("From (yyyy-mm-dd): ");
        LocalDate to = readDate("To (yyyy-mm-dd): ");
        Money revenue = platform.reports().totalRevenue(from.atStartOfDay(), to.atTime(23, 59, 59));
        System.out.println("Total revenue: " + revenue);
    }

    static void report2() {
        YearMonth month = YearMonth.parse(readLine("Month (yyyy-mm): "));
        var top = platform.reports().topRestaurantsByRevenue(month);
        if (top.isEmpty()) { System.out.println("No delivered orders that month."); return; }
        top.forEach(e -> System.out.println("  " + e.getKey().getName() + ": " + e.getValue()));
    }

    static void report3() {
        Map<District, Double> avg = platform.reports().averageOrderValuePerDistrict();
        avg.forEach((d, v) -> System.out.println("  " + d + ": " + String.format("%.2f EGP", v)));
    }

    static void report4() {
        List<Restaurant> top = platform.reports().topRatedActiveRestaurants();
        if (top.isEmpty()) { System.out.println("None yet."); return; }
        top.forEach(System.out::println);
    }

    static void report5() {
        platform.reports().orderCountsByStatus().forEach((status, count) -> System.out.println("  " + status + ": " + count));
    }

    static void report6() {
        platform.reports().ridersByDeliveriesDescending().forEach(r ->
                System.out.println("  " + r.getName() + ": " + r.getCompletedDeliveries()
                        + " deliveries, avg " + String.format("%.1f", r.averageDeliveryMinutes()) + " min"));
    }

    static void report7() {
        Optional<Map.Entry<String, Long>> top = platform.reports().mostFrequentMenuItem();
        System.out.println(top.map(e -> e.getKey() + " (" + e.getValue() + " orders)")
                .orElse("No orders have been placed yet - there is no such item."));
    }

    static void report8() {
        Customer customer = platform.getCustomer(readPositiveInt("Customer ID: "));
        orderHistory(customer);
    }

    static void report9() {
        Optional<Integer> hour = platform.reports().peakOrderingHour();
        System.out.println(hour.map(h -> "Peak ordering hour: " + h + ":00").orElse("No orders placed yet."));
    }

    static void report10() {
        List<Customer> inactive = platform.reports().customersInactiveSince30Days(LocalDateTime.now());
        if (inactive.isEmpty()) { System.out.println("No inactive customers."); return; }
        inactive.forEach(System.out::println);
    }

    // ============================== SHARED HELPERS ==============================

    /** Wraps a menu action so a business-rule failure prints a precise message instead of crashing. */
    static void safe(Runnable action) {
        try {
            action.run();
        } catch (PlatformException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (java.util.NoSuchElementException | java.time.format.DateTimeParseException e) {
            System.out.println("Error: invalid input.");
        }
    }

    static byte readMenuChoice(String title, List<String> options) {
        byte choice;
        while (true) {
            System.out.println();
            System.out.println("=========================================");
            System.out.println(" " + title);
            System.out.println("=========================================");
            for (int i = 0; i < options.size(); i++) System.out.println((i + 1) + ". " + options.get(i));
            System.out.print("Enter your choice: ");

            if (!sc.hasNextByte()) {
                System.out.println("Invalid choice.");
                sc.next();
                continue;
            }
            choice = sc.nextByte();
            sc.nextLine();
            if (choice > 0 && choice <= options.size()) return choice;
            System.out.println("Invalid choice");
        }
    }

    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextInt()) { int v = sc.nextInt(); sc.nextLine(); return v; }
            System.out.println("Invalid number, try again");
            sc.nextLine();
        }
    }

    static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextDouble()) { double v = sc.nextDouble(); sc.nextLine(); return v; }
            System.out.println("Invalid number, try again");
            sc.nextLine();
        }
    }

    static String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    static double readPositiveDouble(String prompt) {
        while (true) {
            double v = readDouble(prompt);
            if (v > 0) return v;
            System.out.println("Value must be greater than zero, try again");
        }
    }

    static int readPositiveInt(String prompt) {
        while (true) {
            int v = readInt(prompt);
            if (v > 0) return v;
            System.out.println("Value must be greater than zero, try again");
        }
    }

    static int readNonNegativeInt(String prompt) {
        while (true) {
            int v = readInt(prompt);
            if (v >= 0) return v;
            System.out.println("Value cannot be negative, try again");
        }
    }

    static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int v = readInt(prompt);
            if (v >= min && v <= max) return v;
            System.out.println("Enter a number between " + min + " and " + max);
        }
    }

    static boolean readYesNo(String prompt) {
        while (true) {
            String line = readLine(prompt).trim().toLowerCase();
            if (line.equals("y") || line.equals("yes")) return true;
            if (line.equals("n") || line.equals("no")) return false;
            System.out.println("Please enter y or n");
        }
    }

    static LocalDate readDate(String prompt) {
        while (true) {
            try {
                return LocalDate.parse(readLine(prompt).trim());
            } catch (java.time.format.DateTimeParseException e) {
                System.out.println("Invalid date, use yyyy-mm-dd");
            }
        }
    }

    static District readDistrict(String label) {
        District[] values = District.values();
        for (int i = 0; i < values.length; i++) System.out.println((i + 1) + ". " + values[i]);
        int choice = readIntInRange(label + ": ", 1, values.length);
        return values[choice - 1];
    }

    static District readOptionalDistrict(String prompt) {
        String line = readLine(prompt).trim();
        if (line.isEmpty()) return null;
        try {
            return District.valueOf(line.toUpperCase().replace(' ', '_'));
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown district, showing all districts instead.");
            return null;
        }
    }

    // ============================== SEED DATA ==============================


    static void seedData() {
        Restaurant cairoGrill = new Restaurant(1, "Cairo Grill", District.MAADI,
                List.of("Grill", "Egyptian"), 4.7, true);
        cairoGrill.addMenuItem(new StandardItem(1, 1, "Grilled Chicken Sandwich", "Sandwich", 10, Money.of(60.0)), 50);
        // 1 kg of this at 240 EGP/kg reproduces the worked example's 240.00 EGP subtotal exactly.
        cairoGrill.addMenuItem(new WeightedItem(2, 1, "Mixed Kebab & Kofta (per kg)", "Grill", 20, Money.of(240.0)), 20);
        platform.addRestaurant(cairoGrill);

        Restaurant nileBites = new Restaurant(2, "Nile Bites", District.DOKKI,
                List.of("Fast Food", "Burgers"), 4.3, true);
        nileBites.addMenuItem(new StandardItem(1, 2, "Cheeseburger", "Burger", 8, Money.of(75.0)), 40);
        nileBites.addMenuItem(new StandardItem(2, 2, "Fries", "Side", 5, Money.of(25.0)), 60);
        List<MenuItem> comboParts = List.of(nileBites.getMenuItem(1), nileBites.getMenuItem(2));
        nileBites.addMenuItem(new ComboItem(3, 2, "Burger + Fries Combo", "Combo", 10, comboParts, 0.10), 30);
        platform.addRestaurant(nileBites);

        Customer youssef = new Customer(5001, "Youssef Anwar", "01012345678");
        youssef.addAddress(new Address(District.FAISAL, "12 Talaat Harb St, Apt 4"));
        youssef.credit(Money.of(500.0));
        platform.addCustomer(youssef);

        platform.addRider(new Rider(9001, "Omar Adel", new MotorcycleDispatch(), District.NASR_CITY));
        platform.addRider(new Rider(9002, "Mariam Fathy", new BicycleDispatch(), District.DOKKI));
        platform.addRider(new Rider(9003, "Karim Hossam", new CarDispatch(), District.MAADI));


        platform.addPromotion(new PercentageOffPromotion("NILE20", 0.20, Money.of(50.0), null, null, null, false));

        System.out.println("Seeded sample data.");
        System.out.println("  Customer 5001 (Youssef Anwar, Faisal), Restaurant 1 (Cairo Grill, Maadi),");
        System.out.println("  item 2 (1 kg) + promo NILE20 reproduces the spec's worked example: total 258.00 EGP.");
        System.out.println("  Riders 9001/9002/9003 exist but start off duty - go on duty in the Rider menu first.");
    }
}
