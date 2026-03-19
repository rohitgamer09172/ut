/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.axolotl;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.axolotl.AxolotlVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.axolotl.StaticAxolotlVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class AxolotlVariants {
    private static final VersionedRegistry<AxolotlVariant> REGISTRY = new VersionedRegistry("axolotl_variant");
    public static final AxolotlVariant LUCY = AxolotlVariants.define("lucy");
    public static final AxolotlVariant WILD = AxolotlVariants.define("wild");
    public static final AxolotlVariant GOLD = AxolotlVariants.define("gold");
    public static final AxolotlVariant CYAN = AxolotlVariants.define("cyan");
    public static final AxolotlVariant BLUE = AxolotlVariants.define("blue");

    private AxolotlVariants() {
    }

    @ApiStatus.Internal
    public static AxolotlVariant define(String name) {
        return REGISTRY.define(name, StaticAxolotlVariant::new);
    }

    public static VersionedRegistry<AxolotlVariant> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}

