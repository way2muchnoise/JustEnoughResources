package jeresources.api.restrictions;

import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class DimensionRestriction {
    public static final DimensionRestriction OVERWORLD = new DimensionRestriction(0);
    public static final DimensionRestriction NETHER = new DimensionRestriction(-1);
    public static final DimensionRestriction END = new DimensionRestriction(1);
    public static final DimensionRestriction NONE = new DimensionRestriction();

    private Type type;
    private String name;

    private DimensionRestriction() {
        this.type = Type.NONE;
    }

    public DimensionRestriction(DimensionType type) {
        this(Type.WHITELIST, DimensionType.func_212678_a(type).toString());
    }

    public DimensionRestriction(Type type, DimensionType dimensionType) {
        this(type, DimensionType.func_212678_a(dimensionType).toString());
    }

    public DimensionRestriction(int dim) {
        this(DimensionType.getById(dim));
    }

    public DimensionRestriction(Type type, int dim) {
        this(type, DimensionType.getById(dim));
    }

    public DimensionRestriction(String name) {
        this(Type.WHITELIST, name);
    }

    public DimensionRestriction(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DimensionRestriction) {
            DimensionRestriction other = (DimensionRestriction) obj;
            return this.type == other.type && this.name.equals(other.name);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Dimension: " + (type == Type.NONE ? "None" : type.name() + " " + name);
    }

    @Override
    public int hashCode() {
        return type == Type.NONE ? super.hashCode() : type.hashCode() ^ name.hashCode();
    }

    public String getDimensionName() {
        return type == Type.NONE ? "all" : name;
    }
}
