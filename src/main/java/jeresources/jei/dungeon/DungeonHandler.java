package jeresources.jei.dungeon;

import jeresources.entry.DungeonEntry;
import jeresources.jei.JEIConfig;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class DungeonHandler implements IRecipeHandler<DungeonEntry>
{
    @Nonnull
    @Override
    public Class<DungeonEntry> getRecipeClass()
    {
        return DungeonEntry.class;
    }

    @Deprecated
    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return JEIConfig.DUNGEON;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull DungeonEntry recipe)
    {
        return JEIConfig.DUNGEON;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull DungeonEntry recipe)
    {
        return new DungeonWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@Nonnull DungeonEntry recipe)
    {
        return true;
    }
}
