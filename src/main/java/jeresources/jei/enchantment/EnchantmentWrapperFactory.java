package jeresources.jei.enchantment;

import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;

public class EnchantmentWrapperFactory implements IRecipeWrapperFactory<EnchantmentWrapper> {
    @Override
    public IRecipeWrapper getRecipeWrapper(EnchantmentWrapper recipe) {
        return recipe;
    }
}
