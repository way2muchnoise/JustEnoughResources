package jeresources.fabric;

import jeresources.fabric.config.ConfigFileHandler;
import jeresources.proxy.CommonProxy;
import jeresources.registry.MobRegistry;
import jeresources.registry.VillagerRegistry;
import jeresources.util.LogHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLevelEvents;

public class JEResources implements ClientModInitializer {
    public static CommonProxy PROXY = new CommonProxy();
    @Override
    public void onInitializeClient() {
        LogHelper.info("Loaded");
        ConfigFileHandler.readFromConfig();

        ClientLevelEvents.AFTER_CLIENT_LEVEL_CHANGE.register((mc, clientLevel) -> {
            MobRegistry.getInstance().clearEntities();
            VillagerRegistry.getInstance().clearEntities();
        } );
    }
}
