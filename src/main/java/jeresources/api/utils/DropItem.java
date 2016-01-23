package jeresources.api.utils;

import jeresources.api.utils.conditionals.Conditional;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.ArrayList;
import java.util.List;

public class DropItem implements Comparable<DropItem>
{
    public int minDrop, maxDrop;
    public ItemStack item;
    public float chance;
    public List<String> conditionals = new ArrayList<String>();
    private float sortIndex;

    public DropItem(ItemStack item)
    {
        this(item, item.stackSize);
    }

    public DropItem(ItemStack item, float chance)
    {
        this(item, 0, 1, chance);
    }

    /**
     * @param item    The dropped {@link net.minecraft.item.ItemStack} (chance for drop will be 100%)
     * @param minDrop the maximum amount dropped
     * @param maxDrop the minimum amount dropped
     */
    public DropItem(ItemStack item, int minDrop, int maxDrop, Conditional... conditionals)
    {
        this(item, minDrop, maxDrop, 1F, conditionals);
    }

    /**
     * @param item    The dropped {@link net.minecraft.item.ItemStack}
     * @param minDrop the maximum amount dropped
     * @param maxDrop the minimum amount dropped
     * @param chance  the chance the {@param item} gets dropped
     */
    public DropItem(ItemStack item, int minDrop, int maxDrop, float chance, Conditional... conditionals)
    {
        this.item = item;
        this.minDrop = minDrop;
        this.maxDrop = maxDrop;
        this.chance = chance;
        sortIndex = Math.min(chance, 1F) * (float) (minDrop + maxDrop);
        for (Conditional conditional : conditionals)
            this.conditionals.add(conditional.toString());
    }

    /**
     * @param item    The dropped {@link net.minecraft.item.Item} (chance for drop will be 100% and the itemDamage will be default)
     * @param minDrop the maximum amount dropped
     * @param maxDrop the minimum amount dropped
     */
    public DropItem(Item item, int minDrop, int maxDrop, Conditional... conditionals)
    {
        this(new ItemStack(item), minDrop, maxDrop, 1F, conditionals);
    }

    /**
     * @param item       The dropped {@link net.minecraft.item.Item} (chance for drop will be 100%)
     * @param itemDamage the damage on the item
     * @param minDrop    the maximum amount dropped
     * @param maxDrop    the minimum amount dropped
     */
    public DropItem(Item item, int itemDamage, int minDrop, int maxDrop, Conditional... conditionals)
    {
        this(new ItemStack(item, 1, itemDamage), minDrop, maxDrop, 1F, conditionals);
    }

    /**
     * @param item    The dropped {@link net.minecraft.item.Item}
     * @param minDrop the maximum amount dropped
     * @param maxDrop the minimum amount dropped
     * @param chance  the chance the {@param item} gets dropped
     */
    public DropItem(Item item, int minDrop, int maxDrop, float chance, Conditional... conditionals)
    {
        this(new ItemStack(item), minDrop, maxDrop, chance, conditionals);
    }

    /**
     * @param item       The dropped {@link net.minecraft.item.Item}
     * @param itemDamage the damage on the item
     * @param minDrop    the maximum amount dropped
     * @param maxDrop    the minimum amount dropped
     * @param chance     the chance the {@param item} gets dropped
     */
    public DropItem(Item item, int itemDamage, int minDrop, int maxDrop, float chance, Conditional... conditionals)
    {
        this(new ItemStack(item, 1, itemDamage), minDrop, maxDrop, chance, conditionals);
    }

    public String toString()
    {
        if (minDrop == maxDrop) return minDrop + getDropChance();
        return minDrop + "-" + maxDrop + getDropChance();
    }

    private String getDropChance()
    {
        return chance < 1F ? " (" + formatChance() + "%)" : "";
    }

    private String formatChance()
    {
        float chance = this.chance * 100;
        if (chance < 10) return String.format("%.1f", chance);
        return String.format("%2d", (int) chance);
    }

    public String chanceString()
    {
        if (chance >= 0.995f) {
            return String.format("%.2G", chance);
        } else {
            return String.format("%.2G%%", chance * 100f);
        }
    }

    public List<String> getTooltipText()
    {
        return conditionals;
    }

    public void addConditionals(List<String> conditionals)
    {
        this.conditionals.addAll(conditionals);
    }

    public NBTTagCompound writeToNBT()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setTag("stack", item.writeToNBT(new NBTTagCompound()));
        compound.setInteger("max", this.maxDrop);
        compound.setInteger("min", this.minDrop);
        compound.setFloat("chance", this.chance);
        NBTTagList conditionals = new NBTTagList();
        for (String condition : this.conditionals)
            conditionals.appendTag(new NBTTagString(condition));
        compound.setTag("conditionals", conditionals);
        return compound;
    }

    public static DropItem readFromNBT(NBTTagCompound tagCompound)
    {
        NBTTagCompound item = tagCompound.getCompoundTag("stack");
        if (item.hasNoTags()) return null;
        ItemStack stack = ItemStack.loadItemStackFromNBT(item);
        if (stack == null || stack.getItem() == null) return null;
        int max = Math.max(tagCompound.getInteger("max"), 1);
        int min = tagCompound.getInteger("min");
        float chance = tagCompound.getFloat("chance");
        if (chance == 0) chance = 1F;
        Conditional[] conditionals = decodeConditionals(tagCompound.getTagList("conditionals", 8));
        return new DropItem(stack, min, max, chance, conditionals);
    }

    public static Conditional[] decodeConditionals(NBTTagList conditional)
    {
        List<Conditional> result = new ArrayList<Conditional>();
        for (int i = 0; i < conditional.tagCount(); i++)
        {
            String condition = conditional.getStringTagAt(i);
            if (!condition.equals("")) result.add(new Conditional(condition));
        }
        return result.toArray(new Conditional[result.size()]);
    }

    public float getSortIndex()
    {
        return sortIndex;
    }

    @Override
    public int compareTo(DropItem o)
    {
        float result = getSortIndex() - o.getSortIndex();
        if (Math.round(result) == 0 && item.getIsItemStackEqual(o.item)) return 0;
        return result < 0 ? 1 : -1;
    }
}
