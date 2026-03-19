/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.platform.api.scheduler;

import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.platform.api.scheduler.TaskHandle;
import ac.grim.grimac.platform.api.world.PlatformWorld;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.math.Location;

public interface RegionScheduler {
    public void execute(@NotNull GrimPlugin var1, @NotNull PlatformWorld var2, int var3, int var4, @NotNull Runnable var5);

    public void execute(@NotNull GrimPlugin var1, @NotNull Location var2, @NotNull Runnable var3);

    public TaskHandle run(@NotNull GrimPlugin var1, @NotNull PlatformWorld var2, int var3, int var4, @NotNull Runnable var5);

    public TaskHandle run(@NotNull GrimPlugin var1, @NotNull Location var2, @NotNull Runnable var3);

    public TaskHandle runDelayed(@NotNull GrimPlugin var1, @NotNull PlatformWorld var2, int var3, int var4, @NotNull Runnable var5, long var6);

    public TaskHandle runDelayed(@NotNull GrimPlugin var1, @NotNull Location var2, @NotNull Runnable var3, long var4);

    public TaskHandle runAtFixedRate(@NotNull GrimPlugin var1, @NotNull PlatformWorld var2, int var3, int var4, @NotNull Runnable var5, long var6, long var8);

    public TaskHandle runAtFixedRate(@NotNull GrimPlugin var1, @NotNull Location var2, @NotNull Runnable var3, long var4, long var6);
}

