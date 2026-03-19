/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;
import java.util.Arrays;

public class NBTByteArray
extends NBT {
    protected final byte[] array;

    public NBTByteArray(byte[] array) {
        this.array = array;
    }

    public NBTType<NBTByteArray> getType() {
        return NBTType.BYTE_ARRAY;
    }

    public byte[] getValue() {
        return this.array;
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
        NBTByteArray other = (NBTByteArray)obj;
        return Arrays.equals(this.array, other.array);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.array);
    }

    @Override
    public NBTByteArray copy() {
        byte[] abyte = new byte[this.array.length];
        System.arraycopy(this.array, 0, abyte, 0, this.array.length);
        return new NBTByteArray(abyte);
    }

    @Override
    public String toString() {
        return "ByteArray(" + Arrays.toString(this.array) + ")";
    }
}

