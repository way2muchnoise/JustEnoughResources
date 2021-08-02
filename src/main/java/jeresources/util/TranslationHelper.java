package jeresources.util;


import net.minecraft.client.resources.I18n;

public class TranslationHelper {
    public static String translateAndFormat(String key, Object... params) {
        return I18n.get(key, params);
    }

    public static String getLocalPageInfo(int page, int lastPage) {
        return translateAndFormat("jer.page") + " " + (page + 1) + " " + translateAndFormat("jer.of") + " " + (lastPage + 1);
    }

    public static boolean canTranslate(String key) {
        return I18n.exists(key);
    }

    public static String tryDimensionTranslate(String dimension) {
        if (TranslationHelper.canTranslate("jer.dim." + dimension)) {
            dimension = "jer.dim." + dimension;
        }
        return TranslationHelper.translateAndFormat(dimension);
    }
}
