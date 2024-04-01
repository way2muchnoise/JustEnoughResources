package jeresources.api;

import jeresources.api.drop.PlantDrop;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

/**
 * Used to register plants and {@link PlantDrop}s
 * Note: Don't bother registering drops for tall grass.
 * They are fetched from Forge or Fabric (?)
 */
public interface IPlantRegistry {
    void register(ItemStack itemStack, BushBlock plant, Property<?> ageProperty, PlantDrop... drops);
    void register(ItemStack itemStack, BlockState plantState, Property<?> ageProperty, PlantDrop... drops);
    void register(ItemStack itemStack, BushBlock plant, PlantDrop... drops);
    void register(ItemStack itemStack, BlockState plantState, PlantDrop... drops);
    void register(ItemStack itemStack, Property<?> ageProperty, PlantDrop... drops);
    void register(ItemStack itemStack, PlantDrop... drops);

    <T extends BushBlock> void register(T plant, Property<?> ageProperty, PlantDrop... drops);
    <T extends BushBlock> void register(T plant, PlantDrop... drops);

    void registerWithSoil (ItemStack stack, BushBlock plant, Property<?> ageProperty, BlockState soil, PlantDrop... drops);
    void registerWithSoil (ItemStack stack, BlockState plantState, Property<?> ageProperty, BlockState soil, PlantDrop... drops);
    void registerWithSoil (ItemStack stack, BushBlock plant, BlockState soil, PlantDrop... drops);
    void registerWithSoil (ItemStack stack, BlockState plantState, BlockState soil, PlantDrop... drops);
    void registerWithSoil (ItemStack stack, Property<?> ageProperty, BlockState soil, PlantDrop... drops);
    void registerWithSoil (ItemStack stack, BlockState soil, PlantDrop... drops);

    <T extends BushBlock> void registerWithSoil (T plant, Property<?> ageProperty, BlockState soil, PlantDrop ... drops);
    <T extends BushBlock> void registerWithSoil (T plant, BlockState soil, PlantDrop ... drops);

    void registerDrops(@NotNull ItemStack itemStack, PlantDrop... drops);
}
