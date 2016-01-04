package jeresources.jei;

import jeresources.jei.ore.JEIOreCategory;
import jeresources.jei.ore.OreHandler;
import jeresources.jei.plant.JEIPlantCategory;
import jeresources.jei.plant.PlantHandler;
import jeresources.reference.Reference;
import jeresources.registry.OreRegistry;
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
        registry.addRecipeHandlers(new PlantHandler(), new OreHandler());
        registry.addRecipeCategories(new JEIPlantCategory(), new JEIOreCategory());
    }

    @Override
    public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry)
    {
        JEIConfig.recipeRegistry = recipeRegistry;
    }

    private static IRecipeRegistry recipeRegistry;

    public static boolean isLoaded()
    {
        return recipeRegistry != null;
    }

    public static IRecipeRegistry getRecipeRegistry()
    {
        return recipeRegistry;
    }
}
