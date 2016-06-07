package jeresources.jei.worldgen;

import jeresources.entry.WorldGenEntry;
import jeresources.jei.JEIConfig;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class WorldGenHandler implements IRecipeHandler<WorldGenEntry>
{
    @Nonnull
    @Override
    public Class<WorldGenEntry> getRecipeClass()
    {
        return WorldGenEntry.class;
    }

    @Deprecated
    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return JEIConfig.WORLD_GEN;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull WorldGenEntry recipe)
    {
        return JEIConfig.WORLD_GEN;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull WorldGenEntry recipe)
    {
        return new WorldGenWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@Nonnull WorldGenEntry recipe)
    {
        return true;
    }
}
