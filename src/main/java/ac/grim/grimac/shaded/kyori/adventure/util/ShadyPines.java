/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.util;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.util.MonkeyBars;
import java.util.Set;

public final class ShadyPines {
    private ShadyPines() {
    }

    @Deprecated
    @SafeVarargs
    @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
    @NotNull
    public static <E extends Enum<E>> Set<E> enumSet(Class<E> type, E ... constants) {
        return MonkeyBars.enumSet(type, constants);
    }

    public static boolean equals(double a, double b) {
        return Double.doubleToLongBits(a) == Double.doubleToLongBits(b);
    }

    public static boolean equals(float a, float b) {
        return Float.floatToIntBits(a) == Float.floatToIntBits(b);
    }
}

