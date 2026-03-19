/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.tropicalfish;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.tropicalfish.StaticTropicalFishPattern;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.tropicalfish.TropicalFishPattern;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Collection;

public final class TropicalFishPatterns {
    private static final VersionedRegistry<TropicalFishPattern> REGISTRY = new VersionedRegistry("tropical_fish_pattern");
    public static final TropicalFishPattern KOB = TropicalFishPatterns.define("kob", TropicalFishPattern.Base.SMALL);
    public static final TropicalFishPattern SUNSTREAK = TropicalFishPatterns.define("sunstreak", TropicalFishPattern.Base.SMALL);
    public static final TropicalFishPattern SNOOPER = TropicalFishPatterns.define("snooper", TropicalFishPattern.Base.SMALL);
    public static final TropicalFishPattern DASHER = TropicalFishPatterns.define("dasher", TropicalFishPattern.Base.SMALL);
    public static final TropicalFishPattern BRINELY = TropicalFishPatterns.define("brinely", TropicalFishPattern.Base.SMALL);
    public static final TropicalFishPattern SPOTTY = TropicalFishPatterns.define("spotty", TropicalFishPattern.Base.SMALL);
    public static final TropicalFishPattern FLOPPER = TropicalFishPatterns.define("flopper", TropicalFishPattern.Base.LARGE);
    public static final TropicalFishPattern STRIPEY = TropicalFishPatterns.define("stripey", TropicalFishPattern.Base.LARGE);
    public static final TropicalFishPattern GLITTER = TropicalFishPatterns.define("glitter", TropicalFishPattern.Base.LARGE);
    public static final TropicalFishPattern BLOCKFISH = TropicalFishPatterns.define("blockfish", TropicalFishPattern.Base.LARGE);
    public static final TropicalFishPattern BETTY = TropicalFishPatterns.define("betty", TropicalFishPattern.Base.LARGE);
    public static final TropicalFishPattern CLAYFISH = TropicalFishPatterns.define("clayfish", TropicalFishPattern.Base.LARGE);

    private TropicalFishPatterns() {
    }

    @ApiStatus.Internal
    public static TropicalFishPattern define(String name, TropicalFishPattern.Base base) {
        return REGISTRY.define(name, typesBuilderData -> new StaticTropicalFishPattern((TypesBuilderData)typesBuilderData, base));
    }

    public static VersionedRegistry<TropicalFishPattern> getRegistry() {
        return REGISTRY;
    }

    public static Collection<TropicalFishPattern> values() {
        return REGISTRY.getEntries();
    }

    static {
        REGISTRY.unloadMappings();
    }
}

