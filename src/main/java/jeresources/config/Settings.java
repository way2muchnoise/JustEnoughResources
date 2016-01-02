package jeresources.config;

import jeresources.jei.category.*;
import net.minecraftforge.fml.relauncher.Side;
import jeresources.registry.EnchantmentRegistry;

public final class Settings
{
    public static int ITEMS_PER_ROW;
    public static int ITEMS_PER_COLUMN;
    public static float CYCLE_TIME;

    public static int EXTRA_RANGE;
    public static Side side;
    public static boolean useDimNames;
    
    public static String[] excludedEnchants;
    public static boolean gameLoaded = false;
    public static boolean initedCompat = false;

    public static void reload()
    {
        if (side == Side.CLIENT)
        {
            JEIDungeonCategory.reloadSettings();
            JEIMobCategory.reloadSettings();
            JEIOreCategory.reloadSettings();
            JEIEnchantmentCategory.reloadSettings();
            JEIAdvSeedCategory.reloadSettings();
        }
        if (gameLoaded) EnchantmentRegistry.getInstance().removeAll(excludedEnchants);
    }
}
