import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    //Storage
    static final int MAX_CUSTOMERS = 200;
    static final int MAX_ACCOUNTS = 500;
    static Customer[] customers = new Customer[MAX_CUSTOMERS];
    static Account[] accounts = new Account[MAX_ACCOUNTS];
    static int customerCount = 0;
    static int accountCount = 0;

    //System configuration
    static final double MIN_OPENING_BALANCE_SAVINGS = 100.0;
    static final double MIN_OPENING_BALANCE_CURRENT = 50.0;
    static final double MIN_OPENING_BALANCE_FIXED = 500.0;
    static final double MIN_TRANSACTION_AMOUNT = 1.0;

    static int nextAccountNumber = 2001;

    public static void main(String[] args) {

        boolean running = true;

        while (running) {

            byte choice = menu();

            switch (choice) {

                case 1:
                    registerCustomer();
                    break;

                case 2:
                    openAccount();
                    break;

                case 3:
                    depositMoney();
                    break;

                case 4:
                    withdrawMoney();
                    break;

                case 5:
                    transferBetweenAccounts();
                    break;

                case 6:
                    displayCustomerAccounts();
                    break;

                case 7:
                    displayAllBranchAccounts();
                    break;

                case 8:
                    searchAccountByNumber();
                    break;

                case 9:
                    searchAccountsByType();
                    break;

                case 10:
                    closeAccount();
                    break;

                case 11:
                    running = false;
                    System.out.println("\nThank you for banking with Al Manara Bank");
                    break;
            }
        }
    }

    static byte menu() {

        byte choice = 0;

        do {

            System.out.println();
            System.out.println("========================================");
            System.out.println("         Al Manara Bank");
            System.out.println("========================================");
            System.out.println("1. Register New Customer");
            System.out.println("2. Open New Account");
            System.out.println("3. Deposit Money");
            System.out.println("4. Withdraw Money");
            System.out.println("5. Transfer Between Accounts");
            System.out.println("6. Display Customer Accounts");
            System.out.println("7. Display All Branch Accounts");
            System.out.println("8. Search Account by Number");
            System.out.println("9. Search Accounts by Type");
            System.out.println("10. Close an Account");
            System.out.println("11. Exit");
            System.out.print("Enter your choice: ");

            if (!sc.hasNextByte()) {
                System.out.println("Invalid choice.");
                sc.next();
                continue;
            }

            choice = sc.nextByte();
            sc.nextLine();

            if (choice < 1 || choice > 11) {
                System.out.println("Invalid choice.");
            }

        } while (choice < 1 || choice > 11);

        return choice;
    }


    // input helpers

    static String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    static int readInt(String prompt) {
        while (true) {
            String input = readLine(prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, Please enter a whole number.");
            }
        }
    }

    static double readDouble(String prompt) {
        while (true) {
            String input = readLine(prompt);
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, Please enter a valid number.");
            }
        }
    }

    // Lookup helpers

    static Customer findCustomerById(int id) {
        for (int i = 0; i < customerCount; i++) {
            if (customers[i].getCustomerID() == id) {
                return customers[i];
            }
        }
        return null;
    }

    static Account findAccountByNumber(int number) {
        for (int i = 0; i < accountCount; i++) {
            if (accounts[i].getAccountNumber() == number) {
                return accounts[i];
            }
        }
        return null;
    }

    static boolean isNationalIdTaken(String nationalID) {
        for (int i = 0; i < customerCount; i++) {
            if (customers[i].getNationalID().equalsIgnoreCase(nationalID)) {
                return true;
            }
        }
        return false;
    }


    // Register New Customer


    static void registerCustomer() {

        System.out.println("\n--- Register New Customer ---");

        if (customerCount >= MAX_CUSTOMERS) {
            System.out.println("Cannot register: customer storage is full.");
            return;
        }

        String name = readLine("Full name: ");
        if (name.isEmpty()) {
            System.out.println("Registration failed: name cannot be empty.");
            return;
        }

        String nationalID = readLine("National ID: ");
        if (nationalID.isEmpty()) {
            System.out.println("Registration failed: national ID cannot be empty.");
            return;
        }
        if (isNationalIdTaken(nationalID)) {
            System.out.println("Registration failed: this national ID is already registered.");
            return;
        }

        String phoneNumber = readLine("Phone number (optional, press Enter to skip): ");
        if (!phoneNumber.isEmpty()) {
            if (!phoneNumber.matches("\\d{7,15}")) {
                System.out.println("Registration failed: phone number must contain 7 to 15 digits only.");
                return;
            }
        }

        CustomerTiers tier = chooseTier();
        if (tier == null) {
            System.out.println("Registration cancelled: invalid tier selection.");
            return;
        }

        Customer customer = new Customer(name, nationalID, phoneNumber, tier);
        customers[customerCount++] = customer;

        System.out.println("Customer registered successfully.");
        System.out.println("Customer ID: " + customer.getCustomerID());
    }

    static CustomerTiers chooseTier() {
        System.out.println("Select tier:");
        System.out.println("1. Standard (fee $" + CustomerTiers.standard.getFee()
                + ", bonus $" + CustomerTiers.standard.getBonus() + ")");
        System.out.println("2. Silver (fee $" + CustomerTiers.silver.getFee()
                + ", bonus $" + CustomerTiers.silver.getBonus() + ")");
        System.out.println("3. Gold (fee $" + CustomerTiers.gold.getFee()
                + ", bonus $" + CustomerTiers.gold.getBonus() + ")");
        int choice = readInt("Choice: ");

        switch (choice) {
            case 1: return CustomerTiers.standard;
            case 2: return CustomerTiers.silver;
            case 3: return CustomerTiers.gold;
            default: return null;
        }
    }


    // Open New Account


    static void openAccount() {

        System.out.println("\n--- Open New Account ---");

        if (accountCount >= MAX_ACCOUNTS) {
            System.out.println("Cannot open account: account storage is full.");
            return;
        }

        int customerId = readInt("Customer ID: ");
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            System.out.println("No customer found with ID " + customerId + ". Account not created.");
            return;
        }

        System.out.println("Select account type:");
        System.out.println("1. Savings: (minimum opening balance $" + MIN_OPENING_BALANCE_SAVINGS + ")");
        System.out.println("2. Current: (minimum opening balance $" + MIN_OPENING_BALANCE_CURRENT + ")");
        System.out.println("3. Fixed Deposit (minimum opening balance $" + MIN_OPENING_BALANCE_FIXED + ")");
        int typeChoice = readInt("Choice: ");

        if (typeChoice < 1 || typeChoice > 3) {
            System.out.println("Invalid account type. Account not created.");
            return;
        }

        double openingBalance = readDouble("Opening balance: ");

        Account newAccount;
        int accountNumber = nextAccountNumber;

        switch (typeChoice) {

            case 1:
                if (openingBalance < MIN_OPENING_BALANCE_SAVINGS) {
                    System.out.println("Opening balance is below the minimum of $" + MIN_OPENING_BALANCE_SAVINGS + ". Account not created.");
                    return;
                }
                newAccount = new SavingsAccount(accountNumber, customer, openingBalance, AccountStatus.active);
                break;

            case 2:
                if (openingBalance < MIN_OPENING_BALANCE_CURRENT) {
                    System.out.println("Opening balance is below the minimum of $" + MIN_OPENING_BALANCE_CURRENT + ". Account not created.");
                    return;
                }
                newAccount = new CurrentAccount(accountNumber, customer, openingBalance, AccountStatus.active);
                break;

            case 3:
                if (openingBalance < MIN_OPENING_BALANCE_FIXED) {
                    System.out.println("Opening balance is below the minimum of $" + MIN_OPENING_BALANCE_FIXED + ". Account not created.");
                    return;
                }
                int duration = readInt("Duration in months: ");
                if (duration <= 0) {
                    System.out.println("Invalid duration. Account not created.");
                    return;
                }
                newAccount = new FixedDepositAccount(accountNumber, customer, openingBalance, AccountStatus.active, duration);
                break;

            default:
                return;
        }

        accounts[accountCount++] = newAccount;
        nextAccountNumber++;
        customer.addAccount(newAccount);

        System.out.println("Account opened successfully.");
        System.out.println("Account Number: " + accountNumber);
    }


    // Deposit Money


    static void depositMoney() {

        System.out.println("\n--- Deposit Money ---");

        int accountNumber = readInt("Account number: ");
        Account account = findAccountByNumber(accountNumber);
        if (account == null) {
            System.out.println("No account found with number " + accountNumber + ".");
            return;
        }

        if (account.getStatus() != AccountStatus.active) {
            System.out.println("Deposit rejected: account is " + account.getStatus() + ".");
            return;
        }

        double amount = readDouble("Deposit amount: ");
        if (amount < MIN_TRANSACTION_AMOUNT) {
            System.out.println("Deposit rejected: minimum transaction amount is $" + MIN_TRANSACTION_AMOUNT + ".");
            return;
        }

        if (account.deposit(amount)) {
            System.out.println("Deposit successful. New balance: $" + String.format("%.2f", account.getBalance()));
        } else {
            System.out.println("Deposit failed.");
        }
    }


    // Withdraw Money


    static void withdrawMoney() {

        System.out.println("\n--- Withdraw Money ---");

        int accountNumber = readInt("Account number: ");
        Account account = findAccountByNumber(accountNumber);
        if (account == null) {
            System.out.println("No account found with number " + accountNumber + ".");
            return;
        }

        if (account.getStatus() != AccountStatus.active) {
            System.out.println("Withdrawal rejected: account is " + account.getStatus() + ".");
            return;
        }

        double amount = readDouble("Withdrawal amount: ");
        if (amount < MIN_TRANSACTION_AMOUNT) {
            System.out.println("Withdrawal rejected: minimum transaction amount is $" + MIN_TRANSACTION_AMOUNT + ".");
            return;
        }

        if (account.withdraw(amount)) {
            System.out.println("Withdrawal successful, New balance: $" + String.format("%.2f", account.getBalance()));
        } else {
            System.out.println("Withdrawal failed.");
        }
    }

    // Transfer Between Accounts


    static void transferBetweenAccounts() {

        System.out.println("\n--- Transfer Between Accounts ---");

        int sourceNumber = readInt("Source account number: ");
        int destNumber = readInt("Destination account number: ");

        if (sourceNumber == destNumber) {
            System.out.println("Transfer failed: source and destination must be different accounts.");
            return;
        }

        Account source = findAccountByNumber(sourceNumber);
        Account destination = findAccountByNumber(destNumber);

        if (source == null || destination == null) {
            System.out.println("Transfer failed: one or both accounts do not exist.");
            return;
        }

        if (source.getStatus() != AccountStatus.active) {
            System.out.println("Transfer failed: source account is " + source.getStatus() + ".");
            return;
        }
        if (destination.getStatus() != AccountStatus.active) {
            System.out.println("Transfer failed: destination account is " + destination.getStatus() + ".");
            return;
        }

        double amount = readDouble("Transfer amount: ");
        if (amount < MIN_TRANSACTION_AMOUNT) {
            System.out.println("Transfer failed: minimum transaction amount is $" + MIN_TRANSACTION_AMOUNT + ".");
            return;
        }

        if (!source.withdraw(amount)) {
            System.out.println("Transfer failed: source account could not complete the withdrawal.");
            return;
        }

        if (!destination.deposit(amount)) {
            source.restoreBalance(amount);
            System.out.println("Transfer failed: destination could not accept the deposit. Funds restored to source.");
            return;
        }

        System.out.println("Transfer successful.");
        System.out.println("Source new balance:      $" + String.format("%.2f", source.getBalance()));
        System.out.println("Destination new balance: $" + String.format("%.2f", destination.getBalance()));
    }


    // Display Customer Accounts


    static void displayCustomerAccounts() {

        System.out.println("\n--- Display Customer Accounts ---");

        int customerId = readInt("Customer ID: ");
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            System.out.println("No customer found with ID " + customerId + ".");
            return;
        }

        System.out.println("\nCustomer: " + customer.getName() + " | National ID: " + customer.getNationalID() + " | Tier: " + customer.getCustomerTier());

        if (customer.getAccounts().isEmpty()) {
            System.out.println("This customer has no accounts.");
            return;
        }

        double totalBalance = 0;
        for (Account account : customer.getAccounts()) {
            System.out.println("----------------------------------------");
            account.printCommonDetails();
            totalBalance += account.getBalance();
        }
        System.out.println("----------------------------------------");
        System.out.println("Combined balance across all accounts: $" + String.format("%.2f", totalBalance));
    }


    // Display All Branch Accounts


    static void displayAllBranchAccounts() {

        System.out.println("\n--- All Branch Accounts ---");

        if (accountCount == 0) {
            System.out.println("There are no accounts in the system yet.");
            return;
        }

        System.out.printf("%-8s %-20s %-15s %-12s %-10s %-8s%n", "AccNo", "Owner", "Type", "Balance", "Status", "Txns");

        for (int i = 0; i < accountCount; i++) {
            Account a = accounts[i];
            System.out.printf("%-8d %-20s %-15s %-12s %-10s %-8d%n",
                    a.getAccountNumber(),
                    a.getOwner().getName(),
                    a.getAccountType(),
                    "$" + String.format("%.2f", a.getBalance()),
                    a.getStatus(),
                    a.getTransactionCount());
        }
    }


    // Search Account by Number


    static void searchAccountByNumber() {

        System.out.println("\n--- Search Account by Number ---");

        int accountNumber = readInt("Account number: ");
        Account account = findAccountByNumber(accountNumber);

        if (account == null) {
            System.out.println("No account found with number " + accountNumber + ".");
            return;
        }

        account.printCommonDetails();
        account.displaySpecificDetails();
    }


    //  Search Accounts by Type


    static void searchAccountsByType() {

        System.out.println("\n--- Search Accounts by Type ---");
        System.out.println("1. Savings");
        System.out.println("2. Current");
        System.out.println("3. Fixed Deposit");
        int choice = readInt("Choice: ");

        String targetType;
        switch (choice) {
            case 1: targetType = "Savings"; break;
            case 2: targetType = "Current"; break;
            case 3: targetType = "Fixed Deposit"; break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        int matchCount = 0;
        double totalBalance = 0;

        for (int i = 0; i < accountCount; i++) {
            Account a = accounts[i];
            if (a.getAccountType().equals(targetType)) {
                System.out.println("----------------------------------------");
                a.printCommonDetails();
                matchCount++;
                totalBalance += a.getBalance();
            }
        }

        System.out.println("----------------------------------------");
        if (matchCount == 0) {
            System.out.println("No accounts of type " + targetType + " found.");
        } else {
            System.out.println("Matching accounts: " + matchCount);
            System.out.println("Combined balance: $" + String.format("%.2f", totalBalance));
        }
    }


    //Close an Account


    static void closeAccount() {

        System.out.println("\n--- Close an Account ---");

        int accountNumber = readInt("Account number: ");
        Account account = findAccountByNumber(accountNumber);

        if (account == null) {
            System.out.println("No account found with number " + accountNumber + ".");
            return;
        }

        if (account.getStatus() == AccountStatus.closed) {
            System.out.println("This account is already closed.");
            return;
        }

        if (account.getBalance() != 0.0) {
            System.out.println("Cannot close account: balance must be exactly $0 (current balance: $"
                    + String.format("%.2f", account.getBalance()) + ").");
            return;
        }

        if (account instanceof FixedDepositAccount) {
            FixedDepositAccount fd = (FixedDepositAccount) account;
            if (!fd.isMatured()) {
                System.out.println("Cannot close account: fixed deposit has not matured yet.");
                return;
            }
        }

        account.setStatus(AccountStatus.closed);
        System.out.println("Account " + accountNumber + " closed successfully.");
    }
}