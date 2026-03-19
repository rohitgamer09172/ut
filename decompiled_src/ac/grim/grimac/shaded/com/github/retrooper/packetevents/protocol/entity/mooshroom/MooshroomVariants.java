/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.mooshroom;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.mooshroom.MooshroomVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.mooshroom.StaticMooshroomVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class MooshroomVariants {
    private static final VersionedRegistry<MooshroomVariant> REGISTRY = new VersionedRegistry("mooshroom_variant");
    public static final MooshroomVariant RED = MooshroomVariants.define("red");
    public static final MooshroomVariant BROWN = MooshroomVariants.define("brown");

    private MooshroomVariants() {
    }

    @ApiStatus.Internal
    public static MooshroomVariant define(String name) {
        return REGISTRY.define(name, StaticMooshroomVariant::new);
    }

    public static VersionedRegistry<MooshroomVariant> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}

