/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.payload;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.payload.Payload;

public record PayloadItemName(@NotNull String itemName) implements Payload
{
    public PayloadItemName(byte[] data) {
        this(Payload.wrapper(data).readString());
    }

    @Override
    public void write(PacketWrapper<?> wrapper) {
        wrapper.writeString(this.itemName);
    }
}

