package jeresources.jei.dungeon;

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

public class DungeonCategory implements IRecipeCategory
{
    protected static final int Y_FIRST_ITEM = 47;
    protected static final int X_FIRST_ITEM = 6;
    protected static int SPACING_Y;
    protected static int SPACING_X;
    protected static int ITEMS_PER_PAGE;

    public static void reloadSettings()
    {
        ITEMS_PER_PAGE = Settings.ITEMS_PER_COLUMN * Settings.ITEMS_PER_ROW;
        SPACING_X = 166 / Settings.ITEMS_PER_ROW;
        SPACING_Y = 80 / Settings.ITEMS_PER_COLUMN;
    }

    @Nonnull
    @Override
    public String getUid()
    {
        return JEIConfig.DUNGEON;
    }

    @Nonnull
    @Override
    public String getTitle()
    {
        return TranslationHelper.translateToLocal("jer.dungeon.title");
    }

    @Nonnull
    @Override
    public IDrawable getBackground()
    {
        return Resources.Gui.Jei.DUNGEON;
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
        int x = X_FIRST_ITEM;
        int y = Y_FIRST_ITEM;
        for (int i = 0 ; i < Math.min(ITEMS_PER_PAGE, recipeWrapper.getOutputs().size()); i++)
        {
            recipeLayout.getItemStacks().init(i, false, x, y);
            y += SPACING_Y;
            if (y >= Y_FIRST_ITEM + SPACING_Y * Settings.ITEMS_PER_COLUMN)
            {
                y = Y_FIRST_ITEM;
                x += SPACING_X;
            }
        }

        if (recipeWrapper instanceof DungeonWrapper)
        {
            DungeonWrapper dungeonWrapper = (DungeonWrapper)recipeWrapper;
            for (int i = 0; i < Math.min(ITEMS_PER_PAGE, dungeonWrapper.getItems().size()); i++)
                recipeLayout.getItemStacks().set(i, dungeonWrapper.getItems().get(i));
            dungeonWrapper.resetLid();
        }
    }

}
