package jeresources.proxy;

import jeresources.compatibility.Compatibility;
import jeresources.entries.OreMatchEntry;
import jeresources.entries.PlantEntry;
import jeresources.jei.JEIConfig;
import jeresources.registry.OreRegistry;
import jeresources.registry.PlantRegistry;
import jeresources.utils.ModList;
import jeresources.utils.WorldEventHelper;
import mezz.jei.api.IRecipeRegistry;
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
            if (JEIConfig.isLoaded())
            {
                IRecipeRegistry recipeRegistry = JEIConfig.getRecipeRegistry();
                for (OreMatchEntry entry : OreRegistry.getOres())
                    recipeRegistry.addRecipe(entry);
                for (PlantEntry entry : PlantRegistry.getInstance().getAllPlants())
                    recipeRegistry.addRecipe(entry);
            }
        }
    }

    public void registerEvents()
    {
        MinecraftForge.EVENT_BUS.register(new WorldEventHelper());
    }
}
