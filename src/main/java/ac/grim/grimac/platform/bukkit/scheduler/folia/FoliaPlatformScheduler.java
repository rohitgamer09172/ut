/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.platform.bukkit.scheduler.folia;

import ac.grim.grimac.platform.api.scheduler.PlatformScheduler;
import ac.grim.grimac.platform.bukkit.scheduler.folia.FoliaAsyncScheduler;
import ac.grim.grimac.platform.bukkit.scheduler.folia.FoliaEntityScheduler;
import ac.grim.grimac.platform.bukkit.scheduler.folia.FoliaGlobalRegionScheduler;
import ac.grim.grimac.platform.bukkit.scheduler.folia.FoliaRegionScheduler;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import lombok.Generated;

public class FoliaPlatformScheduler
implements PlatformScheduler {
    @NotNull
    private final FoliaAsyncScheduler asyncScheduler = new FoliaAsyncScheduler();
    @NotNull
    private final FoliaGlobalRegionScheduler globalRegionScheduler = new FoliaGlobalRegionScheduler();
    @NotNull
    private final FoliaEntityScheduler entityScheduler = new FoliaEntityScheduler();
    @NotNull
    private final FoliaRegionScheduler regionScheduler = new FoliaRegionScheduler();

    @Override
    @NotNull
    @Generated
    public FoliaAsyncScheduler getAsyncScheduler() {
        return this.asyncScheduler;
    }

    @Override
    @NotNull
    @Generated
    public FoliaGlobalRegionScheduler getGlobalRegionScheduler() {
        return this.globalRegionScheduler;
    }

    @Override
    @NotNull
    @Generated
    public FoliaEntityScheduler getEntityScheduler() {
        return this.entityScheduler;
    }

    @Override
    @NotNull
    @Generated
    public FoliaRegionScheduler getRegionScheduler() {
        return this.regionScheduler;
    }
}

