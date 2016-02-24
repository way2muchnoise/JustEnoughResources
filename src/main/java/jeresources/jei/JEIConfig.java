package jeresources.jei;

import jeresources.JEResources;
import jeresources.entries.DungeonEntry;
import jeresources.entries.MobEntry;
import jeresources.entries.PlantEntry;
import jeresources.entries.WorldGenEntry;
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
import jeresources.registry.DungeonRegistry;
import jeresources.registry.MobRegistry;
import jeresources.registry.PlantRegistry;
import jeresources.registry.WorldGenRegistry;
import mezz.jei.api.*;
import net.minecraft.item.ItemStack;

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

    @Override
    public void register(@Nonnull IModRegistry registry)
    {
        registry.addRecipeHandlers(new PlantHandler(), new WorldGenHandler(), new MobHandler(), new EnchantmentHandler(), new DungeonHandler());
        registry.addRecipeCategories(new PlantCategory(), new WorldGenCategory(), new MobCategory(), new EnchantmentCategory(), new DungeonCategory());
        JEResources.PROXY.initCompatibility();

        List<Object> recipes = new ArrayList<>();
        for (WorldGenEntry entry : WorldGenRegistry.getInstance().getWorldGen())
            recipes.add(entry);
        for (PlantEntry entry : PlantRegistry.getInstance().getAllPlants())
            recipes.add(entry);
        for (MobEntry entry : MobRegistry.getInstance().getMobs())
            recipes.add(entry);
        for (ItemStack entry : registry.getItemRegistry().getItemList())
            recipes.add(entry);
        for (DungeonEntry entry : DungeonRegistry.getInstance().getDungeons())
            recipes.add(entry);

        registry.addRecipes(recipes);
    }
}
