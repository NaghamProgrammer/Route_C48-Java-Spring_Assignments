public enum CustomerTiers {
    standard(50.0,100.0),
    silver(80.0,200.0),
    gold(200.0, 350.0);

    private final double fee;
    private final double bonus;

    CustomerTiers(double fee, double bonus) {
        this.fee = fee;
        this.bonus = bonus;
    }


    public double getFee() {
        return fee;
    }

    public double getBonus() {
        return bonus;
    }
}
