package jeresources.jei.dungeon;

import jeresources.config.Settings;
import jeresources.jei.JEIConfig;
import jeresources.reference.Resources;
import jeresources.util.TranslationHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IFocus;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("unchecked")
public class DungeonCategory extends BlankRecipeCategory<DungeonWrapper>
{
    protected static final int Y_FIRST_ITEM = 44;
    protected static final int X_FIRST_ITEM = 6;
    protected static int SPACING_Y;
    protected static int SPACING_X;
    protected static int ITEMS_PER_PAGE;

    public static void reloadSettings()
    {
        ITEMS_PER_PAGE = Settings.ITEMS_PER_COLUMN * Settings.ITEMS_PER_ROW * 2;
        SPACING_X = 166 / (Settings.ITEMS_PER_ROW * 2);
        SPACING_Y = 80 / Settings.ITEMS_PER_COLUMN;
    }

    private IDrawable icon;

    public DungeonCategory() {
        icon = JEIConfig.getJeiHelpers().getGuiHelper().createDrawable(Resources.Gui.Jei.TABS, 16, 0, 16, 16);
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

    @Nullable
    @Override
    public IDrawable getIcon()
    {
        return icon;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull DungeonWrapper recipeWrapper)
    {
        int x = X_FIRST_ITEM;
        int y = Y_FIRST_ITEM;
        for (int i = 0; i < Math.min(ITEMS_PER_PAGE, recipeWrapper.getOutputs().size()); i++)
        {
            recipeLayout.getItemStacks().init(i, false, x, y);
            x += SPACING_X;

            if (x >= X_FIRST_ITEM + SPACING_X * Settings.ITEMS_PER_ROW * 2)
            {
                x = X_FIRST_ITEM;
                y += SPACING_Y;
            }
        }

        recipeLayout.getItemStacks().addTooltipCallback(recipeWrapper);
        IFocus<ItemStack> focus = (IFocus<ItemStack>) recipeLayout.getFocus();
        int slots = Math.min(recipeWrapper.amountOfItems(focus), ITEMS_PER_PAGE);
        for (int i = 0; i < slots; i++)
            recipeLayout.getItemStacks().set(i, recipeWrapper.getItems(focus, i, slots));
        recipeWrapper.resetLid();
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull DungeonWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
        int x = X_FIRST_ITEM;
        int y = Y_FIRST_ITEM;
        for (int i = 0; i < Math.min(ITEMS_PER_PAGE, ingredients.getOutputs(ItemStack.class).size()); i++)
        {
            recipeLayout.getItemStacks().init(i, false, x, y);
            x += SPACING_X;

            if (x >= X_FIRST_ITEM + SPACING_X * Settings.ITEMS_PER_ROW * 2)
            {
                x = X_FIRST_ITEM;
                y += SPACING_Y;
            }
        }

        recipeLayout.getItemStacks().addTooltipCallback(recipeWrapper);
        IFocus<ItemStack> focus = (IFocus<ItemStack>) recipeLayout.getFocus();
        int slots = Math.min(recipeWrapper.amountOfItems(focus), ITEMS_PER_PAGE);
        for (int i = 0; i < slots; i++)
            recipeLayout.getItemStacks().set(i, recipeWrapper.getItems(focus, i, slots));
        recipeWrapper.resetLid();
    }

}
