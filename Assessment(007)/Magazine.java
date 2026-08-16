public class Magazine extends LibraryItem implements Renewable{

    private int issueNumber;

    private static final int DURATION_ALLOWED_TO_BE_KEPT = 7;
    private static final double COST_PER_OVERDUE_DAY = 3.0;
    private static final double COST_PER_OVERDUE_DAY_LIMIT = 30.0;
    private static final int ALLOWED_RENEW_CHANCES = 1;

    public Magazine(String catalogueId, String title, int issueNumber) {
        super(catalogueId, title);
        this.issueNumber = issueNumber;
    }

    @Override
    public double calculateFine(int daysOverdue) {
        return Math.min(  daysOverdue * COST_PER_OVERDUE_DAY    ,   COST_PER_OVERDUE_DAY_LIMIT);
    }

    @Override
    public int getLoanPeriod() {
        return DURATION_ALLOWED_TO_BE_KEPT;
    }

    @Override
    public String getCategory() {
        return "Magazine";
    }

    @Override
    public int reportRenewalLimit() {
        return ALLOWED_RENEW_CHANCES;
    }

    @Override
    public boolean renewLoan() {
        if (getStatus() == ItemStatus.ON_LOAN &&
                getRenewalCount() < ALLOWED_RENEW_CHANCES) {

            recordRenewal();
            return true;
        }

        return false;
    }
}
