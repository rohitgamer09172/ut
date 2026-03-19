/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.frog;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.frog.FrogVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.frog.StaticFrogVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class FrogVariants {
    private static final VersionedRegistry<FrogVariant> REGISTRY = new VersionedRegistry("frog_variant");
    public static final FrogVariant COLD = FrogVariants.define("cold", "cold_frog");
    public static final FrogVariant TEMPERATE = FrogVariants.define("temperate", "temperate_frog");
    public static final FrogVariant WARM = FrogVariants.define("warm", "warm_frog");

    private FrogVariants() {
    }

    @ApiStatus.Internal
    public static FrogVariant define(String name, String texture) {
        ResourceLocation assetId = new ResourceLocation("entity/frog/" + texture);
        return REGISTRY.define(name, data -> new StaticFrogVariant((TypesBuilderData)data, assetId));
    }

    public static VersionedRegistry<FrogVariant> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}

