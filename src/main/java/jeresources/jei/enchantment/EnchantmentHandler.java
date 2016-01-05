package jeresources.jei.enchantment;


import jeresources.jei.JEIConfig;
import jeresources.registry.EnchantmentRegistry;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class EnchantmentHandler implements IRecipeHandler<ItemStack>
{
    @Nonnull
    @Override
    public Class<ItemStack> getRecipeClass()
    {
        return ItemStack.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return JEIConfig.ENCHANTMENT;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull ItemStack recipe)
    {
        return new EnchantmentWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@Nonnull ItemStack recipe)
    {
        return EnchantmentRegistry.getInstance().getEnchantments(recipe).size() > 0;
    }
}
