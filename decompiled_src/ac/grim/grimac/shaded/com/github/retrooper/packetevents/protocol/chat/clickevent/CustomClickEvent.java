/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.ClickEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.ClickEventAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.ClickEventActions;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTEnd;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.NbtTagHolder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class CustomClickEvent
implements ClickEvent {
    private final ResourceLocation id;
    private final @Nullable NBT payload;

    public CustomClickEvent(ResourceLocation id, @Nullable NBT payload) {
        this.id = id;
        this.payload = payload;
    }

    public static CustomClickEvent decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        ResourceLocation id = compound.getOrThrow("id", ResourceLocation::decode, wrapper);
        NBT payload = compound.getTagOrNull("payload");
        return new CustomClickEvent(id, payload);
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, CustomClickEvent clickEvent) {
        compound.set("id", clickEvent.id, ResourceLocation::encode, wrapper);
        if (clickEvent.payload != null) {
            compound.setTag("payload", clickEvent.payload);
        }
    }

    @Override
    public ClickEventAction<?> getAction() {
        return ClickEventActions.CUSTOM;
    }

    @Override
    public ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent asAdventure() {
        return ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent.custom(this.id.key(), new NbtTagHolder(this.payload != null ? this.payload : NBTEnd.INSTANCE));
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public @Nullable NBT getPayload() {
        return this.payload;
    }
}

