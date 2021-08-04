package jeresources.api.restrictions;

import java.util.List;

public class Restriction {
    public static final Restriction OVERWORLD = new Restriction(DimensionRestriction.OVERWORLD);
    public static final Restriction NETHER = new Restriction(DimensionRestriction.NETHER);
    public static final Restriction END = new Restriction(DimensionRestriction.END);
    public static final Restriction NONE = new Restriction();

    private BiomeRestriction biomeRestriction;
    private DimensionRestriction dimensionRestriction;

    private Restriction() {
        this(BiomeRestriction.NO_RESTRICTION);
    }

    public Restriction(BiomeRestriction biomeRestriction) {
        this(biomeRestriction, DimensionRestriction.NONE);
    }

    public Restriction(DimensionRestriction dimensionRestriction) {
        this(BiomeRestriction.NO_RESTRICTION, dimensionRestriction);
    }

    public Restriction(BiomeRestriction biomeRestriction, DimensionRestriction dimensionRestriction) {
        this.biomeRestriction = biomeRestriction;
        this.dimensionRestriction = dimensionRestriction;
    }

    public List<String> getBiomeRestrictions() {
        return biomeRestriction.toStringList();
    }

    public String getDimensionRestriction() {
        return dimensionRestriction.getDimensionName();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Restriction)) return false;
        Restriction other = (Restriction) obj;
        if (!other.biomeRestriction.equals(this.biomeRestriction)) return false;
        if (!other.dimensionRestriction.equals(this.dimensionRestriction)) return false;
        return true;
    }

    @Override
    public String toString() {
        return dimensionRestriction.toString() + ", " + biomeRestriction.toString();
    }

    @Override
    public int hashCode() {
        return dimensionRestriction.hashCode() ^ biomeRestriction.hashCode();
    }

    public enum Type {
        NONE, BLACKLIST, WHITELIST
    }
}
