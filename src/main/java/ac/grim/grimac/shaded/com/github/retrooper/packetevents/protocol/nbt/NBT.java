/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class NBT {
    public abstract NBTType<?> getType();

    public abstract boolean equals(Object var1);

    public abstract int hashCode();

    public String toString() {
        return "nbt";
    }

    public abstract NBT copy();

    public <T> T castOrThrow(Class<T> clazz) throws NbtCodecException {
        if (clazz.isInstance(this)) {
            return (T)this;
        }
        throw new NbtCodecException("expected: " + clazz.getName() + ", actual: " + this);
    }
}

