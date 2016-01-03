package jeresources.jei.seed;

import jeresources.config.Settings;
import jeresources.jei.JEIConfig;
import jeresources.reference.Resources;
import jeresources.registry.PlantRegistry;
import jeresources.utils.Font;
import jeresources.utils.TranslationHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;

public class JEIAdvSeedCategory implements IRecipeCategory
{
    private static final int Y = 16;

    private static int CYCLE_TIME = (int) (20 * Settings.CYCLE_TIME);

    public static void reloadSettings()
    {
        CYCLE_TIME = (int) (20 * Settings.CYCLE_TIME);
    }


    @Override
    public void loadCraftingRecipes(ItemStack result)
    {
        if (PlantRegistry.getInstance().containsDrop(result))
            arecipes.add(new SeedWrapper(result));
    }

    @Override
    public void drawExtras(int recipe)
    {
        CachedAbstract cachedAbstract = (CachedAbstract) arecipes.get(recipe);

        float chance = cachedAbstract.getChance();
        String toPrint;
        if (Float.isNaN(chance))
        {
            int[] minMax = cachedAbstract.getMinMax();
            toPrint = minMax[0] + " - " + minMax[1];
        }
        else
            toPrint = String.format("%2.2f", chance * 100).replace(",", ".") + "%";

        Font.normal.print(toPrint, 56, Y + 20);

        cachedAbstract.cycleOutput(cycleticks);
    }

    @Nonnull
    @Override
    public String getUid()
    {
        return JEIConfig.SEED;
    }

    @Nonnull
    @Override
    public String getTitle()
    {
        return TranslationHelper.translateToLocal("ner.advPlant.title");
    }

    @Nonnull
    @Override
    public IDrawable getBackground()
    {
        return Resources.Gui.JeiBackground.ADV_PLANT;
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

    public abstract class CachedAbstract extends TemplateRecipeHandler.CachedRecipe
    {
        protected int i, lastI;
        protected long cycleAt;

        public CachedAbstract(int size)
        {
            this.i = 0;
            this.lastI = size;
            this.cycleAt = -1;
        }

        public void cycleOutput(long tick)
        {
            if (cycleAt == -1) cycleAt = tick + CYCLE_TIME;

            if (cycleAt <= tick)
            {
                if (++i >= lastI) i = 0;
                cycleAt += CYCLE_TIME;
            }
        }

        public abstract float getChance();

        public abstract int[] getMinMax();
    }

}
