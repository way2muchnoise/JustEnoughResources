package jeresources;

import jeresources.config.Config;
import jeresources.proxy.ClientProxy;
import jeresources.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(JEResources.ID)
public class JEResources {
    public static final String ID = "jeresources";
    public static final Logger LOGGER = LogManager.getLogger();
    public static CommonProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public JEResources() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON);

        // TODO create config folder
        Config.instance.loadConfig(Config.COMMON, FMLPaths.CONFIGDIR.get().resolve(ID + ".toml"));
        MinecraftForge.EVENT_BUS.register(Config.COMMON);
    }
}
