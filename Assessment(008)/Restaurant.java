import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.HashMap;

public class Restaurant {

    private ArrayList<MenuItem> menu;
    private LinkedList<Order> kitchenQueue;
    private HashMap<Integer, Order> orders;
    private LinkedHashMap<Integer, Order> completedOrders;



    //non parameterized constructor so the containers don't start with the default value null and throw an error
    public Restaurant() {
        this.menu = new ArrayList<>();
        this.kitchenQueue = new LinkedList<>();
        this.orders = new HashMap<>();
        this.completedOrders = new LinkedHashMap<>();
    }




    public MenuItem getMenuItem(int id) {
        for(MenuItem item : menu) {
            if(item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public Order getOrder(int orderId) {
        return orders.get(orderId);
    }


    //return whether an item with this id already exists
    public boolean addMenuItem(int id, String name, double price, String category) {
        if (id <= 0 || price <= 0) {
            return false;
        }
        if (name == null || name.isBlank() || category == null || category.isBlank()) {
            return false;
        }
        if (getMenuItem(id) != null) {
            return false;
        }
        menu.add(new MenuItem(id, name, price, category));
        return true;
    }

    public boolean removeMenuItem(int id) {
        if(getMenuItem(id) == null) {
            return false;
        }
        menu.remove(getMenuItem(id));
        return true;
    }

    public void displayMenu() {
        if (menu.isEmpty()) {
            System.out.println("Menu is empty");
            return;
        }
        for (MenuItem item : menu) {
            System.out.println(item);
        }
    }

    public boolean createOrder(int orderId , String customerName) {
        if (orderId <= 0) {
            return false;
        }
        if (customerName == null || customerName.isBlank()) {
            return false;
        }
        if(orders.containsKey(orderId)) {
            return false;
        }
        orders.put(orderId , new Order(orderId,customerName));
        return true;
    }



    public String addItemToOrder(int orderId, int menuItemId, int quantity) {
        Order order = orders.get(orderId);
        if (order == null) {
            return "Order not found";
        }
        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
            return "Cannot modify an order that is " + order.getStatus();
        }
        MenuItem item = getMenuItem(menuItemId);
        if (item == null) {
            return "Menu item not found";
        }
        if (quantity <= 0) {
            return "Quantity must be greater than zero";
        }
        order.addItem(item, quantity);
        return "Item added to order";
    }


    public String removeItemFromOrder(int orderId, int menuItemId) {
        Order order = orders.get(orderId);
        if (order == null) {
            return "Order not found";
        }
        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
            return "Cannot modify an order that is " + order.getStatus();
        }
        boolean removed = order.removeItem(menuItemId);
        return removed ? "Item removed from order" : "That item is not in the order";
    }

    public String addOrderToKitchenQueue(int orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            return "Order not found";
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            return "Only PENDING orders can be sent to the kitchen (current status: " + order.getStatus() + ")";
        }
        order.setStatus(OrderStatus.IN_KITCHEN);
        kitchenQueue.addLast(order); // enqueue
        return "Order added to kitchen queue";
    }

    public String processNextOrder() {
        if (kitchenQueue.isEmpty()) {
            return "Kitchen queue is empty, Nothing to process";
        }
        Order order = kitchenQueue.removeFirst();
        order.setStatus(OrderStatus.COMPLETED);
        completedOrders.put(order.getOrderId(), order); //linkedHashMap preserves completion order
        return "Order " + order.getOrderId() + " processed and marked COMPLETED";
    }



    public String checkOrderStatus(int orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            return "Order not found";
        }
        return "Order " + orderId + " status: " + order.getStatus();
    }

    public void displayCompletedOrders() {
        if (completedOrders.isEmpty()) {
            System.out.println("No completed orders yet");
            return;
        }
        for (Order order : completedOrders.values()) { //insertion order == completion order
            order.displayOrder();
            System.out.println("----------------------------------------");
        }
    }






}