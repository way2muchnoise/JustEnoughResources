package jeresources.api.restrictions;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class DimensionRestriction {
    public static final DimensionRestriction OVERWORLD = new DimensionRestriction(Level.OVERWORLD);
    public static final DimensionRestriction NETHER = new DimensionRestriction(Level.NETHER);
    public static final DimensionRestriction END = new DimensionRestriction(Level.END);
    public static final DimensionRestriction NONE = new DimensionRestriction();

    private Restriction.Type type;
    private ResourceKey<Level> dimension;

    private DimensionRestriction() {
        this.type = Restriction.Type.NONE;
    }

    public DimensionRestriction(ResourceKey<Level> type) {
        this(Restriction.Type.WHITELIST, type);
    }

    public DimensionRestriction(Restriction.Type type, ResourceKey<Level> dimension) {
        this.type = type;
        this.dimension = dimension;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DimensionRestriction) {
            DimensionRestriction other = (DimensionRestriction) obj;
            return this.type == other.type && this.dimension.equals(other.dimension);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Dimension: " + (type == Restriction.Type.NONE ? "None" : type.name() + " " + dimension.toString());
    }

    @Override
    public int hashCode() {
        return type == Restriction.Type.NONE ? super.hashCode() : type.hashCode() ^ dimension.hashCode();
    }

    public String getDimensionName() {
        return type == Restriction.Type.NONE ? "all" : dimension.location().toString();
    }
}
