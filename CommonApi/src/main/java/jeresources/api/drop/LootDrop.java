package jeresources.api.drop;

import jeresources.api.conditionals.Conditional;
import jeresources.api.util.ItemHelper;
import jeresources.api.util.LootConditionHelper;
import jeresources.api.util.LootFunctionHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public class LootDrop implements Comparable<LootDrop> {
    public int minDrop, maxDrop;
    public ItemStack item, smeltedItem;
    public float chance;
    private Set<Conditional> conditionals;
    public int fortuneLevel;
    public boolean enchanted;
    private float sortIndex;

    public LootDrop(ItemStack item) {
        this(item, item.getCount());
    }

    public LootDrop(ItemStack item, float chance) {
        this(item, chance, 0);
    }

    /**
     * @param item         the dropped {@link ItemStack}
     * @param chance       chance of drop, can be above 1 to indicate more than 1 drops
     * @param fortuneLevel the fortune level needed for these results
     */
    public LootDrop(ItemStack item, float chance, int fortuneLevel) {
        this(item, (int) Math.floor(chance), (int) Math.ceil(chance), chance, fortuneLevel);
    }

    /**
     * @param item         The dropped {@link ItemStack} (chance for drop will be 100%)
     * @param minDrop      the maximum amount dropped
     * @param maxDrop      the minimum amount dropped
     * @param conditionals a list of conditionals for this drop
     */
    public LootDrop(ItemStack item, int minDrop, int maxDrop, Conditional... conditionals) {
        this(item, minDrop, maxDrop, 1F, 0, conditionals);
    }

    /**
     * @param item         The dropped {@link ItemStack}
     * @param minDrop      the maximum amount dropped
     * @param maxDrop      the minimum amount dropped
     * @param chance       the chance the item gets dropped
     * @param fortuneLevel the level of fortune needed
     * @param conditionals a list of conditionals for this drop
     */
    public LootDrop(ItemStack item, int minDrop, int maxDrop, float chance, int fortuneLevel, Conditional... conditionals) {
        this.item = item;
        this.smeltedItem = null;
        this.minDrop = minDrop;
        this.maxDrop = maxDrop;
        this.chance = chance;
        sortIndex = Math.min(chance, 1F) * (float) (minDrop + maxDrop);
        this.conditionals = new HashSet<>();
        Collections.addAll(this.conditionals, conditionals);
        this.fortuneLevel = fortuneLevel;
    }

    /**
     * @param item    The dropped {@link Item} (chance for drop will be 100% and the itemDamage will be default)
     * @param minDrop the maximum amount dropped
     * @param maxDrop the minimum amount dropped
     * @param conditionals a list of conditionals for this drop
     */
    public LootDrop(Item item, int minDrop, int maxDrop, Conditional... conditionals) {
        this(new ItemStack(item), minDrop, maxDrop, 1F, 0, conditionals);
    }

    /**
     * @param item         The dropped {@link Item} (chance for drop will be 100%)
     * @param tag          {@link CompoundTag} of the Item
     * @param minDrop      the maximum amount dropped
     * @param maxDrop      the minimum amount dropped
     * @param conditionals a list of conditionals for this drop
     */
    public LootDrop(Item item, CompoundTag tag, int minDrop, int maxDrop, Conditional... conditionals) {
        this(ItemHelper.itemStackWithTag(item, 1, tag), minDrop, maxDrop, 1F, 0, conditionals);
    }

    /**
     * @param item         The dropped {@link Item}
     * @param minDrop      the maximum amount dropped
     * @param maxDrop      the minimum amount dropped
     * @param chance       the chance the item gets dropped
     * @param conditionals a list of conditionals for this drop
     */
    public LootDrop(Item item, int minDrop, int maxDrop, float chance, Conditional... conditionals) {
        this(new ItemStack(item), minDrop, maxDrop, chance, 0, conditionals);
    }

    /**
     * @param item         The dropped {@link Item}
     * @param tag          {@link CompoundTag} of the Item
     * @param minDrop      the maximum amount dropped
     * @param maxDrop      the minimum amount dropped
     * @param chance       the chance the item gets dropped
     * @param conditionals a list of conditionals for this drop
     */
    public LootDrop(Item item, CompoundTag tag, int minDrop, int maxDrop, float chance, Conditional... conditionals) {
        this(ItemHelper.itemStackWithTag(item, 1, tag), minDrop, maxDrop, chance, 0, conditionals);
    }

    /**
     * @param item         The dropped {@link ItemStack}
     * @param minDrop      the maximum amount dropped
     * @param maxDrop      the minimum amount dropped
     * @param chance       the chance the item gets dropped
     * @param conditionals a list of conditionals for this drop
     */
    public LootDrop(ItemStack item, int minDrop, int maxDrop, float chance, Conditional... conditionals) {
        this(item, minDrop, maxDrop, chance, 0, conditionals);
    }

    public LootDrop(Item item, float chance, LootItemFunction... lootFunctions) {
        this(new ItemStack(item), chance);
        this.enchanted = false;
        addLootFunctions(lootFunctions);
    }

    public LootDrop(Item item, float chance, LootItemCondition[] lootConditions, LootItemFunction... lootFunctions) {
        this(item, chance, lootFunctions);
        addLootConditions(lootConditions);
    }

    public LootDrop addLootConditions(LootItemCondition[] lootConditions) {
        return addLootConditions(Arrays.asList(lootConditions));
    }

    public LootDrop addLootConditions(Collection<LootItemCondition> lootConditions) {
        lootConditions.forEach(this::addLootCondition);
        return this;
    }

    public LootDrop addLootCondition(LootItemCondition condition) {
        LootConditionHelper.applyCondition(condition, this);
        return this;
    }

    public LootDrop addLootFunctions(LootItemFunction[] lootFunctions) {
        return addLootFunctions(Arrays.asList(lootFunctions));
    }

    public LootDrop addLootFunctions(Collection<LootItemFunction> lootFunctions) {
        lootFunctions.forEach(this::addLootFunction);
        return this;
    }

    public LootDrop addLootFunction(LootItemFunction lootFunction) {
        LootFunctionHelper.applyFunction(lootFunction, this);
        return this;
    }

    public boolean canBeCooked() {
        return smeltedItem != null;
    }

    public List<ItemStack> getDrops() {
        List<ItemStack> list = new LinkedList<>();
        if (item != null) list.add(item);
        if (smeltedItem != null) list.add(smeltedItem);
        return list;
    }

    public String toString() {
        if (minDrop == maxDrop) return minDrop + getDropChance();
        return minDrop + "-" + maxDrop + getDropChance();
    }

    private String getDropChance() {
        return chance < 1F ? " (" + formatChance() + "%)" : "";
    }

    public String formatChance() {
        float chance = this.chance * 100;
        if (chance < 10) return String.format("%.1f", chance);
        return String.format("%2d", (int) chance);
    }

    /**
     * Check if conditionals contains a specified Conditional
     * @param conditional Conditional to check it presence in conditionals
     * @return True if conditional is found, False if it not
     */
    public boolean isAffectedBy(Conditional conditional) {
        return this.conditionals.contains(conditional);
    }

    public String chanceString() {
        if (chance >= 0.995f) return String.format("%.2G", chance);
        else return String.format("%.2G%%", chance * 100f);
    }

    public List<Component> getTooltipText() {
        return getTooltipText(false);
    }

    public List<Component> getTooltipText(boolean smelted) {
        List<Component> list = conditionals.stream().map(Conditional::toStringTextComponent).collect(Collectors.toList());
        if (smelted)
            list.add(Conditional.burning.toStringTextComponent());
        return list;
    }

    public void addConditional(Conditional conditional) {
        this.conditionals.add(conditional);
    }

    public void addConditionals(List<Conditional> conditionals) {
        this.conditionals.addAll(conditionals);
    }

    public float getSortIndex() {
        return sortIndex;
    }

    public Component toStringTextComponent() {
        return Component.literal(toString());
    }

    @Override
    public int compareTo(@Nonnull LootDrop o) {
        if (ItemStack.isSameItem(item, o.item))
            return Integer.compare(o.fortuneLevel, fortuneLevel);
        int cmp = Float.compare(o.getSortIndex(), getSortIndex());
        return cmp != 0 ? cmp : item.getDisplayName().toString().compareTo(o.item.getDisplayName().toString());
    }
}
