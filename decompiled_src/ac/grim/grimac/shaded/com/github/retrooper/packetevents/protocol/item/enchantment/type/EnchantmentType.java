/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.EnchantEffectComponentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.IComponentMap;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.StaticComponentMap;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.EnchantmentDefinition;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type.StaticEnchantmentType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntityRefSet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface EnchantmentType
extends MappedEntity,
CopyableEntity<EnchantmentType>,
DeepComparableEntity {
    public Component getDescription();

    public EnchantmentDefinition getDefinition();

    public MappedEntitySet<EnchantmentType> getExclusiveSet();

    public MappedEntityRefSet<EnchantmentType> getExclusiveRefSet();

    public StaticComponentMap getEffects();

    @Deprecated
    public static EnchantmentType decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        return EnchantmentType.decode(nbt, PacketWrapper.createDummyWrapper(version), data);
    }

    public static EnchantmentType decode(NBT nbt, PacketWrapper<?> wrapper, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound)nbt;
        Component description = compound.getOrThrow("description", wrapper.getSerializers(), wrapper);
        EnchantmentDefinition definition = EnchantmentDefinition.decode((NBT)compound, wrapper);
        MappedEntityRefSet exclusiveSet = Optional.ofNullable(compound.getTagOrNull("exclusive_set")).map(tag -> MappedEntitySet.decodeRefSet(tag, wrapper)).orElseGet(MappedEntitySet::createEmpty);
        StaticComponentMap effects = Optional.ofNullable(compound.getTagOrNull("effects")).map(tag -> IComponentMap.decode(tag, wrapper, EnchantEffectComponentTypes.getRegistry())).orElse(StaticComponentMap.EMPTY);
        return new StaticEnchantmentType(data, description, definition, exclusiveSet, effects);
    }

    @Deprecated
    public static NBT encode(EnchantmentType type, ClientVersion version) {
        return EnchantmentType.encode(type, PacketWrapper.createDummyWrapper(version));
    }

    public static NBT encode(EnchantmentType type, PacketWrapper<?> wrapper) {
        NBTCompound compound = new NBTCompound();
        EnchantmentDefinition.encode(compound, wrapper, type.getDefinition());
        compound.set("description", type.getDescription(), wrapper.getSerializers(), wrapper);
        if (!type.getExclusiveRefSet().isEmpty()) {
            compound.set("exclusive_set", type.getExclusiveRefSet(), MappedEntitySet::encodeRefSet, wrapper);
        }
        if (!type.getEffects().isEmpty()) {
            compound.set("effects", type.getEffects(), IComponentMap::encode, wrapper);
        }
        return compound;
    }
}

