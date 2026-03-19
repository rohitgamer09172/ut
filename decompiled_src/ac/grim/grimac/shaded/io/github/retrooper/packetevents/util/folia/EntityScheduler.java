/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Entity
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitScheduler
 */
package ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.folia;

import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.folia.TaskWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class EntityScheduler {
    private BukkitScheduler bukkitScheduler;

    protected EntityScheduler() {
        if (!FoliaScheduler.isFolia) {
            this.bukkitScheduler = Bukkit.getScheduler();
        }
    }

    public void execute(@NotNull Entity entity, @NotNull Plugin plugin, @NotNull Runnable run, @Nullable Runnable retired, long delay) {
        if (!FoliaScheduler.isFolia) {
            this.bukkitScheduler.runTaskLater(plugin, run, delay);
            return;
        }
        entity.getScheduler().execute(plugin, run, retired, delay);
    }

    public TaskWrapper run(@NotNull Entity entity, @NotNull Plugin plugin, @NotNull Consumer<Object> task, @Nullable Runnable retired) {
        if (!FoliaScheduler.isFolia) {
            return new TaskWrapper(this.bukkitScheduler.runTask(plugin, () -> task.accept(null)));
        }
        return new TaskWrapper(entity.getScheduler().run(plugin, o -> task.accept(null), retired));
    }

    public TaskWrapper runDelayed(@NotNull Entity entity, @NotNull Plugin plugin, @NotNull Consumer<Object> task, @Nullable Runnable retired, long delayTicks) {
        if (delayTicks < 1L) {
            delayTicks = 1L;
        }
        if (!FoliaScheduler.isFolia) {
            return new TaskWrapper(this.bukkitScheduler.runTaskLater(plugin, () -> task.accept(null), delayTicks));
        }
        return new TaskWrapper(entity.getScheduler().runDelayed(plugin, o -> task.accept(null), retired, delayTicks));
    }

    public TaskWrapper runAtFixedRate(@NotNull Entity entity, @NotNull Plugin plugin, @NotNull Consumer<Object> task, @Nullable Runnable retired, long initialDelayTicks, long periodTicks) {
        if (initialDelayTicks < 1L) {
            initialDelayTicks = 1L;
        }
        if (periodTicks < 1L) {
            periodTicks = 1L;
        }
        if (!FoliaScheduler.isFolia) {
            return new TaskWrapper(this.bukkitScheduler.runTaskTimer(plugin, () -> task.accept(null), initialDelayTicks, periodTicks));
        }
        return new TaskWrapper(entity.getScheduler().runAtFixedRate(plugin, o -> task.accept(null), retired, initialDelayTicks, periodTicks));
    }
}

