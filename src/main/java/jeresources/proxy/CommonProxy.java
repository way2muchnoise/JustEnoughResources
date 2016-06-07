package jeresources.proxy;

import jeresources.compatibility.Compatibility;
import jeresources.config.Settings;
import jeresources.registry.EnchantmentRegistry;
import jeresources.handler.WorldEventHandler;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
    public void initCompatibility()
    {
        Compatibility.init();
        EnchantmentRegistry.getInstance().removeAll(Settings.excludedEnchants);
    }

    public void registerEvents()
    {
        MinecraftForge.EVENT_BUS.register(new WorldEventHandler());
    }
}
