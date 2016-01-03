package jeresources.jei.seed;

import jeresources.api.utils.PlantDrop;
import jeresources.entries.PlantEntry;
import jeresources.registry.PlantRegistry;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

public class SeedWrapper implements IRecipeWrapper
{
    private List<PlantEntry> entrys;
    private ItemStack seed;


    public SeedWrapper(ItemStack seed)
    {
        super(PlantRegistry.getInstance().getEntriesForDrop(seed).size());
        this.seed = seed;
        this.entrys = PlantRegistry.getInstance().getEntriesForDrop(seed);
    }

    @Override
    public PositionedStack getResult()
    {
        return new PositionedStack(entrys.get(i).getPlant(), 34, JEIAdvSeedCategory.Y);
    }

    @Override
    public PositionedStack getOtherStack()
    {
        return new PositionedStack(seed, 94, JEIAdvSeedCategory.Y);
    }

    @Override
    public float getChance()
    {
        PlantEntry entry = entrys.get(i);
        PlantDrop drop = entry.getDrop(seed);
        switch (drop.getDropKind())
        {
            case chance:
                return drop.getChance();
            case weight:
                return  (float)drop.getWeight() / entry.getTotalWeight();
            case minMax:
                return Float.NaN;
            default:
                return 0;
        }
    }

    @Override
    public int[] getMinMax()
    {
        PlantEntry entry = entrys.get(i);
        PlantDrop drop = entry.getDrop(seed);
        return new int[] {drop.getMinDrop(), drop.getMaxDrop()};
    }

    @Override
    public List getInputs()
    {
        return null;
    }

    @Override
    public List getOutputs()
    {
        return null;
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
