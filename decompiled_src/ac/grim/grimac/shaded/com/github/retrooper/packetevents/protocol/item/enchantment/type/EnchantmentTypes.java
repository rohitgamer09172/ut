/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.serializer.SequentialNBTReader;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.MappingHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class EnchantmentTypes {
    private static final Map<String, String> STRING_UPDATER = new HashMap<String, String>();
    private static final Map<ResourceLocation, NBTCompound> ENCHANTMENT_DATA;
    private static final VersionedRegistry<EnchantmentType> REGISTRY;
    public static final EnchantmentType ALL_DAMAGE_PROTECTION;
    public static final EnchantmentType FIRE_PROTECTION;
    public static final EnchantmentType FALL_PROTECTION;
    public static final EnchantmentType BLAST_PROTECTION;
    public static final EnchantmentType PROJECTILE_PROTECTION;
    public static final EnchantmentType RESPIRATION;
    public static final EnchantmentType AQUA_AFFINITY;
    public static final EnchantmentType THORNS;
    public static final EnchantmentType DEPTH_STRIDER;
    public static final EnchantmentType FROST_WALKER;
    public static final EnchantmentType BINDING_CURSE;
    public static final EnchantmentType SOUL_SPEED;
    public static final EnchantmentType SWIFT_SNEAK;
    public static final EnchantmentType SHARPNESS;
    public static final EnchantmentType SMITE;
    public static final EnchantmentType BANE_OF_ARTHROPODS;
    public static final EnchantmentType KNOCKBACK;
    public static final EnchantmentType FIRE_ASPECT;
    public static final EnchantmentType MOB_LOOTING;
    public static final EnchantmentType SWEEPING_EDGE;
    public static final EnchantmentType BLOCK_EFFICIENCY;
    public static final EnchantmentType SILK_TOUCH;
    public static final EnchantmentType UNBREAKING;
    public static final EnchantmentType BLOCK_FORTUNE;
    public static final EnchantmentType POWER_ARROWS;
    public static final EnchantmentType PUNCH_ARROWS;
    public static final EnchantmentType FLAMING_ARROWS;
    public static final EnchantmentType INFINITY_ARROWS;
    public static final EnchantmentType FISHING_LUCK;
    public static final EnchantmentType FISHING_SPEED;
    public static final EnchantmentType LOYALTY;
    public static final EnchantmentType IMPALING;
    public static final EnchantmentType RIPTIDE;
    public static final EnchantmentType CHANNELING;
    public static final EnchantmentType MULTISHOT;
    public static final EnchantmentType QUICK_CHARGE;
    public static final EnchantmentType PIERCING;
    public static final EnchantmentType MENDING;
    public static final EnchantmentType VANISHING_CURSE;
    public static final EnchantmentType DENSITY;
    public static final EnchantmentType BREACH;
    public static final EnchantmentType WIND_BURST;
    public static final EnchantmentType LUNGE;

    private EnchantmentTypes() {
    }

    @ApiStatus.Internal
    public static EnchantmentType define(String key) {
        PacketWrapper<?> wrapper = PacketWrapper.createDummyWrapper(ClientVersion.getLatest());
        return REGISTRY.define(key, data -> {
            NBTCompound dataTag = ENCHANTMENT_DATA.get(data.getName());
            if (dataTag == null) {
                throw new IllegalArgumentException("Can't define enchantment " + data.getName() + ", no data found");
            }
            return EnchantmentType.decode((NBT)dataTag, wrapper, data);
        });
    }

    public static VersionedRegistry<EnchantmentType> getRegistry() {
        return REGISTRY;
    }

    @Nullable
    public static EnchantmentType getByName(String name) {
        String fixedName = STRING_UPDATER.getOrDefault(name, name);
        return REGISTRY.getByName(fixedName);
    }

    @Nullable
    public static EnchantmentType getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    static {
        STRING_UPDATER.put("minecraft:sweeping", "minecraft:sweeping_edge");
        ENCHANTMENT_DATA = new HashMap<ResourceLocation, NBTCompound>();
        try (SequentialNBTReader.Compound dataTag = MappingHelper.decompress("mappings/data/enchantment");){
            dataTag.skipOne();
            for (Map.Entry<String, NBT> entry : (SequentialNBTReader.Compound)dataTag.next().getValue()) {
                ResourceLocation enchantKey = new ResourceLocation(entry.getKey());
                ENCHANTMENT_DATA.put(enchantKey, ((SequentialNBTReader.Compound)entry.getValue()).readFully());
            }
        }
        catch (IOException exception) {
            throw new RuntimeException("Error while reading enchantment type data", exception);
        }
        REGISTRY = new VersionedRegistry("enchantment");
        ALL_DAMAGE_PROTECTION = EnchantmentTypes.define("protection");
        FIRE_PROTECTION = EnchantmentTypes.define("fire_protection");
        FALL_PROTECTION = EnchantmentTypes.define("feather_falling");
        BLAST_PROTECTION = EnchantmentTypes.define("blast_protection");
        PROJECTILE_PROTECTION = EnchantmentTypes.define("projectile_protection");
        RESPIRATION = EnchantmentTypes.define("respiration");
        AQUA_AFFINITY = EnchantmentTypes.define("aqua_affinity");
        THORNS = EnchantmentTypes.define("thorns");
        DEPTH_STRIDER = EnchantmentTypes.define("depth_strider");
        FROST_WALKER = EnchantmentTypes.define("frost_walker");
        BINDING_CURSE = EnchantmentTypes.define("binding_curse");
        SOUL_SPEED = EnchantmentTypes.define("soul_speed");
        SWIFT_SNEAK = EnchantmentTypes.define("swift_sneak");
        SHARPNESS = EnchantmentTypes.define("sharpness");
        SMITE = EnchantmentTypes.define("smite");
        BANE_OF_ARTHROPODS = EnchantmentTypes.define("bane_of_arthropods");
        KNOCKBACK = EnchantmentTypes.define("knockback");
        FIRE_ASPECT = EnchantmentTypes.define("fire_aspect");
        MOB_LOOTING = EnchantmentTypes.define("looting");
        SWEEPING_EDGE = EnchantmentTypes.define("sweeping_edge");
        BLOCK_EFFICIENCY = EnchantmentTypes.define("efficiency");
        SILK_TOUCH = EnchantmentTypes.define("silk_touch");
        UNBREAKING = EnchantmentTypes.define("unbreaking");
        BLOCK_FORTUNE = EnchantmentTypes.define("fortune");
        POWER_ARROWS = EnchantmentTypes.define("power");
        PUNCH_ARROWS = EnchantmentTypes.define("punch");
        FLAMING_ARROWS = EnchantmentTypes.define("flame");
        INFINITY_ARROWS = EnchantmentTypes.define("infinity");
        FISHING_LUCK = EnchantmentTypes.define("luck_of_the_sea");
        FISHING_SPEED = EnchantmentTypes.define("lure");
        LOYALTY = EnchantmentTypes.define("loyalty");
        IMPALING = EnchantmentTypes.define("impaling");
        RIPTIDE = EnchantmentTypes.define("riptide");
        CHANNELING = EnchantmentTypes.define("channeling");
        MULTISHOT = EnchantmentTypes.define("multishot");
        QUICK_CHARGE = EnchantmentTypes.define("quick_charge");
        PIERCING = EnchantmentTypes.define("piercing");
        MENDING = EnchantmentTypes.define("mending");
        VANISHING_CURSE = EnchantmentTypes.define("vanishing_curse");
        DENSITY = EnchantmentTypes.define("density");
        BREACH = EnchantmentTypes.define("breach");
        WIND_BURST = EnchantmentTypes.define("wind_burst");
        LUNGE = EnchantmentTypes.define("lunge");
        ENCHANTMENT_DATA.clear();
        REGISTRY.unloadMappings();
    }
}

