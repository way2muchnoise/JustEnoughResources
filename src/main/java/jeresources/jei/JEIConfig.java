package jeresources.jei;

import jeresources.JEResources;
import jeresources.config.Settings;
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
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@JeiPlugin
public class JEIConfig implements IModPlugin {
    public static final ResourceLocation MOB = new ResourceLocation(Reference.ID, "mob");
    public static final RecipeType<MobWrapper> MOB_TYPE = new RecipeType<>(MOB, MobWrapper.class);
    public static final ResourceLocation DUNGEON = new ResourceLocation(Reference.ID , "dungeon");
    public static final RecipeType<DungeonWrapper> DUNGEON_TYPE = new RecipeType<>(DUNGEON, DungeonWrapper.class);
    public static final ResourceLocation WORLD_GEN = new ResourceLocation(Reference.ID , "worldgen");
    public static final RecipeType<WorldGenWrapper> WORLD_GEN_TYPE = new RecipeType<>(WORLD_GEN, WorldGenWrapper.class);
    public static final ResourceLocation PLANT = new ResourceLocation(Reference.ID , "plant");
    public static final RecipeType<PlantWrapper> PLANT_TYPE = new RecipeType<>(PLANT, PlantWrapper.class);
    public static final ResourceLocation ENCHANTMENT = new ResourceLocation(Reference.ID , "enchantment");
    public static final RecipeType<EnchantmentWrapper> ENCHANTMENT_TYPE = new RecipeType<>(ENCHANTMENT, EnchantmentWrapper.class);
    public static final ResourceLocation VILLAGER = new ResourceLocation(Reference.ID , "villager");
    public static final RecipeType<VillagerWrapper> VILLAGER_TYPE = new RecipeType<>(VILLAGER, VillagerWrapper.class);
    public static final Map<ResourceLocation, RecipeType<?>> TYPES = new HashMap<>();
    static {
        TYPES.put(MOB, MOB_TYPE);
        TYPES.put(DUNGEON, DUNGEON_TYPE);
        TYPES.put(WORLD_GEN, WORLD_GEN_TYPE);
        TYPES.put(PLANT, PLANT_TYPE);
        TYPES.put(ENCHANTMENT, ENCHANTMENT_TYPE);
        TYPES.put(VILLAGER, VILLAGER_TYPE);
    }

    private static IJeiHelpers jeiHelpers;
    private static IJeiRuntime jeiRuntime;

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(JEResources.ID);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(WORLD_GEN_TYPE, asRecipes(WorldGenRegistry.getInstance().getWorldGen(), WorldGenWrapper::new));
        registration.addRecipes(PLANT_TYPE, asRecipes(PlantRegistry.getInstance().getAllPlants(), PlantWrapper::new));
        registration.addRecipes(MOB_TYPE, asRecipes(MobRegistry.getInstance().getMobs(), MobWrapper::new));
        registration.addRecipes(DUNGEON_TYPE, asRecipes(DungeonRegistry.getInstance().getDungeons(), DungeonWrapper::new));
        registration.addRecipes(VILLAGER_TYPE, asRecipes(VillagerRegistry.getInstance().getVillagers(), VillagerWrapper::new));
        registration.addRecipes(ENCHANTMENT_TYPE, EnchantmentMaker.createRecipes(registration.getIngredientManager().getAllIngredients(VanillaTypes.ITEM_STACK)));
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
            for (RecipeType<?> recipeType : TYPES.values()) {
                jeiRuntime.getRecipeManager().unhideRecipeCategory(recipeType);
            }
        }
    }

    public static void hideCategories(String[] categories) {
        if (jeiRuntime != null) {
            for (String category : categories) {
                jeiRuntime.getRecipeManager().hideRecipeCategory(TYPES.get(new ResourceLocation(Reference.ID, category)));
            }
        }
    }

    public static IJeiHelpers getJeiHelpers() {
        return JEIConfig.jeiHelpers;
    }

    private static <T, R> List<R> asRecipes(Collection<T> collection, Function<T, R> transformer) {
        return collection.stream().map(transformer).collect(Collectors.toList());
    }
}
