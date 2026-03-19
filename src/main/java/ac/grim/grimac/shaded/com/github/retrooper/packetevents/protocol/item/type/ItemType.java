/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.StaticComponentMap;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Set;

public interface ItemType
extends MappedEntity {
    public int getMaxAmount();

    public int getMaxDurability();

    default public boolean isMusicDisc() {
        return this.hasAttribute(ItemTypes.ItemAttribute.MUSIC_DISC);
    }

    public ItemType getCraftRemainder();

    @Nullable
    public StateType getPlacedType();

    public Set<ItemTypes.ItemAttribute> getAttributes();

    default public boolean hasAttribute(ItemTypes.ItemAttribute attribute) {
        return this.getAttributes().contains((Object)attribute);
    }

    @Deprecated
    default public StaticComponentMap getComponents() {
        return this.getComponents(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }

    default public StaticComponentMap getComponents(ClientVersion clientVersion) {
        return StaticComponentMap.EMPTY;
    }
}

