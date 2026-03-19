/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables.ConsumeEffect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables.ConsumeEffectType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables.StaticConsumeEffectType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables.builtin.ApplyEffectsConsumeEffect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables.builtin.ClearAllEffectsConsumeEffect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables.builtin.PlaySoundConsumeEffect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables.builtin.RemoveEffectsConsumeEffect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables.builtin.TeleportRandomlyConsumeEffect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class ConsumeEffectTypes {
    private static final VersionedRegistry<ConsumeEffectType<?>> REGISTRY = new VersionedRegistry("consume_effect_type");
    public static final ConsumeEffectType<ApplyEffectsConsumeEffect> APPLY_EFFECTS = ConsumeEffectTypes.define("apply_effects", ApplyEffectsConsumeEffect::read, ApplyEffectsConsumeEffect::write);
    public static final ConsumeEffectType<RemoveEffectsConsumeEffect> REMOVE_EFFECTS = ConsumeEffectTypes.define("remove_effects", RemoveEffectsConsumeEffect::read, RemoveEffectsConsumeEffect::write);
    public static final ConsumeEffectType<ClearAllEffectsConsumeEffect> CLEAR_ALL_EFFECTS = ConsumeEffectTypes.define("clear_all_effects", ClearAllEffectsConsumeEffect::read, ClearAllEffectsConsumeEffect::write);
    public static final ConsumeEffectType<TeleportRandomlyConsumeEffect> TELEPORT_RANDOMLY = ConsumeEffectTypes.define("teleport_randomly", TeleportRandomlyConsumeEffect::read, TeleportRandomlyConsumeEffect::write);
    public static final ConsumeEffectType<PlaySoundConsumeEffect> PLAY_SOUND = ConsumeEffectTypes.define("play_sound", PlaySoundConsumeEffect::read, PlaySoundConsumeEffect::write);

    private ConsumeEffectTypes() {
    }

    @ApiStatus.Internal
    public static <T extends ConsumeEffect<?>> ConsumeEffectType<T> define(String name, PacketWrapper.Reader<T> reader, PacketWrapper.Writer<T> writer) {
        return REGISTRY.define(name, data -> new StaticConsumeEffectType((TypesBuilderData)data, reader, writer));
    }

    public static VersionedRegistry<ConsumeEffectType<?>> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}

