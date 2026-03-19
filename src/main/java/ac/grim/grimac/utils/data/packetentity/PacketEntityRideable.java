/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.data.packetentity;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import ac.grim.grimac.utils.data.attribute.ValuedAttribute;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import java.util.UUID;

public class PacketEntityRideable
extends PacketEntity {
    public boolean hasSaddle = false;
    public int boostTimeMax = 0;
    public int currentBoostTime = 0;

    public PacketEntityRideable(GrimPlayer player, UUID uuid, EntityType type, double x, double y, double z) {
        super(player, uuid, type, x, y, z);
        this.setAttribute(Attributes.STEP_HEIGHT, 1.0);
        this.trackAttribute(ValuedAttribute.ranged(Attributes.MOVEMENT_SPEED, 0.1f, 0.0, 1024.0));
    }
}

