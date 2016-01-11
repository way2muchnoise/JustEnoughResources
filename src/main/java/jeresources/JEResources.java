package jeresources;

import jeresources.config.ConfigHandler;
import jeresources.config.Settings;
import jeresources.proxy.CommonProxy;
import jeresources.reference.MetaData;
import jeresources.reference.Reference;
import jeresources.registry.MessageRegistry;
import jeresources.utils.LogHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.ID, name = Reference.NAME, version = Reference.VERSION_FULL, guiFactory = "jeresources.gui.ModGuiFactory", dependencies = "after:JEI;", clientSideOnly = true)
public class JEResources
{
    @Mod.Instance(Reference.ID)
    public static JEResources INSTANCE;

    @Mod.Metadata(Reference.ID)
    public static ModMetadata metadata;

    @SidedProxy(clientSide = "jeresources.proxy.ClientProxy", serverSide = "jeresources.proxy.CommonProxy")
    public static CommonProxy PROXY;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LogHelper.info("Loading configs..");
        Settings.side = event.getSide();
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(new ConfigHandler());

        LogHelper.info("Updating ModMetaData...");
        metadata = MetaData.init(metadata);
        
        LogHelper.info("Registering Events...");
        PROXY.registerEvents();
    }

    @Mod.EventHandler
    public void imcCallback(FMLInterModComms.IMCEvent event)
    {
        for (final FMLInterModComms.IMCMessage imcMessage : event.getMessages())
        {
            LogHelper.debug("Message Received - Sender: " + imcMessage.getSender() + " - Message Type: " + imcMessage.key);
            if (imcMessage.isNBTMessage())
                MessageRegistry.registerMessage(imcMessage.key, imcMessage.getNBTValue());
        }
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event)
    {
        Settings.gameLoaded = true;
    }
}
