/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.nautilus;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.nautilus.StaticZombieNautilusVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.nautilus.ZombieNautilusVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ZombieNautilusVariants {
    private static final VersionedRegistry<ZombieNautilusVariant> REGISTRY = new VersionedRegistry("zombie_nautilus_variant");
    public static final ZombieNautilusVariant TEMPERATE = ZombieNautilusVariants.define("temperate", ZombieNautilusVariant.ModelType.NORMAL, "zombie_nautilus");
    public static final ZombieNautilusVariant WARM = ZombieNautilusVariants.define("warm", ZombieNautilusVariant.ModelType.WARM, "zombie_nautilus_coral");

    private ZombieNautilusVariants() {
    }

    @ApiStatus.Internal
    public static ZombieNautilusVariant define(String name, ZombieNautilusVariant.ModelType modelType, String texture) {
        ResourceLocation assetId = new ResourceLocation("entity/nautilus/" + texture);
        return REGISTRY.define(name, data -> new StaticZombieNautilusVariant((TypesBuilderData)data, modelType, assetId));
    }

    public static VersionedRegistry<ZombieNautilusVariant> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}

