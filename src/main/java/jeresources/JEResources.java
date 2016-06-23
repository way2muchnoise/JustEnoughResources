package jeresources;

import jeresources.compatibility.JERAPI;
import jeresources.config.ConfigHandler;
import jeresources.config.Settings;
import jeresources.profiling.ProfileCommand;
import jeresources.proxy.CommonProxy;
import jeresources.reference.MetaData;
import jeresources.reference.Reference;
import jeresources.util.LogHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Reference.ID, name = Reference.NAME, version = Reference.VERSION, guiFactory = "jeresources.gui.ModGuiFactory", dependencies = "required-after:JEI@[3.7.0,);", clientSideOnly = true)
public class JEResources
{
    @Mod.Metadata(Reference.ID)
    public static ModMetadata metadata;

    @SidedProxy(clientSide = "jeresources.proxy.ClientProxy", serverSide = "jeresources.proxy.CommonProxy")
    public static CommonProxy PROXY;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LogHelper.info("Loading configs..");
        Settings.side = event.getSide();
        ConfigHandler.init(event.getModConfigurationDirectory());
        MinecraftForge.EVENT_BUS.register(new ConfigHandler());

        LogHelper.info("Updating ModMetaData...");
        metadata = MetaData.init(metadata);

        LogHelper.info("Registering Events...");
        PROXY.registerEvents();

        LogHelper.info("Providing API...");
        JERAPI.init(event.getAsmData());
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event)
    {
        Settings.gameLoaded = true;
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new ProfileCommand());
    }
}
