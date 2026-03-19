/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemAttributeModifiers;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.EnchantmentCost;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.AdventureIndexUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class EnchantmentDefinition {
    private final MappedEntitySet<ItemType> supportedItems;
    private final Optional<MappedEntitySet<ItemType>> primaryItems;
    private final int weight;
    private final int maxLevel;
    private final EnchantmentCost minCost;
    private final EnchantmentCost maxCost;
    private final int anvilCost;
    private final List<ItemAttributeModifiers.EquipmentSlotGroup> slots;

    public EnchantmentDefinition(MappedEntitySet<ItemType> supportedItems, Optional<MappedEntitySet<ItemType>> primaryItems, int weight, int maxLevel, EnchantmentCost minCost, EnchantmentCost maxCost, int anvilCost, List<ItemAttributeModifiers.EquipmentSlotGroup> slots) {
        this.supportedItems = supportedItems;
        this.primaryItems = primaryItems;
        this.weight = weight;
        this.maxLevel = maxLevel;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.anvilCost = anvilCost;
        this.slots = slots;
    }

    @Deprecated
    public static EnchantmentDefinition decode(NBT nbt, ClientVersion version) {
        return EnchantmentDefinition.decode(nbt, PacketWrapper.createDummyWrapper(version));
    }

    public static EnchantmentDefinition decode(NBT nbt, PacketWrapper<?> wrapper) {
        ArrayList<ItemAttributeModifiers.EquipmentSlotGroup> slots;
        NBTCompound compound = (NBTCompound)nbt;
        MappedEntitySet supportedItems = compound.getOrThrow("supported_items", (tag, ew) -> MappedEntitySet.decode(tag, ew, ItemTypes.getRegistry()), wrapper);
        Optional<MappedEntitySet<ItemType>> primaryItems = Optional.ofNullable(compound.getOrNull("primary_items", (tag, ew) -> MappedEntitySet.decode(tag, ew, ItemTypes.getRegistry()), wrapper));
        int weight = compound.getNumberTagOrThrow("weight").getAsInt();
        int maxLevel = compound.getNumberTagOrThrow("max_level").getAsInt();
        EnchantmentCost minCost = compound.getOrThrow("min_cost", EnchantmentCost::decode, wrapper);
        EnchantmentCost maxCost = compound.getOrThrow("max_cost", EnchantmentCost::decode, wrapper);
        int anvilCost = compound.getNumberTagOrThrow("anvil_cost").getAsInt();
        NBT slotsTag = compound.getTagOrThrow("slots");
        if (slotsTag instanceof NBTList) {
            NBTList slotsTagList = (NBTList)slotsTag;
            slots = new ArrayList(slotsTagList.size());
            for (NBT tag2 : slotsTagList.getTags()) {
                String slotGroupId = ((NBTString)tag2).getValue();
                slots.add(AdventureIndexUtil.indexValueOrThrow(ItemAttributeModifiers.EquipmentSlotGroup.ID_INDEX, slotGroupId));
            }
        } else {
            String slotGroupId = ((NBTString)slotsTag).getValue();
            ItemAttributeModifiers.EquipmentSlotGroup slotGroup = AdventureIndexUtil.indexValueOrThrow(ItemAttributeModifiers.EquipmentSlotGroup.ID_INDEX, slotGroupId);
            slots = Collections.singletonList(slotGroup);
        }
        return new EnchantmentDefinition(supportedItems, primaryItems, weight, maxLevel, minCost, maxCost, anvilCost, slots);
    }

    @Deprecated
    public static NBT encode(EnchantmentDefinition definition, ClientVersion version) {
        return EnchantmentDefinition.encode(PacketWrapper.createDummyWrapper(version), definition);
    }

    public static NBT encode(PacketWrapper<?> wrapper, EnchantmentDefinition definition) {
        NBTCompound compound = new NBTCompound();
        EnchantmentDefinition.encode(compound, wrapper, definition);
        return compound;
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, EnchantmentDefinition definition) {
        NBTList<NBTString> slotsTag = NBTList.createStringList();
        for (ItemAttributeModifiers.EquipmentSlotGroup slot : definition.slots) {
            slotsTag.addTag(new NBTString(slot.getId()));
        }
        compound.set("supported_items", definition.supportedItems, MappedEntitySet::encode, wrapper);
        definition.primaryItems.ifPresent(set -> compound.set("primary_items", set, MappedEntitySet::encode, wrapper));
        compound.setTag("weight", new NBTInt(definition.weight));
        compound.setTag("max_level", new NBTInt(definition.maxLevel));
        compound.set("min_cost", definition.minCost, EnchantmentCost::encode, wrapper);
        compound.set("max_cost", definition.maxCost, EnchantmentCost::encode, wrapper);
        compound.setTag("anvil_cost", new NBTInt(definition.anvilCost));
        compound.setTag("slots", slotsTag);
    }

    public MappedEntitySet<ItemType> getSupportedItems() {
        return this.supportedItems;
    }

    public Optional<MappedEntitySet<ItemType>> getPrimaryItems() {
        return this.primaryItems;
    }

    public int getWeight() {
        return this.weight;
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    public EnchantmentCost getMinCost() {
        return this.minCost;
    }

    public EnchantmentCost getMaxCost() {
        return this.maxCost;
    }

    public int getAnvilCost() {
        return this.anvilCost;
    }

    public List<ItemAttributeModifiers.EquipmentSlotGroup> getSlots() {
        return this.slots;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EnchantmentDefinition)) {
            return false;
        }
        EnchantmentDefinition that = (EnchantmentDefinition)obj;
        if (this.weight != that.weight) {
            return false;
        }
        if (this.maxLevel != that.maxLevel) {
            return false;
        }
        if (this.anvilCost != that.anvilCost) {
            return false;
        }
        if (!this.supportedItems.equals(that.supportedItems)) {
            return false;
        }
        if (!this.primaryItems.equals(that.primaryItems)) {
            return false;
        }
        if (!this.minCost.equals(that.minCost)) {
            return false;
        }
        if (!this.maxCost.equals(that.maxCost)) {
            return false;
        }
        return this.slots.equals(that.slots);
    }

    public int hashCode() {
        return Objects.hash(this.supportedItems, this.primaryItems, this.weight, this.maxLevel, this.minCost, this.maxCost, this.anvilCost, this.slots);
    }

    public String toString() {
        return "EnchantmentDefinition{supportedItems=" + this.supportedItems + ", primaryItems=" + this.primaryItems + ", weight=" + this.weight + ", maxLevel=" + this.maxLevel + ", minCost=" + this.minCost + ", maxCost=" + this.maxCost + ", anvilCost=" + this.anvilCost + ", slots=" + this.slots + '}';
    }
}

