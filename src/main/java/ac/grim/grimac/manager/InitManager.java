/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  lombok.Generated
 */
package ac.grim.grimac.manager;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.manager.init.Initable;
import ac.grim.grimac.manager.init.load.LoadableInitable;
import ac.grim.grimac.manager.init.load.PacketEventsInit;
import ac.grim.grimac.manager.init.start.CommandRegister;
import ac.grim.grimac.manager.init.start.JavaVersion;
import ac.grim.grimac.manager.init.start.PacketLimiter;
import ac.grim.grimac.manager.init.start.PacketManager;
import ac.grim.grimac.manager.init.start.StartableInitable;
import ac.grim.grimac.manager.init.start.TAB;
import ac.grim.grimac.manager.init.start.TickRunner;
import ac.grim.grimac.manager.init.start.UpdateChecker;
import ac.grim.grimac.manager.init.start.ViaBackwardsManager;
import ac.grim.grimac.manager.init.start.ViaVersion;
import ac.grim.grimac.manager.init.stop.StoppableInitable;
import ac.grim.grimac.manager.init.stop.TerminatePacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEventsAPI;
import ac.grim.grimac.utils.anticheat.LogUtil;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import lombok.Generated;

public class InitManager {
    private final ImmutableList<LoadableInitable> initializersOnLoad;
    private final ImmutableList<StartableInitable> initializersOnStart;
    private final ImmutableList<StoppableInitable> initializersOnStop;
    private boolean loaded = false;
    private boolean started = false;
    private boolean stopped = false;

    public InitManager(PacketEventsAPI<?> packetEventsAPI, Initable ... platformSpecificInitables) {
        ArrayList<LoadableInitable> extraLoadableInitables = new ArrayList<LoadableInitable>();
        ArrayList<StartableInitable> extraStartableInitables = new ArrayList<StartableInitable>();
        ArrayList<StoppableInitable> extraStoppableInitables = new ArrayList<StoppableInitable>();
        for (Initable initable : platformSpecificInitables) {
            if (initable instanceof LoadableInitable) {
                extraLoadableInitables.add((LoadableInitable)initable);
            }
            if (initable instanceof StartableInitable) {
                extraStartableInitables.add((StartableInitable)initable);
            }
            if (!(initable instanceof StoppableInitable)) continue;
            extraStoppableInitables.add((StoppableInitable)initable);
        }
        this.initializersOnLoad = ImmutableList.builder().add((Object)new PacketEventsInit(packetEventsAPI)).add(() -> GrimAPI.INSTANCE.getExternalAPI().load()).addAll(extraLoadableInitables).build();
        this.initializersOnStart = ImmutableList.builder().add((Object)GrimAPI.INSTANCE.getExternalAPI()).add((Object)new PacketManager()).add((Object)new ViaBackwardsManager()).add((Object)new TickRunner()).add((Object)new CommandRegister(GrimAPI.INSTANCE.getCommandService())).add((Object)new UpdateChecker()).add((Object)new PacketLimiter()).add((Object)GrimAPI.INSTANCE.getAlertManager()).add((Object)GrimAPI.INSTANCE.getDiscordManager()).add((Object)GrimAPI.INSTANCE.getSpectateManager()).add((Object)GrimAPI.INSTANCE.getViolationDatabaseManager()).add((Object)new JavaVersion()).add((Object)new ViaVersion()).add((Object)new TAB()).addAll(extraStartableInitables).build();
        this.initializersOnStop = ImmutableList.builder().add((Object)new TerminatePacketEvents()).addAll(extraStoppableInitables).build();
    }

    public void load() {
        for (LoadableInitable initable : this.initializersOnLoad) {
            try {
                initable.load();
            }
            catch (Exception e) {
                LogUtil.error("Failed to load " + initable.getClass().getSimpleName(), e);
            }
        }
        this.loaded = true;
    }

    public void start() {
        for (StartableInitable initable : this.initializersOnStart) {
            try {
                initable.start();
            }
            catch (Exception e) {
                LogUtil.error("Failed to start " + initable.getClass().getSimpleName(), e);
            }
        }
        this.started = true;
    }

    public void stop() {
        for (StoppableInitable initable : this.initializersOnStop) {
            try {
                initable.stop();
            }
            catch (Exception e) {
                LogUtil.error("Failed to stop " + initable.getClass().getSimpleName(), e);
            }
        }
        this.stopped = true;
    }

    @Generated
    public boolean isLoaded() {
        return this.loaded;
    }

    @Generated
    public boolean isStarted() {
        return this.started;
    }

    @Generated
    public boolean isStopped() {
        return this.stopped;
    }
}

