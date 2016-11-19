package jeresources.util;

import jeresources.compatibility.CompatBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class FakeMerchant implements IMerchant {
    @Override
    public void setCustomer(EntityPlayer player) {

    }

    @Override
    public EntityPlayer getCustomer() {
        return null;
    }

    @Nullable
    @Override
    public MerchantRecipeList getRecipes(EntityPlayer player) {
        return null;
    }

    @Override
    public void setRecipes(@Nullable MerchantRecipeList recipeList) {

    }

    @Override
    public void useRecipe(MerchantRecipe recipe) {

    }

    @Override
    public void verifySellingItem(ItemStack stack) {

    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }

    @Override
    public World getWorld() {
        return CompatBase.getWorld();
    }

    @Override
    public BlockPos getPos() {
        return BlockPos.ORIGIN;
    }
}
