package jeresources.jei;

import jeresources.JEResources;
import jeresources.jei.dungeon.DungeonCategory;
import jeresources.jei.dungeon.DungeonHandler;
import jeresources.jei.enchantment.EnchantmentCategory;
import jeresources.jei.enchantment.EnchantmentHandler;
import jeresources.jei.enchantment.EnchantmentMaker;
import jeresources.jei.mob.MobCategory;
import jeresources.jei.mob.MobHandler;
import jeresources.jei.plant.PlantCategory;
import jeresources.jei.plant.PlantHandler;
import jeresources.jei.villager.VillagerCategory;
import jeresources.jei.villager.VillagerHandler;
import jeresources.jei.worldgen.WorldGenCategory;
import jeresources.jei.worldgen.WorldGenHandler;
import jeresources.reference.Reference;
import jeresources.registry.*;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;

import javax.annotation.Nonnull;

@JEIPlugin
public class JEIConfig extends BlankModPlugin
{
    public static final String MOB = Reference.ID + ".mob";
    public static final String DUNGEON = Reference.ID + ".dungeon";
    public static final String WORLD_GEN = Reference.ID + ".worldgen";
    public static final String PLANT = Reference.ID + ".plant";
    public static final String ENCHANTMENT = Reference.ID + ".enchantment";
    public static final String VILLAGER = Reference.ID + ".villager";

    private static IJeiHelpers jeiHelpers;

    @Override
    public void register(@Nonnull IModRegistry registry)
    {
        JEIConfig.jeiHelpers = registry.getJeiHelpers();
        registry.addRecipeHandlers(new PlantHandler(), new WorldGenHandler(), new MobHandler(), new EnchantmentHandler(), new DungeonHandler(), new VillagerHandler());
        registry.addRecipeCategories(new PlantCategory(), new WorldGenCategory(), new MobCategory(), new EnchantmentCategory(), new DungeonCategory(), new VillagerCategory());
        JEResources.PROXY.initCompatibility();

        registry.addRecipes(WorldGenRegistry.getInstance().getWorldGen());
        registry.addRecipes(PlantRegistry.getInstance().getAllPlants());
        registry.addRecipes(MobRegistry.getInstance().getMobs());
        registry.addRecipes(DungeonRegistry.getInstance().getDungeons());
        registry.addRecipes(VillagerRegistry.getInstance().getVillagers());
        registry.addRecipes(EnchantmentMaker.createRecipes(registry.getItemRegistry()));
    }

    public static IJeiHelpers getJeiHelpers()
    {
        return jeiHelpers;
    }
}
