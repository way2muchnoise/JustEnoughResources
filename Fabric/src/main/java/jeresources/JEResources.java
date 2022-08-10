package jeresources;

import jeresources.fabric.config.ConfigFileHandler;
import jeresources.proxy.CommonProxy;
import jeresources.util.LogHelper;
import net.fabricmc.api.ClientModInitializer;

public class JEResources implements ClientModInitializer {
    public static CommonProxy PROXY = new CommonProxy();
    @Override
    public void onInitializeClient() {
        LogHelper.info("Loaded");
        ConfigFileHandler.readFromConfig();
    }
}
