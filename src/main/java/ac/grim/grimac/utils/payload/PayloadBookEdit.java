/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.payload;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.payload.Payload;

public record PayloadBookEdit(@NotNull ItemStack itemStack) implements Payload
{
    public PayloadBookEdit(byte[] data) {
        this(Payload.wrapper(data).readItemStack());
    }

    @Override
    public void write(PacketWrapper<?> wrapper) {
        wrapper.writeItemStack(this.itemStack);
    }
}

