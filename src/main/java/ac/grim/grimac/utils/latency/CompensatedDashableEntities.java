/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.latency;

import ac.grim.grimac.shaded.fastutil.ints.Int2ObjectMap;
import ac.grim.grimac.shaded.fastutil.ints.Int2ObjectOpenHashMap;
import ac.grim.grimac.utils.data.packetentity.DashableEntity;

public class CompensatedDashableEntities {
    private final Int2ObjectMap<DashableEntity> dashableMap = new Int2ObjectOpenHashMap<DashableEntity>();

    public void tick() {
        if (this.dashableMap.isEmpty()) {
            return;
        }
        for (DashableEntity dashable : this.dashableMap.values()) {
            dashable.setDashCooldown(Math.max(0, dashable.getDashCooldown() - 1));
        }
    }

    public void addEntity(int entityId, DashableEntity dashable) {
        this.dashableMap.put(entityId, dashable);
    }

    public void removeEntity(int entityId) {
        this.dashableMap.remove(entityId);
    }
}

