package jeresources.jei.mob;

import jeresources.config.Settings;
import jeresources.jei.JEIConfig;
import jeresources.reference.Resources;
import jeresources.utils.TranslationHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;

public class MobCategory implements IRecipeCategory
{
    protected static final int X_FIRST_ITEM = 90;
    protected static final int Y_FIRST_ITEM = 43;
    protected static int SPACING_Y = 90 / Settings.ITEMS_PER_COLUMN;

    @Nonnull
    @Override
    public String getUid()
    {
        return JEIConfig.MOB;
    }

    @Nonnull
    @Override
    public String getTitle()
    {
        return TranslationHelper.translateToLocal("jer.mob.title");
    }

    @Nonnull
    @Override
    public IDrawable getBackground()
    {
        return Resources.Gui.Jei.MOB;
    }

    @Override
    public void drawExtras(Minecraft minecraft)
    {

    }

    @Override
    public void drawAnimations(Minecraft minecraft)
    {

    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper)
    {
        int yOffset = 0;
        for (int i = 0; i < recipeWrapper.getOutputs().size(); i++)
        {
            recipeLayout.getItemStacks().init(i, false, X_FIRST_ITEM, Y_FIRST_ITEM + yOffset);
            yOffset += MobCategory.SPACING_Y;
        }

        if (recipeWrapper instanceof MobWrapper)
        {
            MobWrapper mobWrapper = (MobWrapper)recipeWrapper;
            for (int i = 0; i < recipeWrapper.getOutputs().size(); i++)
                recipeLayout.getItemStacks().set(i, mobWrapper.getDrops().get(i));
        }
    }
}
