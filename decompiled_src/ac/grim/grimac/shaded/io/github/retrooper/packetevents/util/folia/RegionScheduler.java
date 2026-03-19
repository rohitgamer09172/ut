/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.papermc.paper.threadedregions.scheduler.RegionScheduler
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitScheduler
 */
package ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.folia;

import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.folia.TaskWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class RegionScheduler {
    private BukkitScheduler bukkitScheduler;
    private io.papermc.paper.threadedregions.scheduler.RegionScheduler regionScheduler;

    protected RegionScheduler() {
        if (FoliaScheduler.isFolia) {
            this.regionScheduler = Bukkit.getRegionScheduler();
        } else {
            this.bukkitScheduler = Bukkit.getScheduler();
        }
    }

    public void execute(@NotNull Plugin plugin, @NotNull World world, int chunkX, int chunkZ, @NotNull Runnable run) {
        if (!FoliaScheduler.isFolia) {
            this.bukkitScheduler.runTask(plugin, run);
            return;
        }
        this.regionScheduler.execute(plugin, world, chunkX, chunkZ, run);
    }

    public void execute(@NotNull Plugin plugin, @NotNull Location location, @NotNull Runnable run) {
        if (!FoliaScheduler.isFolia) {
            Bukkit.getScheduler().runTask(plugin, run);
            return;
        }
        this.regionScheduler.execute(plugin, location, run);
    }

    public TaskWrapper run(@NotNull Plugin plugin, @NotNull World world, int chunkX, int chunkZ, @NotNull Consumer<Object> task) {
        if (!FoliaScheduler.isFolia) {
            return new TaskWrapper(Bukkit.getScheduler().runTask(plugin, () -> task.accept(null)));
        }
        return new TaskWrapper(this.regionScheduler.run(plugin, world, chunkX, chunkZ, o -> task.accept(null)));
    }

    public TaskWrapper run(@NotNull Plugin plugin, @NotNull Location location, @NotNull Consumer<Object> task) {
        if (!FoliaScheduler.isFolia) {
            return new TaskWrapper(Bukkit.getScheduler().runTask(plugin, () -> task.accept(null)));
        }
        return new TaskWrapper(this.regionScheduler.run(plugin, location, o -> task.accept(null)));
    }

    public TaskWrapper runDelayed(@NotNull Plugin plugin, @NotNull World world, int chunkX, int chunkZ, @NotNull Consumer<Object> task, long delayTicks) {
        if (delayTicks < 1L) {
            delayTicks = 1L;
        }
        if (!FoliaScheduler.isFolia) {
            return new TaskWrapper(Bukkit.getScheduler().runTaskLater(plugin, () -> task.accept(null), delayTicks));
        }
        return new TaskWrapper(this.regionScheduler.runDelayed(plugin, world, chunkX, chunkZ, o -> task.accept(null), delayTicks));
    }

    public TaskWrapper runDelayed(@NotNull Plugin plugin, @NotNull Location location, @NotNull Consumer<Object> task, long delayTicks) {
        if (delayTicks < 1L) {
            delayTicks = 1L;
        }
        if (!FoliaScheduler.isFolia) {
            return new TaskWrapper(Bukkit.getScheduler().runTaskLater(plugin, () -> task.accept(null), delayTicks));
        }
        return new TaskWrapper(this.regionScheduler.runDelayed(plugin, location, o -> task.accept(null), delayTicks));
    }

    public TaskWrapper runAtFixedRate(@NotNull Plugin plugin, @NotNull World world, int chunkX, int chunkZ, @NotNull Consumer<Object> task, long initialDelayTicks, long periodTicks) {
        if (initialDelayTicks < 1L) {
            initialDelayTicks = 1L;
        }
        if (periodTicks < 1L) {
            periodTicks = 1L;
        }
        if (!FoliaScheduler.isFolia) {
            return new TaskWrapper(Bukkit.getScheduler().runTaskTimer(plugin, () -> task.accept(null), initialDelayTicks, periodTicks));
        }
        return new TaskWrapper(this.regionScheduler.runAtFixedRate(plugin, world, chunkX, chunkZ, o -> task.accept(null), initialDelayTicks, periodTicks));
    }

    public TaskWrapper runAtFixedRate(@NotNull Plugin plugin, @NotNull Location location, @NotNull Consumer<Object> task, long initialDelayTicks, long periodTicks) {
        if (initialDelayTicks < 1L) {
            initialDelayTicks = 1L;
        }
        if (periodTicks < 1L) {
            periodTicks = 1L;
        }
        if (!FoliaScheduler.isFolia) {
            return new TaskWrapper(Bukkit.getScheduler().runTaskTimer(plugin, () -> task.accept(null), initialDelayTicks, periodTicks));
        }
        return new TaskWrapper(this.regionScheduler.runAtFixedRate(plugin, location, o -> task.accept(null), initialDelayTicks, periodTicks));
    }
}

