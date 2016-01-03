package jeresources.jei.plant;

import jeresources.entries.PlantEntry;
import jeresources.utils.CollectionHelper;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
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
        return CollectionHelper.create(plantEntry.getPlant());
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

    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
    {

    }
}
