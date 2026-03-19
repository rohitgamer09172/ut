/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public final class EntityPositionData {
    private Vector3d position;
    private Vector3d deltaMovement;
    private float yaw;
    private float pitch;

    public EntityPositionData(Vector3d position, Vector3d deltaMovement, float yaw, float pitch) {
        this.position = position;
        this.deltaMovement = deltaMovement;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public static EntityPositionData read(PacketWrapper<?> wrapper) {
        Vector3d position = Vector3d.read(wrapper);
        Vector3d deltaMovement = Vector3d.read(wrapper);
        float yaw = wrapper.readFloat();
        float pitch = wrapper.readFloat();
        return new EntityPositionData(position, deltaMovement, yaw, pitch);
    }

    public static void write(PacketWrapper<?> wrapper, EntityPositionData positionData) {
        Vector3d.write(wrapper, positionData.position);
        Vector3d.write(wrapper, positionData.deltaMovement);
        wrapper.writeFloat(positionData.yaw);
        wrapper.writeFloat(positionData.pitch);
    }

    public Vector3d getPosition() {
        return this.position;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public Vector3d getDeltaMovement() {
        return this.deltaMovement;
    }

    public void setDeltaMovement(Vector3d deltaMovement) {
        this.deltaMovement = deltaMovement;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EntityPositionData)) {
            return false;
        }
        EntityPositionData that = (EntityPositionData)obj;
        if (Float.compare(that.yaw, this.yaw) != 0) {
            return false;
        }
        if (Float.compare(that.pitch, this.pitch) != 0) {
            return false;
        }
        if (!this.position.equals(that.position)) {
            return false;
        }
        return this.deltaMovement.equals(that.deltaMovement);
    }

    public int hashCode() {
        return Objects.hash(this.position, this.deltaMovement, Float.valueOf(this.yaw), Float.valueOf(this.pitch));
    }

    public String toString() {
        return "EntityPositionData{position=" + this.position + ", deltaMovement=" + this.deltaMovement + ", yaw=" + this.yaw + ", pitch=" + this.pitch + '}';
    }
}

