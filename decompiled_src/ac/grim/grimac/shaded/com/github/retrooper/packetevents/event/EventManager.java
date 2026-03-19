/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.event;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListener;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerAbstract;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerPriority;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.ProtocolPacketEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.exception.InvalidHandshakeException;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;

public class EventManager {
    private final Map<PacketListenerPriority, Set<PacketListenerCommon>> listenersMap = new ConcurrentHashMap<PacketListenerPriority, Set<PacketListenerCommon>>();
    private volatile PacketListenerCommon[] listeners = new PacketListenerCommon[0];

    public void callEvent(PacketEvent event) {
        this.callEvent(event, null);
    }

    public void callEvent(PacketEvent event, @Nullable Runnable postCallListenerAction, boolean preVia) {
        for (PacketListenerCommon listener : this.listeners) {
            block5: {
                try {
                    if (listener.isPreVia() == preVia) {
                        event.call(listener);
                    }
                }
                catch (Exception t) {
                    if (t.getClass() == InvalidHandshakeException.class || t.getCause() != null && t.getCause().getClass() == InvalidHandshakeException.class) break block5;
                    PacketEvents.getAPI().getLogger().log(Level.WARNING, "PacketEvents caught an unhandled exception while calling your listener.", t);
                }
            }
            if (postCallListenerAction == null) continue;
            postCallListenerAction.run();
        }
        if (event instanceof ProtocolPacketEvent && !((ProtocolPacketEvent)event).needsReEncode()) {
            ((ProtocolPacketEvent)event).setLastUsedWrapper(null);
        }
    }

    public void callEvent(PacketEvent event, @Nullable Runnable postCallListenerAction) {
        this.callEvent(event, postCallListenerAction, false);
    }

    public PacketListenerCommon registerListener(PacketListener listener, PacketListenerPriority priority) {
        PacketListenerAbstract packetListenerAbstract = listener.asAbstract(priority);
        return this.registerListener(packetListenerAbstract);
    }

    public PacketListenerCommon registerListener(PacketListenerCommon listener) {
        this.registerListenerNoRecalculation(listener);
        this.recalculateListeners();
        return listener;
    }

    public PacketListenerCommon[] registerListeners(PacketListenerCommon ... listeners) {
        for (PacketListenerCommon listener : listeners) {
            this.registerListenerNoRecalculation(listener);
        }
        this.recalculateListeners();
        return listeners;
    }

    public void unregisterListener(PacketListenerCommon listener) {
        if (this.unregisterListenerNoRecalculation(listener)) {
            this.recalculateListeners();
        }
    }

    public void unregisterListeners(PacketListenerCommon ... listeners) {
        boolean modified = false;
        for (PacketListenerCommon listener : listeners) {
            modified |= this.unregisterListenerNoRecalculation(listener);
        }
        if (modified) {
            this.recalculateListeners();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void unregisterAllListeners() {
        this.listenersMap.clear();
        EventManager eventManager = this;
        synchronized (eventManager) {
            this.listeners = new PacketListenerCommon[0];
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void recalculateListeners() {
        EventManager eventManager = this;
        synchronized (eventManager) {
            ArrayList<PacketListenerCommon> list = new ArrayList<PacketListenerCommon>();
            for (PacketListenerPriority priority : PacketListenerPriority.values()) {
                Set<PacketListenerCommon> set = this.listenersMap.get((Object)priority);
                if (set == null) continue;
                list.addAll(set);
            }
            this.listeners = list.toArray(new PacketListenerCommon[0]);
        }
    }

    private void registerListenerNoRecalculation(PacketListenerCommon listener) {
        Set listenerSet = this.listenersMap.computeIfAbsent(listener.getPriority(), p -> new CopyOnWriteArraySet());
        listenerSet.add(listener);
    }

    private boolean unregisterListenerNoRecalculation(PacketListenerCommon listener) {
        Set<PacketListenerCommon> listenerSet = this.listenersMap.get((Object)listener.getPriority());
        return listenerSet != null && listenerSet.remove(listener);
    }
}

