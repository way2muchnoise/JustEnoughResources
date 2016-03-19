package jeresources.entries;

import jeresources.api.drop.LootDrop;
import jeresources.registry.DungeonRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DungeonEntry
{
    private List<LootDrop> drops;
    private String name;
    private int maxStacks, minStacks;

    public DungeonEntry(String name, LootTable lootTable)
    {
        this.drops = new ArrayList<>();
        this.name = name;
        final float[] tmpMinStacks = {0};
        final float[] tmpMaxStacks = {0};
        Arrays.stream(lootTable.pools).forEach(
            pool -> {
                tmpMinStacks[0] += pool.rolls.getMin();
                tmpMaxStacks[0] += pool.rolls.getMax() + pool.bonusRolls.getMax();
                final float totalWeight = Arrays.stream(pool.lootEntries).parallel().mapToInt(entry -> entry.weight).sum();
                Arrays.stream(pool.lootEntries).parallel()
                    .filter(entry -> entry instanceof LootEntryItem).map(entry -> (LootEntryItem)entry)
                    .map(entry -> new LootDrop(entry.item, entry.weight / totalWeight, entry.functions)).forEach(drops::add);
            }
        );
        this.drops.removeIf(drop -> drop == null);
        this.minStacks = MathHelper.floor_float(tmpMinStacks[0]);
        this.maxStacks = MathHelper.floor_float(tmpMaxStacks[0]);
    }

    public boolean containsItem(ItemStack itemStack)
    {
        return drops.parallelStream().anyMatch(drop -> drop.item.isItemEqual(itemStack));
    }

    public String getName()
    {
        String name = DungeonRegistry.categoryToLocalKeyMap.get(this.name);
        return name == null ? this.name : name;
    }

    public List<ItemStack> getItemStacks()
    {
        return drops.parallelStream().map(drop -> drop.item).collect(Collectors.toList());
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
        return drops.parallelStream().filter(drop -> ItemStack.areItemsEqual(drop.item, ingredient)).findFirst().orElse(null);
    }

}
