package jeresources.jei.plant;

import jeresources.jei.BlankJEIRecipeCategory;
import jeresources.jei.JEIConfig;
import jeresources.jei.mob.MobWrapper;
import jeresources.reference.Resources;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;


public class PlantCategory extends BlankJEIRecipeCategory<PlantWrapper> {
    private static final int GRASS_X = 80;
    private static final int GRASS_Y = 11;
    private static final int OUTPUT_X = 7;
    private static final int OUTPUT_SCALE = 20;
    private static final int OUTPUT_Y = 61;

    public PlantCategory() {
        super(JEIConfig.getJeiHelpers().getGuiHelper().createDrawable(Resources.Gui.Jei.TABS, 0, 16, 16, 16));
    }

    @Override
    public @NotNull ResourceLocation getUid() {
        return JEIConfig.PLANT;
    }

    @Override
    public @NotNull Component getTitle() {
        return new TranslatableComponent("jer.plant.title");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return Resources.Gui.Jei.PLANT;
    }

    @Override
    public @NotNull Class<? extends PlantWrapper> getRecipeClass() {
        return PlantWrapper.class;
    }

    @Override
    public @NotNull RecipeType<PlantWrapper> getRecipeType() {
        return JEIConfig.PLANT_TYPE;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull PlantWrapper recipeWrapper, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, GRASS_X, GRASS_Y)
            .addItemStack(recipeWrapper.plantEntry.getPlantItemStack())
            .addTooltipCallback(recipeWrapper);

        int xOffset = 0;
        int yOffset = 0;
        for (int i = 0; i < recipeWrapper.plantEntry.getLootDropStacks().size(); i++) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X + xOffset, OUTPUT_Y + yOffset)
                .addItemStack(recipeWrapper.plantEntry.getLootDropStacks().get(i))
                .addTooltipCallback(recipeWrapper);
            xOffset += OUTPUT_SCALE;
            if (xOffset > 147) {
                xOffset = 0;
                yOffset += OUTPUT_SCALE;
            }
        }
    }
}
