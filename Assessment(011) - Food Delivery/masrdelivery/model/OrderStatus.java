package masrdelivery.model;

import masrdelivery.exceptions.IllegalOrderTransitionException;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

//the order lifecycle, with CANCELLED reachable from anything before OUT_FOR_DELIVERY
public enum OrderStatus {
    PLACED, ACCEPTED, PREPARING, READY, ASSIGNED, OUT_FOR_DELIVERY, DELIVERED, CANCELLED;

    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED = new EnumMap<>(OrderStatus.class);

    static {
        ALLOWED.put(PLACED, EnumSet.of(ACCEPTED, CANCELLED));
        ALLOWED.put(ACCEPTED, EnumSet.of(PREPARING, CANCELLED));
        ALLOWED.put(PREPARING, EnumSet.of(READY, CANCELLED));
        ALLOWED.put(READY, EnumSet.of(ASSIGNED, CANCELLED));
        ALLOWED.put(ASSIGNED, EnumSet.of(OUT_FOR_DELIVERY, CANCELLED));
        ALLOWED.put(OUT_FOR_DELIVERY, EnumSet.of(DELIVERED));
        ALLOWED.put(DELIVERED, EnumSet.noneOf(OrderStatus.class));
        ALLOWED.put(CANCELLED, EnumSet.noneOf(OrderStatus.class));
    }

    public void assertCanTransitionTo(OrderStatus target) {
        if (!ALLOWED.get(this).contains(target)) {
            throw new IllegalOrderTransitionException("Cannot move an order from " + this + " to " + target);
        }
    }
}
