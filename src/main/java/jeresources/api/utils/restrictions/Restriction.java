package jeresources.api.utils.restrictions;

import jeresources.api.messages.utils.MessageKeys;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

public class Restriction
{
    public static final Restriction OVERWORLD_LIKE = new Restriction();
    public static final Restriction NETHER_LIKE = new Restriction(BlockRestriction.NETHER);
    public static final Restriction END_LIKE = new Restriction(BlockRestriction.END);

    public static final Restriction OVERWORLD = new Restriction(DimensionRestriction.OVERWORLD);
    public static final Restriction NETHER = new Restriction(BlockRestriction.NETHER, DimensionRestriction.NETHER);
    public static final Restriction END = new Restriction(BlockRestriction.END, DimensionRestriction.END);

    private BlockRestriction blockRestriction;
    private BiomeRestriction biomeRestriction;
    private DimensionRestriction dimensionRestriction;

    public Restriction()
    {
        this(BiomeRestriction.NONE);
    }

    public Restriction(BlockRestriction blockRestriction)
    {
        this(blockRestriction, BiomeRestriction.NONE, DimensionRestriction.NONE);
    }

    public Restriction(BiomeRestriction biomeRestriction)
    {
        this(BlockRestriction.STONE, biomeRestriction, DimensionRestriction.NONE);
    }

    public Restriction(DimensionRestriction dimensionRestriction)
    {
        this(BlockRestriction.STONE, BiomeRestriction.NONE, dimensionRestriction);
    }

    public Restriction(BlockRestriction blockRestriction, BiomeRestriction biomeRestriction)
    {
        this(blockRestriction, biomeRestriction, DimensionRestriction.NONE);
    }

    public Restriction(BlockRestriction blockRestriction, DimensionRestriction dimensionRestriction)
    {
        this(blockRestriction, BiomeRestriction.NONE, dimensionRestriction);
    }

    public Restriction(BiomeRestriction biomeRestriction, DimensionRestriction dimensionRestriction)
    {
        this(BlockRestriction.STONE, biomeRestriction, dimensionRestriction);
    }

    public Restriction(BlockRestriction blockRestriction, BiomeRestriction biomeRestriction, DimensionRestriction dimensionRestriction)
    {
        this.blockRestriction = blockRestriction;
        this.biomeRestriction = biomeRestriction;
        this.dimensionRestriction = dimensionRestriction;
    }

    public Restriction(NBTTagCompound tagCompound)
    {
        blockRestriction = new BlockRestriction(tagCompound.getCompoundTag(MessageKeys.blockRestriction));
        biomeRestriction = new BiomeRestriction(tagCompound.getCompoundTag(MessageKeys.biomeRestriction));
        dimensionRestriction = new DimensionRestriction(tagCompound.getCompoundTag(MessageKeys.dimensionRestriction));
    }

    public List<String> getStringList(boolean getNames)
    {
        List<String> result = new ArrayList<String>();
        result.add(StatCollector.translateToLocal("jer.ore.dimensions") + ":");
        result.addAll(dimensionRestriction.getValidDimensions(blockRestriction, getNames));
        List<String> biomes = new ArrayList<String>();
        biomes.add(StatCollector.translateToLocal("jer.ore.biomes") + ":");
        biomes.addAll(biomeRestriction.toStringList());
        if (biomes.size() > 1) result.addAll(biomes);
        return result;
    }

    public NBTTagCompound writeToNBT()
    {
        return writeToNBT(new NBTTagCompound());
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setTag(MessageKeys.blockRestriction, blockRestriction.writeToNBT());
        tagCompound.setTag(MessageKeys.dimensionRestriction, dimensionRestriction.writeToNBT());
        tagCompound.setTag(MessageKeys.biomeRestriction, biomeRestriction.writeToNBT());
        return tagCompound;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Restriction)) return false;
        Restriction other = (Restriction) obj;
        if (!other.biomeRestriction.equals(this.biomeRestriction)) return false;
        if (!other.blockRestriction.equals(this.blockRestriction)) return false;
        if (!other.dimensionRestriction.equals(this.dimensionRestriction)) return false;
        return true;
    }

    public boolean isMergeable(Restriction restriction)
    {
        if (!biomeRestriction.isMergeable(restriction.biomeRestriction)) return false;
        if (!blockRestriction.equals(restriction.blockRestriction)) return false;
        if (!dimensionRestriction.isMergeable(restriction.dimensionRestriction)) return false;
        return true;
    }

    @Override
    public String toString()
    {
        return blockRestriction.toString() + ", " + dimensionRestriction.toString() + ", " + biomeRestriction.toString();
    }

    @Override
    public int hashCode()
    {
        return blockRestriction.hashCode() ^ dimensionRestriction.hashCode() ^ biomeRestriction.hashCode();
    }
}
