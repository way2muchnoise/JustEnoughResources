package jeresources.utils;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import jeresources.JEResources;
import jeresources.config.Settings;
import jeresources.network.MessageHandler;
import jeresources.network.message.ClientSyncRequestMessage;

;


public class NetworkEventHelper
{
    @SubscribeEvent
    public void onConnectedToServer(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        if (!Settings.initedCompat)
            JEResources.PROXY.initCompatibility();
        if (!event.isLocal)
            MessageHandler.INSTANCE.sendToServer(new ClientSyncRequestMessage());

    }
}
