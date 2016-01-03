package jeresources.utils;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import jeresources.JEResources;
import jeresources.config.Settings;

public class NetworkEventHelper
{
    @SubscribeEvent
    public void onConnectedToServer(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        if (!Settings.initedCompat)
            JEResources.PROXY.initCompatibility();
    }
}
