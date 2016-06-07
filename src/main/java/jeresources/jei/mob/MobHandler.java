package jeresources.jei.mob;

import jeresources.entry.MobEntry;
import jeresources.jei.JEIConfig;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class MobHandler implements IRecipeHandler<MobEntry>
{
    @Nonnull
    @Override
    public Class<MobEntry> getRecipeClass()
    {
        return MobEntry.class;
    }

    @Deprecated
    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return JEIConfig.MOB;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull MobEntry recipe)
    {
        return JEIConfig.MOB;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull MobEntry recipe)
    {
        return new MobWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@Nonnull MobEntry recipe)
    {
        return true;
    }
}
