package jeresources.jei.dungeon;

import jeresources.config.Settings;
import jeresources.jei.BlankJEIRecipeCategory;
import jeresources.jei.JEIConfig;
import jeresources.reference.Resources;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;


public class DungeonCategory extends BlankJEIRecipeCategory<DungeonWrapper> {
    protected static final int Y_FIRST_ITEM = 44;
    protected static final int X_FIRST_ITEM = 6;
    protected static int SPACING_Y;
    protected static int SPACING_X;
    protected static int ITEMS_PER_PAGE;

    public static void reloadSettings() {
        ITEMS_PER_PAGE = Settings.ITEMS_PER_COLUMN * Settings.ITEMS_PER_ROW * 2;
        SPACING_X = 166 / (Settings.ITEMS_PER_ROW * 2);
        SPACING_Y = 80 / Settings.ITEMS_PER_COLUMN;
    }

    public DungeonCategory() {
        super(JEIConfig.getJeiHelpers().getGuiHelper().createDrawable(Resources.Gui.Jei.TABS, 16, 0, 16, 16));
        reloadSettings();
    }


    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jer.dungeon.title");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return Resources.Gui.Jei.DUNGEON;
    }

    @Override
    public @NotNull RecipeType<DungeonWrapper> getRecipeType() {
        return JEIConfig.DUNGEON_TYPE;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull DungeonWrapper recipeWrapper, @NotNull IFocusGroup focuses) {
        int x = X_FIRST_ITEM;
        int y = Y_FIRST_ITEM;
        int slots = Math.min(recipeWrapper.amountOfItems(focuses.getFocuses(VanillaTypes.ITEM_STACK).findFirst().orElse(null)), ITEMS_PER_PAGE);
        for (int i = 0; i < slots; i++) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, x, y)
                .addTooltipCallback(recipeWrapper)
                .addItemStacks(recipeWrapper.getItems(focuses.getFocuses(VanillaTypes.ITEM_STACK).findFirst().orElse(null), i, slots));
            x += SPACING_X;

            if (x >= X_FIRST_ITEM + SPACING_X * Settings.ITEMS_PER_ROW * 2) {
                x = X_FIRST_ITEM;
                y += SPACING_Y;
            }
        }
        recipeWrapper.resetLid();
    }
}
