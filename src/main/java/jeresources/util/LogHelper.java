package jeresources.util;

import jeresources.reference.Reference;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

/**
 * Helper class for logging
 *
 * @author way2muchnoise
 */
public class LogHelper
{
    /**
     * General logging method
     *
     * @param level Level of the log
     * @param obj   object to log
     */
    public static void log(Level level, Object obj)
    {
        FMLLog.log(Reference.ID, level, String.valueOf(obj));
    }

    /**
     * Used for logging on debug level
     *
     * @param obj object to log
     */
    public static void debug(Object obj)
    {
        log(Level.DEBUG, obj);
    }

    /**
     * Used for logging on info level
     *
     * @param obj object to log
     */
    public static void info(Object obj)
    {
        log(Level.INFO, obj);
    }

    public static void warn(Object obj)
    {
        log(Level.WARN, obj);

    }
}
