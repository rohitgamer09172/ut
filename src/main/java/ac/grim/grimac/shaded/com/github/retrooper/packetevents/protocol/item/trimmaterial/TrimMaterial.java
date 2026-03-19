/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimmaterial;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.armormaterial.ArmorMaterial;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.armormaterial.ArmorMaterials;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimmaterial.StaticTrimMaterial;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimmaterial.TrimMaterials;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface TrimMaterial
extends MappedEntity,
CopyableEntity<TrimMaterial>,
DeepComparableEntity {
    public static final float FALLBACK_ITEM_MODEL_INDEX = 0.0f;

    public String getAssetName();

    @ApiStatus.Obsolete
    public ItemType getIngredient();

    @ApiStatus.Obsolete
    public float getItemModelIndex();

    default public @Nullable String getArmorMaterialOverride(ArmorMaterial armorMaterial) {
        return this.getOverrideArmorMaterials().get(armorMaterial);
    }

    public Map<ArmorMaterial, String> getOverrideArmorMaterials();

    public Component getDescription();

    public static TrimMaterial read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntityOrDirect(TrimMaterials.getRegistry(), TrimMaterial::readDirect);
    }

    public static TrimMaterial readDirect(PacketWrapper<?> wrapper) {
        String assetName = wrapper.readString();
        ItemType ingredient = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) ? null : wrapper.readMappedEntity(ItemTypes::getById);
        float itemModelIndex = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_4) ? 0.0f : wrapper.readFloat();
        Map<ArmorMaterial, String> overrideArmorMaterials = wrapper.readMap(ew -> ew.readMappedEntity(ArmorMaterials::getById), PacketWrapper::readString);
        Component description = wrapper.readComponent();
        return new StaticTrimMaterial(assetName, ingredient, itemModelIndex, overrideArmorMaterials, description);
    }

    public static void write(PacketWrapper<?> wrapper, TrimMaterial material) {
        wrapper.writeMappedEntityOrDirect(material, TrimMaterial::writeDirect);
    }

    public static void writeDirect(PacketWrapper<?> wrapper, TrimMaterial material) {
        wrapper.writeString(material.getAssetName());
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
            wrapper.writeMappedEntity(material.getIngredient());
        }
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_4)) {
            wrapper.writeFloat(material.getItemModelIndex());
        }
        wrapper.writeMap(material.getOverrideArmorMaterials(), PacketWrapper::writeMappedEntity, PacketWrapper::writeString);
        wrapper.writeComponent(material.getDescription());
    }

    @Deprecated
    public static TrimMaterial decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        return TrimMaterial.decode(nbt, PacketWrapper.createDummyWrapper(version), data);
    }

    public static TrimMaterial decode(NBT nbt, PacketWrapper<?> wrapper, @Nullable TypesBuilderData data) {
        Map<ArmorMaterial, String> overrideArmorMaterials;
        NBTCompound compound = (NBTCompound)nbt;
        String assetName = compound.getStringTagValueOrThrow("asset_name");
        ItemType ingredient = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) ? null : ItemTypes.getByName(compound.getStringTagValueOrThrow("ingredient"));
        float itemModelIndex = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_4) ? 0.0f : compound.getNumberTagOrThrow("item_model_index").getAsFloat();
        NBTCompound overrideArmorMaterialsTag = compound.getCompoundTagOrNull("override_armor_materials");
        if (overrideArmorMaterialsTag != null) {
            overrideArmorMaterials = new HashMap();
            for (Map.Entry<String, NBT> entry : overrideArmorMaterialsTag.getTags().entrySet()) {
                ArmorMaterial material = ArmorMaterials.getByName(entry.getKey());
                String override = ((NBTString)entry.getValue()).getValue();
                overrideArmorMaterials.put(material, override);
            }
        } else {
            overrideArmorMaterials = Collections.emptyMap();
        }
        Component description = ((NBTCompound)nbt).getOrThrow("description", wrapper.getSerializers(), wrapper);
        return new StaticTrimMaterial(data, assetName, ingredient, itemModelIndex, overrideArmorMaterials, description);
    }

    @Deprecated
    public static NBT encode(TrimMaterial material, ClientVersion version) {
        return TrimMaterial.encode(PacketWrapper.createDummyWrapper(version), material);
    }

    public static NBT encode(PacketWrapper<?> wrapper, TrimMaterial material) {
        NBTCompound overrideArmorMaterialsTag;
        if (!material.getOverrideArmorMaterials().isEmpty()) {
            overrideArmorMaterialsTag = new NBTCompound();
            for (Map.Entry<ArmorMaterial, String> entry : material.getOverrideArmorMaterials().entrySet()) {
                String materialName = entry.getKey().getName().toString();
                NBTString overrideTag = new NBTString(entry.getValue());
                overrideArmorMaterialsTag.setTag(materialName, overrideTag);
            }
        } else {
            overrideArmorMaterialsTag = null;
        }
        NBTCompound compound = new NBTCompound();
        compound.setTag("asset_name", new NBTString(material.getAssetName()));
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
            compound.setTag("ingredient", new NBTString(material.getIngredient().getName().toString()));
        }
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_4)) {
            compound.setTag("item_model_index", new NBTFloat(material.getItemModelIndex()));
        }
        if (overrideArmorMaterialsTag != null) {
            compound.setTag("override_armor_materials", overrideArmorMaterialsTag);
        }
        compound.set("description", material.getDescription(), wrapper.getSerializers(), wrapper);
        return compound;
    }
}

