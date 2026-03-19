/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;
import java.util.Objects;

public class NBTString
extends NBT {
    protected final String string;

    public NBTString(String string) {
        this.string = string;
    }

    public NBTType<NBTString> getType() {
        return NBTType.STRING;
    }

    public String getValue() {
        return this.string;
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
        NBTString other = (NBTString)obj;
        return Objects.equals(this.string, other.string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.string);
    }

    @Override
    public NBTString copy() {
        return this;
    }

    @Override
    public String toString() {
        return "String(" + this.string + ")";
    }
}

