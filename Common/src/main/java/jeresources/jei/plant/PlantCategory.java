package jeresources.jei.plant;

import jeresources.entry.PlantEntry;
import jeresources.jei.BlankJEIRecipeCategory;
import jeresources.jei.JEIConfig;
import jeresources.reference.Resources;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;


public class PlantCategory extends BlankJEIRecipeCategory<PlantEntry> {
    private static final int GRASS_X = 80;
    private static final int GRASS_Y = 11;
    private static final int OUTPUT_X = 7;
    private static final int OUTPUT_SCALE = 20;
    private static final int OUTPUT_Y = 61;

    public PlantCategory() {
        super(JEIConfig.getJeiHelpers().getGuiHelper().createDrawable(Resources.Gui.Jei.TABS, 0, 16, 16, 16), new PlantWrapper());
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jer.plant.title");
    }

    @Override
    public @NotNull IDrawable getJERBackground() {
        return Resources.Gui.Jei.PLANT;
    }

    @Override
    public @NotNull IRecipeType<PlantEntry> getRecipeType() {
        return JEIConfig.PLANT_TYPE;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull PlantEntry recipe, @NotNull IFocusGroup focuses) {
        PlantTooltip plantTooltip = new PlantTooltip(recipe);
        builder.addSlot(RecipeIngredientRole.INPUT, GRASS_X, GRASS_Y)
            .add(recipe.getPlantItemStack())
            .addRichTooltipCallback(plantTooltip);

        int xOffset = 0;
        int yOffset = 0;
        for (int i = 0; i < recipe.getLootDropStacks().size(); i++) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X + xOffset, OUTPUT_Y + yOffset)
                .add(recipe.getLootDropStacks().get(i))
                .addRichTooltipCallback(plantTooltip);
            xOffset += OUTPUT_SCALE;
            if (xOffset > 147) {
                xOffset = 0;
                yOffset += OUTPUT_SCALE;
            }
        }
    }
}
