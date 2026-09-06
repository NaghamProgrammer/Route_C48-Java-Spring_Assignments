import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static Store store = new Store();

    // for testing purposes only
    static final String ADMIN_PIN = "123";
    static final int MAX_PIN_ATTEMPTS = 3;

    static Map<Byte, Runnable> userActions = new LinkedHashMap<>();
    static Map<Byte, Runnable> adminActions = new LinkedHashMap<>();

    static final List<String> MAIN_MENU_OPTIONS = Arrays.asList(
            "User Menu",
            "Admin Menu",
            "Exit"
    );

    static final List<String> USER_MENU_OPTIONS = Arrays.asList(
            "Display All Products",
            "Search Product by ID",
            "Show All Categories",
            "Display Products Ordered by Price",
            "Create Order",
            "Add Item to Order",
            "Remove Item from Order",
            "Display Order",
            "Checkout (Add Order to Shipping List)",
            "Cancel Order",
            "Add Review to a Product",
            "Show All Reviews for a Product",
            "Back to Main Menu"
    );

    static final List<String> ADMIN_MENU_OPTIONS = Arrays.asList(
            "Add Product",
            "Remove Product",
            "Remove Out-of-Stock Products",
            "Ship Next Order",
            "Search Order by ID",
            "Display Orders Ordered by Total",
            "Back to Main Menu"
    );

    public static void main(String[] args) {
        registerUserActions();
        registerAdminActions();

        boolean running = true;
        while (running) {
            byte choice = readMenuChoice("E-Commerce Order & Inventory Manager", MAIN_MENU_OPTIONS);

            switch (choice) {
                case 1:
                    runUserMenu();
                    break;
                case 2:
                    if (verifyAdminPin()) {
                        runAdminMenu();
                    }
                    break;
                case 3:
                    running = false;
                    System.out.println("\nThank you for using our system");
                    break;
            }
        }

        sc.close();
    }

    static void runUserMenu() {
        boolean inMenu = true;
        while (inMenu) {
            byte choice = readMenuChoice("User Menu", USER_MENU_OPTIONS);
            if (choice == USER_MENU_OPTIONS.size()) { // last option = Back
                inMenu = false;
            } else {
                userActions.getOrDefault(choice, () -> System.out.println("Invalid choice")).run();
            }
        }
    }

    static void runAdminMenu() {
        boolean inMenu = true;
        while (inMenu) {
            byte choice = readMenuChoice("Admin Menu", ADMIN_MENU_OPTIONS);
            if (choice == ADMIN_MENU_OPTIONS.size()) { // last option = Back
                inMenu = false;
            } else {
                adminActions.getOrDefault(choice, () -> System.out.println("Invalid choice")).run();
            }
        }
    }

    static boolean verifyAdminPin() {
        for (int attempt = 1; attempt <= MAX_PIN_ATTEMPTS; attempt++) {
            String pin = readLine("Enter admin PIN (for testing purposes admin pin is 123): ");
            if (pin.equals(ADMIN_PIN)) {
                return true;
            }
            System.out.println("Incorrect PIN. Attempts remaining: " + (MAX_PIN_ATTEMPTS - attempt));
        }
        System.out.println("Too many incorrect attempts. Returning to main menu.");
        return false;
    }

    static void registerUserActions() {
        userActions.put((byte) 1, store::displayAllProducts);

        userActions.put((byte) 2, () -> {
            int id = readPositiveInt("Product ID: ");
            store.searchProductById(id);
        });

        userActions.put((byte) 3, store::showAllCategories);

        userActions.put((byte) 4, store::displayProductsOrderedByPrice);

        userActions.put((byte) 5, () -> {
            int orderId = readPositiveInt("Order ID: ");
            String customerName = readLine("Customer name: ");
            store.createOrder(orderId, customerName);
        });

        userActions.put((byte) 6, () -> {
            int orderId = readPositiveInt("Order ID: ");
            int productId = readPositiveInt("Product ID: ");
            int quantity = readPositiveInt("Quantity: ");
            store.addItemToOrder(orderId, productId, quantity);
        });

        userActions.put((byte) 7, () -> {
            int orderId = readPositiveInt("Order ID: ");
            int productId = readPositiveInt("Product ID: ");
            store.removeItemFromOrder(orderId, productId);
        });

        userActions.put((byte) 8, () -> {
            int orderId = readPositiveInt("Order ID: ");
            store.displayOrder(orderId);
        });

        userActions.put((byte) 9, () -> {
            int orderId = readPositiveInt("Order ID: ");
            store.addOrderToShippingList(orderId);
        });

        userActions.put((byte) 10, () -> {
            int orderId = readPositiveInt("Order ID: ");
            store.cancelOrder(orderId);
        });

        userActions.put((byte) 11, () -> {
            int productId = readPositiveInt("Product ID: ");
            String customerName = readLine("Customer name: ");
            String comment = readLine("Comment: ");
            store.addReview(productId, customerName, comment);
        });

        userActions.put((byte) 12, () -> {
            int productId = readPositiveInt("Product ID: ");
            store.showReviewsForProduct(productId);
        });
    }

    static void registerAdminActions() {
        adminActions.put((byte) 1, () -> {
            int id = readPositiveInt("Product ID: ");
            String name = readLine("Name: ");
            double price = readPositiveDouble("Price: ");
            String category = readLine("Category: ");
            int stock = readNonNegativeInt("Stock quantity: "); // allow 0 quantity like in the assignment example
            store.addProduct(id, name, price, category, stock);
        });

        adminActions.put((byte) 2, () -> {
            int id = readPositiveInt("Product ID to remove: ");
            store.removeProduct(id);
        });

        adminActions.put((byte) 3, store::removeOutOfStockProducts);

        adminActions.put((byte) 4, store::shipNextOrder);

        adminActions.put((byte) 5, () -> {
            int orderId = readPositiveInt("Order ID: ");
            store.searchOrderById(orderId);
        });

        adminActions.put((byte) 6, store::displayOrdersOrderedByTotal);
    }

    // generic menu printer/reader — shared by main menu, user menu, and admin menu
    static byte readMenuChoice(String title, List<String> options) {
        byte choice;

        while (true) {
            System.out.println();
            System.out.println("=========================================");
            System.out.println(" " + title);
            System.out.println("=========================================");

            for (int i = 0; i < options.size(); i++) {
                System.out.println((i + 1) + ". " + options.get(i));
            }
            System.out.print("Enter your choice: ");

            if (!sc.hasNextByte()) {
                System.out.println("Invalid choice.");
                sc.next();
                continue;
            }

            choice = sc.nextByte();
            sc.nextLine();

            if (choice > 0 && choice <= options.size()) {
                return choice;
            }

            System.out.println("Invalid choice");
        }
    }


    //input helpers
    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextInt()) {
                int value = sc.nextInt();
                sc.nextLine();
                return value;
            }
            System.out.println("Invalid number, try again");
            sc.nextLine();
        }
    }

    static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextDouble()) {
                double value = sc.nextDouble();
                sc.nextLine();
                return value;
            }
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
            double value = readDouble(prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Value must be greater than zero, try again");
        }
    }

    static int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Value must be greater than zero, try again");
        }
    }

    static int readNonNegativeInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value >= 0) return value;
            System.out.println("Value cannot be negative, try again");
        }
    }

}