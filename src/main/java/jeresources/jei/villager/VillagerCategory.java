package jeresources.jei.villager;

import jeresources.jei.JEIConfig;
import jeresources.reference.Resources;
import jeresources.util.TranslationHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class VillagerCategory extends BlankRecipeCategory
{
    protected static final int X_FIRST_ITEM = 90;
    protected static final int X_ITEM_DISTANCE = 18;
    protected static final int X_ITEM_RESULT = 145;
    protected static final int Y_ITEM_DISTANCE = 22;

    @Nonnull
    @Override
    public String getUid()
    {
        return JEIConfig.VILLAGER;
    }

    @Nonnull
    @Override
    public String getTitle()
    {
        return TranslationHelper.translateToLocal("jer.villager.title");
    }

    @Nonnull
    @Override
    public IDrawable getBackground()
    {
        return Resources.Gui.Jei.VILLAGER;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper)
    {
        if (recipeWrapper instanceof VillagerWrapper)
        {
            VillagerWrapper wrapper = (VillagerWrapper)recipeWrapper;
            int y = Y_ITEM_DISTANCE * (6 - wrapper.getMaxLevel()) / 2;
            for (int i = 0; i < wrapper.getMaxLevel(); i++)
            {
                recipeLayout.getItemStacks().init(3 * i, true, X_FIRST_ITEM, y + i * Y_ITEM_DISTANCE);
                recipeLayout.getItemStacks().init(3 * i + 1, true, X_FIRST_ITEM + X_ITEM_DISTANCE, y + i * Y_ITEM_DISTANCE);
                recipeLayout.getItemStacks().init(3 * i + 2, false, X_ITEM_RESULT, y + i * Y_ITEM_DISTANCE);
            }

            // TODO: change focus of recipe mode or figure out way to handle it properly
            // added sublist functions to the TradeList (but where to get the item searched for?)
            for (int i = 0; i < wrapper.getMaxLevel(); i++)
            {
                recipeLayout.getItemStacks().set(3 * i, wrapper.getTrades(i).getFirstBuyStacks());
                recipeLayout.getItemStacks().set(3 * i + 1, wrapper.getTrades(i).getSecondBuyStacks());
                recipeLayout.getItemStacks().set(3 * i + 2, wrapper.getTrades(i).getSellStacks());
            }
        }
    }
}
