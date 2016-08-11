package jeresources.jei.mob;

import jeresources.config.Settings;
import jeresources.jei.JEIConfig;
import jeresources.reference.Resources;
import jeresources.util.TranslationHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class MobCategory extends BlankRecipeCategory
{
    protected static final int X_FIRST_ITEM = 97;
    protected static final int Y_FIRST_ITEM = 43;
    protected static int SPACING_X = 72 / Settings.ITEMS_PER_ROW;
    protected static int SPACING_Y = 80 / Settings.ITEMS_PER_COLUMN;

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
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper)
    {
        int xOffset = 0;
        int slot = 0;
        for (int i = 0; i < Settings.ITEMS_PER_ROW; i++)
        {
            int yOffset = 0;
            for (int ii = 0; ii < Settings.ITEMS_PER_COLUMN; ii++)
            {
                recipeLayout.getItemStacks().init(slot++, false, X_FIRST_ITEM + xOffset, Y_FIRST_ITEM + yOffset);
                yOffset += SPACING_Y;
            }
            xOffset += SPACING_X;
        }

        if (recipeWrapper instanceof MobWrapper)
        {
            MobWrapper mobWrapper = (MobWrapper) recipeWrapper;
            recipeLayout.getItemStacks().addTooltipCallback(mobWrapper);
            for (int i = 0; i < Math.min(mobWrapper.getDrops().length, Settings.ITEMS_PER_ROW * Settings.ITEMS_PER_COLUMN); i++)
                recipeLayout.getItemStacks().set(i, mobWrapper.getDrops()[i].getDrops());
        }
    }
}
