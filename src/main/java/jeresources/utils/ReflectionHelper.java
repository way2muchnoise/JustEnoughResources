package jeresources.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class ReflectionHelper extends net.minecraftforge.fml.relauncher.ReflectionHelper
{
    public static Class findClass(String name)
    {
        try
        {
            return Class.forName(name);
        } catch (ClassNotFoundException e)
        {
            return null;
        }
    }

    public static Object initialize(Class clazz, Class argClass, Object arg)
    {
        try
        {
            return clazz.getConstructor(argClass).newInstance(arg);
        } catch (Exception e)
        {
            for (Constructor constructor : clazz.getConstructors())
            {
                System.out.println(constructor);
            }
        }
        return null;
    }

    public static Object initialize(Class clazz)
    {
        try
        {
            return clazz.getConstructor().newInstance();
        } catch (Exception e)
        {
            for (Constructor constructor : clazz.getConstructors())
            {
                System.out.println(constructor);
            }
        }
        return null;
    }

    public static boolean isInstanceOf(Class clazz, Class checkClass)
    {
        for (Object instanceOf : ClassScraper.getGeneralizations(clazz))
            if (instanceOf == checkClass) return true;
        return false;
    }
}
