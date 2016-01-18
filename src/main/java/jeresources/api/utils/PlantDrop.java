package jeresources.api.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PlantDrop
{
    private ItemStack drop;
    private int itemWeight;
    private int minDrop, maxDrop;
    private float chance;
    private DropKind dropKind;

    public enum DropKind
    {
        chance, weight, minMax
    }

    /**
     * DON'T USE!
     * This constructor is meant for registering grass
     * Which get auto collected by NER
     *
     * @param drop       the The drop an {@link net.minecraft.item.ItemStack}
     * @param itemWeight The Weight of the drop
     */
    public PlantDrop(ItemStack drop, int itemWeight)
    {
        this.drop = drop;
        this.itemWeight = itemWeight;
        this.dropKind = DropKind.weight;
    }

    /**
     * Use this for registering seeds that have a chance less then one to drop
     *
     * @param drop   The drop an {@link net.minecraft.item.ItemStack}
     * @param chance the chance as float
     */
    public PlantDrop(ItemStack drop, float chance)
    {
        this.drop = drop;
        this.chance = chance;
        this.dropKind = DropKind.chance;
    }

    /**
     * Use this for registering seeds that have a chance of dropping more then one time
     *
     * @param drop    The drop an {@link net.minecraft.item.ItemStack}
     * @param minDrop minimum of seeds dropped
     * @param maxDrop maximum of seeds dropped
     */
    public PlantDrop(ItemStack drop, int minDrop, int maxDrop)
    {
        this.drop = drop;
        this.minDrop = minDrop;
        this.maxDrop = maxDrop;
        this.dropKind = DropKind.minMax;
    }

    public ItemStack getDrop()
    {
        return drop;
    }

    public int getWeight()
    {
        return itemWeight;
    }

    public int getMinDrop()
    {
        return minDrop;
    }

    public int getMaxDrop()
    {
        return maxDrop;
    }

    public float getChance()
    {
        return chance;
    }

    public DropKind getDropKind()
    {
        return dropKind;
    }

    public NBTTagCompound writeToNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setTag("drop", drop.writeToNBT(new NBTTagCompound()));
        compound.setInteger("dropKind", dropKind.ordinal());
        switch (dropKind)
        {
            case chance:
                compound.setFloat("chance", chance);
                break;
            case weight:
                compound.setInteger("weight", itemWeight);
                break;
            case minMax:
                compound.setInteger("min", minDrop);
                compound.setInteger("max", maxDrop);
                break;
            default:
                break;
        }
        return compound;
    }

    public static PlantDrop readFromNBT(NBTTagCompound tagCompound)
    {
        NBTTagCompound drop = tagCompound.getCompoundTag("drop");
        if (drop.hasNoTags()) return null;
        ItemStack stack = ItemStack.loadItemStackFromNBT(drop);
        if (stack == null || stack.getItem() == null) return null;
        DropKind kind = DropKind.values()[tagCompound.getInteger("dropKind")];
        switch (kind)
        {
            case chance:
                float chance = tagCompound.getFloat("chance");
                return new PlantDrop(stack, chance);
            case weight:
                int weight = tagCompound.getInteger("weight");
                return new PlantDrop(stack, weight);
            case minMax:
                int min = tagCompound.getInteger("min");
                int max = tagCompound.getInteger("max");
                return new PlantDrop(stack, min, max);
            default:
                return null;
        }
    }
}
