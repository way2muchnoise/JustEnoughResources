package jeresources.config;

import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import jeresources.reference.Reference;
import jeresources.utils.TranslationHelper;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler
{
    public static Configuration config;

    public static void init(File configFile)
    {
        if (config == null)
        {
            config = new Configuration(configFile);
            loadConfig();
        }
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

        Settings.CYCLE_TIME = config.getFloat(TranslationHelper.translateToLocal("jer.config.cycleTime.title"), Configuration.CATEGORY_GENERAL, 1.5F, 0.5F, 3.0F, TranslationHelper.translateToLocal("jer.config.cycleTime.description"));

        Settings.EXTRA_RANGE = config.getInt(TranslationHelper.translateToLocal("jer.config.extraRange.title"), Configuration.CATEGORY_GENERAL, 3, 0, 25, TranslationHelper.translateToLocal("jer.config.extraRange.description"));

        Settings.useDimNames = config.getBoolean(TranslationHelper.translateToLocal("jer.config.dimNames.title"), Configuration.CATEGORY_GENERAL, true, TranslationHelper.translateToLocal("jer.config.dimNames.description"));

        Settings.excludedEnchants = config.getStringList(TranslationHelper.translateToLocal("jer.config.echantsBlacklist.title"), Configuration.CATEGORY_GENERAL, new String[] { "flimflam" }, TranslationHelper.translateToLocal("jer.config.echantsBlacklist.description"));

        if (config.hasChanged())
        {
            config.save();
        }
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
