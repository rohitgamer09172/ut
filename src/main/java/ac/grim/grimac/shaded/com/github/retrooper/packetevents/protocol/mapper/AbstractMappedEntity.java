/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class AbstractMappedEntity
implements MappedEntity {
    protected final @Nullable TypesBuilderData data;

    protected AbstractMappedEntity(@Nullable TypesBuilderData data) {
        this.data = data;
    }

    public @Nullable TypesBuilderData getRegistryData() {
        return this.data;
    }

    @Override
    public ResourceLocation getName() {
        if (this.data != null) {
            return this.data.getName();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public int getId(ClientVersion version) {
        if (this.data != null) {
            return this.data.getId(version);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRegistered() {
        return this.data != null;
    }

    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        AbstractMappedEntity that = (AbstractMappedEntity)obj;
        if (this.data != null && that.data != null) {
            return this.data.getName().equals(that.data.getName());
        }
        if (this instanceof DeepComparableEntity) {
            return ((DeepComparableEntity)((Object)this)).deepEquals(obj);
        }
        return false;
    }

    public int hashCode() {
        if (this.data != null) {
            return Objects.hash(this.getClass(), this.data.getName());
        }
        if (this instanceof DeepComparableEntity) {
            return ((DeepComparableEntity)((Object)this)).deepHashCode();
        }
        return System.identityHashCode(this);
    }

    public String toString() {
        return this.getClass().getSimpleName() + "[" + (this.data == null ? Integer.valueOf(this.hashCode()) : this.data.getName()) + ']';
    }
}

