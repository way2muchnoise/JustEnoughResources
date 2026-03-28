package jeresources.jei;

import jeresources.config.Settings;
import jeresources.entry.*;
import jeresources.jei.dungeon.DungeonCategory;
import jeresources.jei.enchantment.EnchantmentCategory;
import jeresources.jei.enchantment.EnchantmentMaker;
import jeresources.jei.enchantment.EnchantmentWrapper;
import jeresources.jei.mob.MobCategory;
import jeresources.jei.plant.PlantCategory;
import jeresources.jei.villager.VillagerCategory;
import jeresources.jei.worldgen.WorldGenCategory;
import jeresources.platform.Services;
import jeresources.reference.Reference;
import jeresources.registry.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


@JeiPlugin
public class JEIConfig implements IModPlugin {
    public static final Identifier MOB = Identifier.fromNamespaceAndPath(Reference.ID, "mob");
    public static final IRecipeType<MobEntry> MOB_TYPE = IRecipeType.create(MOB, MobEntry.class);
    public static final Identifier DUNGEON = Identifier.fromNamespaceAndPath(Reference.ID , "dungeon");
    public static final IRecipeType<DungeonEntry> DUNGEON_TYPE = IRecipeType.create(DUNGEON, DungeonEntry.class);
    public static final Identifier WORLD_GEN = Identifier.fromNamespaceAndPath(Reference.ID , "worldgen");
    public static final IRecipeType<WorldGenEntry> WORLD_GEN_TYPE = IRecipeType.create(WORLD_GEN, WorldGenEntry.class);
    public static final Identifier PLANT = Identifier.fromNamespaceAndPath(Reference.ID , "plant");
    public static final IRecipeType<PlantEntry> PLANT_TYPE = IRecipeType.create(PLANT, PlantEntry.class);
    public static final Identifier ENCHANTMENT = Identifier.fromNamespaceAndPath(Reference.ID , "enchantment");
    public static final IRecipeType<EnchantmentWrapper> ENCHANTMENT_TYPE = IRecipeType.create(ENCHANTMENT, EnchantmentWrapper.class);
    public static final Identifier VILLAGER = Identifier.fromNamespaceAndPath(Reference.ID , "villager");
    public static final IRecipeType<AbstractVillagerEntry> VILLAGER_TYPE = IRecipeType.create(VILLAGER, AbstractVillagerEntry.class);
    public static final Map<Identifier, IRecipeType<?>> TYPES = new HashMap<>();
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
    public @NotNull Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(Reference.ID, "minecraft");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(DUNGEON_TYPE, DungeonRegistry.getInstance().getDungeons());
        registration.addRecipes(ENCHANTMENT_TYPE, EnchantmentMaker.createRecipes(registration.getIngredientManager().getAllIngredients(VanillaTypes.ITEM_STACK)));
        registration.addRecipes(MOB_TYPE, MobRegistry.getInstance().getMobs());
        registration.addRecipes(PLANT_TYPE, PlantRegistry.getInstance().getAllPlants());
        registration.addRecipes(VILLAGER_TYPE, VillagerRegistry.getInstance().getVillagers());
        registration.addRecipes(WORLD_GEN_TYPE, WorldGenRegistry.getInstance().getWorldGen());
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        JEIConfig.jeiRuntime = jeiRuntime;
        hideCategories(Settings.hiddenCategories);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        JEIConfig.jeiHelpers = registration.getJeiHelpers();
        registration.addRecipeCategories(
            new DungeonCategory(),
            new EnchantmentCategory(),
            new MobCategory(),
            new PlantCategory(),
            new VillagerCategory(),
            new WorldGenCategory()
        );
        Services.PLATFORM.getProxy().initCompatibility();
    }

    public static void resetCategories() {
        if (jeiRuntime != null) {
            for (IRecipeType<?> recipeType : TYPES.values()) {
                jeiRuntime.getRecipeManager().unhideRecipeCategory(recipeType);
            }
        }
    }

    public static void hideCategories(String[] categories) {
        if (jeiRuntime != null) {
            for (String category : categories) {
                jeiRuntime.getRecipeManager().hideRecipeCategory(TYPES.get(Identifier.fromNamespaceAndPath(Reference.ID, category)));
            }
        }
    }

    public static IJeiHelpers getJeiHelpers() {
        return JEIConfig.jeiHelpers;
    }
}
