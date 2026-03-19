/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.cat;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.cat.CatVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.cat.StaticCatVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface CatVariant
extends MappedEntity,
CopyableEntity<CatVariant>,
DeepComparableEntity {
    public ResourceLocation getAssetId();

    public static CatVariant read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntity(CatVariants.getRegistry());
    }

    public static void write(PacketWrapper<?> wrapper, CatVariant variant) {
        wrapper.writeMappedEntity(variant);
    }

    public static CatVariant decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound)nbt;
        ResourceLocation assetId = new ResourceLocation(compound.getStringTagValueOrThrow("asset_id"));
        return new StaticCatVariant(data, assetId);
    }

    public static NBT encode(CatVariant variant, ClientVersion version) {
        NBTCompound compound = new NBTCompound();
        compound.setTag("asset_id", new NBTString(variant.getAssetId().toString()));
        return compound;
    }
}

