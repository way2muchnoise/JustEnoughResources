package jeresources.jei.ore;

import jeresources.config.Settings;
import jeresources.entries.OreMatchEntry;
import jeresources.utils.Font;
import jeresources.utils.RenderHelper;
import jeresources.utils.TranslationHelper;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

public class OreWrapper implements IRecipeWrapper
{
    private OreMatchEntry oreMatchEntry;
    private List<ItemStack> oresAndDrops;

    public OreWrapper(OreMatchEntry oreMatchEntry)
    {
        this.oreMatchEntry = oreMatchEntry;
        this.oresAndDrops = oreMatchEntry.getOresAndDrops();
    }

    public int getLineColor()
    {
        return this.oreMatchEntry.getColour();
    }

    public List<String> getRestrictions()
    {
        return this.oreMatchEntry.getRestrictions();
    }

    public boolean contains(ItemStack itemStack)
    {
        for (ItemStack listStack : this.oresAndDrops)
            if (listStack.isItemEqual(itemStack)) return true;
        return false;
    }

    @Override
    public List getInputs()
    {
        return null;
    }

    @Override
    public List getOutputs()
    {
        return this.oresAndDrops;
    }

    public List<ItemStack> getOresAndDrops()
    {
        return this.oresAndDrops;
    }

    @Override
    public List<FluidStack> getFluidInputs()
    {
        return null;
    }

    @Override
    public List<FluidStack> getFluidOutputs()
    {
        return null;
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
    {
        float[] array = this.oreMatchEntry.getChances();
        double max = 0;
        for (double d : array)
            if (d > max) max = d;
        double xPrev = JEIOreCategory.X_OFFSPRING;
        double yPrev = JEIOreCategory.Y_OFFSPRING;
        double space = JEIOreCategory.X_AXIS_SIZE / ((array.length - 1) * 1D);
        for (int i = 0; i < array.length; i++)
        {
            double value = array[i];
            double y = JEIOreCategory.Y_OFFSPRING - ((value / max) * JEIOreCategory.Y_AXIS_SIZE);
            if (i > 0) // Only draw a line after the first element (cannot draw line with only one point)
            {
                double x = xPrev + space;
                RenderHelper.drawLine(xPrev, yPrev, x, y, getLineColor());
                xPrev = x;
            }
            yPrev = y;
        }

        Font.small.print("0%", JEIOreCategory.X_OFFSPRING - 10, JEIOreCategory.Y_OFFSPRING - 7);
        Font.small.print(String.format("%.2f", max * 100) + "%", JEIOreCategory.X_OFFSPRING - 20, JEIOreCategory.Y_OFFSPRING - JEIOreCategory.Y_AXIS_SIZE);
        int minY = this.oreMatchEntry.getMinY() - Settings.EXTRA_RANGE;
        Font.small.print(minY < 0 ? 0 : minY, JEIOreCategory.X_OFFSPRING - 3, JEIOreCategory.Y_OFFSPRING + 2);
        int maxY = this.oreMatchEntry.getMaxY() + Settings.EXTRA_RANGE;
        Font.small.print(maxY > 255 ? 255 : maxY, JEIOreCategory.X_OFFSPRING + JEIOreCategory.X_AXIS_SIZE, JEIOreCategory.Y_OFFSPRING + 2);
        Font.small.print(TranslationHelper.translateToLocal("jer.ore.bestY") + ": " + this.oreMatchEntry.getBestY(), JEIOreCategory.X_ITEM - 2, JEIOreCategory.Y_ITEM + 20);
    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
    {

    }
}
