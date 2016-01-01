package jeresources;

import jeresources.config.ConfigHandler;
import jeresources.config.Settings;
import jeresources.network.MessageHandler;
import jeresources.proxy.CommonProxy;
import jeresources.reference.MetaData;
import jeresources.reference.Reference;
import jeresources.registry.EnchantmentRegistry;
import jeresources.registry.MessageRegistry;
import jeresources.utils.LogHelper;
import jeresources.utils.ReflectionHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Reference.ID, name = Reference.NAME, version = Reference.VERSION_FULL, guiFactory = "jeresources.gui.ModGuiFactory", dependencies = "after:JEI;")
public class JEResources
{

    @Mod.Instance(value = Reference.ID)
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
        FMLCommonHandler.instance().bus().register(new ConfigHandler());

        LogHelper.info("Updating ModMetaData...");
        metadata = MetaData.init(metadata);

        ReflectionHelper.isObf = ReflectionHelper.doesFieldExist(WeightedRandom.Item.class, "field_76292_a");
        LogHelper.debug("Minecraft is " + (ReflectionHelper.isObf ? "obf" : "deObf"));

        LogHelper.info("Registering Network Messages...");
        MessageHandler.init();
        
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

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event)
    {
        PROXY.initCompatibility();
        EnchantmentRegistry.getInstance().removeAll(Settings.excludedEnchants);
    }
}
