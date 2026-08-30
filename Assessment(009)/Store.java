import java.util.*;

public class Store {


    private List<Product> productList = new ArrayList<>();   //insertion order, for "Display All"
    private Map<Integer, Product> productMap = new HashMap<>(); //O(1) lookup by ID

    //Orders: permanent record, reachable by ID, never deleted
    private Map<Integer, Order> allOrders = new HashMap<>();

    //Categories: no duplicates, order doesn't matter
    private Set<String> categories = new HashSet<>();

    //Shipping list: FIFO
    private Queue<Order> shippingQueue = new LinkedList<>();

    //Delivered orders: kept in delivery order, still reachable by ID
    private Map<Integer, Order> deliveredOrders = new LinkedHashMap<>();

    //Reviews: single flat list, insertion order ---
    private List<Review> reviews = new ArrayList<>();


    public void addProduct(int id, String name, double price, String category, int stock) {
        if (productMap.containsKey(id)) {
            System.out.println("Product ID " + id + " already exists");
            return;
        }
        Product product = new Product(id, name, price, category, stock);
        productList.add(product);
        productMap.put(id, product);
        categories.add(category);
        System.out.println("Product added: " + product);
    }



    private void deleteProductEverywhere(int id) {
        productMap.remove(id);
        productList.removeIf(p -> p.getId() == id);
    }

    public void removeProduct(int id) {
        if (!productMap.containsKey(id)) {
            System.out.println("No product with ID " + id);
            return;
        }
        deleteProductEverywhere(id);
        System.out.println("Product " + id + " removed");
    }

    public void displayAllProducts() {
        if (productList.isEmpty()) {
            System.out.println("No products in the shop");
            return;
        }
        for (Product p : productList) {
            System.out.println(p);
        }
    }

    public void searchProductById(int id) {
        Product p = productMap.get(id);
        if (p == null) {
            System.out.println("No product with ID " + id );
        } else {
            System.out.println(p);
        }
    }

    public void showAllCategories() {
        if (categories.isEmpty()) {
            System.out.println("No categories yet");
            return;
        }
        for (String c : categories) {
            System.out.println(c);
        }
    }

    public void displayProductsOrderedByPrice() {
        List<Product> sorted = new ArrayList<>(productList);
        Collections.sort(sorted);
        for (Product p : sorted) {
            System.out.println(p);
        }
    }

    public void removeOutOfStockProducts() {
        int removedCount = 0;
        Iterator<Product> it = productList.iterator();
        while (it.hasNext()) {
            Product p = it.next();
            if (p.getStockQuantity() == 0) {
                productMap.remove(p.getId());
                it.remove();
                removedCount++;
            }
        }
        System.out.println(removedCount + " out-of-stock products removed");
    }



    public void createOrder(int orderId, String customerName) {
        if (allOrders.containsKey(orderId)) {
            System.out.println("Order ID " + orderId + " already exists");
            return;
        }
        Order order = new Order(orderId, customerName, 0.0, OrderStatus.pending);
        allOrders.put(orderId, order);
        System.out.println("Order " + orderId + " created for " + customerName);
    }

    public void addItemToOrder(int orderId, int productId, int quantity) {
        Order order = allOrders.get(orderId);
        if (order == null) {
            System.out.println("No order with ID " + orderId);
            return;
        }
        Product product = productMap.get(productId);
        if (product == null) {
            System.out.println("Product " + productId + " does not exist in the shop");
            return;
        }
        if (order.addItem(product, quantity)) {
            System.out.println("Added " + quantity + " x " + product.getName() + " to order " + orderId);
        }
    }

    public void removeItemFromOrder(int orderId, int productId) {
        Order order = allOrders.get(orderId);
        if (order == null) {
            System.out.println("No order with ID " + orderId);
            return;
        }
        if (order.removeItem(productId)) {
            System.out.println("Removed product " + productId + " from order " + orderId);
        }
    }

    public void displayOrder(int orderId) {
        Order order = allOrders.get(orderId);
        if (order == null) {
            System.out.println("No order with ID " + orderId);
            return;
        }
        order.displayOrder();
    }

    public void searchOrderById(int orderId) {
        Order order = allOrders.get(orderId);
        if (order == null) {
            System.out.println("No order with ID " + orderId);
            return;
        }
        order.displayOrder();
    }

    public void addOrderToShippingList(int orderId) {
        Order order = allOrders.get(orderId);
        if (order == null) {
            System.out.println("No order with ID " + orderId);
            return;
        }

        if (order.getOrderStatus() != OrderStatus.pending) {
            System.out.println("Order " + orderId + " is already " + order.getOrderStatus() + ", cannot queue it");
            return;
        }
        if (order.getItems().isEmpty()) {
            System.out.println("Order " + orderId + " has no items and cannot be shipped");
            return;
        }
        shippingQueue.offer(order);
        order.setOrderStatus(OrderStatus.shipped);
        System.out.println("Order " + orderId + " added to the shipping list");
    }

    public void shipNextOrder() {
        if (shippingQueue.isEmpty()) {
            System.out.println("The shipping list is empty");
            return;
        }
        Order next = shippingQueue.peek();
        if (next.getItems().isEmpty()) {
            System.out.println("This order has no items and cannot be shipped");
            return;
        }
        shippingQueue.poll(); // now actually remove it
        next.setOrderStatus(OrderStatus.delivered);
        deliveredOrders.put(next.getOrderId(), next);
        System.out.println("Order " + next.getOrderId() + " shipped and marked Delivered");
    }

    public void cancelOrder(int orderId) {
        Order order = allOrders.get(orderId);
        if (order == null) {
            System.out.println("No order with ID " + orderId);
            return;
        }
        OrderStatus status = order.getOrderStatus();
        if (status == OrderStatus.delivered || status == OrderStatus.cancelled) {
            System.out.println("Order " + orderId + " is already " + status + ", cannot cancel");
            return;
        }
        if (status == OrderStatus.shipped) {
            shippingQueue.remove(order);
        }
        order.setOrderStatus(OrderStatus.cancelled);
        System.out.println("Order " + orderId + " cancelled");
    }

    public void displayOrdersOrderedByTotal() {
        if (allOrders.isEmpty()) {
            System.out.println("No orders yet");
            return;
        }
        List<Order> sorted = new ArrayList<>(allOrders.values()); // sort a copy
        sorted.sort(Comparator.comparingDouble(Order::getTotal));
        for (Order o : sorted) {
            o.displayOrder();
        }
    }




    public void addReview(int productId, String customerName, String comment) {
        if (!productMap.containsKey(productId)) {
            System.out.println("Product " + productId + " does not exist, cannot review it");
            return;
        }
        reviews.add(new Review(productId, customerName, comment));
        System.out.println("Review added");
    }

    public void showReviewsForProduct(int productId) {
        boolean found = false;
        for (Review r : reviews) {
            if (r.getProductId() == productId) {
                System.out.println(r);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No reviews yet for product " + productId);
        }
    }
}