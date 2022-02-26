package jeresources.jei;

import jeresources.JEResources;
import jeresources.config.Settings;
import jeresources.jei.dungeon.DungeonCategory;
import jeresources.jei.dungeon.DungeonWrapper;
import jeresources.jei.enchantment.EnchantmentCategory;
import jeresources.jei.enchantment.EnchantmentMaker;
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
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;


@JeiPlugin
public class JEIConfig implements IModPlugin {
    public static final ResourceLocation MOB = new ResourceLocation(Reference.ID, "mob");
    public static final ResourceLocation DUNGEON = new ResourceLocation(Reference.ID , "dungeon");
    public static final ResourceLocation WORLD_GEN = new ResourceLocation(Reference.ID , "worldgen");
    public static final ResourceLocation PLANT = new ResourceLocation(Reference.ID , "plant");
    public static final ResourceLocation ENCHANTMENT = new ResourceLocation(Reference.ID , "enchantment");
    public static final ResourceLocation VILLAGER = new ResourceLocation(Reference.ID , "villager");
    public static final ResourceLocation[] CATEGORIES = {MOB, DUNGEON, WORLD_GEN, PLANT, ENCHANTMENT, VILLAGER};

    private static IJeiHelpers jeiHelpers;
    private static IJeiRuntime jeiRuntime;

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(JEResources.ID);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(asRecipes(WorldGenRegistry.getInstance().getWorldGen(), WorldGenWrapper::new), WORLD_GEN);
        registration.addRecipes(asRecipes(PlantRegistry.getInstance().getAllPlants(), PlantWrapper::new), PLANT);
        registration.addRecipes(asRecipes(MobRegistry.getInstance().getMobs(), MobWrapper::new), MOB);
        registration.addRecipes(asRecipes(DungeonRegistry.getInstance().getDungeons(), DungeonWrapper::new), DUNGEON);
        registration.addRecipes(asRecipes(VillagerRegistry.getInstance().getVillagers(), VillagerWrapper::new), VILLAGER);
        registration.addRecipes(EnchantmentMaker.createRecipes(registration.getIngredientManager().getAllIngredients(VanillaTypes.ITEM)), ENCHANTMENT);
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        JEIConfig.jeiRuntime = jeiRuntime;
        hideCategories(Settings.hiddenCategories);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        JEIConfig.jeiHelpers = registration.getJeiHelpers();
        registration.addRecipeCategories(new PlantCategory(), new WorldGenCategory(), new MobCategory(), new EnchantmentCategory(), new DungeonCategory(), new VillagerCategory());
        JEResources.PROXY.initCompatibility();
    }

    public static void resetCategories() {
        if (jeiRuntime != null) {
            for (ResourceLocation category : CATEGORIES)
                jeiRuntime.getRecipeManager().unhideRecipeCategory(category);
        }
    }

    public static void hideCategories(String[] categories) {
        if (jeiRuntime != null) {
            for (String category : categories)
                jeiRuntime.getRecipeManager().hideRecipeCategory(new ResourceLocation(Reference.ID, category));
        }
    }

    public static IJeiHelpers getJeiHelpers() {
        return JEIConfig.jeiHelpers;
    }

    private static <T, R> Collection<R> asRecipes(Collection<T> collection, Function<T, R> transformer) {
        return collection.stream().map(transformer).collect(Collectors.toList());
    }
}
