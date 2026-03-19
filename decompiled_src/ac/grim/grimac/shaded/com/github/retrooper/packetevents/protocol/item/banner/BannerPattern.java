/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.banner;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.banner.StaticBannerPattern;
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
public interface BannerPattern
extends MappedEntity,
CopyableEntity<BannerPattern>,
DeepComparableEntity {
    public ResourceLocation getAssetId();

    public String getTranslationKey();

    public static BannerPattern readDirect(PacketWrapper<?> wrapper) {
        ResourceLocation assetId = wrapper.readIdentifier();
        String translationKey = wrapper.readString();
        return new StaticBannerPattern(assetId, translationKey);
    }

    public static void writeDirect(PacketWrapper<?> wrapper, BannerPattern pattern) {
        wrapper.writeIdentifier(pattern.getAssetId());
        wrapper.writeString(pattern.getTranslationKey());
    }

    public static BannerPattern decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound)nbt;
        ResourceLocation assetId = new ResourceLocation(compound.getStringTagValueOrThrow("asset_id"));
        String translationKey = compound.getStringTagValueOrThrow("translation_key");
        return new StaticBannerPattern(data, assetId, translationKey);
    }

    public static NBT encode(BannerPattern bannerPattern, ClientVersion version) {
        NBTCompound compound = new NBTCompound();
        compound.setTag("asset_id", new NBTString(bannerPattern.getAssetId().toString()));
        compound.setTag("translation_key", new NBTString(bannerPattern.getTranslationKey()));
        return compound;
    }
}

