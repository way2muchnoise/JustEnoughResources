package jeresources.jei.villager;

import jeresources.entry.VillagerEntry;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;

public class VillagerWrapperFactory implements IRecipeWrapperFactory<VillagerEntry> {
    @Override
    public IRecipeWrapper getRecipeWrapper(VillagerEntry recipe) {
        return new VillagerWrapper(recipe);
    }
}
