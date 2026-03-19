/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.serializer;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import java.io.IOException;

public interface NBTWriter<T extends NBT, OUT> {
    default public void serializeTag(OUT to, T tag) throws IOException {
        this.serializeTag(to, (NBT)tag, true);
    }

    public void serializeTag(OUT var1, NBT var2, boolean var3) throws IOException;
}

