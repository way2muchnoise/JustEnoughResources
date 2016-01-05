package jeresources.utils;

import jeresources.JEResources;
import jeresources.config.Settings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class NetworkEventHelper
{
    @SubscribeEvent
    public void onConnectedToServer(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        if (!Settings.initedCompat)
            JEResources.PROXY.initCompatibility();
    }
}
