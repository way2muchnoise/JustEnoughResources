package jeresources.jei.villager;

import jeresources.collection.TradeList;
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
public class VillagerCategory extends BlankRecipeCategory<VillagerWrapper> {
    protected static final int X_FIRST_ITEM = 95;
    protected static final int X_ITEM_DISTANCE = 18;
    protected static final int X_ITEM_RESULT = 150;
    protected static final int Y_ITEM_DISTANCE = 22;

    private IDrawable icon;

    public VillagerCategory() {
        icon = JEIConfig.getJeiHelpers().getGuiHelper().createDrawable(Resources.Gui.Jei.TABS, 0, 0, 16, 16);
    }

    @Nonnull
    @Override
    public String getUid() {
        return JEIConfig.VILLAGER;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return TranslationHelper.translateToLocal("jer.villager.title");
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return Resources.Gui.Jei.VILLAGER;
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull VillagerWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
        IFocus<ItemStack> focus = (IFocus<ItemStack>) recipeLayout.getFocus();
        recipeWrapper.setFocus(focus);
        int y = Y_ITEM_DISTANCE * (6 - recipeWrapper.getPossibleLevels(focus).size()) / 2;
        for (int i = 0; i < recipeWrapper.getPossibleLevels(focus).size(); i++) {
            recipeLayout.getItemStacks().init(3 * i, true, X_FIRST_ITEM, y + i * Y_ITEM_DISTANCE);
            recipeLayout.getItemStacks().init(3 * i + 1, true, X_FIRST_ITEM + X_ITEM_DISTANCE, y + i * Y_ITEM_DISTANCE);
            recipeLayout.getItemStacks().init(3 * i + 2, false, X_ITEM_RESULT, y + i * Y_ITEM_DISTANCE);
        }

        int i = 0;
        for (int level : recipeWrapper.getPossibleLevels(focus)) {
            TradeList tradeList = recipeWrapper.getTrades(level).getFocusedList(focus);
            recipeLayout.getItemStacks().set(3 * i, tradeList.getFirstBuyStacks());
            recipeLayout.getItemStacks().set(3 * i + 1, tradeList.getSecondBuyStacks());
            recipeLayout.getItemStacks().set(3 * i + 2, tradeList.getSellStacks());
            i++;
        }
    }
}
