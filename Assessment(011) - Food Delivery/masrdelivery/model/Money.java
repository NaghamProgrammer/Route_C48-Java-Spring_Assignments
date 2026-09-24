package masrdelivery.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;


// Represents an amount of EGP. Backed by BigDecimal, always kept at
// scale 2 (piastres) with HALF_UP rounding applied at every arithmetic
// operation, chosen instead of double/float specifically because of
// the assignment's warning about binary floating point and currency:
// double cannot represent 0.10 exactly, and repeated addition/rounding
// of such values drifts away from the exact totals the spec's worked
// example demands. BigDecimal at a fixed scale gives deterministic,
// auditable arithmetic and reads naturally against percentage-based
// business rules (service fee 10%, promotion percentages, caps).

public final class Money implements Comparable<Money> {
    public static final Money ZERO = new Money(BigDecimal.ZERO);

    private final BigDecimal amount;

    private Money(BigDecimal amount) {
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public static Money of(double egp) { return new Money(BigDecimal.valueOf(egp)); }
    public static Money of(BigDecimal egp) { return new Money(egp); }
    public static Money ofPiastres(long piastres) { return new Money(BigDecimal.valueOf(piastres, 2)); }

    public Money add(Money other) { return new Money(this.amount.add(other.amount)); }

    //Never goes below zero - matches "a total may never be negative" (Part B.5)
    public Money subtract(Money other) {
        BigDecimal result = this.amount.subtract(other.amount);
        if (result.signum() < 0) result = BigDecimal.ZERO;
        return new Money(result);
    }

    public Money multiply(double factor) { return new Money(this.amount.multiply(BigDecimal.valueOf(factor))); }

    public Money min(Money other) { return this.amount.compareTo(other.amount) <= 0 ? this : other; }

    public boolean isZero() { return amount.signum() == 0; }
    public boolean isNegative() { return amount.signum() < 0; }
    public BigDecimal raw() { return amount; }

    @Override public int compareTo(Money o) { return amount.compareTo(o.amount); }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        return amount.compareTo(((Money) o).amount) == 0;
    }

    @Override public int hashCode() { return Objects.hash(amount.stripTrailingZeros()); }

    @Override public String toString() { return amount.toPlainString() + " EGP"; }
}
