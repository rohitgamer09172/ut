/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimpattern;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimpattern.StaticTrimPattern;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimpattern.TrimPatterns;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface TrimPattern
extends MappedEntity,
CopyableEntity<TrimPattern>,
DeepComparableEntity {
    public ResourceLocation getAssetId();

    @ApiStatus.Obsolete
    public ItemType getTemplateItem();

    public Component getDescription();

    public boolean isDecal();

    public static TrimPattern read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntityOrDirect(TrimPatterns.getRegistry(), TrimPattern::readDirect);
    }

    public static TrimPattern readDirect(PacketWrapper<?> wrapper) {
        ResourceLocation assetId = wrapper.readIdentifier();
        ItemType templateItem = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) ? null : wrapper.readMappedEntity(ItemTypes::getById);
        Component description = wrapper.readComponent();
        boolean decal = wrapper.readBoolean();
        return new StaticTrimPattern(assetId, templateItem, description, decal);
    }

    public static void write(PacketWrapper<?> wrapper, TrimPattern pattern) {
        wrapper.writeMappedEntityOrDirect(pattern, TrimPattern::writeDirect);
    }

    public static void writeDirect(PacketWrapper<?> wrapper, TrimPattern pattern) {
        wrapper.writeIdentifier(pattern.getAssetId());
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
            wrapper.writeMappedEntity(pattern.getTemplateItem());
        }
        wrapper.writeComponent(pattern.getDescription());
        wrapper.writeBoolean(pattern.isDecal());
    }

    @Deprecated
    public static TrimPattern decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        return TrimPattern.decode(nbt, PacketWrapper.createDummyWrapper(version), data);
    }

    public static TrimPattern decode(NBT nbt, PacketWrapper<?> wrapper, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound)nbt;
        ResourceLocation assetId = new ResourceLocation(compound.getStringTagValueOrThrow("asset_id"));
        ItemType templateItem = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) ? null : ItemTypes.getByName(compound.getStringTagValueOrThrow("template_item"));
        Component description = compound.getOrThrow("description", wrapper.getSerializers(), wrapper);
        boolean decal = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_2) && compound.getBoolean("decal");
        return new StaticTrimPattern(data, assetId, templateItem, description, decal);
    }

    @Deprecated
    public static NBT encode(TrimPattern pattern, ClientVersion version) {
        return TrimPattern.encode(PacketWrapper.createDummyWrapper(version), pattern);
    }

    public static NBT encode(PacketWrapper<?> wrapper, TrimPattern pattern) {
        NBTCompound compound = new NBTCompound();
        compound.setTag("asset_id", new NBTString(pattern.getAssetId().toString()));
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
            compound.setTag("template_item", new NBTString(pattern.getTemplateItem().getName().toString()));
        }
        compound.set("description", pattern.getDescription(), wrapper.getSerializers(), wrapper);
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_2)) {
            compound.setTag("decal", new NBTByte(pattern.isDecal()));
        }
        return compound;
    }
}

