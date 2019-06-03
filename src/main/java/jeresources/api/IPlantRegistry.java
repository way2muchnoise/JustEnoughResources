package jeresources.api;

import jeresources.api.drop.PlantDrop;
import net.minecraft.block.state.IBlockState;
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

    <T extends Item & IPlantable> void register(T plant, PlantDrop... drops);

    void registerWithSoil (ItemStack stack, IPlantable plant, IBlockState soil, PlantDrop... drops);
    void registerWithSoil (ItemStack stack, IBlockState soil, PlantDrop... drops);

    <T extends Item & IPlantable> void registerWithSoil (T plant, IBlockState soil, PlantDrop ... drops);

    void registerDrops(@Nonnull ItemStack itemStack, PlantDrop... drops);
}
