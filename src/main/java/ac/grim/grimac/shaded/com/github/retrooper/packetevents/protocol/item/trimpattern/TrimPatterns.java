/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimpattern;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimpattern.StaticTrimPattern;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimpattern.TrimPattern;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.TranslatableComponent;
import org.jspecify.annotations.Nullable;

public final class TrimPatterns {
    private static final VersionedRegistry<TrimPattern> REGISTRY = new VersionedRegistry("trim_pattern");
    public static final TrimPattern COAST = TrimPatterns.define("coast");
    public static final TrimPattern DUNE = TrimPatterns.define("dune");
    public static final TrimPattern EYE = TrimPatterns.define("eye");
    public static final TrimPattern RIB = TrimPatterns.define("rib");
    public static final TrimPattern SENTRY = TrimPatterns.define("sentry");
    public static final TrimPattern SNOUT = TrimPatterns.define("snout");
    public static final TrimPattern SPIRE = TrimPatterns.define("spire");
    public static final TrimPattern TIDE = TrimPatterns.define("tide");
    public static final TrimPattern VEX = TrimPatterns.define("vex");
    public static final TrimPattern WARD = TrimPatterns.define("ward");
    public static final TrimPattern WILD = TrimPatterns.define("wild");
    public static final TrimPattern RAISER = TrimPatterns.define("raiser");
    public static final TrimPattern HOST = TrimPatterns.define("host");
    public static final TrimPattern SILENCE = TrimPatterns.define("silence");
    public static final TrimPattern SHAPER = TrimPatterns.define("shaper");
    public static final TrimPattern WAYFINDER = TrimPatterns.define("wayfinder");
    public static final TrimPattern BOLT = TrimPatterns.define("bolt");
    public static final TrimPattern FLOW = TrimPatterns.define("flow");

    private TrimPatterns() {
    }

    @ApiStatus.Internal
    public static TrimPattern define(String name) {
        ResourceLocation assetId = ResourceLocation.minecraft(name);
        ItemType templateItem = ItemTypes.getByName(assetId + "_armor_trim_smithing_template");
        TranslatableComponent description = Component.translatable("trim_pattern.minecraft." + name);
        boolean decal = false;
        return TrimPatterns.define(name, assetId, templateItem, description, decal);
    }

    @ApiStatus.Internal
    public static TrimPattern define(String name, ResourceLocation assetId, @Nullable ItemType templateItem, Component description, boolean decal) {
        return REGISTRY.define(name, data -> new StaticTrimPattern((TypesBuilderData)data, assetId, templateItem, description, decal));
    }

    public static VersionedRegistry<TrimPattern> getRegistry() {
        return REGISTRY;
    }

    public static TrimPattern getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static TrimPattern getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    static {
        REGISTRY.unloadMappings();
    }
}

