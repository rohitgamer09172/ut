/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.serializer;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTLimiter;
import java.io.IOException;

public interface NBTReader<T extends NBT, IN> {
    default public T deserializeTag(NBTLimiter limiter, IN from) throws IOException {
        return this.deserializeTag(limiter, from, true);
    }

    public T deserializeTag(NBTLimiter var1, IN var2, boolean var3) throws IOException;
}

