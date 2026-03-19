/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.data;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import lombok.Generated;

public final class RotationData {
    private final float yaw;
    private final float pitch;
    private final int transaction;
    private boolean isAccepted;

    @Contract(mutates="this")
    public void accept() {
        this.isAccepted = true;
    }

    @Generated
    public float getYaw() {
        return this.yaw;
    }

    @Generated
    public float getPitch() {
        return this.pitch;
    }

    @Generated
    public int getTransaction() {
        return this.transaction;
    }

    @Generated
    public boolean isAccepted() {
        return this.isAccepted;
    }

    @Generated
    public RotationData(float yaw, float pitch, int transaction) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.transaction = transaction;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RotationData)) {
            return false;
        }
        RotationData other = (RotationData)o;
        if (Float.compare(this.getYaw(), other.getYaw()) != 0) {
            return false;
        }
        if (Float.compare(this.getPitch(), other.getPitch()) != 0) {
            return false;
        }
        if (this.getTransaction() != other.getTransaction()) {
            return false;
        }
        return this.isAccepted() == other.isAccepted();
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + Float.floatToIntBits(this.getYaw());
        result = result * 59 + Float.floatToIntBits(this.getPitch());
        result = result * 59 + this.getTransaction();
        result = result * 59 + (this.isAccepted() ? 79 : 97);
        return result;
    }

    @Generated
    public String toString() {
        return "RotationData(yaw=" + this.getYaw() + ", pitch=" + this.getPitch() + ", transaction=" + this.getTransaction() + ", isAccepted=" + this.isAccepted() + ")";
    }
}

