package jeresources.jei;

import jeresources.JEResources;
import jeresources.entries.*;
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
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@JEIPlugin
public class JEIConfig extends BlankModPlugin
{
    public static final String MOB = Reference.ID + ".mob";
    public static final String DUNGEON = Reference.ID + ".dungeon";
    public static final String WORLD_GEN = Reference.ID + ".worldgen";
    public static final String PLANT = Reference.ID + ".plant";
    public static final String ENCHANTMENT = Reference.ID + ".enchantment";
    public static final String VILLAGER = Reference.ID + ".villager";

    @Override
    public void register(@Nonnull IModRegistry registry)
    {
        registry.addRecipeHandlers(new PlantHandler(), new WorldGenHandler(), new MobHandler(), new EnchantmentHandler(), new DungeonHandler(), new VillagerHandler());
        registry.addRecipeCategories(new PlantCategory(), new WorldGenCategory(), new MobCategory(), new EnchantmentCategory(), new DungeonCategory(), new VillagerCategory());
        JEResources.PROXY.initCompatibility();

        List<Object> recipes = new ArrayList<>();
        for (WorldGenEntry entry : WorldGenRegistry.getInstance().getWorldGen())
            recipes.add(entry);
        for (PlantEntry entry : PlantRegistry.getInstance().getAllPlants())
            recipes.add(entry);
        for (MobEntry entry : MobRegistry.getInstance().getMobs())
            recipes.add(entry);
        for (DungeonEntry entry : DungeonRegistry.getInstance().getDungeons())
            recipes.add(entry);
        for (VillagerEntry entry : VillagerRegistry.getInstance().getVillagers())
            recipes.add(entry);

        registry.addRecipes(recipes);
        registry.addRecipes(EnchantmentMaker.createRecipes(registry.getItemRegistry()));
    }
}
