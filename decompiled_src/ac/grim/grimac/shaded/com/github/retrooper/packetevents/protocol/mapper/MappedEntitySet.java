/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntityRefSet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistryHolder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class MappedEntitySet<T extends MappedEntity>
implements MappedEntityRefSet<T> {
    @Nullable
    private final ResourceLocation tagKey;
    @Nullable
    private final List<T> entities;

    public MappedEntitySet(ResourceLocation tagKey) {
        this(tagKey, null);
    }

    public MappedEntitySet(List<T> entities) {
        this(null, entities);
    }

    public MappedEntitySet(@Nullable ResourceLocation tagKey, @Nullable List<T> entities) {
        if (tagKey == null && entities == null) {
            throw new IllegalArgumentException("Illegal generic holder set: either tag key or holder ids have to be set");
        }
        this.tagKey = tagKey;
        this.entities = entities;
    }

    public static <Z extends MappedEntity> MappedEntitySet<Z> createEmpty() {
        return new MappedEntitySet(new ArrayList(0));
    }

    public static <Z extends MappedEntity> MappedEntityRefSet<Z> readRefSet(PacketWrapper<?> wrapper) {
        int count = wrapper.readVarInt() - 1;
        if (count == -1) {
            return new MappedEntitySet(wrapper.readIdentifier());
        }
        int[] entries = wrapper.readVarIntArrayOfSize(Math.min(count, 65536));
        return new IdRefSetImpl(entries);
    }

    public static void writeRefSet(PacketWrapper<?> wrapper, MappedEntityRefSet<?> refSet) {
        if (refSet instanceof IdRefSetImpl) {
            IdRefSetImpl idRefSet = (IdRefSetImpl)refSet;
            wrapper.writeVarInt(idRefSet.entries.length + 1);
            wrapper.writeVarIntArrayOfSize(idRefSet.entries);
        } else if (refSet instanceof MappedEntitySet) {
            MappedEntitySet.write(wrapper, (MappedEntitySet)refSet);
        } else {
            throw new UnsupportedOperationException("Unsupported mapped entity reference set implementation: " + refSet);
        }
    }

    public static <Z extends MappedEntity> MappedEntitySet<Z> read(PacketWrapper<?> wrapper, BiFunction<ClientVersion, Integer, Z> getter) {
        int count = wrapper.readVarInt() - 1;
        if (count == -1) {
            return new MappedEntitySet(wrapper.readIdentifier(), null);
        }
        ArrayList<Z> entities = new ArrayList<Z>(Math.min(count, 65536));
        for (int i = 0; i < count; ++i) {
            entities.add(wrapper.readMappedEntity(getter));
        }
        return new MappedEntitySet(null, entities);
    }

    public static <Z extends MappedEntity> void write(PacketWrapper<?> wrapper, MappedEntitySet<Z> set) {
        if (set.tagKey != null) {
            wrapper.writeVarInt(0);
            wrapper.writeIdentifier(set.tagKey);
            return;
        }
        assert (set.entities != null);
        wrapper.writeVarInt(set.entities.size() + 1);
        for (MappedEntity entity : set.entities) {
            wrapper.writeMappedEntity(entity);
        }
    }

    @Deprecated
    public static <Z extends MappedEntity> MappedEntitySet<Z> decode(NBT nbt, ClientVersion version, IRegistry<Z> registry) {
        return MappedEntitySet.decode(nbt, PacketWrapper.createDummyWrapper(version), registry);
    }

    public static <Z extends MappedEntity> MappedEntitySet<Z> decode(NBT nbt, PacketWrapper<?> wrapper, IRegistry<Z> registry) {
        ArrayList<Z> list;
        ClientVersion version = wrapper.getServerVersion().toClientVersion();
        if (nbt instanceof NBTString) {
            String singleEntry = ((NBTString)nbt).getValue();
            if (!singleEntry.isEmpty() && singleEntry.charAt(0) == '#') {
                String tagName = singleEntry.substring(1);
                ResourceLocation tagKey = new ResourceLocation(tagName);
                return new MappedEntitySet(tagKey);
            }
            list = new ArrayList(1);
            ResourceLocation key = new ResourceLocation(singleEntry);
            list.add(registry.getByNameOrThrow(version, key));
        } else {
            NBTList listTag = (NBTList)nbt;
            list = new ArrayList<Z>(listTag.size());
            for (NBT tag : listTag.getTags()) {
                ResourceLocation key = new ResourceLocation(((NBTString)tag).getValue());
                list.add(registry.getByNameOrThrow(version, key));
            }
        }
        return new MappedEntitySet(list);
    }

    @Deprecated
    public static <Z extends MappedEntity> NBT encode(MappedEntitySet<Z> set, ClientVersion version) {
        return MappedEntitySet.encodeRefSet(PacketWrapper.createDummyWrapper(version), set);
    }

    public static <Z extends MappedEntity> NBT encode(PacketWrapper<?> wrapper, MappedEntitySet<Z> set) {
        if (set.tagKey != null) {
            return new NBTString("#" + set.tagKey);
        }
        assert (set.entities != null);
        NBTList<NBTString> listTag = NBTList.createStringList();
        for (MappedEntity entity : set.entities) {
            listTag.addTag(new NBTString(entity.getName().toString()));
        }
        return listTag;
    }

    public static <Z extends MappedEntity> MappedEntityRefSet<Z> decodeRefSet(NBT nbt, PacketWrapper<?> wrapper) {
        return MappedEntitySet.decodeRefSet(nbt, wrapper.getServerVersion().toClientVersion());
    }

    @Deprecated
    public static <Z extends MappedEntity> MappedEntityRefSet<Z> decodeRefSet(NBT nbt, ClientVersion version) {
        ArrayList<String> list;
        if (nbt instanceof NBTString) {
            String singleEntry = ((NBTString)nbt).getValue();
            if (!singleEntry.isEmpty() && singleEntry.charAt(0) == '#') {
                String tagName = singleEntry.substring(1);
                ResourceLocation tagKey = new ResourceLocation(tagName);
                return new MappedEntitySet(tagKey);
            }
            list = Collections.singletonList(singleEntry);
        } else {
            NBTList listTag = (NBTList)nbt;
            list = new ArrayList(listTag.size());
            for (NBT tag : listTag.getTags()) {
                list.add(((NBTString)tag).getValue());
            }
        }
        return new NameRefSetImpl(list);
    }

    public static <Z extends MappedEntity> NBT encodeRefSet(PacketWrapper<?> wrapper, MappedEntityRefSet<Z> refSet) {
        return MappedEntitySet.encodeRefSet(refSet, wrapper.getServerVersion().toClientVersion());
    }

    @Deprecated
    public static <Z extends MappedEntity> NBT encodeRefSet(MappedEntityRefSet<Z> refSet, ClientVersion version) {
        if (refSet instanceof NameRefSetImpl) {
            NameRefSetImpl nameRefSet = (NameRefSetImpl)refSet;
            NBTList<NBTString> listTag = NBTList.createStringList();
            for (String entityName : nameRefSet.entries) {
                listTag.addTag(new NBTString(entityName));
            }
            return listTag;
        }
        if (refSet instanceof MappedEntitySet) {
            return MappedEntitySet.encode((MappedEntitySet)refSet, version);
        }
        throw new UnsupportedOperationException("Unsupported mapped entity reference set implementation: " + refSet);
    }

    @Override
    public MappedEntitySet<T> resolve(PacketWrapper<?> wrapper, IRegistry<T> registry) {
        return this;
    }

    @Override
    public MappedEntitySet<T> resolve(ClientVersion version, IRegistryHolder registryHolder, IRegistry<T> registry) {
        return this;
    }

    @Override
    public MappedEntitySet<T> resolve(ClientVersion version, IRegistry<T> registry) {
        return this;
    }

    @Override
    public boolean isEmpty() {
        return this.entities != null && this.entities.isEmpty();
    }

    @Nullable
    public ResourceLocation getTagKey() {
        return this.tagKey;
    }

    @Nullable
    public List<T> getEntities() {
        return this.entities;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MappedEntitySet)) {
            return false;
        }
        MappedEntitySet that = (MappedEntitySet)obj;
        if (!Objects.equals(this.tagKey, that.tagKey)) {
            return false;
        }
        return Objects.equals(this.entities, that.entities);
    }

    public int hashCode() {
        return Objects.hash(this.tagKey, this.entities);
    }

    public String toString() {
        return "MappedEntitySet{tagKey=" + this.tagKey + ", entities=" + this.entities + '}';
    }

    private static final class IdRefSetImpl<T extends MappedEntity>
    implements MappedEntityRefSet<T> {
        private final int[] entries;

        public IdRefSetImpl(int[] entries) {
            this.entries = entries;
        }

        @Override
        public MappedEntitySet<T> resolve(ClientVersion version, IRegistry<T> registry) {
            ArrayList<T> entities = new ArrayList<T>(this.entries.length);
            for (int entityId : this.entries) {
                entities.add(registry.getByIdOrThrow(version, entityId));
            }
            return new MappedEntitySet(entities);
        }

        @Override
        public boolean isEmpty() {
            return this.entries.length == 0;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof IdRefSetImpl)) {
                return false;
            }
            IdRefSetImpl idRefSet = (IdRefSetImpl)obj;
            return Arrays.equals(this.entries, idRefSet.entries);
        }

        public int hashCode() {
            return Arrays.hashCode(this.entries);
        }

        public String toString() {
            return "IdRefSetImpl{entries=" + Arrays.toString(this.entries) + '}';
        }
    }

    private static final class NameRefSetImpl<T extends MappedEntity>
    implements MappedEntityRefSet<T> {
        private final List<String> entries;

        public NameRefSetImpl(List<String> entries) {
            this.entries = entries;
        }

        @Override
        public MappedEntitySet<T> resolve(ClientVersion version, IRegistry<T> registry) {
            ArrayList<T> entities = new ArrayList<T>(this.entries.size());
            for (String entityName : this.entries) {
                entities.add(registry.getByNameOrThrow(version, entityName));
            }
            return new MappedEntitySet(entities);
        }

        @Override
        public boolean isEmpty() {
            return this.entries.isEmpty();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof NameRefSetImpl)) {
                return false;
            }
            NameRefSetImpl that = (NameRefSetImpl)obj;
            return this.entries.equals(that.entries);
        }

        public int hashCode() {
            return Objects.hashCode(this.entries);
        }

        public String toString() {
            return "NameRefSetImpl{entries=" + this.entries + '}';
        }
    }
}

