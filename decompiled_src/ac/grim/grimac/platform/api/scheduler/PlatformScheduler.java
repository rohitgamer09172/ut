/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.platform.api.scheduler;

import ac.grim.grimac.platform.api.scheduler.AsyncScheduler;
import ac.grim.grimac.platform.api.scheduler.EntityScheduler;
import ac.grim.grimac.platform.api.scheduler.GlobalRegionScheduler;
import ac.grim.grimac.platform.api.scheduler.RegionScheduler;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.concurrent.TimeUnit;

public interface PlatformScheduler {
    public static long convertTimeToTicks(long time, TimeUnit timeUnit) {
        return timeUnit.toMillis(time) / 50L;
    }

    public static long convertTicksToTime(long ticks, TimeUnit timeUnit) {
        long millis = ticks * 50L;
        return timeUnit.convert(millis, TimeUnit.MILLISECONDS);
    }

    @NotNull
    public AsyncScheduler getAsyncScheduler();

    @NotNull
    public GlobalRegionScheduler getGlobalRegionScheduler();

    @NotNull
    public EntityScheduler getEntityScheduler();

    @NotNull
    public RegionScheduler getRegionScheduler();
}

