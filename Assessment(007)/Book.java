public class Book extends LibraryItem implements Renewable{

    private String author;
    private int pageCount;

    private static final int DURATION_ALLOWED_TO_BE_KEPT = 14;
    private static final double COST_PER_OVERDUE_DAY = 5.0;
    private static final int ALLOWED_RENEW_CHANCES = 2;


    public Book(String catalogueId, String title, String author, int pageCount) {
        super(catalogueId, title);
        this.author = author;
        this.pageCount = pageCount;
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
        return "Book";
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
