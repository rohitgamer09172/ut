/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.util;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.time.Duration;

public interface Ticks {
    public static final int TICKS_PER_SECOND = 20;
    public static final long SINGLE_TICK_DURATION_MS = 50L;

    @NotNull
    public static Duration duration(long ticks) {
        return Duration.ofMillis(ticks * 50L);
    }
}

