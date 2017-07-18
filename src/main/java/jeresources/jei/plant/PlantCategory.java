package jeresources.jei.plant;

import jeresources.jei.BlankJEIRecipeCategory;
import jeresources.jei.JEIConfig;
import jeresources.reference.Resources;
import jeresources.util.TranslationHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class PlantCategory extends BlankJEIRecipeCategory<PlantWrapper> {
    private static final int GRASS_X = 79;
    private static final int GRASS_Y = 10;
    private static final int OUTPUT_X = 6;
    private static final int OUTPUT_SCALE = 20;
    private static final int OUTPUT_Y = 60;

    public PlantCategory() {
        super(JEIConfig.getJeiHelpers().getGuiHelper().createDrawable(Resources.Gui.Jei.TABS, 0, 16, 16, 16));
    }

    @Nonnull
    @Override
    public String getUid() {
        return JEIConfig.PLANT;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return TranslationHelper.translateToLocal("jer.plant.title");
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return Resources.Gui.Jei.PLANT;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull PlantWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, GRASS_X, GRASS_Y);
        int xOffset = 0;
        int yOffset = 0;
        for (int i = 0; i < ingredients.getOutputs(ItemStack.class).size(); i++) {
            recipeLayout.getItemStacks().init(i + 1, false, OUTPUT_X + xOffset, OUTPUT_Y + yOffset);
            xOffset += OUTPUT_SCALE;
            if (xOffset > 147) {
                xOffset = 0;
                yOffset += OUTPUT_SCALE;
            }
        }

        recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));
        recipeLayout.getItemStacks().addTooltipCallback(recipeWrapper);
        for (int i = 0; i < ingredients.getOutputs(ItemStack.class).size(); i++)
            recipeLayout.getItemStacks().set(i + 1, ingredients.getOutputs(ItemStack.class).get(i));
    }

}
