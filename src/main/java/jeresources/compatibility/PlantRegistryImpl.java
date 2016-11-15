package jeresources.compatibility;

import jeresources.api.IPlantRegistry;
import jeresources.api.drop.PlantDrop;
import jeresources.entry.PlantEntry;
import jeresources.registry.PlantRegistry;
import jeresources.util.LogHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class PlantRegistryImpl implements IPlantRegistry {
    private static List<PlantEntry> registers = new ArrayList<>();
    private static List<Tuple<ItemStack, PlantDrop[]>> addedDrops = new ArrayList<>();
    private static final ItemStack grass = new ItemStack(Blocks.TALLGRASS, 1, 1);

    protected PlantRegistryImpl() {

    }

    @Override
    public void register(ItemStack itemStack, IPlantable plant, PlantDrop... drops) {
        try {
            registers.add(new PlantEntry(itemStack, plant, drops));
        } catch (Exception e) {
            LogHelper.debug("Error while registering plant %s", itemStack.toString());
        }
    }

    @Override
    public void register(ItemStack itemStack, PlantDrop... drops) {
        try {
            registers.add(new PlantEntry(itemStack, drops));
        } catch (Exception e) {
            LogHelper.debug("Error while registering plant %s", itemStack.toString());
        }
    }

    @Override
    public <T extends Item & IPlantable> void register(T plant, PlantDrop... drops) {
        try {
            registers.add(new PlantEntry(plant, drops));
        } catch (Exception e) {
            LogHelper.debug("Error while registering plant %s", plant.getRegistryName());
        }
    }

    @Override
    public void registerDrops(@Nonnull ItemStack itemStack, PlantDrop... drops) {
        try {
            if (drops.length > 0 || ItemStack.areItemStacksEqual(itemStack, grass))
                addedDrops.add(new Tuple<>(itemStack, drops));
        } catch (Exception e) {
            LogHelper.debug("Error while registering drops for %s", itemStack.toString());
        }
    }

    protected static void commit() {
        for (PlantEntry entry : registers)
            PlantRegistry.getInstance().registerPlant(entry);
        for (Tuple<ItemStack, PlantDrop[]> tuple : addedDrops)
            PlantRegistry.getInstance().addDrops(tuple.getFirst(), tuple.getSecond());
    }
}
