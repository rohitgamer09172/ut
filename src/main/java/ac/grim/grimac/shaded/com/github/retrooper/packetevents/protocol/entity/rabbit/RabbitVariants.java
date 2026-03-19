/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.rabbit;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.rabbit.RabbitVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.rabbit.StaticRabbitVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class RabbitVariants {
    private static final VersionedRegistry<RabbitVariant> REGISTRY = new VersionedRegistry("rabbit_variant");
    public static final RabbitVariant BROWN = RabbitVariants.define("brown");
    public static final RabbitVariant WHITE = RabbitVariants.define("white");
    public static final RabbitVariant BLACK = RabbitVariants.define("black");
    public static final RabbitVariant WHITE_SPLOTCHED = RabbitVariants.define("white_splotched");
    public static final RabbitVariant GOLD = RabbitVariants.define("gold");
    public static final RabbitVariant SALT = RabbitVariants.define("salt");
    public static final RabbitVariant EVIL = RabbitVariants.define("evil");

    private RabbitVariants() {
    }

    @ApiStatus.Internal
    public static RabbitVariant define(String name) {
        return REGISTRY.define(name, StaticRabbitVariant::new);
    }

    public static VersionedRegistry<RabbitVariant> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}

