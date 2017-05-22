package jeresources.jei.worldgen;

import jeresources.entry.WorldGenEntry;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;

public class WorldGenWrapperFactory implements IRecipeWrapperFactory<WorldGenEntry> {
    @Override
    public IRecipeWrapper getRecipeWrapper(WorldGenEntry recipe) {
        return new WorldGenWrapper(recipe);
    }
}
