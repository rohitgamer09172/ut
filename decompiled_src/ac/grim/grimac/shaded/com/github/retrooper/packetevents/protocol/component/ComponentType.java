/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.function.Function;

public interface ComponentType<T>
extends MappedEntity {
    public T read(PacketWrapper<?> var1);

    public void write(PacketWrapper<?> var1, T var2);

    public T decode(NBT var1, ClientVersion var2);

    public NBT encode(T var1, ClientVersion var2);

    @ApiStatus.Internal
    public <Z> ComponentType<Z> legacyMap(Function<T, Z> var1, Function<Z, T> var2);

    public static interface Encoder<T> {
        public NBT encode(T var1, ClientVersion var2);
    }

    public static interface Decoder<T> {
        public T decode(NBT var1, ClientVersion var2);
    }
}

