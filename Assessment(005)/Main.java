import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    static final int MAX_NUM_OF_CARS = 20;
    static final int MAX_NUM_OF_CUSTOMERS = 20;

    private static Car[] cars = new Car[MAX_NUM_OF_CARS];
    private static Customer[] customers = new Customer[MAX_NUM_OF_CUSTOMERS];

    private static double totalIncome = 0;

    public static void main(String[] args) {

        boolean running = true;

        while (running) {

            byte choice = menu();

            switch (choice) {

                case 1:
                    addRegularCar();
                    break;

                case 2:
                    addLuxuryCar();
                    break;

                case 3:
                    addCustomer();
                    break;

                case 4:
                    displayAllCars();
                    break;

                case 5:
                    displayAvailableCars();
                    break;

                case 6:
                    rentCar();
                    break;

                case 7:
                    returnCar();
                    break;

                case 8:
                    searchCarById();
                    break;

                case 9:
                    searchCarByBrand();
                    break;

                case 10:
                    displayAllCustomers();
                    break;

                case 11:
                    displayOfficeStatistics();
                    break;

                case 12:
                    exitMessage();
                    running = false;
                    break;
            }
        }
    }

    static byte menu() {

        byte choice = 0;

        do {

            System.out.println();
            System.out.println("========================================");
            System.out.println("         SPEEDWAY RENTALS SYSTEM");
            System.out.println("========================================");
            System.out.println("1. Add Regular Car");
            System.out.println("2. Add Luxury Car");
            System.out.println("3. Add Customer");
            System.out.println("4. Display All Cars");
            System.out.println("5. Display Available Cars");
            System.out.println("6. Rent a Car");
            System.out.println("7. Return a Car");
            System.out.println("8. Search Car by ID");
            System.out.println("9. Search Car by Brand");
            System.out.println("10. Display All Customers");
            System.out.println("11. Display Office Statistics");
            System.out.println("12. Exit");
            System.out.print("Enter your choice: ");

            if (!sc.hasNextByte()) {
                System.out.println("Invalid choice.");
                sc.next();
                continue;
            }

            choice = sc.nextByte();

            if (choice < 1 || choice > 12) {
                System.out.println("Invalid choice.");
            }

        } while (choice < 1 || choice > 12);

        return choice;
    }


    // Helper Methods


    static int findCarIndexById(String id) {

        for (int i = 0; i < Car.getCarCount(); i++) {
            if (cars[i].getId().equals(id)) {
                return i;
            }
        }

        return -1;
    }

    static Car findCarById(String id) {

        int index = findCarIndexById(id);

        if (index == -1)
            return null;

        return cars[index];
    }

    static Customer findCustomerById(String id) {

        for (int i = 0; i < Customer.getCustomerCount(); i++) {

            if (customers[i].getId().equals(id)) {
                return customers[i];
            }
        }

        return null;
    }

    static boolean carIdExists(String id) {
        return findCarById(id) != null;
    }

    static boolean customerIdExists(String id) {
        return findCustomerById(id) != null;
    }

    static int availableCarsCount() {

        int count = 0;

        for (int i = 0; i < Car.getCarCount(); i++) {

            if (cars[i].isAvailable()) {
                count++;
            }
        }

        return count;
    }

    static Car getMostExpensiveCar() {

        if (Car.getCarCount() == 0)
            return null;

        Car max = cars[0];

        for (int i = 1; i < Car.getCarCount(); i++) {

            if (cars[i].getPricePerDay() > max.getPricePerDay()) {
                max = cars[i];
            }
        }

        return max;
    }

    static double averageDailyPrice() {

        if (Car.getCarCount() == 0)
            return 0;

        double sum = 0;

        for (int i = 0; i < Car.getCarCount(); i++) {
            sum += cars[i].getPricePerDay();
        }

        return sum / Car.getCarCount();
    }


    static void addRegularCar() {

        if (Car.getCarCount() >= MAX_NUM_OF_CARS) {
            System.out.println("Cannot add more cars. Fleet is full.");
            return;
        }

        sc.nextLine();

        System.out.print("Enter Car ID: ");
        String id = sc.nextLine();

        if (carIdExists(id)) {
            System.out.println("A car with this ID already exists.");
            return;
        }

        System.out.print("Enter Brand: ");
        String brand = sc.nextLine();

        System.out.print("Enter Model: ");
        String model = sc.nextLine();

        System.out.print("Enter Manufacturing Year: ");
        int year = sc.nextInt();

        if (year < 1990 || year > 2026) {
            System.out.println("Year must be between 1990 and 2026.");
            return;
        }

        System.out.print("Enter Price Per Day: ");
        double price = sc.nextDouble();

        if (price <= 0) {
            System.out.println("Price per day must be greater than zero.");
            return;
        }

        cars[Car.getCarCount()] = new Car(id, brand, model, year, price);

        System.out.println("Regular car added successfully.");
    }

    static void addLuxuryCar() {

        if (Car.getCarCount() >= MAX_NUM_OF_CARS) {
            System.out.println("Cannot add more cars. Fleet is full.");
            return;
        }

        sc.nextLine();

        System.out.print("Enter Car ID: ");
        String id = sc.nextLine();

        if (carIdExists(id)) {
            System.out.println("A car with this ID already exists.");
            return;
        }

        System.out.print("Enter Brand: ");
        String brand = sc.nextLine();

        System.out.print("Enter Model: ");
        String model = sc.nextLine();

        System.out.print("Enter Manufacturing Year: ");
        int year = sc.nextInt();

        if (year < 1990 || year > 2026) {
            System.out.println("Year must be between 1990 and 2026.");
            return;
        }

        System.out.print("Enter Price Per Day: ");
        double price = sc.nextDouble();

        if (price <= 0) {
            System.out.println("Price per day must be greater than zero.");
            return;
        }

        System.out.print("Enter Insurance Fee: ");
        double insurance = sc.nextDouble();

        if (insurance < 0) {
            System.out.println("Insurance fee cannot be negative.");
            return;
        }

        cars[Car.getCarCount()] =
                new LuxuryCar(id, brand, model, year, price, insurance);

        System.out.println("Luxury car added successfully.");
    }

    static void addCustomer() {

        if (Customer.getCustomerCount() >= MAX_NUM_OF_CUSTOMERS) {
            System.out.println("Cannot add more customers.");
            return;
        }

        sc.nextLine();

        System.out.print("Enter Customer ID: ");
        String id = sc.nextLine();

        if (customerIdExists(id)) {
            System.out.println("Customer ID already exists.");
            return;
        }

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Phone Number: ");
        String phone = sc.nextLine();

        customers[Customer.getCustomerCount()] =
                new Customer(id, name, phone);

        System.out.println("Customer added successfully.");
    }



    static void displayAllCars() {

        if (Car.getCarCount() == 0) {
            System.out.println("No cars in the fleet.");
            return;
        }

        System.out.println("\n========== ALL CARS ==========");

        for (int i = 0; i < Car.getCarCount(); i++) {
            System.out.println((i + 1) + ".");
            cars[i].displayInfo();
        }
    }

    static void displayAvailableCars() {

        if (Car.getCarCount() == 0) {
            System.out.println("No cars in the fleet.");
            return;
        }

        int count = 0;

        System.out.println("\n====== AVAILABLE CARS ======");

        for (int i = 0; i < Car.getCarCount(); i++) {

            if (cars[i].isAvailable()) {
                count++;
                System.out.println(count + ".");
                cars[i].displayInfo();
            }
        }

        if (count == 0) {
            System.out.println("There are no available cars.");
        } else {
            System.out.println("Total Available Cars: " + count);
        }
    }


    static void searchCarById() {

        if (Car.getCarCount() == 0) {
            System.out.println("No cars available.");
            return;
        }

        sc.nextLine();

        System.out.print("Enter Car ID: ");
        String id = sc.nextLine();

        Car car = findCarById(id);

        if (car == null) {
            System.out.println("Car not found.");
            return;
        }

        car.displayInfo();
    }

    static void searchCarByBrand() {

        if (Car.getCarCount() == 0) {
            System.out.println("No cars available.");
            return;
        }

        sc.nextLine();

        System.out.print("Enter Brand: ");
        String brand = sc.nextLine();

        int matches = 0;

        for (int i = 0; i < Car.getCarCount(); i++) {

            if (cars[i].getBrand().equalsIgnoreCase(brand)) {

                matches++;
                System.out.println(matches + ".");
                cars[i].displayInfo();
            }
        }

        if (matches == 0) {
            System.out.println("No cars found with this brand.");
        } else {
            System.out.println("Number of matches: " + matches);
        }
    }



    static void rentCar() {

        if (Car.getCarCount() == 0 || Customer.getCustomerCount() == 0) {
            System.out.println("Cars or customers are not available.");
            return;
        }

        sc.nextLine();

        System.out.print("Enter Customer ID: ");
        String customerId = sc.nextLine();

        Customer customer = findCustomerById(customerId);

        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        if (customer.hasRentedCar()) {
            System.out.println("Customer already has a rented car.");
            return;
        }

        System.out.print("Enter Car ID: ");
        String carId = sc.nextLine();

        Car car = findCarById(carId);

        if (car == null) {
            System.out.println("Car not found.");
            return;
        }

        if (!car.isAvailable()) {
            System.out.println("This car is already rented.");
            return;
        }

        System.out.print("Enter Number of Rental Days: ");
        int days = sc.nextInt();

        if (days <= 0) {
            System.out.println("Number of rental days must be greater than zero.");
            return;
        }

        if (car instanceof LuxuryCar) {

            if (days < LuxuryCar.getMinimumNumberOfRentalDays()) {
                System.out.println("Luxury cars must be rented for at least "
                        + LuxuryCar.getMinimumNumberOfRentalDays() + " days.");
                return;
            }
        }

        double cost = car.calculateRentalCost(days);

        car.setAvailable(false);
        customer.rentCar(car.getId(), days);
        customer.addPayment(cost);

        totalIncome += cost;

        System.out.println("\n========== RENTAL RECEIPT ==========");
        System.out.println("Customer : " + customer.getName());
        System.out.println("Car      : " + car.getBrand() + " " + car.getModel());
        System.out.println("Days     : " + days);
        System.out.printf("Total Cost (with tax): %.2f%n", cost);
    }



    static void returnCar() {

        if (Customer.getCustomerCount() == 0) {
            System.out.println("No customers available.");
            return;
        }

        sc.nextLine();

        System.out.print("Enter Customer ID: ");
        String customerId = sc.nextLine();

        Customer customer = findCustomerById(customerId);

        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        if (!customer.hasRentedCar()) {
            System.out.println("This customer has no rented car.");
            return;
        }

        Car car = findCarById(customer.getRentedCarId());

        if (car != null) {
            car.setAvailable(true);
        }

        String returnedCar = car.getBrand() + " " + car.getModel();

        customer.returnCar();

        System.out.println("Car returned successfully.");
        System.out.println("Returned Car: " + returnedCar);
    }


    static void displayAllCustomers() {

        if (Customer.getCustomerCount() == 0) {
            System.out.println("No customers found.");
            return;
        }

        System.out.println("\n========== CUSTOMERS ==========");

        for (int i = 0; i < Customer.getCustomerCount(); i++) {

            Customer customer = customers[i];

            System.out.println("--------------------------------");
            System.out.println("ID: " + customer.getId());
            System.out.println("Name: " + customer.getName());
            System.out.println("Phone: " + customer.getPhoneNumber());

            if (!customer.hasRentedCar()) {

                System.out.println("Current Car: None");

            } else {

                Car car = findCarById(customer.getRentedCarId());

                if (car != null) {
                    System.out.println("Current Car: "
                            + car.getBrand() + " "
                            + car.getModel()
                            + " (" + car.getId() + ")");
                } else {
                    System.out.println("Current Car: Unknown");
                }
            }

            System.out.printf("Total Paid: %.2f%n", customer.getTotalPaid());
        }
    }


    static void displayOfficeStatistics() {

        System.out.println("\n========== OFFICE STATISTICS ==========");

        System.out.printf("Total Income: %.2f%n", totalIncome);

        int rentedCars = Car.getCarCount() - availableCarsCount();

        System.out.println("Number of Rented Cars: " + rentedCars);

        Car expensive = getMostExpensiveCar();

        if (expensive == null) {
            System.out.println("Most Expensive Car: None");
        } else {
            System.out.println("Most Expensive Car: "
                    + expensive.getBrand() + " "
                    + expensive.getModel()
                    + " (" + expensive.getPricePerDay() + "/day)");
        }

        System.out.printf("Average Daily Price: %.2f%n",
                averageDailyPrice());
    }


    static void exitMessage() {

        System.out.println();
        System.out.println("========================================");
        System.out.println("  Thank you for using SPEEDWAY RENTALS  ");
        System.out.println("========================================");

        System.out.println("Total Cars: " + Car.getCarCount());
        System.out.println("Total Customers: " + Customer.getCustomerCount());
        System.out.println("Total Income: "+ totalIncome);

    }

}
