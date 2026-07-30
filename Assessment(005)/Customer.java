public class Customer {

    private String id;
    private String name;
    private String phoneNumber;

    private String rentedCarId;
    private int numberOfRentedDays;
    private double totalPaid;

    private static int customerCount;

    // Default constructor (constructor chaining)
    public Customer() {
        this("", "", "");
    }

    // Main constructor
    public Customer(String id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;

        this.rentedCarId = "-1";
        this.numberOfRentedDays = 0;
        this.totalPaid = 0;

        customerCount++;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRentedCarId() {
        return rentedCarId;
    }

    public int getNumberOfRentedDays() {
        return numberOfRentedDays;
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public static int getCustomerCount() {
        return customerCount;
    }

    // Setters (only for allowed fields)
    public void setRentedCarId(String rentedCarId) {
        this.rentedCarId = rentedCarId;
    }

    public void setNumberOfRentedDays(int numberOfRentedDays) {
        this.numberOfRentedDays = numberOfRentedDays;
    }

    // Add payment to customer's total payments
    public void addPayment(double amount) {
        if (amount > 0) {
            totalPaid += amount;
        }
    }

    // Check if customer currently has a rented car
    public boolean hasRentedCar() {
        return !rentedCarId.equals("-1");
    }

    // Record a rental
    public void rentCar(String carId, int days) {
        rentedCarId = carId;
        numberOfRentedDays = days;
    }

    // Return the rented car
    public void returnCar() {
        rentedCarId = "-1";
        numberOfRentedDays = 0;
    }

    // Display customer information
    public void displayInfo() {
        System.out.println("----------------------------------------");
        System.out.println("Customer ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Phone: " + phoneNumber);
        System.out.println("Current Car: " +
                (hasRentedCar() ? rentedCarId : "None"));
        System.out.println("Current Rental Days: " + numberOfRentedDays);
        System.out.printf("Total Paid: %.2f%n", totalPaid);
    }

    @Override
    public String toString() {
        return "ID: " + id +
                " | Name: " + name +
                " | Phone: " + phoneNumber +
                " | Current Car: " +
                (hasRentedCar() ? rentedCarId : "None") +
                " | Total Paid: " + totalPaid;
    }
}