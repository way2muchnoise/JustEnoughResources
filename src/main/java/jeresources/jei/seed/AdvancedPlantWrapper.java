package jeresources.jei.seed;

import jeresources.api.utils.PlantDrop;
import jeresources.entries.PlantEntry;

public class AdvancedPlantWrapper extends JEIAdvSeedCategory.CachedAbstract
{
    private PlantEntry entry;

    public AdvancedPlantWrapper(PlantEntry entry)
    {
        super(entry.getDrops().size());
        this.entry = entry;
    }

    @Override
    public PositionedStack getResult()
    {
        return new PositionedStack(entry.getPlant(), 34, JEIAdvSeedCategory.Y);
    }

    @Override
    public PositionedStack getOtherStack()
    {
        return new PositionedStack(entry.getDrops().get(i).getDrop(), 94, JEIAdvSeedCategory.Y);
    }

    @Override
    public float getChance()
    {
        PlantDrop drop = entry.getDrops().get(i);
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
        PlantDrop drop = entry.getDrops().get(i);
        return new int[] {drop.getMinDrop(), drop.getMaxDrop()};
    }
}
