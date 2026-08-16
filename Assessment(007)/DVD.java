public class DVD extends LibraryItem{

    private double runtime;

    private static final int DURATION_ALLOWED_TO_BE_KEPT = 3;
    private static final double COST_PER_OVERDUE_DAY = 15.0;


    public DVD(String catalogueId, String title, double runtime) {
        super(catalogueId, title);
        this.runtime = runtime;
    }

    @Override
    public double calculateFine(int daysOverdue) {
        return daysOverdue * COST_PER_OVERDUE_DAY;
    }

    @Override
    public int getLoanPeriod() {
        return DURATION_ALLOWED_TO_BE_KEPT;
    }

    @Override
    public String getCategory() {
        return "DVD";
    }
}
