package jeresources;

import jeresources.compatibility.JERAPI;
import jeresources.config.Config;
import jeresources.profiling.ProfileCommand;
import jeresources.proxy.ClientProxy;
import jeresources.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(JEResources.ID)
public class JEResources {
    public static final String ID = "jeresources";
    public static final Logger LOGGER = LogManager.getLogger();
    public static CommonProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public JEResources() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON);

        // TODO create config folder
        Config.instance.loadConfig(Config.COMMON, FMLPaths.CONFIGDIR.get().resolve(ID + ".toml"));
        MinecraftForge.EVENT_BUS.register(Config.COMMON);
        MinecraftForge.EVENT_BUS.register(new ProfileCommand());
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        JERAPI.init();
    }
}
