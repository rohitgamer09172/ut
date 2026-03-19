/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attribute;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.StaticAttribute;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public final class Attributes {
    private static final VersionedRegistry<Attribute> REGISTRY = new VersionedRegistry("attribute");
    public static final Attribute ARMOR = Attributes.define("armor", "generic", 0.0, 0.0, 30.0);
    public static final Attribute ARMOR_TOUGHNESS = Attributes.define("armor_toughness", "generic", 0.0, 0.0, 20.0);
    public static final Attribute ATTACK_DAMAGE = Attributes.define("attack_damage", "generic", 2.0, 0.0, 2048.0);
    public static final Attribute ATTACK_KNOCKBACK = Attributes.define("attack_knockback", "generic", 0.0, 0.0, 5.0);
    public static final Attribute ATTACK_SPEED = Attributes.define("attack_speed", "generic", 4.0, 0.0, 1024.0);
    public static final Attribute FLYING_SPEED = Attributes.define("flying_speed", "generic", 0.4, 0.0, 1024.0);
    public static final Attribute FOLLOW_RANGE = Attributes.define("follow_range", "generic", 32.0, 0.0, 2048.0);
    public static final Attribute KNOCKBACK_RESISTANCE = Attributes.define("knockback_resistance", "generic", 0.0, 0.0, 1.0);
    public static final Attribute LUCK = Attributes.define("luck", "generic", 0.0, -1024.0, 1024.0);
    public static final Attribute MAX_HEALTH = Attributes.define("max_health", "generic", 20.0, 1.0, 1024.0);
    public static final Attribute MOVEMENT_SPEED = Attributes.define("movement_speed", "generic", 0.7, 0.0, 1024.0);
    public static final Attribute SPAWN_REINFORCEMENTS = Attributes.define("spawn_reinforcements", "zombie", 0.0, 0.0, 1.0);
    public static final Attribute MAX_ABSORPTION = Attributes.define("max_absorption", "generic", 0.0, 0.0, 2048.0);
    public static final Attribute BLOCK_BREAK_SPEED = Attributes.define("block_break_speed", "player", 1.0, 0.0, 1024.0);
    public static final Attribute BLOCK_INTERACTION_RANGE = Attributes.define("block_interaction_range", "player", 4.5, 0.0, 64.0);
    public static final Attribute ENTITY_INTERACTION_RANGE = Attributes.define("entity_interaction_range", "player", 3.0, 0.0, 64.0);
    public static final Attribute FALL_DAMAGE_MULTIPLIER = Attributes.define("fall_damage_multiplier", "generic", 1.0, 0.0, 100.0);
    public static final Attribute GRAVITY = Attributes.define("gravity", "generic", 0.08, -1.0, 1.0);
    public static final Attribute JUMP_STRENGTH = Attributes.define("jump_strength", "generic", 0.42, 0.0, 32.0);
    public static final Attribute SAFE_FALL_DISTANCE = Attributes.define("safe_fall_distance", "generic", 3.0, 0.0, 1024.0);
    public static final Attribute SCALE = Attributes.define("scale", "generic", 1.0, 0.0625, 16.0);
    public static final Attribute STEP_HEIGHT = Attributes.define("step_height", "generic", 0.6, 0.0, 10.0);
    public static final Attribute BURNING_TIME = Attributes.define("burning_time", "generic", 0.0, 1.0, 1024.0);
    public static final Attribute EXPLOSION_KNOCKBACK_RESISTANCE = Attributes.define("explosion_knockback_resistance", "generic", 0.0, 0.0, 1.0);
    public static final Attribute MINING_EFFICIENCY = Attributes.define("mining_efficiency", "player", 0.0, 0.0, 1024.0);
    public static final Attribute MOVEMENT_EFFICIENCY = Attributes.define("movement_efficiency", "generic", 0.0, 0.0, 1.0);
    public static final Attribute OXYGEN_BONUS = Attributes.define("oxygen_bonus", "generic", 0.0, 0.0, 1024.0);
    public static final Attribute SNEAKING_SPEED = Attributes.define("sneaking_speed", "player", 0.3, 0.0, 1.0);
    public static final Attribute SUBMERGED_MINING_SPEED = Attributes.define("submerged_mining_speed", "player", 0.2, 0.0, 20.0);
    public static final Attribute SWEEPING_DAMAGE_RATIO = Attributes.define("sweeping_damage_ratio", "player", 0.0, 0.0, 1.0);
    public static final Attribute WATER_MOVEMENT_EFFICIENCY = Attributes.define("water_movement_efficiency", "generic", 0.0, 0.0, 1.0);
    public static final Attribute TEMPT_RANGE = Attributes.define("tempt_range", null, 10.0, 0.0, 2048.0);
    public static final Attribute CAMERA_DISTANCE = Attributes.define("camera_distance", null, 4.0, 0.0, 32.0);
    public static final Attribute WAYPOINT_TRANSMIT_RANGE = Attributes.define("waypoint_transmit_range", null, 0.0, 0.0, 6.0E7);
    public static final Attribute WAYPOINT_RECEIVE_RANGE = Attributes.define("waypoint_receive_range", null, 0.0, 0.0, 6.0E7);
    @ApiStatus.Obsolete
    public static final Attribute HORSE_JUMP_STRENGTH = Attributes.define("horse.jump_strength", null, 0.7, 0.0, 2.0);
    @Deprecated
    public static final Attribute GENERIC_ARMOR = ARMOR;
    @Deprecated
    public static final Attribute GENERIC_ARMOR_TOUGHNESS = ARMOR_TOUGHNESS;
    @Deprecated
    public static final Attribute GENERIC_ATTACK_DAMAGE = ATTACK_DAMAGE;
    @Deprecated
    public static final Attribute GENERIC_ATTACK_KNOCKBACK = ATTACK_KNOCKBACK;
    @Deprecated
    public static final Attribute GENERIC_ATTACK_SPEED = ATTACK_SPEED;
    @Deprecated
    public static final Attribute GENERIC_FLYING_SPEED = FLYING_SPEED;
    @Deprecated
    public static final Attribute GENERIC_FOLLOW_RANGE = FOLLOW_RANGE;
    @Deprecated
    public static final Attribute GENERIC_KNOCKBACK_RESISTANCE = KNOCKBACK_RESISTANCE;
    @Deprecated
    public static final Attribute GENERIC_LUCK = LUCK;
    @Deprecated
    public static final Attribute GENERIC_MAX_HEALTH = MAX_HEALTH;
    @Deprecated
    public static final Attribute GENERIC_MOVEMENT_SPEED = MOVEMENT_SPEED;
    @Deprecated
    public static final Attribute ZOMBIE_SPAWN_REINFORCEMENTS = SPAWN_REINFORCEMENTS;
    @Deprecated
    public static final Attribute GENERIC_MAX_ABSORPTION = MAX_ABSORPTION;
    @Deprecated
    public static final Attribute PLAYER_BLOCK_BREAK_SPEED = BLOCK_BREAK_SPEED;
    @Deprecated
    public static final Attribute PLAYER_BLOCK_INTERACTION_RANGE = BLOCK_INTERACTION_RANGE;
    @Deprecated
    public static final Attribute PLAYER_ENTITY_INTERACTION_RANGE = ENTITY_INTERACTION_RANGE;
    @Deprecated
    public static final Attribute GENERIC_FALL_DAMAGE_MULTIPLIER = FALL_DAMAGE_MULTIPLIER;
    @Deprecated
    public static final Attribute GENERIC_GRAVITY = GRAVITY;
    @Deprecated
    public static final Attribute GENERIC_JUMP_STRENGTH = JUMP_STRENGTH;
    @Deprecated
    public static final Attribute GENERIC_SAFE_FALL_DISTANCE = SAFE_FALL_DISTANCE;
    @Deprecated
    public static final Attribute GENERIC_SCALE = SCALE;
    @Deprecated
    public static final Attribute GENERIC_STEP_HEIGHT = STEP_HEIGHT;
    @Deprecated
    public static final Attribute GENERIC_BURNING_TIME = BURNING_TIME;
    @Deprecated
    public static final Attribute GENERIC_EXPLOSION_KNOCKBACK_RESISTANCE = EXPLOSION_KNOCKBACK_RESISTANCE;
    @Deprecated
    public static final Attribute PLAYER_MINING_EFFICIENCY = MINING_EFFICIENCY;
    @Deprecated
    public static final Attribute GENERIC_MOVEMENT_EFFICIENCY = MOVEMENT_EFFICIENCY;
    @Deprecated
    public static final Attribute GENERIC_OXYGEN_BONUS = OXYGEN_BONUS;
    @Deprecated
    public static final Attribute PLAYER_SNEAKING_SPEED = SNEAKING_SPEED;
    @Deprecated
    public static final Attribute PLAYER_SUBMERGED_MINING_SPEED = SUBMERGED_MINING_SPEED;
    @Deprecated
    public static final Attribute PLAYER_SWEEPING_DAMAGE_RATIO = SWEEPING_DAMAGE_RATIO;
    @Deprecated
    public static final Attribute GENERIC_WATER_MOVEMENT_EFFICIENCY = WATER_MOVEMENT_EFFICIENCY;

    private Attributes() {
    }

    private static Attribute define(String key, @Nullable String legacyPrefix, double def, double min, double max) {
        return REGISTRY.define(key, data -> new StaticAttribute((TypesBuilderData)data, legacyPrefix, def, min, max));
    }

    public static VersionedRegistry<Attribute> getRegistry() {
        return REGISTRY;
    }

    public static Attribute getByName(String name) {
        String normedName = ResourceLocation.normString(name);
        if (normedName.startsWith("minecraft:generic.") || normedName.startsWith("minecraft:player.") || normedName.startsWith("minecraft:zombie.")) {
            normedName = normedName.substring(normedName.indexOf(46) + 1);
        }
        return REGISTRY.getByName(normedName);
    }

    public static Attribute getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    static {
        REGISTRY.unloadMappings();
    }
}

