package jeresources.entries;

import net.minecraft.item.ItemStack;

public class SeedEntry
{
    private ItemStack seed;
    private int itemWeight;

    public SeedEntry(ItemStack seed, int itemWeight)
    {
        this.seed = seed;
        this.itemWeight = itemWeight;
    }

    public ItemStack getDrop()
    {
        return seed;
    }

    public int getWeight()
    {
        return itemWeight;
    }
}
