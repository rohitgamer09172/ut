/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.StaticEntityType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Collection;

public final class EntityTypes {
    private static final VersionedRegistry<EntityType> REGISTRY = new VersionedRegistry("entity_type");
    private static final VersionedRegistry<EntityType> LEGACY_SPAWN_REGISTRY = new VersionedRegistry("legacy_spawn_entity_type");
    public static final EntityType ENTITY = EntityTypes.define("entity", null);
    public static final EntityType LIVINGENTITY = EntityTypes.define("livingentity", ENTITY);
    public static final EntityType ABSTRACT_INSENTIENT = EntityTypes.define("abstract_insentient", LIVINGENTITY);
    public static final EntityType ABSTRACT_CREATURE = EntityTypes.define("abstract_creature", ABSTRACT_INSENTIENT);
    public static final EntityType ABSTRACT_AGEABLE = EntityTypes.define("abstract_ageable", ABSTRACT_CREATURE);
    public static final EntityType ABSTRACT_ANIMAL = EntityTypes.define("abstract_animal", ABSTRACT_AGEABLE);
    public static final EntityType ABSTRACT_TAMEABLE_ANIMAL = EntityTypes.define("abstract_tameable_animal", ABSTRACT_ANIMAL);
    public static final EntityType ABSTRACT_PARROT = EntityTypes.define("abstract_parrot", ABSTRACT_TAMEABLE_ANIMAL);
    public static final EntityType ABSTRACT_HORSE = EntityTypes.define("abstract_horse", ABSTRACT_ANIMAL);
    public static final EntityType CHESTED_HORSE = EntityTypes.define("chested_horse", ABSTRACT_HORSE);
    public static final EntityType ABSTRACT_GOLEM = EntityTypes.define("abstract_golem", ABSTRACT_CREATURE);
    public static final EntityType ABSTRACT_FISHES = EntityTypes.define("abstract_fishes", ABSTRACT_CREATURE);
    public static final EntityType ABSTRACT_MONSTER = EntityTypes.define("abstract_monster", ABSTRACT_CREATURE);
    public static final EntityType ABSTRACT_PIGLIN = EntityTypes.define("abstract_piglin", ABSTRACT_MONSTER);
    public static final EntityType ABSTRACT_ILLAGER_BASE = EntityTypes.define("abstract_illager_base", ABSTRACT_MONSTER);
    public static final EntityType ABSTRACT_EVO_ILLU_ILLAGER = EntityTypes.define("abstract_evo_illu_illager", ABSTRACT_ILLAGER_BASE);
    public static final EntityType ABSTRACT_SKELETON = EntityTypes.define("abstract_skeleton", ABSTRACT_MONSTER);
    public static final EntityType ABSTRACT_FLYING = EntityTypes.define("abstract_flying", ABSTRACT_INSENTIENT);
    public static final EntityType ABSTRACT_AMBIENT = EntityTypes.define("abstract_ambient", ABSTRACT_INSENTIENT);
    public static final EntityType ABSTRACT_WATERMOB = EntityTypes.define("abstract_watermob", ABSTRACT_INSENTIENT);
    public static final EntityType ABSTRACT_HANGING = EntityTypes.define("abstract_hanging", ENTITY);
    public static final EntityType ABSTRACT_LIGHTNING = EntityTypes.define("abstract_lightning", ENTITY);
    public static final EntityType ABSTRACT_ARROW = EntityTypes.define("abstract_arrow", ENTITY);
    public static final EntityType ABSTRACT_FIREBALL = EntityTypes.define("abstract_fireball", ENTITY);
    public static final EntityType PROJECTILE_ABSTRACT = EntityTypes.define("projectile_abstract", ENTITY);
    public static final EntityType MINECART_ABSTRACT = EntityTypes.define("minecart_abstract", ENTITY);
    public static final EntityType CHESTED_MINECART_ABSTRACT = EntityTypes.define("chested_minecart_abstract", MINECART_ABSTRACT);
    public static final EntityType AVATAR = EntityTypes.define("avatar", LIVINGENTITY);
    public static final EntityType AREA_EFFECT_CLOUD = EntityTypes.define("area_effect_cloud", ENTITY);
    public static final EntityType ARMOR_STAND = EntityTypes.define("armor_stand", LIVINGENTITY);
    public static final EntityType ALLAY = EntityTypes.define("allay", ABSTRACT_CREATURE);
    public static final EntityType ARROW = EntityTypes.define("arrow", ABSTRACT_ARROW);
    public static final EntityType AXOLOTL = EntityTypes.define("axolotl", ABSTRACT_ANIMAL);
    public static final EntityType BAT = EntityTypes.define("bat", ABSTRACT_AMBIENT);
    public static final EntityType BEE = EntityTypes.define("bee", ABSTRACT_INSENTIENT);
    public static final EntityType BLAZE = EntityTypes.define("blaze", ABSTRACT_MONSTER);
    public static final EntityType BOAT = EntityTypes.define("boat", ENTITY);
    public static final EntityType CHEST_BOAT = EntityTypes.define("chest_boat", BOAT);
    public static final EntityType CAT = EntityTypes.define("cat", ABSTRACT_TAMEABLE_ANIMAL);
    public static final EntityType CAMEL = EntityTypes.define("camel", ABSTRACT_HORSE);
    public static final EntityType SPIDER = EntityTypes.define("spider", ABSTRACT_MONSTER);
    public static final EntityType CAVE_SPIDER = EntityTypes.define("cave_spider", SPIDER);
    public static final EntityType CHICKEN = EntityTypes.define("chicken", ABSTRACT_ANIMAL);
    public static final EntityType COD = EntityTypes.define("cod", ABSTRACT_FISHES);
    public static final EntityType COW = EntityTypes.define("cow", ABSTRACT_ANIMAL);
    public static final EntityType CREEPER = EntityTypes.define("creeper", ABSTRACT_MONSTER);
    public static final EntityType DOLPHIN = EntityTypes.define("dolphin", ABSTRACT_INSENTIENT);
    public static final EntityType DONKEY = EntityTypes.define("donkey", CHESTED_HORSE);
    public static final EntityType DRAGON_FIREBALL = EntityTypes.define("dragon_fireball", ABSTRACT_FIREBALL);
    public static final EntityType ZOMBIE = EntityTypes.define("zombie", ABSTRACT_MONSTER);
    public static final EntityType DROWNED = EntityTypes.define("drowned", ZOMBIE);
    public static final EntityType GUARDIAN = EntityTypes.define("guardian", ABSTRACT_MONSTER);
    public static final EntityType ELDER_GUARDIAN = EntityTypes.define("elder_guardian", GUARDIAN);
    public static final EntityType END_CRYSTAL = EntityTypes.define("end_crystal", ENTITY);
    public static final EntityType ENDER_DRAGON = EntityTypes.define("ender_dragon", ABSTRACT_INSENTIENT);
    public static final EntityType ENDERMAN = EntityTypes.define("enderman", ABSTRACT_MONSTER);
    public static final EntityType ENDERMITE = EntityTypes.define("endermite", ABSTRACT_MONSTER);
    public static final EntityType EVOKER = EntityTypes.define("evoker", ABSTRACT_EVO_ILLU_ILLAGER);
    public static final EntityType EVOKER_FANGS = EntityTypes.define("evoker_fangs", ENTITY);
    public static final EntityType EXPERIENCE_ORB = EntityTypes.define("experience_orb", ENTITY);
    public static final EntityType EYE_OF_ENDER = EntityTypes.define("eye_of_ender", ENTITY);
    public static final EntityType FALLING_BLOCK = EntityTypes.define("falling_block", ENTITY);
    public static final EntityType FIREWORK_ROCKET = EntityTypes.define("firework_rocket", ENTITY);
    public static final EntityType FOX = EntityTypes.define("fox", ABSTRACT_ANIMAL);
    public static final EntityType FROG = EntityTypes.define("frog", ABSTRACT_ANIMAL);
    public static final EntityType GHAST = EntityTypes.define("ghast", ABSTRACT_FLYING);
    public static final EntityType GIANT = EntityTypes.define("giant", ABSTRACT_MONSTER);
    public static final EntityType ITEM_FRAME = EntityTypes.define("item_frame", ABSTRACT_HANGING);
    public static final EntityType GLOW_ITEM_FRAME = EntityTypes.define("glow_item_frame", ITEM_FRAME);
    public static final EntityType SQUID = EntityTypes.define("squid", ABSTRACT_WATERMOB);
    public static final EntityType GLOW_SQUID = EntityTypes.define("glow_squid", SQUID);
    public static final EntityType GOAT = EntityTypes.define("goat", ABSTRACT_ANIMAL);
    public static final EntityType HOGLIN = EntityTypes.define("hoglin", ABSTRACT_ANIMAL);
    public static final EntityType HORSE = EntityTypes.define("horse", ABSTRACT_HORSE);
    public static final EntityType HUSK = EntityTypes.define("husk", ZOMBIE);
    public static final EntityType ILLUSIONER = EntityTypes.define("illusioner", ABSTRACT_EVO_ILLU_ILLAGER);
    public static final EntityType IRON_GOLEM = EntityTypes.define("iron_golem", ABSTRACT_GOLEM);
    public static final EntityType ITEM = EntityTypes.define("item", ENTITY);
    public static final EntityType FIREBALL = EntityTypes.define("fireball", ABSTRACT_FIREBALL);
    public static final EntityType LEASH_KNOT = EntityTypes.define("leash_knot", ABSTRACT_HANGING);
    public static final EntityType LIGHTNING_BOLT = EntityTypes.define("lightning_bolt", ABSTRACT_LIGHTNING);
    public static final EntityType LLAMA = EntityTypes.define("llama", CHESTED_HORSE);
    public static final EntityType LLAMA_SPIT = EntityTypes.define("llama_spit", ENTITY);
    public static final EntityType SLIME = EntityTypes.define("slime", ABSTRACT_INSENTIENT);
    public static final EntityType MAGMA_CUBE = EntityTypes.define("magma_cube", SLIME);
    public static final EntityType MARKER = EntityTypes.define("marker", ENTITY);
    public static final EntityType MINECART = EntityTypes.define("minecart", MINECART_ABSTRACT);
    public static final EntityType CHEST_MINECART = EntityTypes.define("chest_minecart", CHESTED_MINECART_ABSTRACT);
    public static final EntityType COMMAND_BLOCK_MINECART = EntityTypes.define("command_block_minecart", MINECART_ABSTRACT);
    public static final EntityType FURNACE_MINECART = EntityTypes.define("furnace_minecart", MINECART_ABSTRACT);
    public static final EntityType HOPPER_MINECART = EntityTypes.define("hopper_minecart", CHESTED_MINECART_ABSTRACT);
    public static final EntityType SPAWNER_MINECART = EntityTypes.define("spawner_minecart", MINECART_ABSTRACT);
    public static final EntityType TNT_MINECART = EntityTypes.define("tnt_minecart", MINECART_ABSTRACT);
    public static final EntityType MULE = EntityTypes.define("mule", CHESTED_HORSE);
    public static final EntityType MOOSHROOM = EntityTypes.define("mooshroom", COW);
    public static final EntityType OCELOT = EntityTypes.define("ocelot", ABSTRACT_TAMEABLE_ANIMAL);
    public static final EntityType PAINTING = EntityTypes.define("painting", ABSTRACT_HANGING);
    public static final EntityType PANDA = EntityTypes.define("panda", ABSTRACT_INSENTIENT);
    public static final EntityType PARROT = EntityTypes.define("parrot", ABSTRACT_PARROT);
    public static final EntityType PHANTOM = EntityTypes.define("phantom", ABSTRACT_FLYING);
    public static final EntityType PIG = EntityTypes.define("pig", ABSTRACT_ANIMAL);
    public static final EntityType PIGLIN = EntityTypes.define("piglin", ABSTRACT_PIGLIN);
    public static final EntityType PIGLIN_BRUTE = EntityTypes.define("piglin_brute", ABSTRACT_PIGLIN);
    public static final EntityType PILLAGER = EntityTypes.define("pillager", ABSTRACT_ILLAGER_BASE);
    public static final EntityType POLAR_BEAR = EntityTypes.define("polar_bear", ABSTRACT_ANIMAL);
    public static final EntityType TNT = EntityTypes.define("tnt", ENTITY);
    public static final EntityType PUFFERFISH = EntityTypes.define("pufferfish", ABSTRACT_FISHES);
    public static final EntityType RABBIT = EntityTypes.define("rabbit", ABSTRACT_ANIMAL);
    public static final EntityType RAVAGER = EntityTypes.define("ravager", ABSTRACT_MONSTER);
    public static final EntityType SALMON = EntityTypes.define("salmon", ABSTRACT_FISHES);
    public static final EntityType SHEEP = EntityTypes.define("sheep", ABSTRACT_ANIMAL);
    public static final EntityType SHULKER = EntityTypes.define("shulker", ABSTRACT_GOLEM);
    public static final EntityType SHULKER_BULLET = EntityTypes.define("shulker_bullet", ENTITY);
    public static final EntityType SILVERFISH = EntityTypes.define("silverfish", ABSTRACT_MONSTER);
    public static final EntityType SKELETON = EntityTypes.define("skeleton", ABSTRACT_SKELETON);
    public static final EntityType SKELETON_HORSE = EntityTypes.define("skeleton_horse", ABSTRACT_HORSE);
    public static final EntityType SMALL_FIREBALL = EntityTypes.define("small_fireball", ABSTRACT_FIREBALL);
    public static final EntityType SNOW_GOLEM = EntityTypes.define("snow_golem", ABSTRACT_GOLEM);
    public static final EntityType SNOWBALL = EntityTypes.define("snowball", PROJECTILE_ABSTRACT);
    public static final EntityType SPECTRAL_ARROW = EntityTypes.define("spectral_arrow", ABSTRACT_ARROW);
    public static final EntityType STRAY = EntityTypes.define("stray", ABSTRACT_SKELETON);
    public static final EntityType STRIDER = EntityTypes.define("strider", ABSTRACT_ANIMAL);
    public static final EntityType EGG = EntityTypes.define("egg", PROJECTILE_ABSTRACT);
    public static final EntityType ENDER_PEARL = EntityTypes.define("ender_pearl", PROJECTILE_ABSTRACT);
    public static final EntityType EXPERIENCE_BOTTLE = EntityTypes.define("experience_bottle", PROJECTILE_ABSTRACT);
    public static final EntityType POTION = EntityTypes.define("potion", PROJECTILE_ABSTRACT);
    public static final EntityType TADPOLE = EntityTypes.define("tadpole", ABSTRACT_FISHES);
    @Deprecated
    public static final EntityType TIPPED_ARROW = EntityTypes.define("tipped_arrow", ARROW);
    public static final EntityType TRIDENT = EntityTypes.define("trident", ABSTRACT_ARROW);
    public static final EntityType TRADER_LLAMA = EntityTypes.define("trader_llama", CHESTED_HORSE);
    public static final EntityType TROPICAL_FISH = EntityTypes.define("tropical_fish", ABSTRACT_FISHES);
    public static final EntityType TURTLE = EntityTypes.define("turtle", ABSTRACT_ANIMAL);
    public static final EntityType VEX = EntityTypes.define("vex", ABSTRACT_MONSTER);
    public static final EntityType VILLAGER = EntityTypes.define("villager", ABSTRACT_AGEABLE);
    public static final EntityType VINDICATOR = EntityTypes.define("vindicator", ABSTRACT_ILLAGER_BASE);
    public static final EntityType WANDERING_TRADER = EntityTypes.define("wandering_trader", ABSTRACT_AGEABLE);
    public static final EntityType WARDEN = EntityTypes.define("warden", ABSTRACT_MONSTER);
    public static final EntityType WITCH = EntityTypes.define("witch", ABSTRACT_MONSTER);
    public static final EntityType WITHER = EntityTypes.define("wither", ABSTRACT_MONSTER);
    public static final EntityType WITHER_SKELETON = EntityTypes.define("wither_skeleton", ABSTRACT_SKELETON);
    public static final EntityType WITHER_SKULL = EntityTypes.define("wither_skull", ABSTRACT_FIREBALL);
    public static final EntityType WOLF = EntityTypes.define("wolf", ABSTRACT_TAMEABLE_ANIMAL);
    public static final EntityType ZOGLIN = EntityTypes.define("zoglin", ABSTRACT_MONSTER);
    public static final EntityType ZOMBIE_HORSE = EntityTypes.define("zombie_horse", ABSTRACT_HORSE);
    public static final EntityType ZOMBIE_VILLAGER = EntityTypes.define("zombie_villager", ZOMBIE);
    public static final EntityType ZOMBIFIED_PIGLIN = EntityTypes.define("zombified_piglin", ZOMBIE);
    public static final EntityType PLAYER = EntityTypes.define("player", AVATAR);
    public static final EntityType FISHING_BOBBER = EntityTypes.define("fishing_bobber", ENTITY);
    public static final EntityType ENDER_SIGNAL = EntityTypes.define("ender_signal", ENTITY);
    public static final EntityType THROWN_EXP_BOTTLE = EntityTypes.define("thrown_exp_bottle", PROJECTILE_ABSTRACT);
    public static final EntityType PRIMED_TNT = EntityTypes.define("primed_tnt", ENTITY);
    public static final EntityType FIREWORK = EntityTypes.define("firework", ENTITY);
    public static final EntityType MINECART_COMMAND = EntityTypes.define("minecart_command", MINECART_ABSTRACT);
    public static final EntityType MINECART_RIDEABLE = EntityTypes.define("minecart_rideable", MINECART_ABSTRACT);
    public static final EntityType MINECART_CHEST = EntityTypes.define("minecart_chest", MINECART_ABSTRACT);
    public static final EntityType MINECART_FURNACE = EntityTypes.define("minecart_furnace", MINECART_ABSTRACT);
    public static final EntityType MINECART_TNT = EntityTypes.define("minecart_tnt", MINECART_ABSTRACT);
    public static final EntityType MINECART_HOPPER = EntityTypes.define("minecart_hopper", MINECART_ABSTRACT);
    public static final EntityType MINECART_MOB_SPAWNER = EntityTypes.define("minecart_mob_spawner", MINECART_ABSTRACT);
    public static final EntityType DISPLAY = EntityTypes.define("display", ENTITY);
    public static final EntityType BLOCK_DISPLAY = EntityTypes.define("block_display", DISPLAY);
    public static final EntityType ITEM_DISPLAY = EntityTypes.define("item_display", DISPLAY);
    public static final EntityType TEXT_DISPLAY = EntityTypes.define("text_display", DISPLAY);
    public static final EntityType INTERACTION = EntityTypes.define("interaction", DISPLAY);
    public static final EntityType SNIFFER = EntityTypes.define("sniffer", ABSTRACT_ANIMAL);
    public static final EntityType BREEZE = EntityTypes.define("breeze", ABSTRACT_MONSTER);
    public static final EntityType ABSTRACT_WIND_CHARGE = EntityTypes.define("abstract_wind_charge", PROJECTILE_ABSTRACT);
    public static final EntityType WIND_CHARGE = EntityTypes.define("wind_charge", ABSTRACT_WIND_CHARGE);
    public static final EntityType ARMADILLO = EntityTypes.define("armadillo", ABSTRACT_ANIMAL);
    public static final EntityType BOGGED = EntityTypes.define("bogged", ABSTRACT_SKELETON);
    public static final EntityType BREEZE_WIND_CHARGE = EntityTypes.define("breeze_wind_charge", ABSTRACT_WIND_CHARGE);
    public static final EntityType OMINOUS_ITEM_SPAWNER = EntityTypes.define("ominous_item_spawner", ENTITY);
    public static final EntityType ACACIA_BOAT = EntityTypes.define("acacia_boat", BOAT);
    public static final EntityType ACACIA_CHEST_BOAT = EntityTypes.define("acacia_chest_boat", CHEST_BOAT);
    public static final EntityType BAMBOO_CHEST_RAFT = EntityTypes.define("bamboo_chest_raft", CHEST_BOAT);
    public static final EntityType BAMBOO_RAFT = EntityTypes.define("bamboo_raft", BOAT);
    public static final EntityType BIRCH_BOAT = EntityTypes.define("birch_boat", BOAT);
    public static final EntityType BIRCH_CHEST_BOAT = EntityTypes.define("birch_chest_boat", CHEST_BOAT);
    public static final EntityType CHERRY_BOAT = EntityTypes.define("cherry_boat", BOAT);
    public static final EntityType CHERRY_CHEST_BOAT = EntityTypes.define("cherry_chest_boat", CHEST_BOAT);
    public static final EntityType CREAKING = EntityTypes.define("creaking", ABSTRACT_MONSTER);
    @ApiStatus.Obsolete
    public static final EntityType CREAKING_TRANSIENT = EntityTypes.define("creaking_transient", CREAKING);
    public static final EntityType DARK_OAK_BOAT = EntityTypes.define("dark_oak_boat", BOAT);
    public static final EntityType DARK_OAK_CHEST_BOAT = EntityTypes.define("dark_oak_chest_boat", CHEST_BOAT);
    public static final EntityType JUNGLE_BOAT = EntityTypes.define("jungle_boat", BOAT);
    public static final EntityType JUNGLE_CHEST_BOAT = EntityTypes.define("jungle_chest_boat", CHEST_BOAT);
    public static final EntityType MANGROVE_BOAT = EntityTypes.define("mangrove_boat", BOAT);
    public static final EntityType MANGROVE_CHEST_BOAT = EntityTypes.define("mangrove_chest_boat", CHEST_BOAT);
    public static final EntityType OAK_BOAT = EntityTypes.define("oak_boat", BOAT);
    public static final EntityType OAK_CHEST_BOAT = EntityTypes.define("oak_chest_boat", CHEST_BOAT);
    public static final EntityType PALE_OAK_BOAT = EntityTypes.define("pale_oak_boat", BOAT);
    public static final EntityType PALE_OAK_CHEST_BOAT = EntityTypes.define("pale_oak_chest_boat", CHEST_BOAT);
    public static final EntityType SPRUCE_BOAT = EntityTypes.define("spruce_boat", BOAT);
    public static final EntityType SPRUCE_CHEST_BOAT = EntityTypes.define("spruce_chest_boat", CHEST_BOAT);
    public static final EntityType SPLASH_POTION = EntityTypes.define("splash_potion", POTION);
    public static final EntityType LINGERING_POTION = EntityTypes.define("lingering_potion", POTION);
    public static final EntityType HAPPY_GHAST = EntityTypes.define("happy_ghast", ABSTRACT_ANIMAL);
    public static final EntityType COPPER_GOLEM = EntityTypes.define("copper_golem", ABSTRACT_GOLEM);
    public static final EntityType MANNEQUIN = EntityTypes.define("mannequin", AVATAR);
    public static final EntityType CAMEL_HUSK = EntityTypes.define("camel_husk", CAMEL);
    public static final EntityType ABSTRACT_NAUTILUS = EntityTypes.define("abstract_nautilus", ABSTRACT_TAMEABLE_ANIMAL);
    public static final EntityType NAUTILUS = EntityTypes.define("nautilus", ABSTRACT_NAUTILUS);
    public static final EntityType PARCHED = EntityTypes.define("parched", ABSTRACT_SKELETON);
    public static final EntityType ZOMBIE_NAUTILUS = EntityTypes.define("zombie_nautilus", ABSTRACT_NAUTILUS);

    private EntityTypes() {
    }

    public static VersionedRegistry<EntityType> getRegistry() {
        return REGISTRY;
    }

    @ApiStatus.Obsolete
    public static VersionedRegistry<EntityType> getLegacySpawnRegistry() {
        return LEGACY_SPAWN_REGISTRY;
    }

    @ApiStatus.Internal
    public static EntityType define(String name, @Nullable EntityType parent) {
        StaticEntityType type = REGISTRY.define(name, data -> new StaticEntityType((TypesBuilderData)data, parent));
        return LEGACY_SPAWN_REGISTRY.define(name, type::setLegacyData);
    }

    public static boolean isTypeInstanceOf(EntityType type, EntityType parent) {
        return type != null && type.isInstanceOf(parent);
    }

    public static EntityType getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static EntityType getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    @ApiStatus.Obsolete
    public static EntityType getByLegacyId(ClientVersion version, int id) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_14)) {
            return null;
        }
        return LEGACY_SPAWN_REGISTRY.getById(version, id);
    }

    public static Collection<EntityType> values() {
        return REGISTRY.getEntries();
    }

    static {
        REGISTRY.unloadMappings();
        LEGACY_SPAWN_REGISTRY.unloadMappings();
    }
}

