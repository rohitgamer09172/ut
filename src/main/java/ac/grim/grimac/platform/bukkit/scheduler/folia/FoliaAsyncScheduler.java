/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.papermc.paper.threadedregions.scheduler.AsyncScheduler
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package ac.grim.grimac.platform.bukkit.scheduler.folia;

import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.platform.api.scheduler.TaskHandle;
import ac.grim.grimac.platform.bukkit.GrimACBukkitLoaderPlugin;
import ac.grim.grimac.platform.bukkit.scheduler.folia.FoliaTaskHandle;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class FoliaAsyncScheduler
implements ac.grim.grimac.platform.api.scheduler.AsyncScheduler {
    private final AsyncScheduler scheduler = Bukkit.getAsyncScheduler();

    @Override
    public TaskHandle runNow(@NotNull GrimPlugin plugin, @NotNull Runnable task) {
        return new FoliaTaskHandle(this.scheduler.runNow((Plugin)GrimACBukkitLoaderPlugin.LOADER, ignored -> task.run()));
    }

    @Override
    public TaskHandle runDelayed(@NotNull GrimPlugin plugin, @NotNull Runnable task, long delay, @NotNull TimeUnit timeUnit) {
        return new FoliaTaskHandle(this.scheduler.runDelayed((Plugin)GrimACBukkitLoaderPlugin.LOADER, ignored -> task.run(), delay, timeUnit));
    }

    @Override
    public TaskHandle runAtFixedRate(@NotNull GrimPlugin plugin, @NotNull Runnable task, long delay, long period, @NotNull TimeUnit timeUnit) {
        return new FoliaTaskHandle(this.scheduler.runAtFixedRate((Plugin)GrimACBukkitLoaderPlugin.LOADER, ignored -> task.run(), delay, period, timeUnit));
    }

    @Override
    public TaskHandle runAtFixedRate(@NotNull GrimPlugin plugin, @NotNull Runnable task, long initialDelayTicks, long periodTicks) {
        return new FoliaTaskHandle(this.scheduler.runAtFixedRate((Plugin)GrimACBukkitLoaderPlugin.LOADER, ignored -> task.run(), initialDelayTicks * 50L, periodTicks * 50L, TimeUnit.MILLISECONDS));
    }

    @Override
    public void cancel(@NotNull GrimPlugin plugin) {
        this.scheduler.cancelTasks((Plugin)GrimACBukkitLoaderPlugin.LOADER);
    }
}

