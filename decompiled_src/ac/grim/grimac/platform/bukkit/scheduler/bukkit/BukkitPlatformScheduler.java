/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.platform.bukkit.scheduler.bukkit;

import ac.grim.grimac.platform.api.scheduler.PlatformScheduler;
import ac.grim.grimac.platform.bukkit.scheduler.bukkit.BukkitAsyncScheduler;
import ac.grim.grimac.platform.bukkit.scheduler.bukkit.BukkitEntityScheduler;
import ac.grim.grimac.platform.bukkit.scheduler.bukkit.BukkitGlobalRegionScheduler;
import ac.grim.grimac.platform.bukkit.scheduler.bukkit.BukkitRegionScheduler;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import lombok.Generated;

public class BukkitPlatformScheduler
implements PlatformScheduler {
    @NotNull
    private final BukkitAsyncScheduler asyncScheduler = new BukkitAsyncScheduler();
    @NotNull
    private final BukkitGlobalRegionScheduler globalRegionScheduler = new BukkitGlobalRegionScheduler();
    @NotNull
    private final BukkitEntityScheduler entityScheduler = new BukkitEntityScheduler();
    @NotNull
    private final BukkitRegionScheduler regionScheduler = new BukkitRegionScheduler();

    @Override
    @NotNull
    @Generated
    public BukkitAsyncScheduler getAsyncScheduler() {
        return this.asyncScheduler;
    }

    @Override
    @NotNull
    @Generated
    public BukkitGlobalRegionScheduler getGlobalRegionScheduler() {
        return this.globalRegionScheduler;
    }

    @Override
    @NotNull
    @Generated
    public BukkitEntityScheduler getEntityScheduler() {
        return this.entityScheduler;
    }

    @Override
    @NotNull
    @Generated
    public BukkitRegionScheduler getRegionScheduler() {
        return this.regionScheduler;
    }
}

