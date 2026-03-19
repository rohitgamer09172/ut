/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.horse;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.horse.HorseVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.horse.StaticHorseVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class HorseVariants {
    private static final VersionedRegistry<HorseVariant> REGISTRY = new VersionedRegistry("horse_variant");
    public static final HorseVariant WHITE = HorseVariants.define("white");
    public static final HorseVariant CREAMY = HorseVariants.define("creamy");
    public static final HorseVariant CHESTNUT = HorseVariants.define("chestnut");
    public static final HorseVariant BROWN = HorseVariants.define("brown");
    public static final HorseVariant BLACK = HorseVariants.define("black");
    public static final HorseVariant GRAY = HorseVariants.define("gray");
    public static final HorseVariant DARK_BROWN = HorseVariants.define("dark_brown");

    private HorseVariants() {
    }

    @ApiStatus.Internal
    public static HorseVariant define(String name) {
        return REGISTRY.define(name, StaticHorseVariant::new);
    }

    public static VersionedRegistry<HorseVariant> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}

