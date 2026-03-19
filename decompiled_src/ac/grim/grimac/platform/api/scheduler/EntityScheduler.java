/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.platform.api.scheduler;

import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.platform.api.entity.GrimEntity;
import ac.grim.grimac.platform.api.scheduler.TaskHandle;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public interface EntityScheduler {
    public void execute(@NotNull GrimEntity var1, @NotNull GrimPlugin var2, @NotNull Runnable var3, @Nullable Runnable var4, long var5);

    public TaskHandle run(@NotNull GrimEntity var1, @NotNull GrimPlugin var2, @NotNull Runnable var3, @Nullable Runnable var4);

    public TaskHandle runDelayed(@NotNull GrimEntity var1, @NotNull GrimPlugin var2, @NotNull Runnable var3, @Nullable Runnable var4, long var5);

    public TaskHandle runAtFixedRate(@NotNull GrimEntity var1, @NotNull GrimPlugin var2, @NotNull Runnable var3, @Nullable Runnable var4, long var5, long var7);
}

