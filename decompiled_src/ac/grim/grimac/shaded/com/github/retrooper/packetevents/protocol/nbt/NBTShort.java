/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;

public class NBTShort
extends NBTNumber {
    protected final short value;

    public NBTShort(short value) {
        this.value = value;
    }

    public NBTType<NBTShort> getType() {
        return NBTType.SHORT;
    }

    @Override
    public Number getAsNumber() {
        return this.value;
    }

    @Override
    public byte getAsByte() {
        return (byte)this.value;
    }

    @Override
    public short getAsShort() {
        return this.value;
    }

    @Override
    public int getAsInt() {
        return this.value;
    }

    @Override
    public long getAsLong() {
        return this.value;
    }

    @Override
    public float getAsFloat() {
        return this.value;
    }

    @Override
    public double getAsDouble() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        NBTShort other = (NBTShort)obj;
        return this.value == other.value;
    }

    @Override
    public int hashCode() {
        return Short.hashCode(this.value);
    }

    @Override
    public NBTShort copy() {
        return this;
    }

    @Override
    public String toString() {
        return "Short(" + this.value + ")";
    }
}

