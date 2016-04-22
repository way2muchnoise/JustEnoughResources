package jeresources.compatibility;

import jeresources.api.IPlantRegistry;
import jeresources.api.drop.PlantDrop;
import jeresources.entry.PlantEntry;
import jeresources.registry.PlantRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.IPlantable;

import java.util.ArrayList;
import java.util.List;

public class PlantRegistryImpl implements IPlantRegistry
{
    private static List<PlantEntry> registers = new ArrayList<>();
    private static List<Tuple<ItemStack, PlantDrop[]>> addedDrops = new ArrayList<>();
    private static final ItemStack grass = new ItemStack(Blocks.TALLGRASS, 1, 1);

    @Override
    public void register(ItemStack itemStack, IPlantable plant, PlantDrop... drops)
    {
        registers.add(new PlantEntry(itemStack, plant, drops));
    }

    @Override
    public void register(ItemStack itemStack, PlantDrop... drops)
    {
        registers.add(new PlantEntry(itemStack, drops));
    }

    @Override
    public <T extends Item & IPlantable> void register(T plant, PlantDrop... drops)
    {
        registers.add(new PlantEntry(plant, drops));
    }

    @Override
    public void registerDrops(ItemStack itemStack, PlantDrop... drops)
    {
        if (itemStack.isItemEqual(grass))
        addedDrops.add(new Tuple<>(itemStack, drops));
    }

    protected static void commit()
    {
        for (PlantEntry entry : registers)
            PlantRegistry.getInstance().registerPlant(entry);
        registers.clear();
        for (Tuple<ItemStack, PlantDrop[]> tuple : addedDrops)
            PlantRegistry.getInstance().addDrops(tuple.getFirst(), tuple.getSecond());
        addedDrops.clear();
    }
}
