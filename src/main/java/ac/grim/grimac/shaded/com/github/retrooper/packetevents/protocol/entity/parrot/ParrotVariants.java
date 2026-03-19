/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.parrot;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.parrot.ParrotVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.parrot.StaticParrotVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class ParrotVariants {
    private static final VersionedRegistry<ParrotVariant> REGISTRY = new VersionedRegistry("parrot_variant");
    public static final ParrotVariant RED_BLUE = ParrotVariants.define("red_blue");
    public static final ParrotVariant BLUE = ParrotVariants.define("blue");
    public static final ParrotVariant GREEN = ParrotVariants.define("green");
    public static final ParrotVariant YELLOW_BLUE = ParrotVariants.define("yellow_blue");
    public static final ParrotVariant GRAY = ParrotVariants.define("gray");

    private ParrotVariants() {
    }

    @ApiStatus.Internal
    public static ParrotVariant define(String name) {
        return REGISTRY.define(name, StaticParrotVariant::new);
    }

    public static VersionedRegistry<ParrotVariant> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}

