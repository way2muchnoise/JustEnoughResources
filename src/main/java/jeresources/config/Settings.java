package jeresources.config;

import jeresources.jei.dungeon.DungeonCategory;
import jeresources.registry.EnchantmentRegistry;
import net.minecraftforge.fml.relauncher.Side;

public final class Settings
{
    public static int ITEMS_PER_ROW;
    public static int ITEMS_PER_COLUMN;

    public static Side side;
    public static boolean useDIYdata;

    public static String[] excludedEnchants;
    public static boolean gameLoaded = false;

    public static void reload()
    {
        if (side == Side.CLIENT)
            DungeonCategory.reloadSettings();
        if (gameLoaded)
            EnchantmentRegistry.getInstance().removeAll(excludedEnchants);
    }
}
