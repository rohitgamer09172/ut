/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.banner;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.banner.BannerPattern;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.banner.StaticBannerPattern;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Collection;

public final class BannerPatterns {
    private static final VersionedRegistry<BannerPattern> REGISTRY = new VersionedRegistry("banner_pattern");
    public static final BannerPattern SQUARE_BOTTOM_LEFT = BannerPatterns.define("square_bottom_left");
    public static final BannerPattern STRIPE_BOTTOM = BannerPatterns.define("stripe_bottom");
    public static final BannerPattern CREEPER = BannerPatterns.define("creeper");
    public static final BannerPattern HALF_HORIZONTAL = BannerPatterns.define("half_horizontal");
    public static final BannerPattern STRIPE_MIDDLE = BannerPatterns.define("stripe_middle");
    public static final BannerPattern BASE = BannerPatterns.define("base");
    public static final BannerPattern DIAGONAL_UP_RIGHT = BannerPatterns.define("diagonal_up_right");
    public static final BannerPattern HALF_HORIZONTAL_BOTTOM = BannerPatterns.define("half_horizontal_bottom");
    public static final BannerPattern SMALL_STRIPES = BannerPatterns.define("small_stripes");
    public static final BannerPattern GRADIENT_UP = BannerPatterns.define("gradient_up");
    public static final BannerPattern CIRCLE = BannerPatterns.define("circle");
    public static final BannerPattern STRIPE_DOWNLEFT = BannerPatterns.define("stripe_downleft");
    public static final BannerPattern RHOMBUS = BannerPatterns.define("rhombus");
    public static final BannerPattern TRIANGLES_BOTTOM = BannerPatterns.define("triangles_bottom");
    public static final BannerPattern STRIPE_CENTER = BannerPatterns.define("stripe_center");
    public static final BannerPattern SQUARE_BOTTOM_RIGHT = BannerPatterns.define("square_bottom_right");
    public static final BannerPattern DIAGONAL_RIGHT = BannerPatterns.define("diagonal_right");
    public static final BannerPattern MOJANG = BannerPatterns.define("mojang");
    public static final BannerPattern STRIPE_LEFT = BannerPatterns.define("stripe_left");
    public static final BannerPattern SQUARE_TOP_LEFT = BannerPatterns.define("square_top_left");
    public static final BannerPattern TRIANGLE_BOTTOM = BannerPatterns.define("triangle_bottom");
    public static final BannerPattern SKULL = BannerPatterns.define("skull");
    public static final BannerPattern SQUARE_TOP_RIGHT = BannerPatterns.define("square_top_right");
    public static final BannerPattern GLOBE = BannerPatterns.define("globe");
    public static final BannerPattern STRIPE_TOP = BannerPatterns.define("stripe_top");
    public static final BannerPattern CROSS = BannerPatterns.define("cross");
    public static final BannerPattern BRICKS = BannerPatterns.define("bricks");
    public static final BannerPattern HALF_VERTICAL = BannerPatterns.define("half_vertical");
    public static final BannerPattern STRIPE_DOWNRIGHT = BannerPatterns.define("stripe_downright");
    public static final BannerPattern TRIANGLES_TOP = BannerPatterns.define("triangles_top");
    public static final BannerPattern STRIPE_RIGHT = BannerPatterns.define("stripe_right");
    public static final BannerPattern DIAGONAL_UP_LEFT = BannerPatterns.define("diagonal_up_left");
    public static final BannerPattern HALF_VERTICAL_RIGHT = BannerPatterns.define("half_vertical_right");
    public static final BannerPattern TRIANGLE_TOP = BannerPatterns.define("triangle_top");
    public static final BannerPattern FLOWER = BannerPatterns.define("flower");
    public static final BannerPattern STRAIGHT_CROSS = BannerPatterns.define("straight_cross");
    public static final BannerPattern GRADIENT = BannerPatterns.define("gradient");
    public static final BannerPattern CURLY_BORDER = BannerPatterns.define("curly_border");
    public static final BannerPattern BORDER = BannerPatterns.define("border");
    public static final BannerPattern PIGLIN = BannerPatterns.define("piglin");
    public static final BannerPattern DIAGONAL_LEFT = BannerPatterns.define("diagonal_left");
    public static final BannerPattern FLOW = BannerPatterns.define("flow");
    public static final BannerPattern GUSTER = BannerPatterns.define("guster");

    private BannerPatterns() {
    }

    @ApiStatus.Internal
    public static BannerPattern define(String key) {
        ResourceLocation assetId = ResourceLocation.minecraft(key);
        String translationKey = "block.minecraft.banner." + key;
        return BannerPatterns.define(key, assetId, translationKey);
    }

    @ApiStatus.Internal
    public static BannerPattern define(String key, ResourceLocation assetId, String translationKey) {
        return REGISTRY.define(key, data -> new StaticBannerPattern((TypesBuilderData)data, assetId, translationKey));
    }

    public static VersionedRegistry<BannerPattern> getRegistry() {
        return REGISTRY;
    }

    public static BannerPattern getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static BannerPattern getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    public static Collection<BannerPattern> values() {
        return REGISTRY.getEntries();
    }

    static {
        REGISTRY.unloadMappings();
    }
}

