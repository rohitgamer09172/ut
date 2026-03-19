/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;

public class NBTByte
extends NBTNumber {
    protected final byte value;

    public NBTByte(byte value) {
        this.value = value;
    }

    public NBTByte(boolean value) {
        this((byte)(value ? 1 : 0));
    }

    public boolean getAsBool() {
        return this.value != 0;
    }

    public NBTType<NBTByte> getType() {
        return NBTType.BYTE;
    }

    @Override
    public Number getAsNumber() {
        return this.value;
    }

    @Override
    public byte getAsByte() {
        return this.value;
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
    public int hashCode() {
        return Byte.hashCode(this.value);
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
        NBTByte other = (NBTByte)obj;
        return this.value == other.value;
    }

    @Override
    public NBTByte copy() {
        return this;
    }

    @Override
    public String toString() {
        return "Byte(" + this.value + ")";
    }
}

