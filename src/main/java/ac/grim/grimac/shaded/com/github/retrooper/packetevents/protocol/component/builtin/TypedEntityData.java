/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TypedEntityData {
    private static final EntityType FALLBACK_TYPE = EntityTypes.PIG;
    private final EntityType type;
    private final NBTCompound compound;

    public TypedEntityData(NBTCompound compound) {
        this(FALLBACK_TYPE, compound);
    }

    public TypedEntityData(EntityType type, NBTCompound compound) {
        this.type = type;
        this.compound = compound;
    }

    public static TypedEntityData read(PacketWrapper<?> wrapper) {
        EntityType type = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_9) ? wrapper.readMappedEntity(EntityTypes.getRegistry()) : FALLBACK_TYPE;
        NBTCompound compound = wrapper.readNBT();
        return new TypedEntityData(type, compound);
    }

    public static void write(PacketWrapper<?> wrapper, TypedEntityData data) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
            wrapper.writeMappedEntity(data.type);
        }
        wrapper.writeNBT(data.compound);
    }

    public EntityType getType() {
        return this.type;
    }

    public NBTCompound getCompound() {
        return this.compound;
    }

    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        TypedEntityData that = (TypedEntityData)obj;
        if (!this.type.equals(that.type)) {
            return false;
        }
        return this.compound.equals(that.compound);
    }

    public int hashCode() {
        return Objects.hash(this.type, this.compound);
    }
}

