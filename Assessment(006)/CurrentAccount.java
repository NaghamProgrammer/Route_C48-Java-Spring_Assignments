public class CurrentAccount extends Account {

    private static final double NEGATIVE_OVERDRAFT_LIMIT = -1000.0;

    public CurrentAccount(int accountNumber, Customer owner, double balance, AccountStatus status) {
        super(accountNumber, owner, balance, status);
    }

    @Override
    public boolean withdraw(double amount) {

        if (amount > 0 && getBalance() - amount >= NEGATIVE_OVERDRAFT_LIMIT) {
            decreaseBalance(amount);
            return true;
        }

        System.out.println("Withdrawal rejected: would exceed the overdraft limit of $"
                + Math.abs(NEGATIVE_OVERDRAFT_LIMIT) + ".");
        return false;
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

    public boolean isUsingOverdraft() {
        return getBalance() < 0;
    }

    public double getOverdraftLimit() {
        return NEGATIVE_OVERDRAFT_LIMIT;
    }

    @Override
    public String getAccountType() {
        return "Current";
    }

    @Override
    public void displaySpecificDetails() {
        System.out.println("Overdraft Limit: $" + Math.abs(NEGATIVE_OVERDRAFT_LIMIT));
        System.out.println("Using Overdraft: " + (isUsingOverdraft() ? "Yes" : "No"));
    }
}