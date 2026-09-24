package masrdelivery.model;

import java.util.EnumMap;
import java.util.Map;


// Districts the platform operates in, with a symmetric km distance table used for the delivery-fee calculation in Part B. Maadi<->Faisal is fixed at 12km to match the assignment's worked example.

public enum District {
    MAADI, DOKKI, FAISAL, NASR_CITY, HELIOPOLIS, ZAMALEK, DOWNTOWN, MOHANDESSIN;

    private static final Map<District, Map<District, Double>> DISTANCES = new EnumMap<>(District.class);

    static {
        for (District d : values()) DISTANCES.put(d, new EnumMap<>(District.class));
        link(MAADI, FAISAL, 12.0);
        link(MAADI, DOKKI, 14.0);
        link(MAADI, NASR_CITY, 10.0);
        link(MAADI, HELIOPOLIS, 16.0);
        link(MAADI, ZAMALEK, 15.0);
        link(MAADI, DOWNTOWN, 13.0);
        link(MAADI, MOHANDESSIN, 17.0);
        link(DOKKI, FAISAL, 6.0);
        link(DOKKI, NASR_CITY, 15.0);
        link(DOKKI, HELIOPOLIS, 18.0);
        link(DOKKI, ZAMALEK, 3.0);
        link(DOKKI, DOWNTOWN, 5.0);
        link(DOKKI, MOHANDESSIN, 2.0);
        link(FAISAL, NASR_CITY, 20.0);
        link(FAISAL, HELIOPOLIS, 22.0);
        link(FAISAL, ZAMALEK, 9.0);
        link(FAISAL, DOWNTOWN, 11.0);
        link(FAISAL, MOHANDESSIN, 4.0);
        link(NASR_CITY, HELIOPOLIS, 6.0);
        link(NASR_CITY, ZAMALEK, 13.0);
        link(NASR_CITY, DOWNTOWN, 9.0);
        link(NASR_CITY, MOHANDESSIN, 17.0);
        link(HELIOPOLIS, ZAMALEK, 12.0);
        link(HELIOPOLIS, DOWNTOWN, 10.0);
        link(HELIOPOLIS, MOHANDESSIN, 19.0);
        link(ZAMALEK, DOWNTOWN, 3.0);
        link(ZAMALEK, MOHANDESSIN, 4.0);
        link(DOWNTOWN, MOHANDESSIN, 6.0);
    }

    private static void link(District a, District b, double km) {
        DISTANCES.get(a).put(b, km);
        DISTANCES.get(b).put(a, km);
    }

    public double distanceTo(District other) {
        if (this == other) return 0.0;
        Double km = DISTANCES.get(this).get(other);
        if (km == null) throw new IllegalStateException("No distance defined between " + this + " and " + other);
        return km;
    }
}
