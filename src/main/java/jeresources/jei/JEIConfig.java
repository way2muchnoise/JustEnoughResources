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

import javax.annotation.Nonnull;

@JEIPlugin
public class JEIConfig extends BlankModPlugin
{
    public static final String MOB = Reference.ID + ".mob";
    public static final String DUNGEON = Reference.ID + ".dungeon";
    public static final String WORLD_GEN = Reference.ID + ".worldgen";
    public static final String PLANT = Reference.ID + ".plant";
    public static final String ENCHANTMENT = Reference.ID + ".enchantment";

    @Override
    public void register(@Nonnull IModRegistry registry)
    {
        registry.addRecipeHandlers(new PlantHandler(), new WorldGenHandler(), new MobHandler(), new EnchantmentHandler(), new DungeonHandler());
        registry.addRecipeCategories(new PlantCategory(), new WorldGenCategory(), new MobCategory(), new EnchantmentCategory(), new DungeonCategory());
        JEIConfig.itemRegistry = registry.getItemRegistry();
    }

    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime)
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
