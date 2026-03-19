/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.cat;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.cat.CatVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.cat.StaticCatVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class CatVariants {
    private static final VersionedRegistry<CatVariant> REGISTRY = new VersionedRegistry("cat_variant");
    public static final CatVariant ALL_BLACK = CatVariants.define("all_black");
    public static final CatVariant BLACK = CatVariants.define("black");
    public static final CatVariant BRITISH_SHORTHAIR = CatVariants.define("british_shorthair");
    public static final CatVariant CALICO = CatVariants.define("calico");
    public static final CatVariant JELLIE = CatVariants.define("jellie");
    public static final CatVariant PERSIAN = CatVariants.define("persian");
    public static final CatVariant RAGDOLL = CatVariants.define("ragdoll");
    public static final CatVariant RED = CatVariants.define("red");
    public static final CatVariant SIAMESE = CatVariants.define("siamese");
    public static final CatVariant TABBY = CatVariants.define("tabby");
    public static final CatVariant WHITE = CatVariants.define("white");

    private CatVariants() {
    }

    @ApiStatus.Internal
    public static CatVariant define(String name) {
        ResourceLocation assetId = new ResourceLocation("entity/cat/" + name);
        return REGISTRY.define(name, data -> new StaticCatVariant((TypesBuilderData)data, assetId));
    }

    public static VersionedRegistry<CatVariant> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}

