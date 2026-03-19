/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.fox;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.fox.FoxVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.fox.StaticFoxVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class FoxVariants {
    private static final VersionedRegistry<FoxVariant> REGISTRY = new VersionedRegistry("fox_variant");
    public static final FoxVariant RED = FoxVariants.define("red");
    public static final FoxVariant SNOW = FoxVariants.define("snow");

    private FoxVariants() {
    }

    @ApiStatus.Internal
    public static FoxVariant define(String name) {
        return REGISTRY.define(name, StaticFoxVariant::new);
    }

    public static VersionedRegistry<FoxVariant> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}

