package masrdelivery.model;

import masrdelivery.dispatch.DispatchBehavior;
import masrdelivery.exceptions.RiderUnavailableException;

public class Rider {
    private final int id;
    private String name;
    private final DispatchBehavior dispatchBehavior;
    private District currentDistrict;
    private boolean onDuty = false;
    private boolean busy = false; // Part A.3 - a rider holds at most one active order
    private int completedDeliveries = 0;
    private long totalDeliveryMinutes = 0;

    public Rider(int id, String name, DispatchBehavior dispatchBehavior, District currentDistrict) {
        this.id = id;
        this.name = name;
        this.dispatchBehavior = dispatchBehavior;
        this.currentDistrict = currentDistrict;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public DispatchBehavior getDispatchBehavior() { return dispatchBehavior; }
    public District getCurrentDistrict() { return currentDistrict; }
    public void setCurrentDistrict(District d) { this.currentDistrict = d; }
    public boolean isOnDuty() { return onDuty; }
    public void setOnDuty(boolean onDuty) { this.onDuty = onDuty; }

    //The single source of truth for "may this rider take a new order?"
    public boolean isAvailable() { return onDuty && !busy; }

    public int getCompletedDeliveries() { return completedDeliveries; }

    public double averageDeliveryMinutes() {
        return completedDeliveries == 0 ? 0.0 : (double) totalDeliveryMinutes / completedDeliveries;
    }

    //Absolute rule (Part A.3): rejects if this rider already has an active order
    public void assignOrder() {
        if (!isAvailable()) throw new RiderUnavailableException("Rider " + name + " is not available for a new order");
        this.busy = true;
    }

    public void completeDelivery(long minutesTaken) {
        this.busy = false;
        this.completedDeliveries++;
        this.totalDeliveryMinutes += minutesTaken;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rider)) return false;
        return id == ((Rider) o).id;
    }
    @Override public int hashCode() { return Integer.hashCode(id); }
    @Override public String toString() {
        return "Rider#" + id + " " + name + " (" + dispatchBehavior.vehicleLabel() + ", " + currentDistrict + ", "
                + (onDuty ? (busy ? "busy" : "available") : "off duty") + ")";
    }
}
