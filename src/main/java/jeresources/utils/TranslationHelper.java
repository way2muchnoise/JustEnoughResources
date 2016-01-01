package jeresources.utils;

import net.minecraft.util.StatCollector;

public class TranslationHelper
{
    public static String translateToLocal(String key)
    {
        return StatCollector.translateToLocal(key);
    }

    public static String getLocalPageInfo(int page, int lastPage)
    {
        return translateToLocal("ner.page") + " " + (page + 1) + " " + translateToLocal("ner.of") + " " + (lastPage + 1);
    }

    public static boolean canTranslate(String key)
    {
        return StatCollector.canTranslate(key);
    }
}
