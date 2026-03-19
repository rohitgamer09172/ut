/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.scheduler.BukkitTask
 */
package ac.grim.grimac.platform.bukkit.scheduler.bukkit;

import ac.grim.grimac.platform.api.scheduler.TaskHandle;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.Objects;
import org.bukkit.scheduler.BukkitTask;

public class BukkitTaskHandle
implements TaskHandle {
    @NotNull
    private final BukkitTask task;

    @Contract(pure=true)
    public BukkitTaskHandle(@NotNull BukkitTask task) {
        this.task = Objects.requireNonNull(task);
    }

    @Override
    public boolean isSync() {
        return this.task.isSync();
    }

    @Override
    public boolean isCancelled() {
        return this.task.isCancelled();
    }

    @Override
    public void cancel() {
        this.task.cancel();
    }
}

