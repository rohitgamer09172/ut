/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;

public interface StaticMappedEntity
extends MappedEntity {
    public int getId();

    @Override
    default public int getId(ClientVersion version) {
        return this.getId();
    }
}

