/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.instrument;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.instrument.Instrument;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.instrument.StaticInstrument;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sounds;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;

public final class Instruments {
    private static final VersionedRegistry<Instrument> REGISTRY = new VersionedRegistry("instrument");
    public static final Instrument PONDER_GOAT_HORN = Instruments.define("ponder_goat_horn", Sounds.ITEM_GOAT_HORN_SOUND_0);
    public static final Instrument SING_GOAT_HORN = Instruments.define("sing_goat_horn", Sounds.ITEM_GOAT_HORN_SOUND_1);
    public static final Instrument SEEK_GOAT_HORN = Instruments.define("seek_goat_horn", Sounds.ITEM_GOAT_HORN_SOUND_2);
    public static final Instrument FEEL_GOAT_HORN = Instruments.define("feel_goat_horn", Sounds.ITEM_GOAT_HORN_SOUND_3);
    public static final Instrument ADMIRE_GOAT_HORN = Instruments.define("admire_goat_horn", Sounds.ITEM_GOAT_HORN_SOUND_4);
    public static final Instrument CALL_GOAT_HORN = Instruments.define("call_goat_horn", Sounds.ITEM_GOAT_HORN_SOUND_5);
    public static final Instrument YEARN_GOAT_HORN = Instruments.define("yearn_goat_horn", Sounds.ITEM_GOAT_HORN_SOUND_6);
    public static final Instrument DREAM_GOAT_HORN = Instruments.define("dream_goat_horn", Sounds.ITEM_GOAT_HORN_SOUND_7);

    private Instruments() {
    }

    @ApiStatus.Internal
    public static Instrument define(String key, Sound sound) {
        return Instruments.define(key, sound, 140, 256.0f);
    }

    @ApiStatus.Internal
    public static Instrument define(String key, Sound sound, int useDuration, float range) {
        return REGISTRY.define(key, data -> new StaticInstrument((TypesBuilderData)data, sound, useDuration, range, Component.translatable("instrument.minecraft." + key)));
    }

    public static VersionedRegistry<Instrument> getRegistry() {
        return REGISTRY;
    }

    public static Instrument getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static Instrument getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    static {
        REGISTRY.unloadMappings();
    }
}

