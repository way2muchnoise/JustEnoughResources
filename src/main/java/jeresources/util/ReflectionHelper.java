package jeresources.util;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;


public class ReflectionHelper extends ObfuscationReflectionHelper {
    public static Class findClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static boolean isInstanceOf(Class clazz, Class checkClass) {
        for (Object instanceOf : ClassScraper.getGeneralizations(clazz))
            if (instanceOf == checkClass) return true;
        return false;
    }

    public static <T> void injectIntoFields(Class annotationClass, Class<T> instanceClass, T instance) {
        // TODO
    }
}
