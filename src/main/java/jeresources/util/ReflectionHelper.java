package jeresources.util;

import net.minecraftforge.fml.common.discovery.ASMDataTable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Set;

@SuppressWarnings("unchecked")
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

    public static <T> void injectIntoFields(ASMDataTable asmDataTable, Class annotation, Class<T> type, T instance) {
        String annotationClassName = annotation.getCanonicalName();
        Set<ASMDataTable.ASMData> asmDataSet = asmDataTable.getAll(annotationClassName);
        for (ASMDataTable.ASMData asmData : asmDataSet) {
            try {
                Class clazz = Class.forName(asmData.getClassName());
                Field field = clazz.getField(asmData.getObjectName());
                if (field.getType() == type)
                    field.set(null, instance);
            } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                LogHelper.warn("Failed to set: {}" + asmData.getClassName() + "." + asmData.getObjectName());
            }
        }
    }
}
