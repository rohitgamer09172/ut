/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTByteArray;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTDouble;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTEnd;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTIntArray;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTLong;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTLongArray;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTShort;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import java.util.Objects;

public class NBTType<T extends NBT> {
    public static final NBTType<NBTEnd> END = new NBTType<NBTEnd>(NBTEnd.class);
    public static final NBTType<NBTByte> BYTE = new NBTType<NBTByte>(NBTByte.class);
    public static final NBTType<NBTShort> SHORT = new NBTType<NBTShort>(NBTShort.class);
    public static final NBTType<NBTInt> INT = new NBTType<NBTInt>(NBTInt.class);
    public static final NBTType<NBTLong> LONG = new NBTType<NBTLong>(NBTLong.class);
    public static final NBTType<NBTFloat> FLOAT = new NBTType<NBTFloat>(NBTFloat.class);
    public static final NBTType<NBTDouble> DOUBLE = new NBTType<NBTDouble>(NBTDouble.class);
    public static final NBTType<NBTByteArray> BYTE_ARRAY = new NBTType<NBTByteArray>(NBTByteArray.class);
    public static final NBTType<NBTString> STRING = new NBTType<NBTString>(NBTString.class);
    public static final NBTType<NBTList> LIST = new NBTType<NBTList>(NBTList.class);
    public static final NBTType<NBTCompound> COMPOUND = new NBTType<NBTCompound>(NBTCompound.class);
    public static final NBTType<NBTIntArray> INT_ARRAY = new NBTType<NBTIntArray>(NBTIntArray.class);
    public static final NBTType<NBTLongArray> LONG_ARRAY = new NBTType<NBTLongArray>(NBTLongArray.class);
    protected final Class<T> clazz;

    protected NBTType(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Class<T> getNBTClass() {
        return this.clazz;
    }

    public String toString() {
        return this.clazz.getSimpleName();
    }

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
        NBTType other = (NBTType)obj;
        return Objects.equals(this.clazz, other.clazz);
    }

    public int hashCode() {
        return this.clazz.hashCode();
    }
}

