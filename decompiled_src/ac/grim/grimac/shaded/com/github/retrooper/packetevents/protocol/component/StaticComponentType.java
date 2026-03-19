/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.function.Function;

public class StaticComponentType<T>
extends AbstractMappedEntity
implements ComponentType<T> {
    @Nullable
    private final PacketWrapper.Reader<T> reader;
    @Nullable
    private final PacketWrapper.Writer<T> writer;
    @Nullable
    private final ComponentType.Decoder<T> decoder;
    @Nullable
    private final ComponentType.Encoder<T> encoder;

    @ApiStatus.Internal
    public StaticComponentType(@Nullable TypesBuilderData data, @Nullable PacketWrapper.Reader<T> reader, @Nullable PacketWrapper.Writer<T> writer) {
        this(data, reader, writer, null, null);
    }

    @ApiStatus.Internal
    public StaticComponentType(@Nullable TypesBuilderData data, @Nullable ComponentType.Decoder<T> decoder, @Nullable ComponentType.Encoder<T> encoder) {
        this(data, null, null, decoder, encoder);
    }

    @ApiStatus.Internal
    public StaticComponentType(@Nullable TypesBuilderData data, @Nullable PacketWrapper.Reader<T> reader, @Nullable PacketWrapper.Writer<T> writer, @Nullable ComponentType.Decoder<T> decoder, @Nullable ComponentType.Encoder<T> encoder) {
        super(data);
        this.reader = reader;
        this.writer = writer;
        this.decoder = decoder;
        this.encoder = encoder;
    }

    @Override
    public T read(PacketWrapper<?> wrapper) {
        return this.reader != null ? (T)this.reader.apply(wrapper) : null;
    }

    @Override
    public void write(PacketWrapper<?> wrapper, T content) {
        if (this.writer != null) {
            this.writer.accept(wrapper, content);
        }
    }

    @Override
    public T decode(NBT nbt, ClientVersion version) {
        if (this.decoder != null) {
            return this.decoder.decode(nbt, version);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public NBT encode(T value, ClientVersion version) {
        if (this.encoder != null) {
            return this.encoder.encode(value, version);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public <Z> ComponentType<Z> legacyMap(Function<T, Z> mapper, Function<Z, T> unmapper) {
        PacketWrapper.Reader reader = this.reader != null ? wrapper -> mapper.apply(this.reader.apply((PacketWrapper)wrapper)) : null;
        PacketWrapper.Writer writer = this.writer != null ? (wrapper, value) -> this.writer.accept((PacketWrapper)wrapper, unmapper.apply(value)) : null;
        ComponentType.Decoder<Object> decoder = this.decoder != null ? (nbt, version) -> mapper.apply(this.decoder.decode(nbt, version)) : null;
        ComponentType.Encoder<Object> encoder = this.encoder != null ? (value, version) -> this.encoder.encode(unmapper.apply(value), version) : null;
        return new StaticComponentType<Object>(this.data, reader, writer, decoder, encoder);
    }
}

