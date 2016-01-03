package jeresources.jei;

import jeresources.jei.plant.JEIPlantCategory;
import jeresources.jei.plant.PlantHandler;
import jeresources.reference.Reference;
import jeresources.registry.PlantRegistry;
import mezz.jei.api.*;

@JEIPlugin
public class JEIConfig implements IModPlugin
{
    public static final String MOB = Reference.ID + ".mob";
    public static final String DUNGEON = Reference.ID + ".dungeon";
    public static final String ORE = Reference.ID + ".ore";
    public static final String PLANT = Reference.ID + ".plant";
    public static final String SEED = Reference.ID + ".seed";

    @Override
    public boolean isModLoaded()
    {
        return true;
    }

    @Override
    public void onJeiHelpersAvailable(IJeiHelpers jeiHelpers)
    {

    }

    @Override
    public void onItemRegistryAvailable(IItemRegistry itemRegistry)
    {

    }

    @Override
    public void register(IModRegistry registry)
    {
        registry.addRecipeHandlers(new PlantHandler());
        registry.addRecipeCategories(new JEIPlantCategory());
        registry.addRecipes(PlantRegistry.getInstance().getAllPlants());
    }

    @Override
    public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry)
    {

    }
}
