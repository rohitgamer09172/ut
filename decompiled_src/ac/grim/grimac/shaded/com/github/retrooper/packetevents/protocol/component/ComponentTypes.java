/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.DyeColor;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.StaticComponentType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.AxolotlVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.CatCollarComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.CatVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.ChickenVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.CowVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.FoxVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.FrogVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.HorseVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.LlamaVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.MooshroomVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.PaintingVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.ParrotVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.PigVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.RabbitVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.SalmonSizeComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.SheepColorComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.ShulkerColorComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.TropicalFishBaseColorComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.TropicalFishPatternColorComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.TropicalFishPatternComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.TypedBlockEntityData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.TypedEntityData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.VillagerVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.WolfCollarComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.WolfSoundVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.WolfVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.ZombieNautilusVariantComponent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ArmorTrim;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.BannerLayers;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.BundleContents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ChargedProjectiles;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.CustomData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.DebugStickState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.FireworkExplosion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.FoodProperties;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemAdventurePredicate;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemAttackRange;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemAttributeModifiers;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemBees;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemBlockStateProperties;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemBlocksAttacks;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemBreakSound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemConsumable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemContainerContents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemContainerLoot;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemCustomModelData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemDamageResistant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemDamageType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemDeathProtection;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemDyeColor;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemEnchantable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemEnchantments;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemEquippable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemFireworks;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemInstrument;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemJukeboxPlayable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemKineticWeapon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemLock;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemLore;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemMapDecorations;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemMapPostProcessingState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemMinimumAttackCharge;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemModel;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemPiercingWeapon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemPotionContents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemPotionDurationScale;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemProfile;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemProvidesBannerPatterns;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemProvidesTrimMaterial;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemRarity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemRecipes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemRepairable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemSwingAnimation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemTool;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemTooltipDisplay;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemTooltipStyle;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemUnbreakable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemUseCooldown;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemUseEffects;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemUseRemainder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemWeapon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.LodestoneTracker;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.PotDecorations;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.SuspiciousStewEffects;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.WritableBookContent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.WrittenBookContent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.instrument.Instrument;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MaybeMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Dummy;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Collection;

public final class ComponentTypes {
    private static final VersionedRegistry<ComponentType<?>> REGISTRY = new VersionedRegistry("data_component_type");
    public static final ComponentType<NBTCompound> CUSTOM_DATA = ComponentTypes.define("custom_data", CustomData::read, CustomData::write);
    public static final ComponentType<Integer> MAX_STACK_SIZE = ComponentTypes.define("max_stack_size", PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
    public static final ComponentType<Integer> MAX_DAMAGE = ComponentTypes.define("max_damage", PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
    public static final ComponentType<Integer> DAMAGE = ComponentTypes.define("damage", PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
    public static final ComponentType<ItemUnbreakable> UNBREAKABLE_MODERN = ComponentTypes.define("unbreakable", ItemUnbreakable::read, ItemUnbreakable::write);
    @Deprecated
    public static final ComponentType<Boolean> UNBREAKABLE = UNBREAKABLE_MODERN.legacyMap(ItemUnbreakable::isShowInTooltip, ItemUnbreakable::new);
    public static final ComponentType<Component> CUSTOM_NAME = ComponentTypes.define("custom_name", PacketWrapper::readComponent, PacketWrapper::writeComponent);
    public static final ComponentType<Component> ITEM_NAME = ComponentTypes.define("item_name", PacketWrapper::readComponent, PacketWrapper::writeComponent);
    public static final ComponentType<ItemLore> LORE = ComponentTypes.define("lore", ItemLore::read, ItemLore::write);
    public static final ComponentType<ItemRarity> RARITY = ComponentTypes.define("rarity", wrapper -> (ItemRarity)wrapper.readEnum(ItemRarity.values()), PacketWrapper::writeEnum);
    public static final ComponentType<ItemEnchantments> ENCHANTMENTS = ComponentTypes.define("enchantments", ItemEnchantments::read, ItemEnchantments::write);
    public static final ComponentType<ItemAdventurePredicate> CAN_PLACE_ON = ComponentTypes.define("can_place_on", ItemAdventurePredicate::read, ItemAdventurePredicate::write);
    public static final ComponentType<ItemAdventurePredicate> CAN_BREAK = ComponentTypes.define("can_break", ItemAdventurePredicate::read, ItemAdventurePredicate::write);
    public static final ComponentType<ItemAttributeModifiers> ATTRIBUTE_MODIFIERS = ComponentTypes.define("attribute_modifiers", ItemAttributeModifiers::read, ItemAttributeModifiers::write);
    public static final ComponentType<ItemCustomModelData> CUSTOM_MODEL_DATA_LISTS = ComponentTypes.define("custom_model_data", ItemCustomModelData::read, ItemCustomModelData::write);
    @Deprecated
    public static final ComponentType<Integer> CUSTOM_MODEL_DATA = CUSTOM_MODEL_DATA_LISTS.legacyMap(ItemCustomModelData::getLegacyId, ItemCustomModelData::new);
    @ApiStatus.Obsolete
    public static final ComponentType<Dummy> HIDE_ADDITIONAL_TOOLTIP = ComponentTypes.define("hide_additional_tooltip", Dummy::dummyRead, Dummy::dummyWrite);
    @ApiStatus.Obsolete
    public static final ComponentType<Dummy> HIDE_TOOLTIP = ComponentTypes.define("hide_tooltip", Dummy::dummyRead, Dummy::dummyWrite);
    public static final ComponentType<Integer> REPAIR_COST = ComponentTypes.define("repair_cost", PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
    public static final ComponentType<Dummy> CREATIVE_SLOT_LOCK = ComponentTypes.define("creative_slot_lock", Dummy::dummyRead, Dummy::dummyWrite);
    public static final ComponentType<Boolean> ENCHANTMENT_GLINT_OVERRIDE = ComponentTypes.define("enchantment_glint_override", PacketWrapper::readBoolean, PacketWrapper::writeBoolean);
    public static final ComponentType<Dummy> INTANGIBLE_PROJECTILE = ComponentTypes.define("intangible_projectile", Dummy::dummyReadNbt, Dummy::dummyWriteNbt);
    public static final ComponentType<FoodProperties> FOOD = ComponentTypes.define("food", FoodProperties::read, FoodProperties::write);
    @ApiStatus.Obsolete
    public static final ComponentType<Dummy> FIRE_RESISTANT = ComponentTypes.define("fire_resistant", Dummy::dummyRead, Dummy::dummyWrite);
    public static final ComponentType<ItemTool> TOOL = ComponentTypes.define("tool", ItemTool::read, ItemTool::write);
    public static final ComponentType<ItemEnchantments> STORED_ENCHANTMENTS = ComponentTypes.define("stored_enchantments", ItemEnchantments::read, ItemEnchantments::write);
    public static final ComponentType<ItemDyeColor> DYED_COLOR = ComponentTypes.define("dyed_color", ItemDyeColor::read, ItemDyeColor::write);
    public static final ComponentType<Integer> MAP_COLOR = ComponentTypes.define("map_color", PacketWrapper::readInt, PacketWrapper::writeInt);
    public static final ComponentType<Integer> MAP_ID = ComponentTypes.define("map_id", PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
    public static final ComponentType<ItemMapDecorations> MAP_DECORATIONS = ComponentTypes.define("map_decorations", ItemMapDecorations::read, ItemMapDecorations::write);
    public static final ComponentType<ItemMapPostProcessingState> MAP_POST_PROCESSING = ComponentTypes.define("map_post_processing", wrapper -> (ItemMapPostProcessingState)wrapper.readEnum(ItemMapPostProcessingState.values()), PacketWrapper::writeEnum);
    public static final ComponentType<ChargedProjectiles> CHARGED_PROJECTILES = ComponentTypes.define("charged_projectiles", ChargedProjectiles::read, ChargedProjectiles::write);
    public static final ComponentType<BundleContents> BUNDLE_CONTENTS = ComponentTypes.define("bundle_contents", BundleContents::read, BundleContents::write);
    public static final ComponentType<ItemPotionContents> POTION_CONTENTS = ComponentTypes.define("potion_contents", ItemPotionContents::read, ItemPotionContents::write);
    public static final ComponentType<SuspiciousStewEffects> SUSPICIOUS_STEW_EFFECTS = ComponentTypes.define("suspicious_stew_effects", SuspiciousStewEffects::read, SuspiciousStewEffects::write);
    public static final ComponentType<WritableBookContent> WRITABLE_BOOK_CONTENT = ComponentTypes.define("writable_book_content", WritableBookContent::read, WritableBookContent::write);
    public static final ComponentType<WrittenBookContent> WRITTEN_BOOK_CONTENT = ComponentTypes.define("written_book_content", WrittenBookContent::read, WrittenBookContent::write);
    public static final ComponentType<ArmorTrim> TRIM = ComponentTypes.define("trim", ArmorTrim::read, ArmorTrim::write);
    public static final ComponentType<DebugStickState> DEBUG_STICK_STATE = ComponentTypes.define("debug_stick_state", DebugStickState::read, DebugStickState::write);
    public static final ComponentType<TypedEntityData> TYPED_ENTITY_DATA = ComponentTypes.define("entity_data", TypedEntityData::read, TypedEntityData::write);
    @Deprecated
    public static final ComponentType<NBTCompound> ENTITY_DATA = TYPED_ENTITY_DATA.legacyMap(TypedEntityData::getCompound, TypedEntityData::new);
    public static final ComponentType<NBTCompound> BUCKET_ENTITY_DATA = ComponentTypes.define("bucket_entity_data", PacketWrapper::readNBT, PacketWrapper::writeNBT);
    public static final ComponentType<TypedBlockEntityData> TYPED_BLOCK_ENTITY_DATA = ComponentTypes.define("block_entity_data", TypedBlockEntityData::read, TypedBlockEntityData::write);
    @Deprecated
    public static final ComponentType<NBTCompound> BLOCK_ENTITY_DATA = TYPED_BLOCK_ENTITY_DATA.legacyMap(TypedBlockEntityData::getCompound, TypedBlockEntityData::new);
    public static final ComponentType<ItemInstrument> ITEM_INSTRUMENT = ComponentTypes.define("instrument", ItemInstrument::read, ItemInstrument::write);
    @Deprecated
    public static final ComponentType<Instrument> INSTRUMENT = ITEM_INSTRUMENT.legacyMap(inst -> inst.getInstrument().getValue(), inst -> new ItemInstrument(new MaybeMappedEntity<Instrument>((Instrument)inst)));
    public static final ComponentType<Integer> OMINOUS_BOTTLE_AMPLIFIER = ComponentTypes.define("ominous_bottle_amplifier", PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
    public static final ComponentType<ItemRecipes> RECIPES = ComponentTypes.define("recipes", ItemRecipes::read, ItemRecipes::write);
    public static final ComponentType<LodestoneTracker> LODESTONE_TRACKER = ComponentTypes.define("lodestone_tracker", LodestoneTracker::read, LodestoneTracker::write);
    public static final ComponentType<FireworkExplosion> FIREWORK_EXPLOSION = ComponentTypes.define("firework_explosion", FireworkExplosion::read, FireworkExplosion::write);
    public static final ComponentType<ItemFireworks> FIREWORKS = ComponentTypes.define("fireworks", ItemFireworks::read, ItemFireworks::write);
    public static final ComponentType<ItemProfile> PROFILE = ComponentTypes.define("profile", ItemProfile::read, ItemProfile::write);
    public static final ComponentType<ResourceLocation> NOTE_BLOCK_SOUND = ComponentTypes.define("note_block_sound", PacketWrapper::readIdentifier, PacketWrapper::writeIdentifier);
    public static final ComponentType<BannerLayers> BANNER_PATTERNS = ComponentTypes.define("banner_patterns", BannerLayers::read, BannerLayers::write);
    public static final ComponentType<DyeColor> BASE_COLOR = ComponentTypes.define("base_color", wrapper -> (DyeColor)wrapper.readEnum(DyeColor.values()), PacketWrapper::writeEnum);
    public static final ComponentType<PotDecorations> POT_DECORATIONS = ComponentTypes.define("pot_decorations", PotDecorations::read, PotDecorations::write);
    public static final ComponentType<ItemContainerContents> CONTAINER = ComponentTypes.define("container", ItemContainerContents::read, ItemContainerContents::write);
    public static final ComponentType<ItemBlockStateProperties> BLOCK_STATE = ComponentTypes.define("block_state", ItemBlockStateProperties::read, ItemBlockStateProperties::write);
    public static final ComponentType<ItemBees> BEES = ComponentTypes.define("bees", ItemBees::read, ItemBees::write);
    public static final ComponentType<ItemLock> LOCK = ComponentTypes.define("lock", ItemLock::read, ItemLock::write);
    public static final ComponentType<ItemContainerLoot> CONTAINER_LOOT = ComponentTypes.define("container_loot", ItemContainerLoot::read, ItemContainerLoot::write);
    public static final ComponentType<ItemJukeboxPlayable> JUKEBOX_PLAYABLE = ComponentTypes.define("jukebox_playable", ItemJukeboxPlayable::read, ItemJukeboxPlayable::write);
    public static final ComponentType<ItemConsumable> CONSUMABLE = ComponentTypes.define("consumable", ItemConsumable::read, ItemConsumable::write);
    public static final ComponentType<ItemUseRemainder> USE_REMAINDER = ComponentTypes.define("use_remainder", ItemUseRemainder::read, ItemUseRemainder::write);
    public static final ComponentType<ItemUseCooldown> USE_COOLDOWN = ComponentTypes.define("use_cooldown", ItemUseCooldown::read, ItemUseCooldown::write);
    public static final ComponentType<ItemEnchantable> ENCHANTABLE = ComponentTypes.define("enchantable", ItemEnchantable::read, ItemEnchantable::write);
    public static final ComponentType<ItemRepairable> REPAIRABLE = ComponentTypes.define("repairable", ItemRepairable::read, ItemRepairable::write);
    public static final ComponentType<ItemModel> ITEM_MODEL = ComponentTypes.define("item_model", ItemModel::read, ItemModel::write);
    public static final ComponentType<ItemDamageResistant> DAMAGE_RESISTANT = ComponentTypes.define("damage_resistant", ItemDamageResistant::read, ItemDamageResistant::write);
    public static final ComponentType<ItemEquippable> EQUIPPABLE = ComponentTypes.define("equippable", ItemEquippable::read, ItemEquippable::write);
    public static final ComponentType<Dummy> GLIDER = ComponentTypes.define("glider", Dummy::dummyRead, Dummy::dummyWrite);
    public static final ComponentType<ItemDeathProtection> DEATH_PROTECTION = ComponentTypes.define("death_protection", ItemDeathProtection::read, ItemDeathProtection::write);
    public static final ComponentType<ItemTooltipStyle> TOOLTIP_STYLE = ComponentTypes.define("tooltip_style", ItemTooltipStyle::read, ItemTooltipStyle::write);
    public static final ComponentType<ItemTooltipDisplay> TOOLTIP_DISPLAY = ComponentTypes.define("tooltip_display", ItemTooltipDisplay::read, ItemTooltipDisplay::write);
    public static final ComponentType<ItemWeapon> WEAPON = ComponentTypes.define("weapon", ItemWeapon::read, ItemWeapon::write);
    public static final ComponentType<ItemBlocksAttacks> BLOCKS_ATTACKS = ComponentTypes.define("blocks_attacks", ItemBlocksAttacks::read, ItemBlocksAttacks::write);
    public static final ComponentType<ItemPotionDurationScale> POTION_DURATION_SCALE = ComponentTypes.define("potion_duration_scale", ItemPotionDurationScale::read, ItemPotionDurationScale::write);
    public static final ComponentType<ItemProvidesTrimMaterial> PROVIDES_TRIM_MATERIAL = ComponentTypes.define("provides_trim_material", ItemProvidesTrimMaterial::read, ItemProvidesTrimMaterial::write);
    public static final ComponentType<ItemProvidesBannerPatterns> PROVIDES_BANNER_PATTERNS = ComponentTypes.define("provides_banner_patterns", ItemProvidesBannerPatterns::read, ItemProvidesBannerPatterns::write);
    public static final ComponentType<ItemBreakSound> BREAK_SOUND = ComponentTypes.define("break_sound", ItemBreakSound::read, ItemBreakSound::write);
    public static final ComponentType<VillagerVariantComponent> VILLAGER_VARIANT = ComponentTypes.define("villager/variant", VillagerVariantComponent::read, VillagerVariantComponent::write);
    public static final ComponentType<WolfVariantComponent> WOLF_VARIANT = ComponentTypes.define("wolf/variant", WolfVariantComponent::read, WolfVariantComponent::write);
    public static final ComponentType<WolfSoundVariantComponent> WOLF_SOUND_VARIANT = ComponentTypes.define("wolf/sound_variant", WolfSoundVariantComponent::read, WolfSoundVariantComponent::write);
    public static final ComponentType<WolfCollarComponent> WOLF_COLLAR = ComponentTypes.define("wolf/collar", WolfCollarComponent::read, WolfCollarComponent::write);
    public static final ComponentType<FoxVariantComponent> FOX_VARIANT = ComponentTypes.define("fox/variant", FoxVariantComponent::read, FoxVariantComponent::write);
    public static final ComponentType<SalmonSizeComponent> SALMON_SIZE = ComponentTypes.define("salmon/size", SalmonSizeComponent::read, SalmonSizeComponent::write);
    public static final ComponentType<ParrotVariantComponent> PARROT_VARIANT = ComponentTypes.define("parrot/variant", ParrotVariantComponent::read, ParrotVariantComponent::write);
    public static final ComponentType<TropicalFishPatternComponent> TROPICAL_FISH_PATTERN = ComponentTypes.define("tropical_fish/pattern", TropicalFishPatternComponent::read, TropicalFishPatternComponent::write);
    public static final ComponentType<TropicalFishBaseColorComponent> TROPICAL_FISH_BASE_COLOR = ComponentTypes.define("tropical_fish/base_color", TropicalFishBaseColorComponent::read, TropicalFishBaseColorComponent::write);
    public static final ComponentType<TropicalFishPatternColorComponent> TROPICAL_FISH_PATTERN_COLOR = ComponentTypes.define("tropical_fish/pattern_color", TropicalFishPatternColorComponent::read, TropicalFishPatternColorComponent::write);
    public static final ComponentType<MooshroomVariantComponent> MOOSHROOM_VARIANT = ComponentTypes.define("mooshroom/variant", MooshroomVariantComponent::read, MooshroomVariantComponent::write);
    public static final ComponentType<RabbitVariantComponent> RABBIT_VARIANT = ComponentTypes.define("rabbit/variant", RabbitVariantComponent::read, RabbitVariantComponent::write);
    public static final ComponentType<PigVariantComponent> PIG_VARIANT = ComponentTypes.define("pig/variant", PigVariantComponent::read, PigVariantComponent::write);
    public static final ComponentType<CowVariantComponent> COW_VARIANT = ComponentTypes.define("cow/variant", CowVariantComponent::read, CowVariantComponent::write);
    public static final ComponentType<ChickenVariantComponent> CHICKEN_VARIANT = ComponentTypes.define("chicken/variant", ChickenVariantComponent::read, ChickenVariantComponent::write);
    public static final ComponentType<FrogVariantComponent> FROG_VARIANT = ComponentTypes.define("frog/variant", FrogVariantComponent::read, FrogVariantComponent::write);
    public static final ComponentType<HorseVariantComponent> HORSE_VARIANT = ComponentTypes.define("horse/variant", HorseVariantComponent::read, HorseVariantComponent::write);
    public static final ComponentType<PaintingVariantComponent> PAINTING_VARIANT = ComponentTypes.define("painting/variant", PaintingVariantComponent::read, PaintingVariantComponent::write);
    public static final ComponentType<LlamaVariantComponent> LLAMA_VARIANT = ComponentTypes.define("llama/variant", LlamaVariantComponent::read, LlamaVariantComponent::write);
    public static final ComponentType<AxolotlVariantComponent> AXOLOTL_VARIANT = ComponentTypes.define("axolotl/variant", AxolotlVariantComponent::read, AxolotlVariantComponent::write);
    public static final ComponentType<CatVariantComponent> CAT_VARIANT = ComponentTypes.define("cat/variant", CatVariantComponent::read, CatVariantComponent::write);
    public static final ComponentType<CatCollarComponent> CAT_COLLAR = ComponentTypes.define("cat/collar", CatCollarComponent::read, CatCollarComponent::write);
    public static final ComponentType<SheepColorComponent> SHEEP_COLOR = ComponentTypes.define("sheep/color", SheepColorComponent::read, SheepColorComponent::write);
    public static final ComponentType<ShulkerColorComponent> SHULKER_COLOR = ComponentTypes.define("shulker/color", ShulkerColorComponent::read, ShulkerColorComponent::write);
    public static final ComponentType<ItemUseEffects> USE_EFFECTS = ComponentTypes.define("use_effects", ItemUseEffects::read, ItemUseEffects::write);
    public static final ComponentType<ItemMinimumAttackCharge> MINIMUM_ATTACK_CHARGE = ComponentTypes.define("minimum_attack_charge", ItemMinimumAttackCharge::read, ItemMinimumAttackCharge::write);
    public static final ComponentType<ItemDamageType> DAMAGE_TYPE = ComponentTypes.define("damage_type", ItemDamageType::read, ItemDamageType::write);
    public static final ComponentType<ItemAttackRange> ATTACK_RANGE = ComponentTypes.define("attack_range", ItemAttackRange::read, ItemAttackRange::write);
    public static final ComponentType<ItemPiercingWeapon> PIERCING_WEAPON = ComponentTypes.define("piercing_weapon", ItemPiercingWeapon::read, ItemPiercingWeapon::write);
    public static final ComponentType<ItemKineticWeapon> KINETIC_WEAPON = ComponentTypes.define("kinetic_weapon", ItemKineticWeapon::read, ItemKineticWeapon::write);
    public static final ComponentType<ItemSwingAnimation> SWING_ANIMATION = ComponentTypes.define("swing_animation", ItemSwingAnimation::read, ItemSwingAnimation::write);
    public static final ComponentType<ZombieNautilusVariantComponent> ZOMBIE_NAUTILUS_VARIANT = ComponentTypes.define("zombie_nautilus/variant", ZombieNautilusVariantComponent::read, ZombieNautilusVariantComponent::write);

    private ComponentTypes() {
    }

    @ApiStatus.Internal
    public static <T> ComponentType<T> define(String key) {
        return ComponentTypes.define(key, null, null);
    }

    @ApiStatus.Internal
    public static <T> ComponentType<T> define(String key, @Nullable PacketWrapper.Reader<T> reader, @Nullable PacketWrapper.Writer<T> writer) {
        return REGISTRY.define(key, data -> new StaticComponentType((TypesBuilderData)data, reader, writer));
    }

    public static VersionedRegistry<ComponentType<?>> getRegistry() {
        return REGISTRY;
    }

    public static ComponentType<?> getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static ComponentType<?> getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    public static Collection<ComponentType<?>> values() {
        return REGISTRY.getEntries();
    }

    static {
        REGISTRY.unloadMappings();
    }
}

