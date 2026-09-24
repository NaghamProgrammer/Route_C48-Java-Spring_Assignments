public class Car {

    private String id;
    private String brand;
    private String model;
    private int year;
    private double pricePerDay;
    private boolean available;

    private static int carCount;
    private static final double TAX_RATE = 0.14;


    public Car() {
        this("", "", "", 1990, 1);
    }


    public Car(String id, String brand, String model, int year, double pricePerDay) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.pricePerDay = pricePerDay;
        this.available = true;

        carCount++;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public boolean isAvailable() {
        return available;
    }

    public static int getCarCount() {
        return carCount;
    }

    public static double getTaxRate() {
        return TAX_RATE;
    }

    // Setters
    public void setPricePerDay(double pricePerDay) {
        if (pricePerDay > 0) {
            this.pricePerDay = pricePerDay;
        }
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    //calculate rental cost including tax
    public double calculateRentalCost(int days) {
        double cost = pricePerDay * days;
        cost += cost * TAX_RATE;
        return cost;
    }


    public void displayInfo() {
        System.out.println("----------------------------------------");
        System.out.println("ID: " + id);
        System.out.println("Brand: " + brand);
        System.out.println("Model: " + model);
        System.out.println("Year: " + year);
        System.out.printf("Price Per Day: %.2f%n", pricePerDay);
        System.out.println("Available: " + (available ? "Yes" : "No"));
    }

    @Override
    public String toString() {
        return "ID: " + id +
                " | Brand: " + brand +
                " | Model: " + model +
                " | Year: " + year +
                " | Price/Day: " + pricePerDay +
                " | Available: " + (available ? "Yes" : "No");
    }
}
