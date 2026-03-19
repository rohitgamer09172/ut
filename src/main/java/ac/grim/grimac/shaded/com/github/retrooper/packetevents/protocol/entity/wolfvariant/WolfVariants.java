/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant.StaticWolfVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant.WolfVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.biome.Biome;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.biome.Biomes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Collections;

public final class WolfVariants {
    private static final VersionedRegistry<WolfVariant> REGISTRY = new VersionedRegistry("wolf_variant");
    public static final WolfVariant PALE = WolfVariants.define("pale", "wolf", new MappedEntitySet<Biome>(Collections.singletonList(Biomes.TAIGA)));
    public static final WolfVariant SPOTTED = WolfVariants.define("spotted", new MappedEntitySet<Biome>(ResourceLocation.minecraft("is_savanna")));
    public static final WolfVariant SNOWY = WolfVariants.define("snowy", new MappedEntitySet<Biome>(Collections.singletonList(Biomes.GROVE)));
    public static final WolfVariant BLACK = WolfVariants.define("black", new MappedEntitySet<Biome>(Collections.singletonList(Biomes.OLD_GROWTH_PINE_TAIGA)));
    public static final WolfVariant ASHEN = WolfVariants.define("ashen", new MappedEntitySet<Biome>(Collections.singletonList(Biomes.SNOWY_TAIGA)));
    public static final WolfVariant RUSTY = WolfVariants.define("rusty", new MappedEntitySet<Biome>(ResourceLocation.minecraft("is_jungle")));
    public static final WolfVariant WOODS = WolfVariants.define("woods", new MappedEntitySet<Biome>(Collections.singletonList(Biomes.FOREST)));
    public static final WolfVariant CHESTNUT = WolfVariants.define("chestnut", new MappedEntitySet<Biome>(Collections.singletonList(Biomes.OLD_GROWTH_SPRUCE_TAIGA)));
    public static final WolfVariant STRIPED = WolfVariants.define("striped", new MappedEntitySet<Biome>(ResourceLocation.minecraft("is_badlands")));

    private WolfVariants() {
    }

    @ApiStatus.Internal
    public static WolfVariant define(String name, MappedEntitySet<Biome> biomes) {
        return WolfVariants.define(name, "wolf_" + name, biomes);
    }

    @ApiStatus.Internal
    public static WolfVariant define(String name, String assetId, MappedEntitySet<Biome> biomes) {
        return WolfVariants.define(name, ResourceLocation.minecraft("entity/wolf/" + assetId), ResourceLocation.minecraft("entity/wolf/" + assetId + "_tame"), ResourceLocation.minecraft("entity/wolf/" + assetId + "_angry"), biomes);
    }

    @ApiStatus.Internal
    public static WolfVariant define(String name, ResourceLocation wildTexture, ResourceLocation tameTexture, ResourceLocation angryTexture, MappedEntitySet<Biome> biomes) {
        return REGISTRY.define(name, data -> new StaticWolfVariant((TypesBuilderData)data, wildTexture, tameTexture, angryTexture, biomes));
    }

    public static VersionedRegistry<WolfVariant> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}

