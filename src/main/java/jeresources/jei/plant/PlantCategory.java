package jeresources.jei.plant;

import jeresources.jei.JEIConfig;
import jeresources.reference.Resources;
import jeresources.util.TranslationHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class PlantCategory extends BlankRecipeCategory
{
    private static final int GRASS_X = 79;
    private static final int GRASS_Y = 10;
    private static final int OUTPUT_X = 6;
    private static final int OUTPUT_SCALE = 20;
    private static final int OUTPUT_Y = 60;

    @Nonnull
    @Override
    public String getUid()
    {
        return JEIConfig.PLANT;
    }

    @Nonnull
    @Override
    public String getTitle()
    {
        return TranslationHelper.translateToLocal("jer.plant.title");
    }

    @Nonnull
    @Override
    public IDrawable getBackground()
    {
        return Resources.Gui.Jei.PLANT;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper)
    {
        recipeLayout.getItemStacks().init(0, true, GRASS_X, GRASS_Y);
        int xOffset = 0;
        int yOffset = 0;
        for (int i = 0; i < recipeWrapper.getOutputs().size(); i++)
        {
            recipeLayout.getItemStacks().init(i + 1, false, OUTPUT_X + xOffset, OUTPUT_Y + yOffset);
            xOffset += OUTPUT_SCALE;
            if (xOffset > 147)
            {
                xOffset = 0;
                yOffset += OUTPUT_SCALE;
            }
        }

        if (recipeWrapper instanceof PlantWrapper)
        {
            PlantWrapper plantWrapper = (PlantWrapper) recipeWrapper;
            recipeLayout.getItemStacks().setFromRecipe(0, plantWrapper.getInputs());
            recipeLayout.getItemStacks().addTooltipCallback(plantWrapper);
            for (int i = 0; i < recipeWrapper.getOutputs().size(); i++)
                recipeLayout.getItemStacks().set(i + 1, plantWrapper.getDrops().get(i));
        }
    }

}
