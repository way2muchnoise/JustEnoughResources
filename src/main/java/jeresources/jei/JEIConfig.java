package jeresources.jei;

import jeresources.JEResources;
import jeresources.config.Settings;
import jeresources.jei.dungeon.DungeonCategory;
import jeresources.jei.dungeon.DungeonHandler;
import jeresources.jei.enchantment.EnchantmentCategory;
import jeresources.jei.enchantment.EnchantmentHandler;
import jeresources.jei.mob.MobCategory;
import jeresources.jei.mob.MobHandler;
import jeresources.jei.ore.OreCategory;
import jeresources.jei.ore.OreHandler;
import jeresources.jei.plant.PlantCategory;
import jeresources.jei.plant.PlantHandler;
import jeresources.reference.Reference;
import jeresources.registry.EnchantmentRegistry;
import mezz.jei.api.*;

@JEIPlugin
public class JEIConfig implements IModPlugin
{
    public static final String MOB = Reference.ID + ".mob";
    public static final String DUNGEON = Reference.ID + ".dungeon";
    public static final String ORE = Reference.ID + ".ore";
    public static final String PLANT = Reference.ID + ".plant";
    public static final String ENCHANTMENT = Reference.ID + ".enchantment";

    @Override
    public void onJeiHelpersAvailable(IJeiHelpers jeiHelpers)
    {

    }

    @Override
    public void onItemRegistryAvailable(IItemRegistry itemRegistry)
    {
        JEIConfig.itemRegistry = itemRegistry;
    }

    @Override
    public void register(IModRegistry registry)
    {
        registry.addRecipeHandlers(new PlantHandler(), new OreHandler(), new MobHandler(), new EnchantmentHandler(), new DungeonHandler());
        registry.addRecipeCategories(new PlantCategory(), new OreCategory(), new MobCategory(), new EnchantmentCategory(), new DungeonCategory());
    }

    @Override
    public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry)
    {
        JEIConfig.recipeRegistry = recipeRegistry;
        if (!Settings.initedCompat)
            JEResources.PROXY.initCompatibility();
        EnchantmentRegistry.getInstance().removeAll(Settings.excludedEnchants);
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
