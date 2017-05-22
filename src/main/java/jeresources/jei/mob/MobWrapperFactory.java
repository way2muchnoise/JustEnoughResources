package jeresources.jei.mob;

import jeresources.entry.MobEntry;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;

public class MobWrapperFactory implements IRecipeWrapperFactory<MobEntry> {
    @Override
    public IRecipeWrapper getRecipeWrapper(MobEntry recipe) {
        return new MobWrapper(recipe);
    }
}
