package jeresources.compatibility;

import jeresources.api.IWorldGenRegistry;
import jeresources.api.distributions.DistributionBase;
import jeresources.api.drop.LootDrop;
import jeresources.api.restrictions.Restriction;
import jeresources.entry.WorldGenEntry;
import jeresources.registry.WorldGenRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;

import java.util.ArrayList;
import java.util.List;

public class WorldGenRegistryImpl implements IWorldGenRegistry
{
    private static List<WorldGenEntry> registers = new ArrayList<>();
    private static List<Tuple<ItemStack, LootDrop[]>> addedDrops = new ArrayList<>();

    protected WorldGenRegistryImpl()
    {

    }

    @Override
    public void register(ItemStack block, DistributionBase distribution, LootDrop... drops)
    {
        registers.add(new WorldGenEntry(block, distribution, drops));
    }

    @Override
    public void register(ItemStack block, DistributionBase distribution, Restriction restriction, LootDrop... drops)
    {
        registers.add(new WorldGenEntry(block, distribution, restriction, drops));
    }

    @Override
    public void register(ItemStack block, DistributionBase distribution, boolean silktouch, LootDrop... drops)
    {
        registers.add(new WorldGenEntry(block, distribution, silktouch, drops));
    }

    @Override
    public void register(ItemStack block, DistributionBase distribution, Restriction restriction, boolean silktouch, LootDrop... drops)
    {
        registers.add(new WorldGenEntry(block, distribution, restriction, silktouch, drops));
    }

    @Override
    public void registerDrops(ItemStack block, LootDrop... drops)
    {
        addedDrops.add(new Tuple<>(block, drops));
    }

    protected static void commit()
    {
        for (WorldGenEntry entry : registers)
            WorldGenRegistry.getInstance().registerEntry(entry);
        registers.clear();
        for (Tuple<ItemStack, LootDrop[]> tuple : addedDrops)
            WorldGenRegistry.getInstance().addDrops(tuple.getFirst(), tuple.getSecond());
        addedDrops.clear();
    }
}
