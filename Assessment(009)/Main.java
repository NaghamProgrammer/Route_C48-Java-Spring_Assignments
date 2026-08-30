import java.util.Scanner;
import java.util.*;


public class Main {

    static Scanner sc = new Scanner(System.in);
    static Store store = new Store();


    public static void main(String[] args) {
        boolean running = true;

        while (running) {

            byte choice = menu();


            switch (choice) {

                case 1: {
                    int id = readPositiveInt("Product ID: ");
                    String name = readLine("Name: ");
                    double price = readPositiveDouble("Price: ");
                    String category = readLine("Category: ");
                    int stock = readNonNegativeInt("Stock quantity: "); // allow 0 quantity like in the assignment example
                    store.addProduct(id, name, price, category, stock);
                    break;
                }

                case 2: {
                    int id = readPositiveInt("Product ID to remove: ");
                    store.removeProduct(id);
                    break;
                }

                case 3:
                    store.displayAllProducts();
                    break;

                case 4: {
                    int id = readPositiveInt("Product ID: ");
                    store.searchProductById(id);
                    break;
                }

                case 5:
                    store.showAllCategories();
                    break;

                case 6:
                    store.displayProductsOrderedByPrice();
                    break;

                case 7: {
                    int orderId = readPositiveInt("Order ID: ");
                    String customerName = readLine("Customer name: ");
                    store.createOrder(orderId, customerName);
                    break;
                }

                case 8: {
                    int orderId = readPositiveInt("Order ID: ");
                    int productId = readPositiveInt("Product ID: ");
                    int quantity = readPositiveInt("Quantity: ");
                    store.addItemToOrder(orderId, productId, quantity);
                    break;
                }

                case 9: {
                    int orderId = readPositiveInt("Order ID: ");
                    int productId = readPositiveInt("Product ID: ");
                    store.removeItemFromOrder(orderId, productId);
                    break;
                }

                case 10: {
                    int orderId = readPositiveInt("Order ID: ");
                    store.displayOrder(orderId);
                    break;
                }

                case 11: {
                    int orderId = readPositiveInt("Order ID: ");
                    store.addOrderToShippingList(orderId);
                    break;
                }

                case 12:
                    store.shipNextOrder();
                    break;

                case 13: {
                    int orderId = readPositiveInt("Order ID: ");
                    store.cancelOrder(orderId);
                    break;
                }

                case 14: {
                    int orderId = readPositiveInt("Order ID: ");
                    store.searchOrderById(orderId);
                    break;
                }

                case 15: {
                    int productId = readPositiveInt("Product ID: ");
                    String customerName = readLine("Customer name: ");
                    String comment = readLine("Comment: ");
                    store.addReview(productId, customerName, comment);
                    break;
                }

                case 16: {
                    int productId = readPositiveInt("Product ID: ");
                    store.showReviewsForProduct(productId);
                    break;
                }

                case 17:
                    store.removeOutOfStockProducts();
                    break;

                case 18:
                    store.displayOrdersOrderedByTotal();
                    break;

                case 19:
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
            System.out.println("=========================================");
            System.out.println(" "+"E-Commerce Order & Inventory Manager" );
            System.out.println("=========================================");
            System.out.println("1. Add Product");
            System.out.println("2. Remove Product");
            System.out.println("3. Display All Products");
            System.out.println("4. Search Product by ID");
            System.out.println("5. Show All Categories");
            System.out.println("6. Display Products Ordered by Price");
            System.out.println("7. Create Order");
            System.out.println("8. Add Item to Order");
            System.out.println("9. Remove Item from Order");
            System.out.println("10. Display Order");
            System.out.println("11. Add Order to the Shipping List");
            System.out.println("12. Ship Next Order");
            System.out.println("13. Cancel Order");
            System.out.println("14. Search Order by ID");
            System.out.println("15. Add Review to a Product");
            System.out.println("16. Show All Reviews for a Product");
            System.out.println("17. Remove Out-of-Stock Products");
            System.out.println("18. Display Orders Ordered by Total");
            System.out.println("19. Exit");
            System.out.print("Enter your choice: ");

            if (!sc.hasNextByte()) {
                System.out.println("Invalid choice.");
                sc.next();
                continue;
            }

            choice = sc.nextByte();
            sc.nextLine();

            if (choice > 0 && choice <= 19) {
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