/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.LegacyMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import java.util.Optional;

public interface EntityType
extends MappedEntity,
LegacyMappedEntity {
    public boolean isInstanceOf(EntityType var1);

    public Optional<EntityType> getParent();
}

