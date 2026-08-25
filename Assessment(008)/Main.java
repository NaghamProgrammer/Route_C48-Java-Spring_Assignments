import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static Restaurant restaurant = new Restaurant();

    public static void main(String[] args) {
        boolean running = true;

        while (running) {

            byte choice = menu();

            switch (choice) {

                case 1:
                    addMenuItem();
                    break;

                case 2:
                    removeMenuItem();
                    break;

                case 3:
                    restaurant.displayMenu();
                    break;

                case 4:
                    searchMenuItem();
                    break;

                case 5:
                    createOrder();
                    break;

                case 6:
                    addItemToOrder();
                    break;

                case 7:
                    removeItemFromOrder();
                    break;

                case 8:
                    displayOrder();
                    break;

                case 9:
                    addOrderToKitchenQueue();
                    break;

                case 10:
                    System.out.println(restaurant.processNextOrder());
                    break;

                case 11:
                    searchOrder();
                    break;

                case 12:
                    checkOrderStatus();
                    break;

                case 13:
                    restaurant.displayCompletedOrders();
                    break;

                case 14:
                    running = false;
                    System.out.println("\nThank you for using our system");
                    break;
            }
        }

        sc.close();
    }


    static byte menu() {

        byte choice;

        while (true) {

            System.out.println();
            System.out.println("========================================");
            System.out.println("     " + "Restaurant Order Manager" );
            System.out.println("========================================");
            System.out.println("1. Add Menu Item");
            System.out.println("2. Remove Menu Item");
            System.out.println("3. Display Menu");
            System.out.println("4. Search Menu Item");
            System.out.println("5. Create Order");
            System.out.println("6. Add Item to Order");
            System.out.println("7. Remove Item from Order");
            System.out.println("8. Display Order");
            System.out.println("9. Add Order to Kitchen Queue");
            System.out.println("10. Process Next Order");
            System.out.println("11. Search Order");
            System.out.println("12. Check Order Status");
            System.out.println("13. Display Completed Orders");
            System.out.println("14. Exit");
            System.out.print("Enter your choice: ");

            if (!sc.hasNextByte()) {
                System.out.println("Invalid choice.");
                sc.next();
                continue;
            }

            choice = sc.nextByte();
            sc.nextLine();

            if (choice >= 0 && choice <= 14) {
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


    //take info as input then pass them to the method in Restaurant
    static void addMenuItem() {

       int id = readPositiveInt("Enter item ID: ");
       String name = readLine("Enter item name: ");
       double price = readPositiveDouble("Enter item price: ");
       String category = readLine("Enter item category: ");

        boolean added = restaurant.addMenuItem(id, name, price, category);
        System.out.println(added ? "Menu item added." : "An item with that ID already exists");

    }


    static void removeMenuItem() {
        int id = readPositiveInt("Enter item ID: ");

        boolean removed = restaurant.removeMenuItem(id);
        System.out.println(removed ? "Menu item removed" : "No menu item found with that ID");

    }


    static void searchMenuItem() {
        int id = readPositiveInt("Enter item ID: ");
        MenuItem item = restaurant.getMenuItem(id);
        System.out.println(item != null ? item : "No menu item found with that ID");
    }



    static void createOrder(){
        int orderId = readPositiveInt("Enter order ID: ");
        String customerName = readLine("Enter customer name: ");
        boolean created = restaurant.createOrder(orderId, customerName);
        System.out.println(created ? "Order created." : "An order with that ID already exists");
    }
    static void addItemToOrder(){
        int orderId = readPositiveInt("Enter order ID: ");
        int itemId = readPositiveInt("Enter menu item ID: ");
        int quantity = readPositiveInt("Enter quantity: ");
        System.out.println(restaurant.addItemToOrder(orderId, itemId, quantity));
    }
    static void removeItemFromOrder() {
        int orderId = readPositiveInt("Enter order ID: ");
        int itemId = readPositiveInt("Enter menu item ID to remove: ");
        System.out.println(restaurant.removeItemFromOrder(orderId, itemId));
    }

    static void displayOrder() {
        int orderId = readPositiveInt("Enter order ID: ");
        Order order = restaurant.getOrder(orderId);
        if (order == null) {
            System.out.println("Order not found");
        } else {
            order.displayOrder();
        }
    }

    static void addOrderToKitchenQueue() {
        int orderId = readPositiveInt("Enter order ID: ");
        System.out.println(restaurant.addOrderToKitchenQueue(orderId));
    }

    static void searchOrder() {
        int orderId = readPositiveInt("Enter order ID: ");
        Order order = restaurant.getOrder(orderId);
        if (order == null) {
            System.out.println("Order not found");
        } else {
            order.displayOrder();
        }
    }

    static void checkOrderStatus() {
        int orderId = readPositiveInt("Enter order ID: ");
        System.out.println(restaurant.checkOrderStatus(orderId));
    }




}