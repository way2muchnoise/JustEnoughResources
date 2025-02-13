package jeresources.neoforge;

import jeresources.compatibility.api.JERAPI;
import jeresources.neoforge.config.Config;
import jeresources.profiling.ProfileCommand;
import jeresources.proxy.ClientProxy;
import jeresources.proxy.CommonProxy;
import jeresources.reference.Reference;
import jeresources.registry.MobRegistry;
import jeresources.registry.VillagerRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.level.LevelEvent;


@Mod(Reference.ID)
public class JEResources {
    public static CommonProxy PROXY;

    public JEResources(ModContainer container, Dist dist) {
        PROXY = dist.isClient() ? new ClientProxy() : new CommonProxy();

        container.getEventBus().addListener(this::commonSetup);
        container.registerConfig(ModConfig.Type.COMMON, Config.COMMON);
        container.getEventBus().register(Config.instance);

        NeoForge.EVENT_BUS.addListener(this::onCommandsRegister);
        NeoForge.EVENT_BUS.addListener(this::onLevelUnload);
      }

    private void commonSetup(FMLCommonSetupEvent event) {
        JERAPI.init();
    }

    private void onCommandsRegister(RegisterCommandsEvent event) {
        ProfileCommand.register(event.getDispatcher());
    }

    private void onLevelUnload(LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()){
            MobRegistry.getInstance().clearEntities();
            VillagerRegistry.getInstance().clearEntities();
        }
    }
}
