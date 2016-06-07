package jeresources.entry;

import jeresources.api.drop.LootDrop;
import jeresources.registry.DungeonRegistry;
import jeresources.util.LootHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;

import java.util.*;
import java.util.stream.Collectors;

public class DungeonEntry
{
    private Set<LootDrop> drops;
    private String name;
    private int maxStacks, minStacks;

    public DungeonEntry(String name, LootTable lootTable)
    {
        this.drops = new HashSet<>();
        this.name = name;
        final float[] tmpMinStacks = {0};
        final float[] tmpMaxStacks = {0};
        LootHelper.getPools(lootTable).forEach(
            pool -> {
                tmpMinStacks[0] += pool.getRolls().getMin();
                tmpMaxStacks[0] += pool.getRolls().getMax() + pool.getBonusRolls().getMax();
                final float totalWeight = LootHelper.getEntries(pool).stream().mapToInt(entry -> entry.getEffectiveWeight(0)).sum();
                LootHelper.getEntries(pool).stream()
                    .filter(entry -> entry instanceof LootEntryItem).map(entry -> (LootEntryItem)entry)
                    .map(entry -> new LootDrop(LootHelper.getItem(entry), entry.getEffectiveWeight(0) / totalWeight, LootHelper.getFunctions(entry))).forEach(drops::add);
            }
        );
        this.drops = new TreeSet<>(this.drops);
        this.minStacks = MathHelper.floor_float(tmpMinStacks[0]);
        this.maxStacks = MathHelper.floor_float(tmpMaxStacks[0]);
    }

    public boolean containsItem(ItemStack itemStack)
    {
        return drops.stream().anyMatch(drop -> drop.item.isItemEqual(itemStack));
    }

    public String getName()
    {
        String name = DungeonRegistry.categoryToLocalKeyMap.get(this.name);
        return name == null ? this.name : name;
    }

    public List<ItemStack> getItemStacks()
    {
        return drops.stream().map(drop -> drop.item).collect(Collectors.toList());
    }

    public int getMaxStacks()
    {
        return maxStacks;
    }

    public int getMinStacks()
    {
        return minStacks;
    }

    public LootDrop getChestDrop(ItemStack ingredient)
    {
        return drops.stream().filter(drop -> ItemStack.areItemsEqual(drop.item, ingredient)).findFirst().orElse(null);
    }

}
