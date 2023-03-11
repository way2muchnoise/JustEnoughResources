package jeresources.jei.villager;

import jeresources.collection.TradeList;
import jeresources.jei.BlankJEIRecipeCategory;
import jeresources.jei.JEIConfig;
import jeresources.jei.plant.PlantWrapper;
import jeresources.reference.Resources;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VillagerCategory extends BlankJEIRecipeCategory<VillagerWrapper> {
    protected static final int X_FIRST_ITEM = 95;
    protected static final int X_ITEM_DISTANCE = 18;
    protected static final int X_ITEM_RESULT = 150;
    protected static final int Y_ITEM_DISTANCE = 22;

    public VillagerCategory() {
        super(JEIConfig.getJeiHelpers().getGuiHelper().createDrawable(Resources.Gui.Jei.TABS, 0, 0, 16, 16));
    }

    @Override
    public @NotNull ResourceLocation getUid() {
        return JEIConfig.VILLAGER;
    }

    @Override
    public @NotNull Component getTitle() {
        return new TranslatableComponent("jer.villager.title");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return Resources.Gui.Jei.VILLAGER;
    }

    @Override
    public @NotNull Class<? extends VillagerWrapper> getRecipeClass() {
        return VillagerWrapper.class;
    }

    @Override
    public @NotNull RecipeType<VillagerWrapper> getRecipeType() {
        return JEIConfig.VILLAGER_TYPE;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull VillagerWrapper recipeWrapper, @NotNull IFocusGroup focuses) {
        if (recipeWrapper.hasPois()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 50, 19)
                .addItemStacks(recipeWrapper.getPois());
        }

        IFocus<ItemStack> focus = focuses.getFocuses(VanillaTypes.ITEM).findFirst().orElse(null);
        recipeWrapper.setFocus(focus);
        List<Integer> possibleLevels = recipeWrapper.getPossibleLevels(focus);
        int y = 1 + Y_ITEM_DISTANCE * (6 - possibleLevels.size()) / 2;
        for (int i = 0; i < possibleLevels.size(); i++) {
            TradeList tradeList = recipeWrapper.getTrades(possibleLevels.get(i)).getFocusedList(focus);
            builder.addSlot(RecipeIngredientRole.INPUT, 1 + X_FIRST_ITEM, y + i * Y_ITEM_DISTANCE)
                .addItemStacks(tradeList.getCostAs());
            builder.addSlot(RecipeIngredientRole.INPUT, 1 + X_FIRST_ITEM + X_ITEM_DISTANCE, y + i * Y_ITEM_DISTANCE)
                .addItemStacks(tradeList.getCostBs());
            builder.addSlot(RecipeIngredientRole.OUTPUT, 1 + X_ITEM_RESULT, y + i * Y_ITEM_DISTANCE)
                .addItemStacks(tradeList.getResults());
        }
    }
}
