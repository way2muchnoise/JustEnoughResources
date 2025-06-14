package jeresources.jei.villager;

import jeresources.collection.TradeList;
import jeresources.entry.AbstractVillagerEntry;
import jeresources.jei.BlankJEIRecipeCategory;
import jeresources.jei.JEIConfig;
import jeresources.reference.Resources;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VillagerCategory extends BlankJEIRecipeCategory<AbstractVillagerEntry> {
    protected static final int X_FIRST_ITEM = 95;
    protected static final int X_ITEM_DISTANCE = 18;
    protected static final int X_ITEM_RESULT = 150;
    protected static final int Y_ITEM_DISTANCE = 22;

    public VillagerCategory() {
        super(JEIConfig.getJeiHelpers().getGuiHelper().createDrawable(Resources.Gui.Jei.TABS, 0, 0, 16, 16), new VillagerWrapper());
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jer.villager.title");
    }

    @Override
    public @NotNull IDrawable getJERBackground() {
        return Resources.Gui.Jei.VILLAGER;
    }

    @Override
    public @NotNull IRecipeType<AbstractVillagerEntry> getRecipeType() {
        return JEIConfig.VILLAGER_TYPE;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull AbstractVillagerEntry recipe, @NotNull IFocusGroup focuses) {
        if (recipe.hasPois()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 50, 19)
                .addItemStacks(recipe.getPois());
        }

        IFocus<ItemStack> focus = focuses.getFocuses(VanillaTypes.ITEM_STACK).findFirst().orElse(null);
        ((VillagerWrapper)recipeCategoryExtension).setFocus(focus);
        List<Integer> possibleLevels = recipe.getPossibleLevels(focus);
        int y = 1 + Y_ITEM_DISTANCE * (6 - possibleLevels.size()) / 2;
        for (int i = 0; i < possibleLevels.size(); i++) {
            TradeList tradeList = recipe.getVillagerTrades(possibleLevels.get(i)).getFocusedList(focus);
            builder.addSlot(RecipeIngredientRole.INPUT, 1 + X_FIRST_ITEM, y + i * Y_ITEM_DISTANCE)
                .addItemStacks(tradeList.getCostAs());
            builder.addSlot(RecipeIngredientRole.INPUT, 1 + X_FIRST_ITEM + X_ITEM_DISTANCE, y + i * Y_ITEM_DISTANCE)
                .addItemStacks(tradeList.getCostBs());
            builder.addSlot(RecipeIngredientRole.OUTPUT, 1 + X_ITEM_RESULT, y + i * Y_ITEM_DISTANCE)
                .addItemStacks(tradeList.getResults());
        }
    }
}
