package jeresources.jei.plant;

import jeresources.api.utils.PlantDrop;
import jeresources.entries.PlantEntry;
import jeresources.utils.CollectionHelper;
import jeresources.utils.RenderHelper;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class PlantWrapper implements IRecipeWrapper
{
    private PlantEntry plantEntry;

    public PlantWrapper(PlantEntry entry)
    {
        plantEntry = entry;
    }

    @Override
    public List getInputs()
    {
        return CollectionHelper.create(plantEntry.getPlantItemStack());
    }

    public List<ItemStack> getDrops()
    {
        return plantEntry.getDropItemStacks();
    }

    @Override
    public List getOutputs()
    {
        return plantEntry.getDropItemStacks();
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
        RenderHelper.renderBlock(this.plantEntry.getPlant() != null ? this.plantEntry.getPlant().getPlant(null, null) : Block.getBlockFromItem(this.plantEntry.getPlantItemStack().getItem()).getDefaultState());
    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
    {

    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
        return null;
    }

    public float getChance(ItemStack itemStack)
    {
        PlantDrop drop = this.plantEntry.getDrop(itemStack);
        switch (drop.getDropKind())
        {
            case chance:
                return drop.getChance();
            case weight:
                return  (float)drop.getWeight() / this.plantEntry.getTotalWeight();
            case minMax:
                return Float.NaN;
            default:
                return 0;
        }
    }

    public int[] getMinMax(ItemStack itemStack)
    {
        PlantDrop drop = this.plantEntry.getDrop(itemStack);
        return new int[] {drop.getMinDrop(), drop.getMaxDrop()};
    }

    private String getChanceString(ItemStack itemStack)
    {
        float chance = getChance(itemStack);
        String toPrint;
        if (Float.isNaN(chance))
        {
            int[] minMax = this.getMinMax(itemStack);
            toPrint = minMax[0] + " - " + minMax[1];
        }
        else
            toPrint = String.format("%2.2f", chance * 100).replace(",", ".") + "%";
        return toPrint;
    }
}
