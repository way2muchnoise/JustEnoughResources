package jeresources.forge;

import jeresources.compatibility.api.JERAPI;
import jeresources.forge.config.Config;
import jeresources.platform.Services;
import jeresources.profiling.ProfileCommand;
import jeresources.proxy.ClientProxy;
import jeresources.proxy.CommonProxy;
import jeresources.reference.Reference;
import jeresources.registry.MobRegistry;
import jeresources.registry.VillagerRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reference.ID)
public class JEResources {
    public static CommonProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public JEResources() {
        //Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(()-> IExtensionPoint.DisplayTest.IGNORESERVERONLY, (remote, isServer)-> true));

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON);

        // TODO create config folder
        Config.instance.loadConfig(Config.COMMON, Services.PLATFORM.getConfigDir().resolve(Reference.ID + ".toml"));
        MinecraftForge.EVENT_BUS.register(Config.COMMON);
        MinecraftForge.EVENT_BUS.register(new ProfileCommand());
        MinecraftForge.EVENT_BUS.addListener(this::onLevelUnload);
      }

    private void commonSetup(FMLCommonSetupEvent event) {
        JERAPI.init();
    }

    private void onLevelUnload(LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()){
            MobRegistry.getInstance().clearEntities();
            VillagerRegistry.getInstance().clearEntities();
        }
    }
}
