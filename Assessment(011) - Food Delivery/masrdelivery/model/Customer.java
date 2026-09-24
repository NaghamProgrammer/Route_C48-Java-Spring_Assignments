package masrdelivery.model;

import masrdelivery.exceptions.InsufficientBalanceException;
import masrdelivery.exceptions.ValidationException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class Customer {
    private static final int SEARCH_HISTORY_SIZE = 5;

    private final int id;
    private String name;
    private String mobile;
    private final List<Address> addresses = new ArrayList<>();
    private Money wallet = Money.ZERO;
    private int completedOrders = 0;
    private final Deque<String> recentSearches = new ArrayDeque<>(); // newest at the front

    public Customer(int id, String name, String mobile) {
        this.id = id;
        this.name = name;
        setMobile(mobile);
    }

    public static void validateMobile(String mobile) {
        if (mobile == null || !mobile.matches("(010|011|012|015)\\d{8}")) {
            throw new ValidationException("Mobile number must be 11 digits starting with 010, 011, 012, or 015");
        }
    }

    public void setMobile(String mobile) {
        validateMobile(mobile);
        this.mobile = mobile;
    }

    public void addAddress(Address address) { addresses.add(address); }
    public List<Address> getAddresses() { return Collections.unmodifiableList(addresses); } // Part C.8
    public boolean ownsAddress(Address address) { return addresses.contains(address); }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getMobile() { return mobile; }
    public Money getWallet() { return wallet; }
    public int getCompletedOrders() { return completedOrders; }
    public LoyaltyTier getTier() { return LoyaltyTier.fromCompletedOrders(completedOrders); }

    public void credit(Money amount) { wallet = wallet.add(amount); }

    public void debit(Money amount) {
        if (wallet.compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                    "Wallet balance " + wallet + " is insufficient to pay " + amount);
        }
        wallet = wallet.subtract(amount);
    }

    public void recordCompletedOrder() { completedOrders++; }

    public void recordSearch(String query) {
        recentSearches.addFirst(query);
        while (recentSearches.size() > SEARCH_HISTORY_SIZE) recentSearches.removeLast();
    }

    public List<String> getRecentSearches() { return List.copyOf(recentSearches); }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        return id == ((Customer) o).id;
    }
    @Override public int hashCode() { return Integer.hashCode(id); }
    @Override public String toString() { return "Customer#" + id + " " + name + " [" + getTier() + "]"; }
}
