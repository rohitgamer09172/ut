/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.papermc.paper.threadedregions.scheduler.AsyncScheduler
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitScheduler
 */
package ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.folia;

import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.folia.TaskWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class AsyncScheduler {
    private BukkitScheduler bukkitScheduler;
    private io.papermc.paper.threadedregions.scheduler.AsyncScheduler asyncScheduler;

    protected AsyncScheduler() {
        if (FoliaScheduler.isFolia) {
            this.asyncScheduler = Bukkit.getAsyncScheduler();
        } else {
            this.bukkitScheduler = Bukkit.getScheduler();
        }
    }

    public TaskWrapper runNow(@NotNull Plugin plugin, @NotNull Consumer<Object> task) {
        if (!FoliaScheduler.isFolia) {
            return new TaskWrapper(this.bukkitScheduler.runTaskAsynchronously(plugin, () -> task.accept(null)));
        }
        return new TaskWrapper(this.asyncScheduler.runNow(plugin, o -> task.accept(null)));
    }

    public TaskWrapper runDelayed(@NotNull Plugin plugin, @NotNull Consumer<Object> task, long delay, @NotNull TimeUnit timeUnit) {
        if (!FoliaScheduler.isFolia) {
            return new TaskWrapper(this.bukkitScheduler.runTaskLaterAsynchronously(plugin, () -> task.accept(null), this.convertTimeToTicks(delay, timeUnit)));
        }
        return new TaskWrapper(this.asyncScheduler.runDelayed(plugin, o -> task.accept(null), delay, timeUnit));
    }

    public TaskWrapper runAtFixedRate(@NotNull Plugin plugin, @NotNull Consumer<Object> task, long delay, long period, @NotNull TimeUnit timeUnit) {
        if (period < 1L) {
            period = 1L;
        }
        if (!FoliaScheduler.isFolia) {
            return new TaskWrapper(this.bukkitScheduler.runTaskTimerAsynchronously(plugin, () -> task.accept(null), this.convertTimeToTicks(delay, timeUnit), this.convertTimeToTicks(period, timeUnit)));
        }
        return new TaskWrapper(this.asyncScheduler.runAtFixedRate(plugin, o -> task.accept(null), delay, period, timeUnit));
    }

    public TaskWrapper runAtFixedRate(@NotNull Plugin plugin, @NotNull Consumer<Object> task, long initialDelayTicks, long periodTicks) {
        if (periodTicks < 1L) {
            periodTicks = 1L;
        }
        if (!FoliaScheduler.isFolia) {
            return new TaskWrapper(this.bukkitScheduler.runTaskTimerAsynchronously(plugin, () -> task.accept(null), initialDelayTicks, periodTicks));
        }
        return new TaskWrapper(this.asyncScheduler.runAtFixedRate(plugin, o -> task.accept(null), initialDelayTicks * 50L, periodTicks * 50L, TimeUnit.MILLISECONDS));
    }

    public void cancel(@NotNull Plugin plugin) {
        if (!FoliaScheduler.isFolia) {
            this.bukkitScheduler.cancelTasks(plugin);
            return;
        }
        this.asyncScheduler.cancelTasks(plugin);
    }

    private long convertTimeToTicks(long time, TimeUnit timeUnit) {
        return timeUnit.toMillis(time) / 50L;
    }
}

