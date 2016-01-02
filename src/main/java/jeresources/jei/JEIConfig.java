package jeresources.jei;

import jeresources.jei.category.*;
import jeresources.reference.Reference;
import mezz.jei.api.*;

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
        return false;
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
        JEIEnchantmentCategory JEIEnchantmentCategory = new JEIEnchantmentCategory();
        JEIMobCategory JEIMobCategory = new JEIMobCategory();
        JEIOreCategory JEIOreCategory = new JEIOreCategory();
        JEIDungeonCategory JEIDungeonCategory = new JEIDungeonCategory();
        JEIPlantCategory JEIPlantCategory = new JEIPlantCategory();
        JEIAdvSeedCategory JEIAdvSeedCategory = new JEIAdvSeedCategory();
        registry.addRecipeCategories(JEIEnchantmentCategory, JEIMobCategory, JEIOreCategory, JEIDungeonCategory, JEIPlantCategory, JEIAdvSeedCategory);
    }

    @Override
    public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry)
    {

    }
}
