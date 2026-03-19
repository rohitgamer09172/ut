/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Objects;

@ApiStatus.Experimental
public final class MaybeMappedEntity<T extends MappedEntity> {
    @Nullable
    private final T entity;
    @Nullable
    private final ResourceLocation name;
    @Nullable
    private final IRegistry<T> registry;

    public MaybeMappedEntity(T entity) {
        this(entity, null, null);
    }

    public MaybeMappedEntity(ResourceLocation name) {
        this(name, (IRegistry<T>)null);
    }

    public MaybeMappedEntity(ResourceLocation name, @Nullable IRegistry<T> registry) {
        this(null, name, registry);
    }

    public MaybeMappedEntity(@Nullable T entity, @Nullable ResourceLocation name) {
        this(entity, name, null);
    }

    public MaybeMappedEntity(@Nullable T entity, @Nullable ResourceLocation name, @Nullable IRegistry<T> registry) {
        if (entity == null && name == null) {
            throw new IllegalArgumentException("Only one of entity and name is allowed to be null");
        }
        this.entity = entity;
        this.name = name;
        this.registry = registry;
    }

    public static <T extends MappedEntity> MaybeMappedEntity<T> read(PacketWrapper<?> wrapper, IRegistry<T> registry, PacketWrapper.Reader<T> reader) {
        if (wrapper.readBoolean()) {
            return new MaybeMappedEntity<MappedEntity>((MappedEntity)reader.apply(wrapper));
        }
        ClientVersion version = wrapper.getServerVersion().toClientVersion();
        IRegistry<T> replacedRegistry = wrapper.getRegistryHolder().getRegistryOr(registry, version);
        return new MaybeMappedEntity<T>(wrapper.readIdentifier(), replacedRegistry);
    }

    public static <T extends MappedEntity> void write(PacketWrapper<?> wrapper, MaybeMappedEntity<T> entity, PacketWrapper.Writer<T> writer) {
        if (entity.entity != null) {
            wrapper.writeBoolean(true);
            writer.accept(wrapper, entity.entity);
        } else {
            wrapper.writeBoolean(false);
            wrapper.writeIdentifier(entity.name);
        }
    }

    public T getValueOrThrow() {
        T value = this.getValue();
        if (value == null) {
            throw new IllegalStateException("Can't resolve entity by name " + this.name);
        }
        return value;
    }

    @Nullable
    public T getValue() {
        if (this.entity != null) {
            return this.entity;
        }
        if (this.registry != null && this.name != null) {
            return this.registry.getByName(this.name);
        }
        return null;
    }

    public ResourceLocation getName() {
        if (this.name != null) {
            return this.name;
        }
        if (this.entity != null) {
            return this.entity.getName();
        }
        throw new AssertionError();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof MaybeMappedEntity)) {
            return false;
        }
        MaybeMappedEntity that = (MaybeMappedEntity)obj;
        if (!Objects.equals(this.entity, that.entity)) {
            return false;
        }
        return Objects.equals(this.name, that.name);
    }

    public int hashCode() {
        return Objects.hash(this.entity, this.name);
    }
}

