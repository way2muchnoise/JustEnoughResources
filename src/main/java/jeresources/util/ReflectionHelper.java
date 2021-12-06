package jeresources.util;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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
        Type annotationType = Type.getType(annotationClass);
        List<ModFileScanData> allScanData = ModList.get().getAllScanData();
        for (ModFileScanData scanData : allScanData) {
            Iterable<ModFileScanData.AnnotationData> annotations = scanData.getAnnotations();
            for (ModFileScanData.AnnotationData a : annotations) {
                if (Objects.equals(a.annotationType(), annotationType)) {
                    try {
                        Class clazz = Class.forName(a.clazz().getClassName());
                        Field field = clazz.getField(a.memberName());
                        if (field.getType() == instanceClass)
                            field.set(null, instance);
                    } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                        LogHelper.warn("Failed to set: {}" + a.clazz().getClassName() + "." + a.memberName());
                    }
                }
            }
        }
    }

    public static <T, E> List<T> getPrivateArrayValueAsList(Class<? super E> classToAccess, E instance, String fieldName) {
        T array = getPrivateValue(classToAccess, instance, fieldName);
        int length = Array.getLength(array);
        List<T> arrayList = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            arrayList.add((T) Array.get(array, i));
        }
        return arrayList;
    }

    public static <T, E> T[] getPrivateArrayValue(Class<? super E> classToAccess, E instance, String fieldName) {
        return (T[]) getPrivateArrayValueAsList(classToAccess, instance, fieldName).toArray();
    }
}
