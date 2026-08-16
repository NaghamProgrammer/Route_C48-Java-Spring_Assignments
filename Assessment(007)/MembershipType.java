public enum MembershipType {
    STUDENT(0.25),
    STAFF(0.10),
    PUBLIC(0.0);


    private final double waiverRate;

    MembershipType(double waiverRate) {
        this.waiverRate = waiverRate;
    }

    //"each value is an object that knows its own rate and can be asked for it" means there is a getter for waiver rate
    public double getWaiverRate() {
        return waiverRate;
    }
}
