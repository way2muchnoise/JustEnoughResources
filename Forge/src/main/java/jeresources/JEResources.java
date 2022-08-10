package jeresources;

import jeresources.compatibility.api.JERAPI;
import jeresources.forge.config.Config;
import jeresources.platform.Services;
import jeresources.profiling.ProfileCommand;
import jeresources.proxy.ClientProxy;
import jeresources.proxy.CommonProxy;
import jeresources.reference.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reference.ID)
public class JEResources {
    public static CommonProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public JEResources() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON);

        // TODO create config folder
        Config.instance.loadConfig(Config.COMMON, Services.PLATFORM.getConfigDir().resolve(Reference.ID + ".toml"));
        MinecraftForge.EVENT_BUS.register(Config.COMMON);
        MinecraftForge.EVENT_BUS.register(new ProfileCommand());
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        JERAPI.init();
    }
}
