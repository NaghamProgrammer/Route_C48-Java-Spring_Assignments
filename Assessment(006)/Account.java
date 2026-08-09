public abstract class Account {

    private int accountNumber;
    private Customer owner;
    private double balance;
    private AccountStatus status;
    private int transactionCount;

    public Account(int accountNumber, Customer owner, double balance, AccountStatus status) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.balance = balance;
        this.status = status;
        this.transactionCount = 0;
    }

    public abstract boolean withdraw(double amount);
    public abstract boolean deposit(double amount);


    public abstract String getAccountType();


    public abstract void displaySpecificDetails();

    public int getAccountNumber() {
        return accountNumber;
    }

    public Customer getOwner() {
        return owner;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    // do NOT make a setter for balance we don't want missing with it
    public double getBalance() {
        return balance;
    }

    protected void increaseBalance(double amount) {
        balance += amount;
        transactionCount++;
    }

    protected void decreaseBalance(double amount) {
        balance -= amount;
        transactionCount++;
    }


    public void restoreBalance(double amount) {
        balance += amount;
    }

    public void printCommonDetails() {
        System.out.println("Account Number : " + accountNumber);
        System.out.println("Owner: " + owner.getName() + " (Customer ID: " + owner.getCustomerID() + ")");
        System.out.println("Type: " + getAccountType());
        System.out.println("Balance: $" + String.format("%.2f", balance));
        System.out.println("Status: " + status);
        System.out.println("Transactions: " + transactionCount);
    }
}