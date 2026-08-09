import java.util.ArrayList;

public class Customer {

    private static int nextCustomerID = 1;

    private int customerID;
    private String name;
    private String nationalID;
    private String phoneNumber;
    private CustomerTiers customerTier;

    private ArrayList<Account> accounts;

    public Customer(String name, String nationalID,
                    String phoneNumber, CustomerTiers customerTier) {

        this.customerID = nextCustomerID++;
        this.name = name;
        this.nationalID = nationalID;
        this.phoneNumber = phoneNumber;
        this.customerTier = customerTier;

        accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void removeAccount(Account account) {
        accounts.remove(account);
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getName() {
        return name;
    }

    public String getNationalID() {
        return nationalID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public CustomerTiers getCustomerTier() {
        return customerTier;
    }

    public int getOpenAccountCount() {
        int count = 0;
        for (Account account : accounts) {
            if (account.getStatus() != AccountStatus.closed) {
                count++;
            }
        }
        return count;
    }
}