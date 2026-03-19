/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.mapdecoration;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;

public interface MapDecorationType
extends MappedEntity {
    public ResourceLocation getAssetId();

    public boolean isShowOnItemFrame();

    public int getMapColor();

    public boolean isExplorationMapElement();

    public boolean isTrackCount();
}

