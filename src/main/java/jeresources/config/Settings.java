package jeresources.config;

import jeresources.jei.JEIConfig;
import jeresources.jei.dungeon.DungeonCategory;
import jeresources.registry.EnchantmentRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.List;

public final class Settings {
    public static int ITEMS_PER_ROW;
    public static int ITEMS_PER_COLUMN;

    public static boolean useDIYdata;

    public static String[] excludedEnchants, hiddenCategories;
    public static boolean gameLoaded = false;
    public static boolean showDevData;
    public static List<Integer> excludedDimensions;

    public static void reload() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            DungeonCategory.reloadSettings();
        }
        if (gameLoaded) {
            EnchantmentRegistry.getInstance().removeAll(excludedEnchants);
            JEIConfig.resetCategories();
            JEIConfig.hideCategories(hiddenCategories);
        }
    }
}
