package jeresources.jei.plant;

import jeresources.entry.PlantEntry;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;

public class PlantWrapperFactory implements IRecipeWrapperFactory<PlantEntry> {
    @Override
    public IRecipeWrapper getRecipeWrapper(PlantEntry recipe) {
        return new PlantWrapper(recipe);
    }
}
