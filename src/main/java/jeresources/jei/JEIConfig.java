package jeresources.jei;

import jeresources.JEResources;
import jeresources.jei.dungeon.DungeonCategory;
import jeresources.jei.dungeon.DungeonHandler;
import jeresources.jei.enchantment.EnchantmentCategory;
import jeresources.jei.enchantment.EnchantmentHandler;
import jeresources.jei.mob.MobCategory;
import jeresources.jei.mob.MobHandler;
import jeresources.jei.plant.PlantCategory;
import jeresources.jei.plant.PlantHandler;
import jeresources.jei.worldgen.WorldGenCategory;
import jeresources.jei.worldgen.WorldGenHandler;
import jeresources.reference.Reference;
import mezz.jei.api.*;

@JEIPlugin
public class JEIConfig implements IModPlugin
{
    public static final String MOB = Reference.ID + ".mob";
    public static final String DUNGEON = Reference.ID + ".dungeon";
    public static final String WORLD_GEN = Reference.ID + ".worldgen";
    public static final String PLANT = Reference.ID + ".plant";
    public static final String ENCHANTMENT = Reference.ID + ".enchantment";

    @Deprecated
    @Override
    public void onJeiHelpersAvailable(IJeiHelpers jeiHelpers)
    {

    }

    @Deprecated
    @Override
    public void onItemRegistryAvailable(IItemRegistry itemRegistry)
    {

    }

    @Override
    public void register(IModRegistry registry)
    {
        registry.addRecipeHandlers(new PlantHandler(), new WorldGenHandler(), new MobHandler(), new EnchantmentHandler(), new DungeonHandler());
        registry.addRecipeCategories(new PlantCategory(), new WorldGenCategory(), new MobCategory(), new EnchantmentCategory(), new DungeonCategory());
        JEIConfig.itemRegistry = registry.getItemRegistry();
    }

    @Deprecated
    @Override
    public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry)
    {

    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
    {
        JEIConfig.recipeRegistry = jeiRuntime.getRecipeRegistry();
        JEResources.PROXY.initCompatibility();
    }

    private static IRecipeRegistry recipeRegistry;
    private static IItemRegistry itemRegistry;

    public static IRecipeRegistry getRecipeRegistry()
    {
        return recipeRegistry;
    }

    public static IItemRegistry getItemRegistry()
    {
        return itemRegistry;
    }
}
