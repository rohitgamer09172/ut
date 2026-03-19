/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant.StaticWolfVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant.WolfVariants;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.biome.Biome;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.biome.Biomes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface WolfVariant
extends MappedEntity,
CopyableEntity<WolfVariant>,
DeepComparableEntity {
    public ResourceLocation getWildTexture();

    public ResourceLocation getTameTexture();

    public ResourceLocation getAngryTexture();

    @ApiStatus.Obsolete
    public MappedEntitySet<Biome> getBiomes();

    public static WolfVariant read(PacketWrapper<?> wrapper) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) || wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21)) {
            return wrapper.readMappedEntity(WolfVariants.getRegistry());
        }
        return wrapper.readMappedEntityOrDirect(WolfVariants.getRegistry(), WolfVariant::readDirect);
    }

    public static void write(PacketWrapper<?> wrapper, WolfVariant variant) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) || wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21)) {
            wrapper.writeMappedEntity(variant);
        } else {
            wrapper.writeMappedEntityOrDirect(variant, WolfVariant::writeDirect);
        }
    }

    @ApiStatus.Obsolete
    public static WolfVariant readDirect(PacketWrapper<?> wrapper) {
        ResourceLocation wildTexture = wrapper.readIdentifier();
        ResourceLocation tameTexture = wrapper.readIdentifier();
        ResourceLocation angryTexture = wrapper.readIdentifier();
        MappedEntitySet<Biome> biomes = MappedEntitySet.read(wrapper, Biomes.getRegistry());
        return new StaticWolfVariant(wildTexture, tameTexture, angryTexture, biomes);
    }

    @ApiStatus.Obsolete
    public static void writeDirect(PacketWrapper<?> wrapper, WolfVariant variant) {
        wrapper.writeIdentifier(variant.getWildTexture());
        wrapper.writeIdentifier(variant.getTameTexture());
        wrapper.writeIdentifier(variant.getAngryTexture());
        MappedEntitySet.write(wrapper, variant.getBiomes());
    }

    @Deprecated
    public static WolfVariant decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        return WolfVariant.decode(nbt, PacketWrapper.createDummyWrapper(version), data);
    }

    public static WolfVariant decode(NBT nbt, PacketWrapper<?> wrapper, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound)nbt;
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
            ResourceLocation wildTexture = new ResourceLocation(compound.getStringTagValueOrThrow("wild_texture"));
            ResourceLocation tameTexture = new ResourceLocation(compound.getStringTagValueOrThrow("tame_texture"));
            ResourceLocation angryTexture = new ResourceLocation(compound.getStringTagValueOrThrow("angry_texture"));
            MappedEntitySet biomes = compound.getOrThrow("biomes", (tag, ew) -> MappedEntitySet.decode(tag, ew, Biomes.getRegistry()), wrapper);
            return new StaticWolfVariant(data, wildTexture, tameTexture, angryTexture, biomes);
        }
        NBTCompound assets = compound.getCompoundTagOrThrow("assets");
        ResourceLocation wildTexture = new ResourceLocation(assets.getStringTagValueOrThrow("wild"));
        ResourceLocation tameTexture = new ResourceLocation(assets.getStringTagValueOrThrow("tame"));
        ResourceLocation angryTexture = new ResourceLocation(assets.getStringTagValueOrThrow("angry"));
        return new StaticWolfVariant(data, wildTexture, tameTexture, angryTexture, MappedEntitySet.createEmpty());
    }

    @Deprecated
    public static NBT encode(WolfVariant variant, ClientVersion version) {
        return WolfVariant.encode(PacketWrapper.createDummyWrapper(version), variant);
    }

    public static NBT encode(PacketWrapper<?> wrapper, WolfVariant variant) {
        NBTCompound compound = new NBTCompound();
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
            compound.setTag("wild_texture", new NBTString(variant.getWildTexture().toString()));
            compound.setTag("tame_texture", new NBTString(variant.getTameTexture().toString()));
            compound.setTag("angry_texture", new NBTString(variant.getAngryTexture().toString()));
            compound.set("biomes", variant.getBiomes(), MappedEntitySet::encode, wrapper);
        } else {
            NBTCompound assets = new NBTCompound();
            assets.setTag("wild", new NBTString(variant.getWildTexture().toString()));
            assets.setTag("tame", new NBTString(variant.getWildTexture().toString()));
            assets.setTag("angry", new NBTString(variant.getWildTexture().toString()));
            compound.setTag("assets", assets);
        }
        return compound;
    }
}

