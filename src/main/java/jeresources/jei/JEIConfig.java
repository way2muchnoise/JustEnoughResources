package jeresources.jei;

import jeresources.JEResources;
import jeresources.entry.*;
import jeresources.jei.dungeon.DungeonCategory;
import jeresources.jei.dungeon.DungeonWrapper;
import jeresources.jei.enchantment.EnchantmentCategory;
import jeresources.jei.enchantment.EnchantmentMaker;
import jeresources.jei.enchantment.EnchantmentWrapper;
import jeresources.jei.mob.MobCategory;
import jeresources.jei.mob.MobWrapper;
import jeresources.jei.plant.PlantCategory;
import jeresources.jei.plant.PlantWrapper;
import jeresources.jei.villager.VillagerCategory;
import jeresources.jei.villager.VillagerWrapper;
import jeresources.jei.worldgen.WorldGenCategory;
import jeresources.jei.worldgen.WorldGenWrapper;
import jeresources.reference.Reference;
import jeresources.registry.*;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

import javax.annotation.Nonnull;

@JEIPlugin
public class JEIConfig implements IModPlugin {
    public static final String MOB = Reference.ID + ".mob";
    public static final String DUNGEON = Reference.ID + ".dungeon";
    public static final String WORLD_GEN = Reference.ID + ".worldgen";
    public static final String PLANT = Reference.ID + ".plant";
    public static final String ENCHANTMENT = Reference.ID + ".enchantment";
    public static final String VILLAGER = Reference.ID + ".villager";

    private static IJeiHelpers jeiHelpers;
    private static IJeiRuntime jeiRuntime;

    @Override
    public void register(@Nonnull IModRegistry registry) {
        // Add recipe handlers
        registry.handleRecipes(WorldGenEntry.class, WorldGenWrapper::new, WORLD_GEN);
        registry.handleRecipes(PlantEntry.class, PlantWrapper::new, PLANT);
        registry.handleRecipes(MobEntry.class, MobWrapper::new, MOB);
        registry.handleRecipes(DungeonEntry.class, DungeonWrapper::new, DUNGEON);
        registry.handleRecipes(VillagerEntry.class, VillagerWrapper::new, VILLAGER);
        registry.handleRecipes(EnchantmentWrapper.class, recipe -> recipe, ENCHANTMENT);
        // Init internals
        JEResources.PROXY.initCompatibility();
        // Add recipes
        registry.addRecipes(WorldGenRegistry.getInstance().getWorldGen(), WORLD_GEN);
        registry.addRecipes(PlantRegistry.getInstance().getAllPlants(), PLANT);
        registry.addRecipes(MobRegistry.getInstance().getMobs(), MOB);
        registry.addRecipes(DungeonRegistry.getInstance().getDungeons(), DUNGEON);
        registry.addRecipes(VillagerRegistry.getInstance().getVillagers(), VILLAGER);
        registry.addRecipes(EnchantmentMaker.createRecipes(registry.getIngredientRegistry().getAllIngredients(VanillaTypes.ITEM)), ENCHANTMENT);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        JEIConfig.jeiRuntime = jeiRuntime;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        JEIConfig.jeiHelpers = registry.getJeiHelpers();
        registry.addRecipeCategories(new PlantCategory(), new WorldGenCategory(), new MobCategory(), new EnchantmentCategory(), new DungeonCategory(), new VillagerCategory());
    }

    public static IJeiHelpers getJeiHelpers() {
        return JEIConfig.jeiHelpers;
    }

    public static IJeiRuntime getJeiRuntime() {
        return JEIConfig.jeiRuntime;
    }
}
