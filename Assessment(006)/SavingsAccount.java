public class SavingsAccount extends Account {

    private static final double ANNUAL_INTEREST_RATE = 0.25;
    private int monthlyWithdrawalCount = 0;

    public SavingsAccount(int accountNumber, Customer owner, double balance, AccountStatus status) {
        super(accountNumber, owner, balance, status);
    }

    @Override
    public boolean withdraw(double amount) {

        if (amount > 0 && amount <= getBalance()) {
            decreaseBalance(amount);
            monthlyWithdrawalCount++;
            return true;
        }

        System.out.println("Withdrawal rejected: insufficient balance (savings accounts cannot go negative).");
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

    public int getMonthlyWithdrawalCount() {
        return monthlyWithdrawalCount;
    }

    public double getAnnualInterestRate() {
        return ANNUAL_INTEREST_RATE;
    }

    @Override
    public String getAccountType() {
        return "Savings";
    }

    @Override
    public void displaySpecificDetails() {
        System.out.println("Annual Interest Rate: " + (ANNUAL_INTEREST_RATE * 100) + "%");
        System.out.println("Withdrawals This Month: " + monthlyWithdrawalCount);
    }
}