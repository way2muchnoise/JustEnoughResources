package jeresources.api.drop;

import net.minecraft.world.item.ItemStack;

public class PlantDrop {
    private ItemStack drop;
    private int itemWeight;
    private int minDrop, maxDrop;
    private float chance;
    private DropKind dropKind;

    public enum DropKind {
        chance, weight, minMax
    }

    /**
     * DON'T USE!
     * This constructor is meant for registering grass
     * Which get auto collected by NER
     *
     * @param drop       the The drop an {@link ItemStack}
     * @param itemWeight The Weight of the drop
     */
    public PlantDrop(ItemStack drop, int itemWeight) {
        this.drop = drop;
        this.itemWeight = itemWeight;
        this.dropKind = DropKind.weight;
    }

    /**
     * Use this for registering seeds that have a chance less than one to drop
     *
     * @param drop   The drop an {@link ItemStack}
     * @param chance the chance as float
     */
    public PlantDrop(ItemStack drop, float chance) {
        this.drop = drop;
        this.chance = chance;
        this.dropKind = DropKind.chance;
    }

    /**
     * Use this for registering seeds that have a chance of dropping more than one time
     *
     * @param drop    The drop an {@link ItemStack}
     * @param minDrop minimum of seeds dropped
     * @param maxDrop maximum of seeds dropped
     */
    public PlantDrop(ItemStack drop, int minDrop, int maxDrop) {
        this.drop = drop;
        this.minDrop = minDrop;
        this.maxDrop = maxDrop;
        this.dropKind = DropKind.minMax;
    }

    public ItemStack getDrop() {
        return drop;
    }

    public int getWeight() {
        return itemWeight;
    }

    public int getMinDrop() {
        return minDrop;
    }

    public int getMaxDrop() {
        return maxDrop;
    }

    public float getChance() {
        return chance;
    }

    public DropKind getDropKind() {
        return dropKind;
    }
}
