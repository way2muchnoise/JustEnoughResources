package jeresources.api.drop;

import jeresources.api.conditionals.Conditional;
import jeresources.util.LootConditionHelper;
import jeresources.util.LootFunctionHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.*;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public class LootDrop implements Comparable<LootDrop>
{
    public int minDrop, maxDrop;
    public ItemStack item, smeltedItem;
    public float chance;
    private List<Conditional> conditionals;
    public int fortuneLevel;
    public boolean enchanted;
    private float sortIndex;

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
        this.smeltedItem = null;
        this.minDrop = minDrop;
        this.maxDrop = maxDrop;
        this.chance = chance;
        sortIndex = Math.min(chance, 1F) * (float) (minDrop + maxDrop);
        this.conditionals = new ArrayList<>();
        Collections.addAll(this.conditionals, conditionals);
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

    public LootDrop(Item item, float chance, LootCondition[] lootConditions, LootFunction... lootFunctions)
    {
        this(item, chance, lootFunctions);
        addLootConditions(lootConditions);
    }

    public LootDrop addLootConditions(LootCondition[] lootFunctions)
    {
        return addLootConditions(Arrays.asList(lootFunctions));
    }

    public LootDrop addLootConditions(Collection<LootCondition> lootFunctions)
    {
        lootFunctions.forEach(this::addLootCondition);
        return this;
    }

    public LootDrop addLootCondition(LootCondition condition)
    {
        LootConditionHelper.applyCondition(condition, this);
        return this;
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
        LootFunctionHelper.applyFunction(lootFunction, this);
        return this;
    }

    public boolean canBeCooked()
    {
        return smeltedItem != null;
    }

    public List<ItemStack> getDrops()
    {
        List<ItemStack> list = new LinkedList<>();
        if (item != null)list.add(item);
        if (smeltedItem != null) list.add(smeltedItem);
        return list;
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
        return getTooltipText(false);
    }

    public List<String> getTooltipText(boolean smelted)
    {
        List<String> list = conditionals.stream().map(Conditional::toString).collect(Collectors.toList());
        if (smelted)
            list.add(Conditional.burning.toString());
        return list;
    }

    public void addConditional(Conditional conditional)
    {
        this.conditionals.add(conditional);
    }

    public void addConditionals(List<Conditional> conditionals)
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
        int cmp = Float.compare(o.getSortIndex(), getSortIndex());
        return cmp != 0 ? cmp : item.getDisplayName().compareTo(o.item.getDisplayName());
    }
}
