package jeresources.jei;

import jeresources.api.utils.PlantDrop;
import jeresources.entries.PlantEntry;
import jeresources.reference.Resources;
import jeresources.registry.PlantRegistry;
import jeresources.utils.TranslationHelper;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class JEIPlantCategory implements IRecipeCategory
{
    private static final int GRASS_X = 75;
    private static final int GRASS_Y = 5;
    private static final int OUTPUT_X = 2;
    private static final int OUTPUT_SCALE = 20;
    private static final int OUTPUT_Y = 51;
    private static final int INPUT_ARROW_Y = 20;

    @Override
    public String getGuiTexture()
    {
        return Resources.Gui.Jei.PLANT.toString();
    }

    @Override
    public String getRecipeName()
    {
        return TranslationHelper.translateToLocal("ner.plant.title");
    }

    @Override
    public int recipiesPerPage()
    {
        return 1;
    }

    @Override
    public void drawBackground(int recipe)
    {
        GL11.glColor4f(1, 1, 1, 1);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 5, 11, 166, 130);
    }

    @Override
    public void loadTransferRects()
    {
        transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(GRASS_X, INPUT_ARROW_Y, 16, 30), JEIConfig.PLANT, new Object()));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results)
    {
        if (outputId.equals(JEIConfig.PLANT))
            for (PlantEntry entry : PlantRegistry.getInstance().getAllPlants())
            arecipes.add(new CachedPlant(entry));
        else
            super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void loadUsageRecipes(String inputId, Object... ingredients)
    {

	if (ingredients.length == 0) return;

        if (ingredients[0] instanceof ItemStack)
        {
            ItemStack ingredient = (ItemStack) ingredients[0];
            if (PlantRegistry.getInstance().contains(ingredient))
                arecipes.add(new CachedPlant(PlantRegistry.getInstance().getEntry(ingredient)));
        } else super.loadUsageRecipes(inputId, ingredients);
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
