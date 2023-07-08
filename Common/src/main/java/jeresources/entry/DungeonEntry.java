package jeresources.entry;

import jeresources.api.drop.LootDrop;
import jeresources.api.util.ItemHelper;
import jeresources.api.util.LootFunctionHelper;
import jeresources.platform.ILootTableHelper;
import jeresources.platform.Services;
import jeresources.registry.DungeonRegistry;
import jeresources.util.LootTableHelper;
import mezz.jei.api.recipe.IFocus;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DungeonEntry {
    private Set<LootDrop> drops;
    private String name;
    private int maxStacks, minStacks;

    public DungeonEntry(String name, LootTable lootTable) {
        this.drops = new TreeSet<>();
        this.name = name;
        final float[] tmpMinStacks = {0};
        final float[] tmpMaxStacks = {0};
        final LootDataManager lootTables = LootTableHelper.getLootDataManager();
        handleTable(lootTable, lootTables, tmpMinStacks, tmpMaxStacks);
        this.minStacks = Mth.floor(tmpMinStacks[0]);
        this.maxStacks = Mth.floor(tmpMaxStacks[0]);
    }

    private void handleTable(LootTable lootTable, LootDataManager lootTables, float[] tmpMinStacks, float[] tmpMaxStacks) {
        ILootTableHelper lootTableHelper = Services.PLATFORM.getLootTableHelper();
        LootTableHelper.getPools(lootTable).forEach(
            pool -> {
                tmpMinStacks[0] += LootFunctionHelper.getMin(lootTableHelper.getRolls(pool));
                tmpMaxStacks[0] += LootFunctionHelper.getMax(lootTableHelper.getRolls(pool)) + LootFunctionHelper.getMax(lootTableHelper.getBonusRolls(pool));
                final float totalWeight = LootTableHelper.getLootEntries(pool).stream()
                    .filter(entry -> entry instanceof LootPoolSingletonContainer).map(entry -> (LootPoolSingletonContainer) entry)
                    .mapToInt(entry -> entry.weight).sum();
                LootTableHelper.getLootEntries(pool).stream()
                    .filter(entry -> entry instanceof LootItem).map(entry -> (LootItem) entry)
                    .map(entry -> new LootDrop(entry.item, entry.weight / totalWeight, entry.functions)).forEach(drops::add);

                LootTableHelper.getLootEntries(pool).stream()
                    .filter(entry -> entry instanceof LootTableReference).map(entry -> (LootTableReference) entry)
                    .map(entry -> lootTables.getLootTable(entry.name))
                    .forEach(table -> handleTable(table, lootTables, tmpMinStacks, tmpMaxStacks));
            }
        );
    }

    public boolean containsItem(ItemStack itemStack) {
        return drops.stream().anyMatch(drop -> drop.item.is(itemStack.getItem()));
    }

    public String getName() {
        String name = DungeonRegistry.categoryToLocalKeyMap.get(this.name);
        return name == null ? this.name : name;
    }

    public List<ItemStack> getItemStacks(IFocus<ItemStack> focus) {
        return drops.stream().map(drop -> drop.item)
            .filter(stack -> focus == null || ItemStack.isSameItem(ItemHelper.copyStackWithSize(stack, focus.getTypedValue().getIngredient().getCount()), focus.getTypedValue().getIngredient()))
            .collect(Collectors.toList());
    }

    public List<ItemStack> getItemStacks(Stream<IFocus<ItemStack>> focuses) {
        return getItemStacks(focuses.findFirst().orElse(null));
    }

    public int getMaxStacks() {
        return maxStacks;
    }

    public int getMinStacks() {
        return minStacks;
    }

    public LootDrop getChestDrop(ItemStack ingredient) {
        return drops.stream().filter(drop -> ItemStack.isSameItem(drop.item, ingredient)).findFirst().orElse(null);
    }

}
