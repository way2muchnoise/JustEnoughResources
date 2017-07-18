package jeresources.jei;

import jeresources.JEResources;
import jeresources.entry.*;
import jeresources.jei.dungeon.DungeonCategory;
import jeresources.jei.dungeon.DungeonWrapperFactory;
import jeresources.jei.enchantment.EnchantmentCategory;
import jeresources.jei.enchantment.EnchantmentMaker;
import jeresources.jei.enchantment.EnchantmentWrapper;
import jeresources.jei.enchantment.EnchantmentWrapperFactory;
import jeresources.jei.mob.MobCategory;
import jeresources.jei.mob.MobWrapperFactory;
import jeresources.jei.plant.PlantCategory;
import jeresources.jei.plant.PlantWrapperFactory;
import jeresources.jei.villager.VillagerCategory;
import jeresources.jei.villager.VillagerWrapperFactory;
import jeresources.jei.worldgen.WorldGenCategory;
import jeresources.jei.worldgen.WorldGenWrapperFactory;
import jeresources.reference.Reference;
import jeresources.registry.*;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

@JEIPlugin
public class JEIConfig extends BlankModPlugin {
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
        registry.handleRecipes(WorldGenEntry.class, new WorldGenWrapperFactory(), WORLD_GEN);
        registry.handleRecipes(PlantEntry.class, new PlantWrapperFactory(), PLANT);
        registry.handleRecipes(MobEntry.class, new MobWrapperFactory(), MOB);
        registry.handleRecipes(DungeonEntry.class, new DungeonWrapperFactory(), DUNGEON);
        registry.handleRecipes(VillagerEntry.class, new VillagerWrapperFactory(), VILLAGER);
        registry.handleRecipes(EnchantmentWrapper.class, new EnchantmentWrapperFactory(), ENCHANTMENT);
        // Init internals
        JEResources.PROXY.initCompatibility();
        // Add recipes
        registry.addRecipes(WorldGenRegistry.getInstance().getWorldGen(), WORLD_GEN);
        registry.addRecipes(PlantRegistry.getInstance().getAllPlants(), PLANT);
        registry.addRecipes(MobRegistry.getInstance().getMobs(), MOB);
        registry.addRecipes(DungeonRegistry.getInstance().getDungeons(), DUNGEON);
        registry.addRecipes(VillagerRegistry.getInstance().getVillagers(), VILLAGER);
        registry.addRecipes(EnchantmentMaker.createRecipes(registry.getIngredientRegistry().getIngredients(ItemStack.class)), ENCHANTMENT);
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

    public static void purgeCategories(String... categories) {
        if (jeiRuntime != null) {
            IRecipeRegistry recipeRegistry = jeiRuntime.getRecipeRegistry();
            List<IRecipeCategory> recipeCategories = recipeRegistry.getRecipeCategories(Arrays.asList(categories));
            for (IRecipeCategory<?> recipeCategory : recipeCategories) {
                List<? extends IRecipeWrapper> recipeWrappers = recipeRegistry.getRecipeWrappers(recipeCategory);
                for (IRecipeWrapper wrapper : recipeWrappers) {
                    recipeRegistry.removeRecipe(wrapper, recipeCategory.getUid());
                }
            }
        }
    }
}
