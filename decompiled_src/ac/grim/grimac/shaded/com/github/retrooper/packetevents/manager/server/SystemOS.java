/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server;

import java.util.Locale;

public enum SystemOS {
    WINDOWS,
    MACOS,
    LINUX,
    OTHER;

    private static SystemOS CACHE;

    public static SystemOS getOSNoCache() {
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        for (SystemOS sysos : SystemOS.values()) {
            if (!os.contains(sysos.name().toLowerCase(Locale.ROOT))) continue;
            return sysos;
        }
        return OTHER;
    }

    public static SystemOS getOS() {
        if (CACHE == null) {
            CACHE = SystemOS.getOSNoCache();
        }
        return CACHE;
    }
}

