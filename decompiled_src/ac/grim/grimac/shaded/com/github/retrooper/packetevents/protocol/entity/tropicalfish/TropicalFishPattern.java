/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.tropicalfish;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;

public interface TropicalFishPattern
extends MappedEntity {
    public Base getBase();

    public static enum Base {
        SMALL,
        LARGE;

    }
}

