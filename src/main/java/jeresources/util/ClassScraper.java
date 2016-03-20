package jeresources.util;

import java.util.HashSet;
import java.util.Set;

public class ClassScraper
{

    public static Set<Class> getSuperInterfaces(Class[] childInterfaces)
    {

        Set<Class> allInterfaces = new HashSet<Class>();

        for (int i = 0; i < childInterfaces.length; i++)
        {
            allInterfaces.add(childInterfaces[i]);
            allInterfaces.addAll(getSuperInterfaces(childInterfaces[i].getInterfaces()));
        }

        return allInterfaces;
    }

    public static Set<Class> getGeneralizations(Class classObject)
    {
        Set<Class> generalizations = new HashSet<Class>();

        generalizations.add(classObject);

        Class superClass = classObject.getSuperclass();
        if (superClass != null)
        {
            generalizations.addAll(getGeneralizations(superClass));
        }

        Class[] superInterfaces = classObject.getInterfaces();
        for (int i = 0; i < superInterfaces.length; i++)
        {
            Class superInterface = superInterfaces[i];
            generalizations.addAll(getGeneralizations(superInterface));
        }

        return generalizations;
    }

}
