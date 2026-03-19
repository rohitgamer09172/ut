/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.ClickEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.ClickEventAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapDecoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapEncoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class StaticClickEventAction<T extends ClickEvent>
extends AbstractMappedEntity
implements ClickEventAction<T> {
    private final boolean allowFromServer;
    private final NbtMapDecoder<T> decoder;
    private final NbtMapEncoder<T> encoder;

    @ApiStatus.Internal
    public StaticClickEventAction(@Nullable TypesBuilderData data, boolean allowFromServer, NbtMapDecoder<T> decoder, NbtMapEncoder<T> encoder) {
        super(data);
        this.allowFromServer = allowFromServer;
        this.decoder = decoder;
        this.encoder = encoder;
    }

    @Override
    public boolean isAllowFromServer() {
        return this.allowFromServer;
    }

    @Override
    public T decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        return (T)((ClickEvent)this.decoder.decode(compound, wrapper));
    }

    @Override
    public void encode(NBTCompound compound, PacketWrapper<?> wrapper, T clickEvent) {
        this.encoder.encode(compound, wrapper, clickEvent);
    }
}

