package jeresources.config;

import jeresources.reference.Reference;
import jeresources.utils.TranslationHelper;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler
{
    public static Configuration config;
    private static File configDir;

    public static void init(File configDir)
    {
        if (config == null)
        {
            configDir = new File(configDir, Reference.ID);
            configDir.mkdir();
            ConfigHandler.configDir = configDir;
            config = new Configuration(new File(configDir, Reference.ID + ".cfg"));
            loadConfig();
        }
    }

    public static File getConfigDir()
    {
        return configDir;
    }

    @SubscribeEvent
    public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.modID.equalsIgnoreCase(Reference.ID))
        {
            loadConfig();
        }
    }

    private static void loadConfig()
    {
        Settings.ITEMS_PER_COLUMN = config.getInt(TranslationHelper.translateToLocal("jer.config.itemsPerColumn.title"), Configuration.CATEGORY_GENERAL, 4, 1, 4, TranslationHelper.translateToLocal("jer.config.itemsPerColumn.description"));
        Settings.ITEMS_PER_ROW = config.getInt(TranslationHelper.translateToLocal("jer.config.itemsPerRow.title"), Configuration.CATEGORY_GENERAL, 4, 1, 4, TranslationHelper.translateToLocal("jer.config.itemsPerRow.description"));

        Settings.EXTRA_RANGE = config.getInt(TranslationHelper.translateToLocal("jer.config.extraRange.title"), Configuration.CATEGORY_GENERAL, 3, 0, 25, TranslationHelper.translateToLocal("jer.config.extraRange.description"));

        Settings.useDimNames = config.getBoolean(TranslationHelper.translateToLocal("jer.config.dimNames.title"), Configuration.CATEGORY_GENERAL, true, TranslationHelper.translateToLocal("jer.config.dimNames.description"));

        Settings.useDIYdata = config.getBoolean(TranslationHelper.translateToLocal("jer.config.diyData.title"), Configuration.CATEGORY_GENERAL, false, TranslationHelper.translateToLocal("jer.config.diyData.description"));

        Settings.excludedEnchants = config.getStringList(TranslationHelper.translateToLocal("jer.config.enchantsBlacklist.title"), Configuration.CATEGORY_GENERAL, new String[] { "flimflam" }, TranslationHelper.translateToLocal("jer.config.echantsBlacklist.description"));

        if (config.hasChanged())
            config.save();
        Settings.reload();
    }

    @SuppressWarnings("unchecked")
    public static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        list.addAll(new ConfigElement(config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements());
        return list;
    }
}
