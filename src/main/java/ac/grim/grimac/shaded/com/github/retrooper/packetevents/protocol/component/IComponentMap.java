/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentValue;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.StaticComponentMap;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Map;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface IComponentMap {
    public static StaticComponentMap decode(NBT nbt, PacketWrapper<?> wrapper, IRegistry<? extends ComponentType<?>> registry) {
        return IComponentMap.decode(nbt, wrapper.getServerVersion().toClientVersion(), registry);
    }

    @Deprecated
    public static StaticComponentMap decode(NBT nbt, ClientVersion version, IRegistry<? extends ComponentType<?>> registry) {
        NBTCompound compound = (NBTCompound)nbt;
        StaticComponentMap.Builder components = StaticComponentMap.builder();
        for (Map.Entry<String, NBT> entry : compound.getTags().entrySet()) {
            ComponentType<?> type = registry.getByName(entry.getKey());
            if (type == null) {
                throw new IllegalStateException("Unknown component type named " + entry.getKey() + " encountered");
            }
            Object value = type.decode(entry.getValue(), version);
            components.set(type, value);
        }
        return components.build();
    }

    public static NBT encode(PacketWrapper<?> wrapper, StaticComponentMap components) {
        return IComponentMap.encode(components, wrapper.getServerVersion().toClientVersion());
    }

    @Deprecated
    public static NBT encode(StaticComponentMap components, ClientVersion version) {
        NBTCompound compound = new NBTCompound();
        for (Map.Entry<ComponentType<?>, ?> entry : components.getDelegate().entrySet()) {
            String key = entry.getKey().getName().toString();
            NBT value = entry.getKey().encode(entry.getValue(), version);
            compound.setTag(key, value);
        }
        return compound;
    }

    default public <T> Optional<T> getOptional(ComponentType<T> type) {
        return Optional.ofNullable(this.get(type));
    }

    public boolean has(ComponentType<?> var1);

    @Contract(value="_, !null -> !null")
    @Nullable
    default public <T> T getOr(ComponentType<T> type, @Nullable T otherValue) {
        T value = this.get(type);
        if (value != null) {
            return value;
        }
        return otherValue;
    }

    @Nullable
    public <T> T get(ComponentType<T> var1);

    default public <T> void set(ComponentValue<T> component) {
        this.set(component.getType(), component.getValue());
    }

    default public <T> void set(ComponentType<T> type, @Nullable T value) {
        this.set(type, Optional.ofNullable(value));
    }

    default public <T> void unset(ComponentType<T> type) {
        this.set(type, Optional.empty());
    }

    public <T> void set(ComponentType<T> var1, Optional<T> var2);
}

