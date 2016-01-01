package jeresources.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class ReflectionHelper
{
    public static boolean isObf = false;

    public static boolean doesFieldExist(Class clazz, String name)
    {
        try
        {
            clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e)
        {
            return false;
        }
        return true;
    }

    public static Integer getInt(Class clazz, String name, Object instance)
    {
        Integer result = null;
        try
        {
            Field getField = clazz.getDeclaredField(name);
            getField.setAccessible(true);
            result = getField.getInt(instance);
        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public static String getString(Class clazz, String name, Object instance)
    {
        String result = null;
        try
        {
            Field getField = clazz.getDeclaredField(name);
            getField.setAccessible(true);
            result = (String) getField.get(instance);
        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        if (result == null) return "";
        return result;
    }

    public static boolean getBoolean(Class clazz, String name, Object instance)
    {
        Boolean result = null;
        try
        {
            Field getField = clazz.getDeclaredField(name);
            getField.setAccessible(true);
            result = (Boolean) getField.get(instance);
        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        if (result == null) return false;
        return result;
    }

    public static Object getObject(Class clazz, String name, Object instance)
    {
        try
        {
            Field getField = clazz.getDeclaredField(name);
            getField.setAccessible(true);
            return getField.get(instance);
        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return null;
    }

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
