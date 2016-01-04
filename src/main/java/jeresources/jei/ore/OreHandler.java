package jeresources.jei.ore;

import jeresources.entries.OreMatchEntry;
import jeresources.jei.JEIConfig;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class OreHandler implements IRecipeHandler<OreMatchEntry>
{
    @Nonnull
    @Override
    public Class<OreMatchEntry> getRecipeClass()
    {
        return OreMatchEntry.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return JEIConfig.ORE;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull OreMatchEntry recipe)
    {
        return new OreWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@Nonnull OreMatchEntry recipe)
    {
        return true;
    }
}
