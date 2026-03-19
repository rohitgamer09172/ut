/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;
import java.util.Arrays;

public class NBTIntArray
extends NBT {
    protected final int[] array;

    public NBTIntArray(int[] array) {
        this.array = array;
    }

    public NBTType<NBTIntArray> getType() {
        return NBTType.INT_ARRAY;
    }

    public int[] getValue() {
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
        NBTIntArray other = (NBTIntArray)obj;
        return Arrays.equals(this.array, other.array);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.array);
    }

    @Override
    public NBTIntArray copy() {
        int[] aint = new int[this.array.length];
        System.arraycopy(this.array, 0, aint, 0, this.array.length);
        return new NBTIntArray(aint);
    }

    @Override
    public String toString() {
        return "IntArray(" + Arrays.toString(this.array) + ")";
    }
}

