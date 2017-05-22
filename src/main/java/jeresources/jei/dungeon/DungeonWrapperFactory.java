package jeresources.jei.dungeon;

import jeresources.entry.DungeonEntry;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;

public class DungeonWrapperFactory implements IRecipeWrapperFactory<DungeonEntry> {
    @Override
    public IRecipeWrapper getRecipeWrapper(DungeonEntry recipe) {
        return new DungeonWrapper(recipe);
    }
}
