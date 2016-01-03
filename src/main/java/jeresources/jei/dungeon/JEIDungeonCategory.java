package jeresources.jei.dungeon;

import jeresources.config.Settings;
import jeresources.jei.JEIConfig;
import jeresources.reference.Resources;
import jeresources.registry.DungeonRegistry;
import jeresources.utils.Font;
import jeresources.utils.TranslationHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;

public class JEIDungeonCategory implements IRecipeCategory
{
    public static final int X_FIRST_ITEM = 5;
    public static final int Y_FIRST_ITEM = 48;

    public static int ITEMS_PER_PAGE;
    public static int SPACING_X;
    public static int SPACING_Y;
    private static int CYCLE_TIME;

    public static void reloadSettings()
    {
        ITEMS_PER_PAGE = Settings.ITEMS_PER_COLUMN * Settings.ITEMS_PER_ROW;
        SPACING_X = 166 / Settings.ITEMS_PER_ROW;
        SPACING_Y = 80 / Settings.ITEMS_PER_COLUMN;
        CYCLE_TIME = (int) (20 * Settings.CYCLE_TIME);
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
        return TranslationHelper.translateToLocal("ner.dungeon.title");
    }

    @Nonnull
    @Override
    public IDrawable getBackground()
    {
        return Resources.Gui.JeiBackground.DUNGEON;
    }

    @Override
    public void drawExtras(Minecraft minecraft)
    {
        DungeonDrawable.DungeonWrapper cachedChest = (DungeonDrawable.DungeonWrapper) arecipes.get(recipe);

        Font.normal.print(TranslationHelper.translateToLocal(cachedChest.chest.getName()), 60, 7);
        Font.small.print(DungeonRegistry.getInstance().getNumStacks(cachedChest.chest), 60, 20);
        if (cachedChest.lastSet > 0)
            Font.small.print(TranslationHelper.getLocalPageInfo(cachedChest.set, cachedChest.lastSet), 60, 36);

        int x = X_FIRST_ITEM + 18;
        int y = Y_FIRST_ITEM + (8 - Settings.ITEMS_PER_COLUMN);
        for (int i = ITEMS_PER_PAGE * cachedChest.set; i < ITEMS_PER_PAGE * cachedChest.set + ITEMS_PER_PAGE; i++)
        {
            if (i >= cachedChest.getContents().length) break;
            double chance = cachedChest.getChances()[i] * 100;
            String format = chance < 100 ? "%2.1f" : "%2.0f";
            String toPrint = String.format(format, chance).replace(',', '.') + "%";
            Font.small.print(toPrint, x, y);
            y += SPACING_Y;
            if (y >= Y_FIRST_ITEM + SPACING_Y * Settings.ITEMS_PER_COLUMN)
            {
                y = Y_FIRST_ITEM + (8 - Settings.ITEMS_PER_COLUMN);
                x += SPACING_X;
            }
        }

        cachedChest.cycleOutputs(cycleticks, recipe);
    }

    @Override
    public void drawAnimations(Minecraft minecraft)
    {
        Resources.Gui.JeiBackground.DUNGEON.doAnimation(true);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper)
    {
        recipeLayout.getItemStacks().init();
    }

}
