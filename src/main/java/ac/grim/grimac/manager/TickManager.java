/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ClassToInstanceMap
 *  com.google.common.collect.ImmutableClassToInstanceMap$Builder
 */
package ac.grim.grimac.manager;

import ac.grim.grimac.manager.tick.Tickable;
import ac.grim.grimac.manager.tick.impl.ClearRecentlyUpdatedBlocks;
import ac.grim.grimac.manager.tick.impl.ClientVersionSetter;
import ac.grim.grimac.manager.tick.impl.ResetTick;
import ac.grim.grimac.manager.tick.impl.TickInventory;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;

public class TickManager {
    public int currentTick;
    private final ClassToInstanceMap<Tickable> syncTick = new ImmutableClassToInstanceMap.Builder().put(ResetTick.class, (Object)new ResetTick()).build();
    private final ClassToInstanceMap<Tickable> asyncTick = new ImmutableClassToInstanceMap.Builder().put(ClientVersionSetter.class, (Object)new ClientVersionSetter()).put(TickInventory.class, (Object)new TickInventory()).put(ClearRecentlyUpdatedBlocks.class, (Object)new ClearRecentlyUpdatedBlocks()).build();

    public void tickSync() {
        ++this.currentTick;
        this.syncTick.values().forEach(Tickable::tick);
    }

    public void tickAsync() {
        this.asyncTick.values().forEach(Tickable::tick);
    }
}

