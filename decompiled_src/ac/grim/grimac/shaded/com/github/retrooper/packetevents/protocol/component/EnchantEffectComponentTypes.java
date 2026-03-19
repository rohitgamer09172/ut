/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.StaticComponentType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public final class EnchantEffectComponentTypes {
    private static final VersionedRegistry<ComponentType<?>> REGISTRY = new VersionedRegistry("enchantment_effect_component_type");
    public static ComponentType<NBT> DAMAGE_PROTECTION = EnchantEffectComponentTypes.define("damage_protection", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> DAMAGE_IMMUNITY = EnchantEffectComponentTypes.define("damage_immunity", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> DAMAGE = EnchantEffectComponentTypes.define("damage", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> SMASH_DAMAGE_PER_FALLEN_BLOCK = EnchantEffectComponentTypes.define("smash_damage_per_fallen_block", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> KNOCKBACK = EnchantEffectComponentTypes.define("knockback", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> ARMOR_EFFECTIVENESS = EnchantEffectComponentTypes.define("armor_effectiveness", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> POST_ATTACK = EnchantEffectComponentTypes.define("post_attack", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> HIT_BLOCK = EnchantEffectComponentTypes.define("hit_block", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> ITEM_DAMAGE = EnchantEffectComponentTypes.define("item_damage", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> ATTRIBUTES = EnchantEffectComponentTypes.define("attributes", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> EQUIPMENT_DROPS = EnchantEffectComponentTypes.define("equipment_drops", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> LOCATION_CHANGED = EnchantEffectComponentTypes.define("location_changed", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> TICK = EnchantEffectComponentTypes.define("tick", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> AMMO_USE = EnchantEffectComponentTypes.define("ammo_use", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> PROJECTILE_PIERCING = EnchantEffectComponentTypes.define("projectile_piercing", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> PROJECTILE_SPAWNED = EnchantEffectComponentTypes.define("projectile_spawned", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> PROJECTILE_SPREAD = EnchantEffectComponentTypes.define("projectile_spread", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> PROJECTILE_COUNT = EnchantEffectComponentTypes.define("projectile_count", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> TRIDENT_RETURN_ACCELERATION = EnchantEffectComponentTypes.define("trident_return_acceleration", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> FISHING_TIME_REDUCTION = EnchantEffectComponentTypes.define("fishing_time_reduction", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> FISHING_LUCK_BONUS = EnchantEffectComponentTypes.define("fishing_luck_bonus", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> BLOCK_EXPERIENCE = EnchantEffectComponentTypes.define("block_experience", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> MOB_EXPERIENCE = EnchantEffectComponentTypes.define("mob_experience", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> REPAIR_WITH_XP = EnchantEffectComponentTypes.define("repair_with_xp", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> CROSSBOW_CHARGE_TIME = EnchantEffectComponentTypes.define("crossbow_charge_time", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> CROSSBOW_CHARGING_SOUNDS = EnchantEffectComponentTypes.define("crossbow_charging_sounds", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> TRIDENT_SOUND = EnchantEffectComponentTypes.define("trident_sound", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> PREVENT_EQUIPMENT_DROP = EnchantEffectComponentTypes.define("prevent_equipment_drop", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> PREVENT_ARMOR_CHANGE = EnchantEffectComponentTypes.define("prevent_armor_change", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> TRIDENT_SPIN_ATTACK_STRENGTH = EnchantEffectComponentTypes.define("trident_spin_attack_strength", (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> POST_PIERCING_ATTACK = EnchantEffectComponentTypes.define("post_piercing_attack", (nbt, version) -> nbt, (val, version) -> val);

    private EnchantEffectComponentTypes() {
    }

    @ApiStatus.Internal
    public static <T> ComponentType<T> define(String key) {
        return EnchantEffectComponentTypes.define(key, null, null);
    }

    @ApiStatus.Internal
    public static <T> ComponentType<T> define(String key, @Nullable ComponentType.Decoder<T> reader, @Nullable ComponentType.Encoder<T> writer) {
        return REGISTRY.define(key, data -> new StaticComponentType((TypesBuilderData)data, reader, writer));
    }

    public static VersionedRegistry<ComponentType<?>> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}

