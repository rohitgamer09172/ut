/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import java.util.List;

public interface EntityMetadataProvider {
    public List<EntityData<?>> entityData(ClientVersion var1);

    @Deprecated
    default public List<EntityData<?>> entityData() {
        return this.entityData(ClientVersion.getLatest());
    }
}

