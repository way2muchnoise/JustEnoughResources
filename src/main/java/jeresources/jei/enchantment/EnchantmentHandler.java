package jeresources.jei.enchantment;


import jeresources.jei.JEIConfig;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class EnchantmentHandler implements IRecipeHandler<EnchantmentWrapper>
{
    @Nonnull
    @Override
    public Class<EnchantmentWrapper> getRecipeClass()
    {
        return EnchantmentWrapper.class;
    }

    @Deprecated
    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return JEIConfig.ENCHANTMENT;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull EnchantmentWrapper recipe)
    {
        return JEIConfig.ENCHANTMENT;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull EnchantmentWrapper recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull EnchantmentWrapper recipe)
    {
        return true;
    }
}
