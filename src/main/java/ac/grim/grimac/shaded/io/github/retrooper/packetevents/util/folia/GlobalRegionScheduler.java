/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitScheduler
 */
package ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.folia;

import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.folia.TaskWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class GlobalRegionScheduler {
    private BukkitScheduler bukkitScheduler;
    private io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler globalRegionScheduler;

    protected GlobalRegionScheduler() {
        if (FoliaScheduler.isFolia) {
            this.globalRegionScheduler = Bukkit.getGlobalRegionScheduler();
        } else {
            this.bukkitScheduler = Bukkit.getScheduler();
        }
    }

    public void execute(@NotNull Plugin plugin, @NotNull Runnable run) {
        if (!FoliaScheduler.isFolia) {
            this.bukkitScheduler.runTask(plugin, run);
            return;
        }
        this.globalRegionScheduler.execute(plugin, run);
    }

    public TaskWrapper run(@NotNull Plugin plugin, @NotNull Consumer<Object> task) {
        if (!FoliaScheduler.isFolia) {
            return new TaskWrapper(this.bukkitScheduler.runTask(plugin, () -> task.accept(null)));
        }
        return new TaskWrapper(this.globalRegionScheduler.run(plugin, o -> task.accept(null)));
    }

    public TaskWrapper runDelayed(@NotNull Plugin plugin, @NotNull Consumer<Object> task, long delay) {
        if (delay < 1L) {
            delay = 1L;
        }
        if (!FoliaScheduler.isFolia) {
            return new TaskWrapper(this.bukkitScheduler.runTaskLater(plugin, () -> task.accept(null), delay));
        }
        return new TaskWrapper(this.globalRegionScheduler.runDelayed(plugin, o -> task.accept(null), delay));
    }

    public TaskWrapper runAtFixedRate(@NotNull Plugin plugin, @NotNull Consumer<Object> task, long initialDelayTicks, long periodTicks) {
        if (initialDelayTicks < 1L) {
            initialDelayTicks = 1L;
        }
        if (periodTicks < 1L) {
            periodTicks = 1L;
        }
        if (!FoliaScheduler.isFolia) {
            return new TaskWrapper(this.bukkitScheduler.runTaskTimer(plugin, () -> task.accept(null), initialDelayTicks, periodTicks));
        }
        return new TaskWrapper(this.globalRegionScheduler.runAtFixedRate(plugin, o -> task.accept(null), initialDelayTicks, periodTicks));
    }

    public void cancel(@NotNull Plugin plugin) {
        if (!FoliaScheduler.isFolia) {
            Bukkit.getScheduler().cancelTasks(plugin);
            return;
        }
        this.globalRegionScheduler.cancelTasks(plugin);
    }
}

