package jeresources.proxy;

import jeresources.compatibility.Compatibility;
import jeresources.config.Settings;
import jeresources.registry.*;
import jeresources.utils.WorldEventHelper;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
    public void initCompatibility()
    {
        if (!Settings.initedCompat)
            Compatibility.init();
        EnchantmentRegistry.getInstance().removeAll(Settings.excludedEnchants);
    }

    public void registerEvents()
    {
        MinecraftForge.EVENT_BUS.register(new WorldEventHelper());
    }
}
