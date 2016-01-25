package jeresources.utils;

import net.minecraftforge.fml.common.discovery.ASMDataTable;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public static <T> List<T> getInstances(ASMDataTable asmDataTable, Class annotationClass, Class<T> instanceClass) {
        String annotationClassName = annotationClass.getCanonicalName();
        Set<ASMDataTable.ASMData> asmDatas = asmDataTable.getAll(annotationClassName);
        List<T> instances = new ArrayList<>();
        for (ASMDataTable.ASMData asmData : asmDatas) {
            try {
                Class<?> asmClass = Class.forName(asmData.getClassName());
                Class<? extends T> asmInstanceClass = asmClass.asSubclass(instanceClass);
                T instance = asmInstanceClass.newInstance();
                instances.add(instance);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                LogHelper.warn("Failed to load: {}" + asmData.getClassName());
            }
        }
        return instances;
    }
}
