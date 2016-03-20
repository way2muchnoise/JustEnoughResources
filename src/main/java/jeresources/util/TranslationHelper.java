package jeresources.util;

import net.minecraft.util.text.translation.I18n;

public class TranslationHelper
{
    public static String translateToLocal(String key)
    {
        return I18n.translateToLocal(key);
    }

    public static String getLocalPageInfo(int page, int lastPage)
    {
        return translateToLocal("jer.page") + " " + (page + 1) + " " + translateToLocal("jer.of") + " " + (lastPage + 1);
    }

    public static boolean canTranslate(String key)
    {
        return I18n.canTranslate(key);
    }
}
