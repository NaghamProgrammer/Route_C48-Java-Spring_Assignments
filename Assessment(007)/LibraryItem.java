public abstract class LibraryItem {
    private String catalogueId; //no setter
    private String title; //no setter
    private ItemStatus status;
    private String borrowerName;
    private int renewalCount;
    private static String libraryName;
    private static double administrativeCharge;
    private static int runningCount;

    public LibraryItem(String catalogueId, String title) {
        this.catalogueId = catalogueId;
        this.title = title;
        this.status = ItemStatus.AVAILABLE;
        this.borrowerName = null;
        this.renewalCount = 0;

        runningCount++;
    }

    //getters for everything because "Report all of its properties"
    public String getCatalogueId() {
        return catalogueId;
    }

    public String getTitle() {
        return title;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public int getRenewalCount() {
        return renewalCount;
    }

    public static String getLibraryName() {
        return libraryName;
    }

    public static double getAdministrativeCharge() {
        return administrativeCharge;
    }

    public static int getRunningCount() {
        return runningCount;
    }


    public static void setAdministrativeCharge(double administrativeCharge) {
        LibraryItem.administrativeCharge = administrativeCharge;
    }
    public static void setLibraryName(String libraryName) {
        LibraryItem.libraryName = libraryName;
    }


    //Be marked reserved or lost, and brought back
    public void makeReserved() {
        this.status = ItemStatus.RESERVED;
    }

    public void makeLost() {
        this.status = ItemStatus.LOST;
    }

    //taking an item back cannot be overridden -> final method
    public final void bringBack() {
        this.status = ItemStatus.AVAILABLE;
        this.borrowerName = null;
        this.renewalCount = 0;
    }


    public boolean lend(String memberName) {
        if (status == ItemStatus.AVAILABLE) {

            status = ItemStatus.ON_LOAN;
            borrowerName = memberName;
            return true;
        }
        else  {
            return  false;
        }
    }

    //abstract methods to answer the 3 questions
    public abstract double calculateFine(int daysOverdue);

    public abstract int getLoanPeriod();

    public abstract String getCategory();


    //protected because Book and Magazine need access to it, but code outside the class shouldn't be able to
    protected void recordRenewal() {
        renewalCount++;
    }

    public void display() {
        System.out.println(
                catalogueId + " | " +
                        getCategory() + " | " +
                        title + " | " +
                        status + " | " +
                        borrowerName + " | " +
                        getLoanPeriod() + " days | " +
                        calculateFine(1) + " EGP"
        );
    }


}
