/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.platform.api.scheduler;

import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.platform.api.scheduler.TaskHandle;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public interface GlobalRegionScheduler {
    public void execute(@NotNull GrimPlugin var1, @NotNull Runnable var2);

    public TaskHandle run(@NotNull GrimPlugin var1, @NotNull Runnable var2);

    public TaskHandle runDelayed(@NotNull GrimPlugin var1, @NotNull Runnable var2, long var3);

    public TaskHandle runAtFixedRate(@NotNull GrimPlugin var1, @NotNull Runnable var2, long var3, long var5);

    public void cancel(@NotNull GrimPlugin var1);
}

