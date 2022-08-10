package jeresources.util;

import jeresources.reference.Reference;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

/**
 * Helper class for logging
 *
 * @author way2muchnoise
 */
public class LogHelper {
    public static void trace(String message, Object... params) {
        log(Level.TRACE, message, params);
    }

    public static void debug(String message, Object... params) {
        log(Level.DEBUG, message, params);
    }

    public static void info(String message, Object... params) {
        log(Level.INFO, message, params);
    }

    public static void warn(String message, Throwable t) {
        log(Level.WARN, message, t);
    }

    public static void warn(String message, Object... params) {
        log(Level.WARN, message, params);
    }

    public static void error(String message, Object... params) {
        log(Level.ERROR, message, params);
    }

    private static void log(Level logLevel, String message, Object... params) {
        LogManager.getLogger(Reference.ID).log(logLevel, message, params);
    }

    private static void log(Level logLevel, String message, Throwable t) {
        LogManager.getLogger(Reference.ID).log(logLevel, message, t);
    }
}
