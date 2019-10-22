package jeresources.api;

import jeresources.api.drop.PlantDrop;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;

/**
 * Use to register plants and {@link PlantDrop}s
 * Note: Don't bother registering drops for tall grass.
 * They are fetched from {@link net.minecraftforge.common.ForgeHooks}
 */
public interface IPlantRegistry {
    void register(ItemStack itemStack, IPlantable plant, PlantDrop... drops);
    void register(ItemStack itemStack, PlantDrop... drops);

    <T extends Block & IPlantable> void register(T plant, PlantDrop... drops);

    void registerWithSoil (ItemStack stack, IPlantable plant, BlockState soil, PlantDrop... drops);
    void registerWithSoil (ItemStack stack, BlockState soil, PlantDrop... drops);

    <T extends Block & IPlantable> void registerWithSoil (T plant, BlockState soil, PlantDrop ... drops);

    void registerDrops(@Nonnull ItemStack itemStack, PlantDrop... drops);
}
