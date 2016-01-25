package jeresources.entries;

import jeresources.api.distributions.DistributionBase;
import jeresources.api.distributions.DistributionHelpers;
import jeresources.api.render.ColourHelper;
import jeresources.api.drop.DropItem;
import jeresources.api.restrictions.Restriction;
import net.minecraft.item.ItemStack;

import java.util.*;

public class WorldGenEntry
{
    private float[] chances;
    private boolean silktouch;
    private ItemStack block;
    private int minY;
    private int maxY;
    private int colour;
    private Restriction restriction;
    private DistributionBase distribution;
    private Set<DropItem> drops = new TreeSet<>();

    public WorldGenEntry(ItemStack block, DistributionBase distribution, Restriction restriction, boolean silktouch, DropItem... drops)
    {
        this.block = block;
        this.distribution = distribution;
        this.restriction = restriction;
        this.colour = ColourHelper.BLACK;
        this.silktouch = silktouch;
        addDrops(drops);
        calcChances();
    }

    public WorldGenEntry(ItemStack block, DistributionBase distribution, DropItem... drops)
    {
        this(block, distribution, Restriction.OVERWORLD_LIKE, false, drops);
    }

    public WorldGenEntry(ItemStack block, DistributionBase distribution, boolean silktouch, DropItem... drops)
    {
        this(block, distribution, Restriction.OVERWORLD_LIKE, silktouch, drops);
    }

    public WorldGenEntry(ItemStack block, DistributionBase distribution, Restriction restriction,  DropItem... drops)
    {
        this(block, distribution, restriction, false, drops);
    }

    public void addDrops(DropItem... drops)
    {
        Collections.addAll(this.drops, drops);
    }

    public void addDrops(Collection<DropItem> drops)
    {
        this.drops.addAll(drops);
    }

    private void calcChances()
    {
        chances = new float[256];
        minY = 256;
        maxY = 0;
        int i = -1;
        for (float chance : this.distribution.getDistribution())
        {
            if (++i == chances.length) break;
            chances[i] += chance;
            if (chances[i] > 0)
            {
                if (minY > i)
                    minY = i;
                if (i > maxY)
                    maxY = i;
            }
        }
        if (minY == 256) minY = 0;
        if (maxY == 0) maxY = 255;

        if (minY < 128)
            minY = 0;
        else
            minY = 128;

        if (maxY <= 127)
            maxY = 127;
        else
            maxY = 255;
    }

    public float[] getChances()
    {
        return Arrays.copyOfRange(chances, minY , maxY + 1);
    }

    public int getMinY()
    {
        return minY;
    }

    public int getMaxY()
    {
        return maxY;
    }

    public boolean isSilkTouchNeeded()
    {
        return silktouch;
    }

    public int getColour()
    {
        return colour;
    }

    public List<DropItem> getDrops()
    {
        return new ArrayList<>(drops);
    }

    public List<ItemStack> getBlockAndDrops()
    {
        List<ItemStack> list = new LinkedList<>();
        list.add(this.block);
        for (DropItem dropItem: drops)
            if (!list.contains(dropItem.item)) list.add(dropItem.item);
        return list;
    }

    public ItemStack getBlock()
    {
        return this.block;
    }

    public List<String> getBiomeRestrictions()
    {
        return this.restriction.getBiomeRestrictions();
    }

    public List<String> getDimensions()
    {
        return this.restriction.getDimensionRestrictions();
    }

    public DropItem getDropItem(ItemStack itemStack)
    {
        for (DropItem drop : getDrops())
            if (drop.item.isItemEqual(itemStack))
                return drop;
        return null;
    }

    @Override
    public String toString()
    {
        return "WorldGenEntry: " + block.getDisplayName() + " - " + restriction.toString();
    }

    public Restriction getRestriction()
    {
        return restriction;
    }

    public void merge(WorldGenEntry entry)
    {
        addDrops(entry.getDrops());
        this.distribution = DistributionHelpers.addDistribution(this.distribution, entry.distribution);
    }
}
