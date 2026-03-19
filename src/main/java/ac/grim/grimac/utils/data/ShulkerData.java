/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import ac.grim.grimac.utils.data.packetentity.PacketEntityShulker;
import java.util.Objects;

public class ShulkerData {
    public final int lastTransactionSent;
    public final boolean isClosing;
    public PacketEntity entity = null;
    public Vector3i blockPos = null;
    private int ticksOfOpeningClosing = 0;

    public ShulkerData(Vector3i position, int lastTransactionSent, boolean isClosing) {
        this.lastTransactionSent = lastTransactionSent;
        this.isClosing = isClosing;
        this.blockPos = position;
    }

    public ShulkerData(PacketEntityShulker entity, int lastTransactionSent, boolean isClosing) {
        this.lastTransactionSent = lastTransactionSent;
        this.isClosing = isClosing;
        this.entity = entity;
    }

    public boolean tickIfGuaranteedFinished() {
        return this.isClosing && ++this.ticksOfOpeningClosing >= 25;
    }

    public SimpleCollisionBox getCollision() {
        if (this.blockPos != null) {
            return new SimpleCollisionBox(this.blockPos);
        }
        return this.entity.getPossibleCollisionBoxes();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ShulkerData that = (ShulkerData)o;
        return Objects.equals(this.entity, that.entity) && Objects.equals(this.blockPos, that.blockPos);
    }

    public int hashCode() {
        return Objects.hash(this.entity, this.blockPos);
    }
}

