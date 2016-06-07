package jeresources.jei.plant;

import jeresources.entry.PlantEntry;
import jeresources.jei.JEIConfig;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class PlantHandler implements IRecipeHandler<PlantEntry>
{
    @Nonnull
    @Override
    public Class<PlantEntry> getRecipeClass()
    {
        return PlantEntry.class;
    }

    @Deprecated
    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return JEIConfig.PLANT;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull PlantEntry recipe)
    {
        return JEIConfig.PLANT;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull PlantEntry recipe)
    {
        return new PlantWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@Nonnull PlantEntry recipe)
    {
        return true;
    }
}
