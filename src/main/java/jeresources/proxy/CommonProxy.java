package jeresources.proxy;

import jeresources.compatibility.Compatibility;
import jeresources.config.Settings;
import jeresources.registry.*;
import jeresources.handler.WorldEventHandler;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
    public void initCompatibility()
    {
        DungeonRegistry.getInstance().clear();
        EnchantmentRegistry.getInstance().clear();
        MobRegistry.getInstance().clear();
        PlantRegistry.getInstance().clear();
        VillagerRegistry.getInstance().clear();
        WorldGenRegistry.getInstance().clear();
        Compatibility.init();
        EnchantmentRegistry.getInstance().removeAll(Settings.excludedEnchants);
    }

    public void registerEvents()
    {
        MinecraftForge.EVENT_BUS.register(new WorldEventHandler());
    }
}
