package jeresources.compatibility;

import jeresources.api.IWorldGenRegistry;
import jeresources.api.distributions.DistributionBase;
import jeresources.api.drop.DropItem;
import jeresources.api.restrictions.Restriction;
import jeresources.entries.WorldGenEntry;
import jeresources.registry.WorldGenRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;

import java.util.ArrayList;
import java.util.List;

public class WorldGenRegistryImpl implements IWorldGenRegistry
{
    private static List<WorldGenEntry> registers = new ArrayList<>();
    private static List<Tuple<ItemStack, DropItem[]>> addedDrops = new ArrayList<>();

    protected WorldGenRegistryImpl()
    {

    }

    @Override
    public void register(ItemStack block, DistributionBase distribution, DropItem... drops)
    {
        registers.add(new WorldGenEntry(block, distribution, drops));
    }

    @Override
    public void register(ItemStack block, DistributionBase distribution, Restriction restriction, DropItem... drops)
    {
        registers.add(new WorldGenEntry(block, distribution, restriction, drops));
    }

    @Override
    public void register(ItemStack block, DistributionBase distribution, boolean silktouch, DropItem... drops)
    {
        registers.add(new WorldGenEntry(block, distribution, silktouch, drops));
    }

    @Override
    public void register(ItemStack block, DistributionBase distribution, Restriction restriction, boolean silktouch, DropItem... drops)
    {
        registers.add(new WorldGenEntry(block, distribution, restriction, silktouch, drops));
    }

    @Override
    public void registerDrops(ItemStack block, DropItem... drops)
    {
        addedDrops.add(new Tuple<>(block, drops));
    }

    protected static void commit()
    {
        for (WorldGenEntry entry : registers)
            WorldGenRegistry.getInstance().registerEntry(entry);
        registers.clear();
        for (Tuple<ItemStack, DropItem[]> tuple : addedDrops)
            WorldGenRegistry.getInstance().addDrops(tuple.getFirst(), tuple.getSecond());
        addedDrops.clear();
    }
}
