package jeresources.proxy;

import jeresources.compatibility.Compatibility;
import jeresources.entries.DungeonEntry;
import jeresources.entries.MobEntry;
import jeresources.entries.OreMatchEntry;
import jeresources.entries.PlantEntry;
import jeresources.jei.JEIConfig;
import jeresources.registry.DungeonRegistry;
import jeresources.registry.MobRegistry;
import jeresources.registry.OreRegistry;
import jeresources.registry.PlantRegistry;
import jeresources.utils.ModList;
import jeresources.utils.WorldEventHelper;
import mezz.jei.api.IRecipeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;

public class CommonProxy
{
    public World getClientWorld()
    {
        return null;
    }

    public void initCompatibility()
    {
        Compatibility.init();
        if (Loader.isModLoaded(ModList.Names.JEI))
        {
            IRecipeRegistry recipeRegistry = JEIConfig.getRecipeRegistry();
            for (OreMatchEntry entry : OreRegistry.getOres())
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
