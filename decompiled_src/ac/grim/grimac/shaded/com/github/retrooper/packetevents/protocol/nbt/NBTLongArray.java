/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;
import java.util.Arrays;

public class NBTLongArray
extends NBT {
    protected final long[] array;

    public NBTLongArray(long[] array) {
        this.array = array;
    }

    public NBTType<NBTLongArray> getType() {
        return NBTType.LONG_ARRAY;
    }

    public long[] getValue() {
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
        NBTLongArray other = (NBTLongArray)obj;
        return Arrays.equals(this.array, other.array);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.array);
    }

    @Override
    public NBTLongArray copy() {
        long[] along = new long[this.array.length];
        System.arraycopy(this.array, 0, along, 0, this.array.length);
        return new NBTLongArray(along);
    }

    @Override
    public String toString() {
        return "LongArray(" + Arrays.toString(this.array) + ")";
    }
}

