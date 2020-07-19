package jeresources.entry;

import jeresources.api.drop.LootDrop;
import jeresources.registry.DungeonRegistry;
import jeresources.util.LootFunctionHelper;
import jeresources.util.LootTableHelper;
import mezz.jei.api.recipe.IFocus;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class DungeonEntry {
    private Set<LootDrop> drops;
    private String name;
    private int maxStacks, minStacks;

    public DungeonEntry(String name, LootTable lootTable) {
        this.drops = new TreeSet<>();
        this.name = name;
        final float[] tmpMinStacks = {0};
        final float[] tmpMaxStacks = {0};
        final LootTableManager manager = LootTableHelper.getManager();
        handleTable(lootTable, manager, tmpMinStacks, tmpMaxStacks);
        this.minStacks = MathHelper.floor(tmpMinStacks[0]);
        this.maxStacks = MathHelper.floor(tmpMaxStacks[0]);
    }

    private void handleTable(LootTable lootTable, LootTableManager manager, float[] tmpMinStacks, float[] tmpMaxStacks) {
        LootTableHelper.getPools(lootTable).forEach(
            pool -> {
                tmpMinStacks[0] += LootFunctionHelper.getMin(pool.getRolls());
                tmpMaxStacks[0] += LootFunctionHelper.getMax(pool.getRolls()) + LootFunctionHelper.getMax(pool.getBonusRolls());
                final float totalWeight = LootTableHelper.getLootEntries(pool).stream()
                    .filter(entry -> entry instanceof StandaloneLootEntry).map(entry -> (StandaloneLootEntry) entry)
                    .mapToInt(entry -> entry.weight).sum();
                LootTableHelper.getLootEntries(pool).stream()
                    .filter(entry -> entry instanceof ItemLootEntry).map(entry -> (ItemLootEntry) entry)
                    .map(entry -> new LootDrop(entry.item, entry.weight / totalWeight, entry.functions)).forEach(drops::add);

                LootTableHelper.getLootEntries(pool).stream()
                    .filter(entry -> entry instanceof TableLootEntry).map(entry -> (TableLootEntry) entry)
                    .map(entry -> manager.getLootTableFromLocation(entry.table))
                    .forEach(table -> handleTable(table, manager, tmpMinStacks, tmpMaxStacks));
            }
        );
    }

    public boolean containsItem(ItemStack itemStack) {
        return drops.stream().anyMatch(drop -> drop.item.isItemEqual(itemStack));
    }

    public String getName() {
        String name = DungeonRegistry.categoryToLocalKeyMap.get(this.name);
        return name == null ? this.name : name;
    }

    public List<ItemStack> getItemStacks() {
        return getItemStacks(null);
    }

    public List<ItemStack> getItemStacks(IFocus<ItemStack> focus) {
        return drops.stream().map(drop -> drop.item)
            .filter(stack -> focus == null || ItemStack.areItemStacksEqual(ItemHandlerHelper.copyStackWithSize(stack, focus.getValue().getCount()), focus.getValue()))
            .collect(Collectors.toList());
    }

    public int getMaxStacks() {
        return maxStacks;
    }

    public int getMinStacks() {
        return minStacks;
    }

    public LootDrop getChestDrop(ItemStack ingredient) {
        return drops.stream().filter(drop -> ItemStack.areItemsEqual(drop.item, ingredient)).findFirst().orElse(null);
    }

}
