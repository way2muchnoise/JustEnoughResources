package jeresources.config;

import jeresources.reference.Reference;
import jeresources.util.TranslationHelper;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
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
    private static String worldGenFileName = "world-gen.json";

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

    public static File getWorldGenFile()
    {
        return new File(configDir, worldGenFileName);
    }

    @SubscribeEvent
    public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equalsIgnoreCase(Reference.ID))
        {
            loadConfig();
        }
    }

    private static void loadConfig()
    {
        Property prop;

        prop = config.get(Configuration.CATEGORY_GENERAL, "itemsPerColumn", 4);
        prop.setComment(TranslationHelper.translateToLocal("jer.config.itemsPerColumn.description"));
        prop.setMinValue(1).setMaxValue(4);
        prop.setLanguageKey("jer.config.itemsPerColumn.title");
        Settings.ITEMS_PER_COLUMN = prop.getInt();

        prop = config.get(Configuration.CATEGORY_GENERAL, "itemsPerRow", 4);
        prop.setComment(TranslationHelper.translateToLocal("jer.config.itemsPerRow.description"));
        prop.setMinValue(1).setMaxValue(4);
        prop.setLanguageKey("jer.config.itemsPerRow.title");
        Settings.ITEMS_PER_ROW = prop.getInt();

        prop = config.get(Configuration.CATEGORY_GENERAL, "diyData", true);
        prop.setComment(TranslationHelper.translateToLocal("jer.config.diyData.description"));
        prop.setLanguageKey("jer.config.diyData.title");
        prop.requiresMcRestart();
        Settings.useDIYdata = prop.getBoolean();

        prop = config.get(Configuration.CATEGORY_GENERAL, "enchantsBlacklist", new String[]{"flimflam", "soulBound"});
        prop.setComment(TranslationHelper.translateToLocal("jer.config.enchantsBlacklist.description"));
        prop.setLanguageKey("jer.config.enchantsBlacklist.title");
        prop.requiresMcRestart();
        Settings.excludedEnchants = prop.getStringList();

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
