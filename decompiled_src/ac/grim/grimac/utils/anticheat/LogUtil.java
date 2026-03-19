/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.anticheat;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;
import lombok.Generated;

public final class LogUtil {
    public static void info(String info) {
        LogUtil.getLogger().info(info);
    }

    public static void warn(String warn) {
        LogUtil.getLogger().warning(warn);
    }

    public static void warn(String description, Throwable throwable) {
        Logger logger = LogUtil.getLogger();
        if (logger != null) {
            logger.warning(description + ": " + LogUtil.getStackTrace(throwable));
        } else {
            throwable.printStackTrace();
        }
    }

    public static void error(String error) {
        LogUtil.getLogger().severe(error);
    }

    public static void error(String description, Throwable throwable) {
        Logger logger = LogUtil.getLogger();
        if (logger != null) {
            logger.severe(description + ": " + LogUtil.getStackTrace(throwable));
        } else {
            throwable.printStackTrace();
        }
    }

    public static void error(Throwable throwable) {
        Logger logger = LogUtil.getLogger();
        if (logger != null) {
            logger.severe(LogUtil.getStackTrace(throwable));
        } else {
            throwable.printStackTrace();
        }
    }

    public static Logger getLogger() {
        return GrimAPI.INSTANCE.getGrimPlugin().getLogger();
    }

    public static void console(String info) {
        GrimAPI.INSTANCE.getPlatformServer().getConsoleSender().sendMessage(MessageUtil.translateAlternateColorCodes('&', info));
    }

    public static void console(Component info) {
        GrimAPI.INSTANCE.getPlatformServer().getConsoleSender().sendMessage(info);
    }

    private static String getStackTrace(Throwable throwable) {
        String message = throwable.getMessage();
        try (StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw);){
            throwable.printStackTrace(pw);
            message = sw.toString();
        }
        catch (Exception exception) {
            // empty catch block
        }
        return message;
    }

    @Generated
    private LogUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

