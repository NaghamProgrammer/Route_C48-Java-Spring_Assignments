package masrdelivery.builder;

import masrdelivery.exceptions.ValidationException;
import masrdelivery.model.Address;
import masrdelivery.model.Customer;
import masrdelivery.model.LineItem;
import masrdelivery.model.MenuItem;
import masrdelivery.model.Order;
import masrdelivery.model.Restaurant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


 //builder pattern for the multi step, mostly-optional job of
 //assembling an order (address, several line items, an optional promo,optional notes)
 //instead of a constructor with nine mostly-null params.

public class OrderBuilder {
    private final int id;
    private final Customer customer;
    private final Restaurant restaurant;
    private Address deliveryAddress;
    private final List<LineItem> lineItems = new ArrayList<>();
    private String promotionCode;
    private String notes = "";

    public OrderBuilder(int id, Customer customer, Restaurant restaurant) {
        this.id = id;
        this.customer = customer;
        this.restaurant = restaurant;
    }

    public OrderBuilder deliverTo(Address address) {
        if (!customer.ownsAddress(address))
            throw new ValidationException("Delivery address must belong to the customer placing the order");
        this.deliveryAddress = address;
        return this;
    }

    public OrderBuilder addLineItem(MenuItem item, double amount) {
        lineItems.add(new LineItem(item, amount));
        return this;
    }

    public OrderBuilder withPromotion(String code) { this.promotionCode = code; return this; }
    public OrderBuilder withNotes(String notes) { this.notes = notes == null ? "" : notes; return this; }

    public Order build() {
        if (deliveryAddress == null) throw new ValidationException("Delivery address is required");
        return new Order(id, customer, restaurant, deliveryAddress, lineItems, promotionCode, notes, LocalDateTime.now());
    }
}
