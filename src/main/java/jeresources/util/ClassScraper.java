package jeresources.util;

import java.util.HashSet;
import java.util.Set;

public class ClassScraper
{

    public static Set<Class> getSuperInterfaces(Class[] childInterfaces)
    {

        Set<Class> allInterfaces = new HashSet<>();

        for (Class childInterface : childInterfaces)
        {
            allInterfaces.add(childInterface);
            allInterfaces.addAll(getSuperInterfaces(childInterface.getInterfaces()));
        }

        return allInterfaces;
    }

    public static Set<Class> getGeneralizations(Class classObject)
    {
        Set<Class> generalizations = new HashSet<>();

        generalizations.add(classObject);

        Class superClass = classObject.getSuperclass();
        if (superClass != null)
        {
            generalizations.addAll(getGeneralizations(superClass));
        }

        Class[] superInterfaces = classObject.getInterfaces();
        for (Class superInterface : superInterfaces)
            generalizations.addAll(getGeneralizations(superInterface));

        return generalizations;
    }

}
