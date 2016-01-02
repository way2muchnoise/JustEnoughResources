package jeresources.jei.category;

import jeresources.api.utils.PlantDrop;
import jeresources.entries.PlantEntry;
import jeresources.jei.JEIConfig;
import jeresources.reference.Resources;
import jeresources.utils.TranslationHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;

public class JEIPlantCategory implements IRecipeCategory
{
    private static final int GRASS_X = 75;
    private static final int GRASS_Y = 5;
    private static final int OUTPUT_X = 2;
    private static final int OUTPUT_SCALE = 20;

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
        return TranslationHelper.translateToLocal("ner.plant.title");
    }

    @Nonnull
    @Override
    public IDrawable getBackground()
    {
        return Resources.Gui.JeiBackground.PLANT;
    }

    @Override
    public void drawExtras(Minecraft minecraft)
    {

    }

    @Override
    public void drawAnimations(Minecraft minecraft)
    {

    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper)
    {

    }

    public class CachedPlant extends TemplateRecipeHandler.CachedRecipe
    {
        private PlantEntry plantEntry;

        public CachedPlant(PlantEntry entry)
        {
            plantEntry = entry;
        }

        @Override
        public PositionedStack getResult()
        {
            return new PositionedStack(plantEntry.getPlant(), GRASS_X, GRASS_Y);
        }

        @Override
        public List<PositionedStack> getOtherStacks()
        {
            List<PositionedStack> list = new ArrayList<PositionedStack>();
            int xOffset = 0;
            int yOffset = 0;
            for (PlantDrop plantDrop : plantEntry.getDrops())
            {
                list.add(new PositionedStack(plantDrop.getDrop(), OUTPUT_X + xOffset, OUTPUT_Y + yOffset));
                xOffset += OUTPUT_SCALE;
                if (xOffset > 147)
                {
                    xOffset = 0;
                    yOffset += OUTPUT_SCALE;
                }
            }
            return list;
        }
    }
}
