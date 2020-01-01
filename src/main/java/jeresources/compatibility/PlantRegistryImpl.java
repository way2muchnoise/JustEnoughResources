package jeresources.compatibility;

import jeresources.api.IPlantRegistry;
import jeresources.api.drop.PlantDrop;
import jeresources.entry.PlantEntry;
import jeresources.registry.PlantRegistry;
import jeresources.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.IProperty;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class PlantRegistryImpl implements IPlantRegistry {
    private static List<PlantEntry> registers = new ArrayList<>();
    private static List<Tuple<ItemStack, PlantDrop[]>> addedDrops = new ArrayList<>();
    private static final ItemStack grass = new ItemStack(Blocks.TALL_GRASS, 1, new CompoundNBT());

    protected PlantRegistryImpl() {

    }

    @Override
    public void register(ItemStack itemStack, IPlantable plant, IProperty<?> ageProperty, PlantDrop... drops) {
        try {
            PlantEntry entry = new PlantEntry(itemStack, plant, drops);
            entry.setAgeProperty(ageProperty);
            registers.add(entry);
        } catch (Exception e) {
            LogHelper.debug("Error while registering plant %s", itemStack.toString());
        }
    }

    @Override
    public void register(ItemStack itemStack, BlockState plantState, IProperty<?> ageProperty, PlantDrop... drops) {
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
    public void register(ItemStack itemStack, IPlantable plant, PlantDrop... drops) {
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
    public void register(ItemStack itemStack, IProperty<?> ageProperty, PlantDrop... drops) {
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
    public <T extends Block & IPlantable> void register(T plant, IProperty<?> ageProperty, PlantDrop... drops) {
        try {
            PlantEntry entry = new PlantEntry(plant, drops);
            entry.setAgeProperty(ageProperty);
            registers.add(entry);
        } catch (Exception e) {
            LogHelper.debug("Error while registering plant %s", plant.getRegistryName());
        }
    }

    @Override
    public <T extends Block & IPlantable> void register(T plant, PlantDrop... drops) {
        try {
            registers.add(new PlantEntry(plant, drops));
        } catch (Exception e) {
            LogHelper.debug("Error while registering plant %s", plant.getRegistryName());
        }
    }

    @Override
    public void registerWithSoil(ItemStack stack, IPlantable plant, IProperty<?> ageProperty, BlockState soil, PlantDrop... drops) {
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
    public void registerWithSoil(ItemStack stack, BlockState plantState, IProperty<?> ageProperty, BlockState soil, PlantDrop... drops) {
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
    public void registerWithSoil(ItemStack stack, IPlantable plant, BlockState soil, PlantDrop... drops) {
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
    public void registerWithSoil(ItemStack stack, IProperty<?> ageProperty, BlockState soil, PlantDrop... drops) {
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
    public <T extends Block & IPlantable> void registerWithSoil(T plant, IProperty<?> ageProperty, BlockState soil, PlantDrop... drops) {
        try {
            PlantEntry entry = new PlantEntry(plant, drops);
            entry.setAgeProperty(ageProperty);
            entry.setSoil(soil);
            registers.add(entry);
        } catch (Exception e) {
            LogHelper.debug("Error while registering plant %s", plant.getRegistryName());
        }
    }

    @Override
    public <T extends Block & IPlantable> void registerWithSoil(T plant, BlockState soil, PlantDrop... drops) {
        try {
            PlantEntry entry = new PlantEntry(plant, drops);
            entry.setSoil(soil);
            registers.add(entry);
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
            PlantRegistry.getInstance().addDrops(tuple.getA(), tuple.getB());
    }
}
