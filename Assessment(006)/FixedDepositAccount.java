public class FixedDepositAccount extends Account {

    private static final double INTEREST_RATE = 0.01;
    private int durationInMonths = 20;
    private int passedMonths = 0;

    public FixedDepositAccount(int accountNumber, Customer owner, double balance, AccountStatus status, int durationInMonths) {
        super(accountNumber, owner, balance, status);
        this.durationInMonths = durationInMonths;
    }

    public boolean isMatured() {
        return passedMonths >= durationInMonths;
    }

    @Override
    public boolean withdraw(double amount) {

        if (amount <= 0 || amount > getBalance()) {
            System.out.println("Withdrawal rejected: invalid amount.");
            return false;
        }

        if (!isMatured()) {
            System.out.println("Withdrawal rejected: you can't withdraw before maturity.");
            System.out.println((durationInMonths - passedMonths) + " months remaining till you can withdraw.");
            return false;
        }

        decreaseBalance(amount);
        return true;
    }

    @Override
    public boolean deposit(double amount) {

        if (amount > 0) {
            increaseBalance(amount);
            return true;
        }

        System.out.println("Deposit rejected: amount must be positive.");
        return false;
    }

    public void advanceMonth() {
        passedMonths++;
    }

    public int getDurationInMonths() {
        return durationInMonths;
    }

    public int getPassedMonths() {
        return passedMonths;
    }

    public double getInterestRate() {
        return INTEREST_RATE;
    }

    @Override
    public String getAccountType() {
        return "Fixed Deposit";
    }

    @Override
    public void displaySpecificDetails() {
        System.out.println("Interest Rate: " + (INTEREST_RATE * 100) + "%");
        System.out.println("Duration: " + durationInMonths + " month");
        System.out.println("Months Passed: " + passedMonths);
        System.out.println("Matured: " + (isMatured() ? "Yes" : "No"));
    }
}