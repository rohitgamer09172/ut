/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant.StaticWolfSoundVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant.WolfSoundVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sounds;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class WolfSoundVariants {
    private static final VersionedRegistry<WolfSoundVariant> REGISTRY = new VersionedRegistry("wolf_sound_variant");
    public static final WolfSoundVariant CLASSIC = WolfSoundVariants.define("classic", "");
    public static final WolfSoundVariant PUGLIN = WolfSoundVariants.define("puglin", "_puglin");
    public static final WolfSoundVariant SAD = WolfSoundVariants.define("sad", "_sad");
    public static final WolfSoundVariant ANGRY = WolfSoundVariants.define("angry", "_angry");
    public static final WolfSoundVariant GRUMPY = WolfSoundVariants.define("grumpy", "_grumpy");
    public static final WolfSoundVariant BIG = WolfSoundVariants.define("big", "_big");
    public static final WolfSoundVariant CUTE = WolfSoundVariants.define("cute", "_cute");

    private WolfSoundVariants() {
    }

    public static VersionedRegistry<WolfSoundVariant> getRegistry() {
        return REGISTRY;
    }

    @ApiStatus.Internal
    public static WolfSoundVariant define(String name, String suffix) {
        return WolfSoundVariants.define(name, Sounds.getByName("entity.wolf" + suffix + ".ambient"), Sounds.getByName("entity.wolf" + suffix + ".death"), Sounds.getByName("entity.wolf" + suffix + ".growl"), Sounds.getByName("entity.wolf" + suffix + ".hurt"), Sounds.getByName("entity.wolf" + suffix + ".pant"), Sounds.getByName("entity.wolf" + suffix + ".whine"));
    }

    @ApiStatus.Internal
    public static WolfSoundVariant define(String name, Sound ambientSound, Sound deathSound, Sound growlSound, Sound hurtSound, Sound pantSound, Sound whineSound) {
        return REGISTRY.define(name, data -> new StaticWolfSoundVariant((TypesBuilderData)data, ambientSound, deathSound, growlSound, hurtSound, pantSound, whineSound));
    }

    static {
        REGISTRY.unloadMappings();
    }
}

