package jeresources.compatibility.api;

import jeresources.api.IPlantRegistry;
import jeresources.api.drop.PlantDrop;
import jeresources.entry.PlantEntry;
import jeresources.registry.PlantRegistry;
import jeresources.util.LogHelper;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class PlantRegistryImpl implements IPlantRegistry {
    private static List<PlantEntry> registers = new ArrayList<>();
    private static List<Tuple<ItemStack, PlantDrop[]>> addedDrops = new ArrayList<>();
    private static final ItemStack grass = new ItemStack(Blocks.TALL_GRASS, 1);

    protected PlantRegistryImpl() {

    }

    @Override
    public void register(ItemStack itemStack, BushBlock plant, Property<?> ageProperty, PlantDrop... drops) {
        try {
            PlantEntry entry = new PlantEntry(itemStack, plant, drops);
            entry.setAgeProperty(ageProperty);
            registers.add(entry);
        } catch (Exception e) {
            LogHelper.debug("Error while registering plant %s", itemStack.toString());
        }
    }

    @Override
    public void register(ItemStack itemStack, BlockState plantState, Property<?> ageProperty, PlantDrop... drops) {
        try {
            PlantEntry entry = new PlantEntry(itemStack, drops);
            entry.setPlantState(plantState);
            entry.setAgeProperty(ageProperty);
            registers.add(entry);
        } catch (Exception e) {
            LogHelper.debug("Error while registering plant %s", itemStack.toString());
        }
    }

    @Override
    public void register(ItemStack itemStack, BushBlock plant, PlantDrop... drops) {
        try {
            registers.add(new PlantEntry(itemStack, plant, drops));
        } catch (Exception e) {
            LogHelper.debug("Error while registering plant %s", itemStack.toString());
        }
    }

    @Override
    public void register(ItemStack itemStack, BlockState plantState, PlantDrop... drops) {
        try {
            PlantEntry entry = new PlantEntry(itemStack, drops);
            entry.setPlantState(plantState);
            registers.add(entry);
        } catch (Exception e) {
            LogHelper.debug("Error while registering plant %s", itemStack.toString());
        }
    }

    @Override
    public void register(ItemStack itemStack, Property<?> ageProperty, PlantDrop... drops) {
        try {
            PlantEntry entry = new PlantEntry(itemStack, drops);
            entry.setAgeProperty(ageProperty);
            registers.add(entry);
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
    public <T extends BushBlock> void register(T plant, Property<?> ageProperty, PlantDrop... drops) {
        try {
            PlantEntry entry = new PlantEntry(plant, drops);
            entry.setAgeProperty(ageProperty);
            registers.add(entry);
        } catch (Exception e) {
            LogHelper.debug("Error while registering plant %s", plant.getDescriptionId());
        }
    }

    @Override
    public <T extends BushBlock> void register(T plant, PlantDrop... drops) {
        try {
            registers.add(new PlantEntry(plant, drops));
        } catch (Exception e) {
            LogHelper.debug("Error while registering plant %s", plant.getDescriptionId());
        }
    }

    @Override
    public void registerWithSoil(ItemStack stack, BushBlock plant, Property<?> ageProperty, BlockState soil, PlantDrop... drops) {
        try {
            PlantEntry entry = new PlantEntry(stack, plant, drops);
            entry.setAgeProperty(ageProperty);
            entry.setSoil(soil);
            registers.add(entry);
        } catch (Exception e) {
            LogHelper.debug("Error while registering plant %s", stack.toString());
        }
    }

    @Override
    public void registerWithSoil(ItemStack stack, BlockState plantState, Property<?> ageProperty, BlockState soil, PlantDrop... drops) {
        try {
            PlantEntry entry = new PlantEntry(stack, drops);
            entry.setPlantState(plantState);
            entry.setAgeProperty(ageProperty);
            entry.setSoil(soil);
            registers.add(entry);
        } catch (Exception e) {
            LogHelper.debug("Error while registering plant %s", stack.toString());
        }
    }

    @Override
    public void registerWithSoil(ItemStack stack, BushBlock plant, BlockState soil, PlantDrop... drops) {
        try {
            PlantEntry entry = new PlantEntry(stack, plant, drops);
            entry.setSoil(soil);
            registers.add(entry);
        } catch (Exception e) {
            LogHelper.debug("Error while registering plant %s", stack.toString());
        }
    }

    @Override
    public void registerWithSoil(ItemStack stack, BlockState plantState, BlockState soil, PlantDrop... drops) {
        try {
            PlantEntry entry = new PlantEntry(stack, drops);
            entry.setPlantState(plantState);
            entry.setSoil(soil);
            registers.add(entry);
        } catch (Exception e) {
            LogHelper.debug("Error while registering plant %s", stack.toString());
        }
    }

    @Override
    public void registerWithSoil(ItemStack stack, Property<?> ageProperty, BlockState soil, PlantDrop... drops) {
        try {
            PlantEntry entry = new PlantEntry(stack, drops);
            entry.setAgeProperty(ageProperty);
            entry.setSoil(soil);
            registers.add(entry);
        } catch (Exception e) {
            LogHelper.debug("Error while registering plant %s", stack.toString());
        }
    }

    @Override
    public void registerWithSoil(ItemStack stack, BlockState soil, PlantDrop... drops) {
        try {
            PlantEntry entry = new PlantEntry(stack, drops);
            entry.setSoil(soil);
            registers.add(entry);
        } catch (Exception e) {
            LogHelper.debug("Error while registering plant %s", stack.toString());
        }
    }

    @Override
    public <T extends BushBlock> void registerWithSoil(T plant, Property<?> ageProperty, BlockState soil, PlantDrop... drops) {
        try {
            PlantEntry entry = new PlantEntry(plant, drops);
            entry.setAgeProperty(ageProperty);
            entry.setSoil(soil);
            registers.add(entry);
        } catch (Exception e) {
            LogHelper.debug("Error while registering plant %s", plant.getDescriptionId());
        }
    }

    @Override
    public <T extends BushBlock> void registerWithSoil(T plant, BlockState soil, PlantDrop... drops) {
        try {
            PlantEntry entry = new PlantEntry(plant, drops);
            entry.setSoil(soil);
            registers.add(entry);
        } catch (Exception e) {
            LogHelper.debug("Error while registering plant %s", plant.getDescriptionId());
        }
    }

    @Override
    public void registerDrops(@Nonnull ItemStack itemStack, PlantDrop... drops) {
        try {
            if (drops.length > 0 || ItemStack.isSameItem(itemStack, grass))
                addedDrops.add(new Tuple<>(itemStack, drops));
        } catch (Exception e) {
            LogHelper.debug("Error while registering drops for %s", itemStack.toString());
        }
    }

    protected static void commit() {
        for (PlantEntry entry : registers)
            PlantRegistry.getInstance().registerPlant(entry);
        for (Tuple<ItemStack, PlantDrop[]> tuple : addedDrops)
            PlantRegistry.getInstance().addDrops(tuple.getA(), tuple.getB());
    }
}
