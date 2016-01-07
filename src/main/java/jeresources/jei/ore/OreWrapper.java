package jeresources.jei.ore;

import jeresources.api.utils.conditionals.Conditional;
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
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class OreWrapper implements IRecipeWrapper
{
    protected static final int X_OFFSPRING = 59;
    protected static final int Y_OFFSPRING = 52;
    protected static final int X_AXIS_SIZE = 90;
    protected static final int Y_AXIS_SIZE = 40;
    protected static final int X_ITEM = 17;
    protected static final int Y_ITEM = 21;

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
        double xPrev = X_OFFSPRING;
        double yPrev = Y_OFFSPRING;
        double space = X_AXIS_SIZE / ((array.length - 1) * 1D);
        for (int i = 0; i < array.length; i++)
        {
            double value = array[i];
            double y = Y_OFFSPRING - ((value / max) * Y_AXIS_SIZE);
            if (i > 0) // Only draw a line after the first element (cannot draw line with only one point)
            {
                double x = xPrev + space;
                RenderHelper.drawLine(xPrev, yPrev, x, y, getLineColor());
                xPrev = x;
            }
            yPrev = y;
        }

        Font.small.print("0%", X_OFFSPRING - 10, Y_OFFSPRING - 7);
        Font.small.print(String.format("%.2f", max * 100) + "%", X_OFFSPRING - 20, Y_OFFSPRING - Y_AXIS_SIZE);
        int minY = this.oreMatchEntry.getMinY() - Settings.EXTRA_RANGE;
        Font.small.print(minY < 0 ? 0 : minY, X_OFFSPRING - 3, Y_OFFSPRING + 2);
        int maxY = this.oreMatchEntry.getMaxY() + Settings.EXTRA_RANGE;
        Font.small.print(maxY > 255 ? 255 : maxY, X_OFFSPRING + X_AXIS_SIZE, Y_OFFSPRING + 2);
        Font.small.print(TranslationHelper.translateToLocal("jer.ore.bestY") + ": " + this.oreMatchEntry.getBestY(), X_ITEM - 2, Y_ITEM + 20);
    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
    {

    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
        List<String> tooltip = null;

        if (onGraph(mouseX, mouseY))
            tooltip = getLineTooltip(mouseX, new LinkedList<String>());
        else if (false)
            tooltip = getItemStackTooltip(null, new LinkedList<String>());

        return tooltip;
    }

    private List<String> getItemStackTooltip(ItemStack itemStack, List<String> tooltip)
    {
        if (itemStack != null)
        {
            if (this.oreMatchEntry.isSilkTouchNeeded(itemStack))
                tooltip.add(Conditional.silkTouch.toString());
            tooltip.addAll(this.getRestrictions());
        }
        return tooltip;
    }

    private List<String> getLineTooltip(int mouseX, List<String> tooltip)
    {
        float[] chances = this.oreMatchEntry.getChances();
        double space = X_AXIS_SIZE / (chances.length * 1D);
        // Calculate the hovered over y value
        int index = (int) ((mouseX - X_OFFSPRING) / space);
        int yValue = Math.max(0, index + this.oreMatchEntry.getMinY() - Settings.EXTRA_RANGE + 1);
        if (index >= 0 && index < chances.length)
            tooltip.add("Y: " + yValue + String.format(" (%.2f%%)", chances[index] * 100));

        return tooltip;
    }

    private boolean onGraph(int mouseX, int mouseY)
    {
        return
                mouseX >= X_OFFSPRING - 1
                && mouseX < X_OFFSPRING + X_AXIS_SIZE
                && mouseY >= Y_OFFSPRING - Y_AXIS_SIZE - 1
                && mouseY < Y_OFFSPRING;
    }
}
