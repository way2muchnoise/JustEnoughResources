package jeresources.api.restrictions;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;

public class DimensionRestriction {
    public static final DimensionRestriction OVERWORLD = new DimensionRestriction(World.field_234918_g_);
    public static final DimensionRestriction NETHER = new DimensionRestriction(World.field_234919_h_);
    public static final DimensionRestriction END = new DimensionRestriction(World.field_234920_i_);
    public static final DimensionRestriction NONE = new DimensionRestriction();

    private Type type;
    private RegistryKey<World> dimension;

    private DimensionRestriction() {
        this.type = Type.NONE;
    }

    public DimensionRestriction(RegistryKey<World> type) {
        this(Type.WHITELIST, type);
    }

    public DimensionRestriction(Type type, RegistryKey<World> dimension) {
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
        return "Dimension: " + (type == Type.NONE ? "None" : type.name() + " " + dimension.toString());
    }

    @Override
    public int hashCode() {
        return type == Type.NONE ? super.hashCode() : type.hashCode() ^ dimension.hashCode();
    }

    public String getDimensionName() {
        return type == Type.NONE ? "all" : dimension.func_240901_a_().toString();
    }
}
