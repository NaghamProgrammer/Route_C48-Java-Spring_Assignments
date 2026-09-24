package masrdelivery.model;

import java.util.Objects;

public final class Address {
    private final District district;
    private final String detail;

    public Address(District district, String detail) {
        this.district = Objects.requireNonNull(district, "district");
        this.detail = Objects.requireNonNull(detail, "detail");
    }

    public District getDistrict() { return district; }
    public String getDetail() { return detail; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address a = (Address) o;
        return district == a.district && detail.equalsIgnoreCase(a.detail);
    }
    @Override public int hashCode() { return Objects.hash(district, detail.toLowerCase()); }
    @Override public String toString() { return detail + ", " + district; }
}
