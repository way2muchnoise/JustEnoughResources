package jeresources.neoforge;

import jeresources.compatibility.api.JERAPI;
import jeresources.neoforge.config.Config;
import jeresources.platform.Services;
import jeresources.profiling.ProfileCommand;
import jeresources.proxy.ClientProxy;
import jeresources.proxy.CommonProxy;
import jeresources.reference.Reference;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.DistExecutor;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;


@Mod(Reference.ID)
public class JEResources {
    public static CommonProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public JEResources(IEventBus modEventBus, Dist dist) {
        //Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(()-> IExtensionPoint.DisplayTest.IGNORESERVERONLY, (remote, isServer)-> true));

        modEventBus.addListener(this::commonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON);

        // TODO create config folder
        Config.instance.loadConfig(Config.COMMON, Services.PLATFORM.getConfigDir().resolve(Reference.ID + ".toml"));
        modEventBus.register(Config.instance);
        NeoForge.EVENT_BUS.addListener(this::onCommandsRegister);
      }

    private void commonSetup(FMLCommonSetupEvent event) {
        JERAPI.init();
    }

    private void onCommandsRegister(RegisterCommandsEvent event) {
        ProfileCommand.register(event.getDispatcher());
    }
}
