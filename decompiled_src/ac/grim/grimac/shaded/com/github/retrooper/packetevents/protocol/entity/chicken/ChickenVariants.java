/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.chicken;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.chicken.ChickenVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.chicken.StaticChickenVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class ChickenVariants {
    private static final VersionedRegistry<ChickenVariant> REGISTRY = new VersionedRegistry("chicken_variant");
    public static final ChickenVariant COLD = ChickenVariants.define("cold", ChickenVariant.ModelType.COLD, "cold_chicken");
    public static final ChickenVariant TEMPERATE = ChickenVariants.define("temperate", ChickenVariant.ModelType.NORMAL, "temperate_chicken");
    public static final ChickenVariant WARM = ChickenVariants.define("warm", ChickenVariant.ModelType.NORMAL, "warm_chicken");

    private ChickenVariants() {
    }

    @ApiStatus.Internal
    public static ChickenVariant define(String name, ChickenVariant.ModelType modelType, String texture) {
        ResourceLocation assetId = new ResourceLocation("entity/chicken/" + texture);
        return REGISTRY.define(name, data -> new StaticChickenVariant((TypesBuilderData)data, modelType, assetId));
    }

    public static VersionedRegistry<ChickenVariant> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}

