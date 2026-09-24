public class LuxuryCar extends Car {

    private double insuranceFee;
    private static final int MINIMUM_NUMBER_OF_RENTAL_DAYS = 3;

    // Default constructor (constructor chaining)
    public LuxuryCar() {
        this("", "", "", 1990, 1, 0);
    }

    // Main constructor
    public LuxuryCar(String id, String brand, String model,
                     int year, double pricePerDay,
                     double insuranceFee) {

        super(id, brand, model, year, pricePerDay);
        this.insuranceFee = insuranceFee;
    }

    // Getter
    public double getInsuranceFee() {
        return insuranceFee;
    }

    public static int getMinimumNumberOfRentalDays() {
        return MINIMUM_NUMBER_OF_RENTAL_DAYS;
    }

    // Setter
    public void setInsuranceFee(double insuranceFee) {
        if (insuranceFee >= 0) {
            this.insuranceFee = insuranceFee;
        }
    }

    // Override rental cost calculation
    @Override
    public double calculateRentalCost(int days) {
        double cost = (getPricePerDay() * days) + insuranceFee;
        cost += cost * getTaxRate();
        return cost;
    }

    // Display information
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.printf("Insurance Fee: %.2f%n", insuranceFee);
        System.out.println("Minimum Rental Days: " + MINIMUM_NUMBER_OF_RENTAL_DAYS);
    }

    @Override
    public String toString() {
        return super.toString() +
                " | Insurance Fee: " + insuranceFee +
                " | Luxury Car";
    }
}
