package jeresources.api.drop;

import jeresources.api.conditionals.Conditional;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.functions.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class LootDrop implements Comparable<LootDrop>
{
    public int minDrop, maxDrop;
    public ItemStack item;
    public float chance;
    public List<String> conditionals;
    public int fortuneLevel;
    private float sortIndex;
    private boolean enchanted;

    public LootDrop(ItemStack item)
    {
        this(item, item.stackSize);
    }

    public LootDrop(ItemStack item, float chance)
    {
        this(item, chance, 0);
    }

    /**
     * @param item the dropped {@link ItemStack}
     * @param chance chance of drop, can be above 1 to indicate more than 1 drops
     * @param fortuneLevel the fortune level needed for these results
     */
    public LootDrop(ItemStack item, float chance, int fortuneLevel)
    {
        this(item, (int)Math.floor(chance), (int)Math.ceil(chance), chance, fortuneLevel);
    }

    /**
     * @param item    The dropped {@link net.minecraft.item.ItemStack} (chance for drop will be 100%)
     * @param minDrop the maximum amount dropped
     * @param maxDrop the minimum amount dropped
     * @param conditionals a list of conditionals for this drop
     */
    public LootDrop(ItemStack item, int minDrop, int maxDrop, Conditional... conditionals)
    {
        this(item, minDrop, maxDrop, 1F, 0, conditionals);
    }

    /**
     * @param item    The dropped {@link net.minecraft.item.ItemStack}
     * @param minDrop the maximum amount dropped
     * @param maxDrop the minimum amount dropped
     * @param chance  the chance the {@param item} gets dropped
     * @param fortuneLevel the level of fortune needed
     * @param conditionals a list of conditionals for this drop
     */
    public LootDrop(ItemStack item, int minDrop, int maxDrop, float chance, int fortuneLevel, Conditional... conditionals)
    {
        this.item = item;
        this.minDrop = minDrop;
        this.maxDrop = maxDrop;
        this.chance = chance;
        sortIndex = Math.min(chance, 1F) * (float) (minDrop + maxDrop);
        this.conditionals = new ArrayList<>();
        for (Conditional conditional : conditionals)
            this.conditionals.add(conditional.toString());
        this.fortuneLevel = fortuneLevel;
    }

    /**
     * @param item    The dropped {@link net.minecraft.item.Item} (chance for drop will be 100% and the itemDamage will be default)
     * @param minDrop the maximum amount dropped
     * @param maxDrop the minimum amount dropped
     */
    public LootDrop(Item item, int minDrop, int maxDrop, Conditional... conditionals)
    {
        this(new ItemStack(item), minDrop, maxDrop, 1F, 0, conditionals);
    }

    /**
     * @param item       The dropped {@link net.minecraft.item.Item} (chance for drop will be 100%)
     * @param itemDamage the damage on the item
     * @param minDrop    the maximum amount dropped
     * @param maxDrop    the minimum amount dropped
     * @param conditionals a list of conditionals for this drop
     */
    public LootDrop(Item item, int itemDamage, int minDrop, int maxDrop, Conditional... conditionals)
    {
        this(new ItemStack(item, 1, itemDamage), minDrop, maxDrop, 1F, 0, conditionals);
    }

    /**
     * @param item    The dropped {@link net.minecraft.item.Item}
     * @param minDrop the maximum amount dropped
     * @param maxDrop the minimum amount dropped
     * @param chance  the chance the {@param item} gets dropped
     * @param conditionals a list of conditionals for this drop
     */
    public LootDrop(Item item, int minDrop, int maxDrop, float chance, Conditional... conditionals)
    {
        this(new ItemStack(item), minDrop, maxDrop, chance, 0, conditionals);
    }

    /**
     * @param item       The dropped {@link net.minecraft.item.Item}
     * @param itemDamage the damage on the item
     * @param minDrop    the maximum amount dropped
     * @param maxDrop    the minimum amount dropped
     * @param chance     the chance the {@param item} gets dropped
     * @param conditionals a list of conditionals for this drop
     */
    public LootDrop(Item item, int itemDamage, int minDrop, int maxDrop, float chance, Conditional... conditionals)
    {
        this(new ItemStack(item, 1, itemDamage), minDrop, maxDrop, chance, 0, conditionals);
    }

    /**
     * @param item    The dropped {@link net.minecraft.item.ItemStack}
     * @param minDrop the maximum amount dropped
     * @param maxDrop the minimum amount dropped
     * @param chance  the chance the {@param item} gets dropped
     * @param conditionals a list of conditionals for this drop
     */
    public LootDrop(ItemStack item, int minDrop, int maxDrop, float chance, Conditional... conditionals)
    {
        this(item, minDrop, maxDrop, chance, 0, conditionals);
    }

    public LootDrop(Item item, float chance, LootFunction... lootFunctions)
    {
        this(new ItemStack(item), chance);
        this.enchanted = false;
        addLootFunctions(lootFunctions);
    }

    public LootDrop addLootFunctions(LootFunction[] lootFunctions)
    {
        return addLootFunctions(Arrays.asList(lootFunctions));
    }

    public LootDrop addLootFunctions(Collection<LootFunction> lootFunctions)
    {
        lootFunctions.forEach(this::addLootFunction);
        return this;
    }

    public LootDrop addLootFunction(LootFunction lootFunction)
    {
        if (lootFunction instanceof SetCount)
        {
            this.minDrop = MathHelper.floor_float(((SetCount)lootFunction).countRange.getMin());
            this.item.stackSize = this.minDrop < 1 ? 1 : this.minDrop;
            this.maxDrop = MathHelper.floor_float(((SetCount)lootFunction).countRange.getMax());
        } else if (lootFunction instanceof SetMetadata)
        {
            this.item.setItemDamage(MathHelper.floor_float(((SetMetadata)lootFunction).metaRange.getMin()));
        } else if (lootFunction instanceof EnchantRandomly || lootFunction instanceof EnchantWithLevels)
        {
            enchanted = true;
        } else
        {
            try
            {
                // TODO: add API thing for loot functions
                item = lootFunction.apply(item, null, null);
            } catch (NullPointerException ignored) {}
        }
        return this;
    }

    public boolean isEnchanted()
    {
        return enchanted;
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
        if (chance >= 0.995f) return String.format("%.2G", chance);
        else return String.format("%.2G%%", chance * 100f);
    }

    public List<String> getTooltipText()
    {
        return conditionals;
    }

    public void addConditionals(List<String> conditionals)
    {
        this.conditionals.addAll(conditionals);
    }

    public float getSortIndex()
    {
        return sortIndex;
    }

    @Override
    public int compareTo(@Nonnull LootDrop o)
    {
        if (ItemStack.areItemStacksEqual(item, o.item))
            return Integer.compare(o.fortuneLevel, fortuneLevel);
        return Float.compare(o.getSortIndex(), getSortIndex());
    }
}
