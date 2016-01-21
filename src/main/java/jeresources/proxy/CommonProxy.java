package jeresources.proxy;

import jeresources.compatibility.Compatibility;
import jeresources.config.Settings;
import jeresources.entries.DungeonEntry;
import jeresources.entries.MobEntry;
import jeresources.entries.OreMatchEntry;
import jeresources.entries.PlantEntry;
import jeresources.jei.JEIConfig;
import jeresources.jei.drops.DropsWrapper;
import jeresources.registry.*;
import jeresources.utils.ModList;
import jeresources.utils.WorldEventHelper;
import mezz.jei.api.IRecipeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;

public class CommonProxy
{
    public void initCompatibility()
    {
        if (!Settings.initedCompat)
            Compatibility.init();
        EnchantmentRegistry.getInstance().removeAll(Settings.excludedEnchants);
        if (Loader.isModLoaded(ModList.Names.JEI))
        {
            IRecipeRegistry recipeRegistry = JEIConfig.getRecipeRegistry();
            for (OreMatchEntry entry : OreRegistry.getOres())
                recipeRegistry.addRecipe(entry);
            for (DropsWrapper entry : DropsRegistry.getDrops())
                recipeRegistry.addRecipe(entry);
            for (PlantEntry entry : PlantRegistry.getInstance().getAllPlants())
                recipeRegistry.addRecipe(entry);
            for (MobEntry entry : MobRegistry.getInstance().getMobs())
                recipeRegistry.addRecipe(entry);
            for (ItemStack entry : JEIConfig.getItemRegistry().getItemList())
                recipeRegistry.addRecipe(entry);
            for (DungeonEntry entry : DungeonRegistry.getInstance().getDungeons())
                recipeRegistry.addRecipe(entry);
        }
    }

    public void registerEvents()
    {
        MinecraftForge.EVENT_BUS.register(new WorldEventHelper());
    }
}
