/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;

public class NBTLong
extends NBTNumber {
    protected final long value;

    public NBTLong(long value) {
        this.value = value;
    }

    public NBTType<NBTLong> getType() {
        return NBTType.LONG;
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
        return (short)this.value;
    }

    @Override
    public int getAsInt() {
        return (int)this.value;
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
        NBTLong other = (NBTLong)obj;
        return this.value == other.value;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.value);
    }

    @Override
    public NBTLong copy() {
        return this;
    }

    @Override
    public String toString() {
        return "Long(" + this.value + ")";
    }
}

