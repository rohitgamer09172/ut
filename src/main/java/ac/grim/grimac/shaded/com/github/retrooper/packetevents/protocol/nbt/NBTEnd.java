/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;

public class NBTEnd
extends NBT {
    public static final NBTEnd INSTANCE = new NBTEnd();

    public NBTType<NBTEnd> getType() {
        return NBTType.END;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        return this.getClass() == obj.getClass();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public NBTEnd copy() {
        return this;
    }

    @Override
    public String toString() {
        return "END";
    }
}

