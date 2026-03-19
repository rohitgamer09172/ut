/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.PlayerModelType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.kyori.adventure.text.object.PlayerHeadObjectContents;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class ItemProfile {
    private @Nullable String name;
    private @Nullable UUID id;
    private List<Property> properties;
    private SkinPatch skinPatch;

    public ItemProfile(@Nullable String name, @Nullable UUID id, List<Property> properties) {
        this(name, id, properties, SkinPatch.EMPTY);
    }

    public ItemProfile(@Nullable String name, @Nullable UUID id, List<Property> properties, SkinPatch skinPatch) {
        this.name = name;
        this.id = id;
        this.properties = properties;
        this.skinPatch = skinPatch;
    }

    public static ItemProfile read(PacketWrapper<?> wrapper) {
        String name;
        UUID id;
        boolean partial;
        boolean bl = partial = wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_9) || !wrapper.readBoolean();
        if (!partial) {
            id = wrapper.readUUID();
            name = wrapper.readString(16);
        } else {
            name = (String)wrapper.readOptional(ew -> ew.readString(16));
            id = (UUID)wrapper.readOptional(PacketWrapper::readUUID);
        }
        List<Property> properties = wrapper.readList(Property::read);
        SkinPatch skinPatch = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_9) ? SkinPatch.read(wrapper) : SkinPatch.EMPTY;
        return new ItemProfile(name, id, properties, skinPatch);
    }

    public static void write(PacketWrapper<?> wrapper, ItemProfile profile) {
        boolean partial;
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
            partial = profile.name == null || profile.id == null;
            wrapper.writeBoolean(!partial);
        } else {
            partial = true;
        }
        if (!partial) {
            wrapper.writeUUID(profile.id);
            wrapper.writeString(profile.name, 16);
        } else {
            wrapper.writeOptional(profile.name, (ew, name) -> ew.writeString((String)name, 16));
            wrapper.writeOptional(profile.id, PacketWrapper::writeUUID);
        }
        wrapper.writeList(profile.properties, Property::write);
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
            SkinPatch.write(wrapper, profile.skinPatch);
        }
    }

    public static ItemProfile decode(NBT nbt, PacketWrapper<?> wrapper) {
        if (nbt instanceof NBTString) {
            String name = ((NBTString)nbt).getValue();
            return new ItemProfile(name, null, new ArrayList<Property>());
        }
        NBTCompound compound = (NBTCompound)nbt;
        UUID id = compound.getOrNull("id", NbtCodecs.UUID, wrapper);
        String name = compound.getStringTagValueOrNull("name");
        List properties = compound.getOrSupply("properties", Property.PROPERTY_MAP, ArrayList::new, wrapper);
        SkinPatch patch = SkinPatch.decode(compound, wrapper);
        return new ItemProfile(name, id, properties, patch);
    }

    public static NBT encode(PacketWrapper<?> wrapper, ItemProfile profile) {
        NBTCompound compound = new NBTCompound();
        if (profile.id != null) {
            compound.set("id", profile.id, NbtCodecs.UUID, wrapper);
        }
        if (profile.name != null) {
            compound.setTag("name", new NBTString(profile.name));
        }
        if (!profile.properties.isEmpty()) {
            compound.set("properties", profile.properties, Property.PROPERTY_MAP, wrapper);
        }
        SkinPatch.encode(compound, wrapper, profile.skinPatch);
        return compound;
    }

    public static ItemProfile fromAdventure(PlayerHeadObjectContents headContents) {
        List<PlayerHeadObjectContents.ProfileProperty> advProps = headContents.profileProperties();
        ArrayList<Property> properties = new ArrayList<Property>(advProps.size());
        for (PlayerHeadObjectContents.ProfileProperty property : advProps) {
            properties.add(Property.fromAdventure(property));
        }
        return new ItemProfile(headContents.name(), headContents.id(), properties);
    }

    public List<PlayerHeadObjectContents.ProfileProperty> getAdventureProperties() {
        if (this.properties.isEmpty()) {
            return Collections.emptyList();
        }
        ArrayList<PlayerHeadObjectContents.ProfileProperty> properties = new ArrayList<PlayerHeadObjectContents.ProfileProperty>(this.properties.size());
        for (Property property : this.properties) {
            properties.add(property.asAdventure());
        }
        return Collections.unmodifiableList(properties);
    }

    public @Nullable String getName() {
        return this.name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public @Nullable UUID getId() {
        return this.id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }

    public void addProperty(Property property) {
        this.properties.add(property);
    }

    public List<Property> getProperties() {
        return this.properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public SkinPatch getSkinPatch() {
        return this.skinPatch;
    }

    public void setSkinPatch(SkinPatch skinPatch) {
        this.skinPatch = skinPatch;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ItemProfile)) {
            return false;
        }
        ItemProfile that = (ItemProfile)obj;
        if (!Objects.equals(this.name, that.name)) {
            return false;
        }
        if (!Objects.equals(this.id, that.id)) {
            return false;
        }
        if (!this.properties.equals(that.properties)) {
            return false;
        }
        return this.skinPatch.equals(that.skinPatch);
    }

    public int hashCode() {
        return Objects.hash(this.name, this.id, this.properties, this.skinPatch);
    }

    public static class SkinPatch {
        public static final SkinPatch EMPTY = new SkinPatch(null, null, null, null);
        private final @Nullable ResourceLocation body;
        private final @Nullable ResourceLocation cape;
        private final @Nullable ResourceLocation elytra;
        private final @Nullable PlayerModelType model;

        public SkinPatch(@Nullable ResourceLocation body, @Nullable ResourceLocation cape, @Nullable ResourceLocation elytra, @Nullable PlayerModelType model) {
            this.body = body;
            this.cape = cape;
            this.elytra = elytra;
            this.model = model;
        }

        public static SkinPatch read(PacketWrapper<?> wrapper) {
            ResourceLocation body = (ResourceLocation)wrapper.readOptional(ResourceLocation::read);
            ResourceLocation cape = (ResourceLocation)wrapper.readOptional(ResourceLocation::read);
            ResourceLocation elytra = (ResourceLocation)wrapper.readOptional(ResourceLocation::read);
            PlayerModelType model = (PlayerModelType)wrapper.readOptional(PlayerModelType::read);
            return new SkinPatch(body, cape, elytra, model);
        }

        public static void write(PacketWrapper<?> wrapper, SkinPatch patch) {
            wrapper.writeOptional(patch.body, ResourceLocation::write);
            wrapper.writeOptional(patch.cape, ResourceLocation::write);
            wrapper.writeOptional(patch.elytra, ResourceLocation::write);
            wrapper.writeOptional(patch.model, PlayerModelType::write);
        }

        public static SkinPatch decode(NBTCompound nbt, PacketWrapper<?> wrapper) {
            ResourceLocation body = nbt.getOrNull("texture", ResourceLocation.CODEC, wrapper);
            ResourceLocation cape = nbt.getOrNull("cape", ResourceLocation.CODEC, wrapper);
            ResourceLocation elytra = nbt.getOrNull("elytra", ResourceLocation.CODEC, wrapper);
            PlayerModelType model = nbt.getOrNull("model", PlayerModelType.CODEC, wrapper);
            return new SkinPatch(body, cape, elytra, model);
        }

        public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, SkinPatch patch) {
            if (patch.body != null) {
                compound.set("texture", patch.body, ResourceLocation.CODEC, wrapper);
            }
            if (patch.cape != null) {
                compound.set("cape", patch.cape, ResourceLocation.CODEC, wrapper);
            }
            if (patch.elytra != null) {
                compound.set("elytra", patch.elytra, ResourceLocation.CODEC, wrapper);
            }
            if (patch.model != null) {
                compound.set("model", patch.model, PlayerModelType.CODEC, wrapper);
            }
        }

        public @Nullable ResourceLocation getBody() {
            return this.body;
        }

        public @Nullable ResourceLocation getCape() {
            return this.cape;
        }

        public @Nullable ResourceLocation getElytra() {
            return this.elytra;
        }

        public @Nullable PlayerModelType getModel() {
            return this.model;
        }

        public boolean equals(Object obj) {
            if (obj == null || this.getClass() != obj.getClass()) {
                return false;
            }
            SkinPatch skinPatch = (SkinPatch)obj;
            if (!Objects.equals(this.body, skinPatch.body)) {
                return false;
            }
            if (!Objects.equals(this.cape, skinPatch.cape)) {
                return false;
            }
            if (!Objects.equals(this.elytra, skinPatch.elytra)) {
                return false;
            }
            return this.model == skinPatch.model;
        }

        public int hashCode() {
            return Objects.hash(this.body, this.cape, this.elytra, this.model);
        }
    }

    public static class Property {
        public static final NbtCodec<Property> CODEC = new NbtMapCodec<Property>(){

            @Override
            public Property decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
                String name = compound.getStringTagValueOrThrow("name");
                String value = compound.getStringTagValueOrThrow("value");
                String signature = compound.getStringTagValueOrNull("signature");
                return new Property(name, value, signature);
            }

            @Override
            public void encode(NBTCompound compound, PacketWrapper<?> wrapper, Property value) throws NbtCodecException {
                compound.setTag("name", new NBTString(value.getName()));
                compound.setTag("value", new NBTString(value.getValue()));
                if (value.getSignature() != null) {
                    compound.setTag("signature", new NBTString(value.getSignature()));
                }
            }
        }.codec();
        public static final NbtCodec<List<Property>> PROPERTY_MAP = new NbtCodec<List<Property>>(){
            private final NbtCodec<List<Property>> propertyList = CODEC.applyList();

            @Override
            public List<Property> decode(NBT nbt, PacketWrapper<?> wrapper) {
                if (nbt instanceof NBTCompound) {
                    Map<String, NBT> tags = ((NBTCompound)nbt).getTags();
                    ArrayList<Property> properties = new ArrayList<Property>(tags.size());
                    for (Map.Entry<String, NBT> entry : tags.entrySet()) {
                        for (String value : (List)NbtCodecs.STRING_LIST.decode(entry.getValue(), wrapper)) {
                            properties.add(new Property(entry.getKey(), value, null));
                        }
                    }
                    return properties;
                }
                return (List)this.propertyList.decode(nbt, wrapper);
            }

            @Override
            public NBT encode(PacketWrapper<?> wrapper, List<Property> value) {
                return this.propertyList.encode(wrapper, value);
            }
        };
        private String name;
        private String value;
        private @Nullable String signature;

        public Property(String name, String value, @Nullable String signature) {
            this.name = name;
            this.value = value;
            this.signature = signature;
        }

        public static Property read(PacketWrapper<?> wrapper) {
            String name = wrapper.readString(64);
            String value = wrapper.readString(Short.MAX_VALUE);
            String signature = (String)wrapper.readOptional(ew -> ew.readString(1024));
            return new Property(name, value, signature);
        }

        public static void write(PacketWrapper<?> wrapper, Property property) {
            wrapper.writeString(property.name, 64);
            wrapper.writeString(property.value, Short.MAX_VALUE);
            wrapper.writeOptional(property.signature, (ew, signature) -> ew.writeString((String)signature, 1024));
        }

        public static Property fromAdventure(PlayerHeadObjectContents.ProfileProperty property) {
            return new Property(property.name(), property.value(), property.signature());
        }

        public PlayerHeadObjectContents.ProfileProperty asAdventure() {
            return PlayerHeadObjectContents.property(this.name, this.value, this.signature);
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public @Nullable String getSignature() {
            return this.signature;
        }

        public void setSignature(@Nullable String signature) {
            this.signature = signature;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Property)) {
                return false;
            }
            Property property = (Property)obj;
            if (!this.name.equals(property.name)) {
                return false;
            }
            if (!this.value.equals(property.value)) {
                return false;
            }
            return Objects.equals(this.signature, property.signature);
        }

        public int hashCode() {
            return Objects.hash(this.name, this.value, this.signature);
        }
    }
}

