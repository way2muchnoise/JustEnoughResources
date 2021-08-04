package jeresources.api.restrictions;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;

public class DimensionRestriction {
    public static final DimensionRestriction OVERWORLD = new DimensionRestriction(World.OVERWORLD);
    public static final DimensionRestriction NETHER = new DimensionRestriction(World.NETHER);
    public static final DimensionRestriction END = new DimensionRestriction(World.END);
    public static final DimensionRestriction NONE = new DimensionRestriction();

    private Restriction.Type type;
    private RegistryKey<World> dimension;

    private DimensionRestriction() {
        this.type = Restriction.Type.NONE;
    }

    public DimensionRestriction(RegistryKey<World> type) {
        this(Restriction.Type.WHITELIST, type);
    }

    public DimensionRestriction(Restriction.Type type, RegistryKey<World> dimension) {
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
