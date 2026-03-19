/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.llama;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.llama.LlamaVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.llama.StaticLlamaVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public final class LlamaVariants {
    private static final VersionedRegistry<LlamaVariant> REGISTRY = new VersionedRegistry("llama_variant");
    public static final LlamaVariant CREAMY = LlamaVariants.define("creamy");
    public static final LlamaVariant WHITE = LlamaVariants.define("white");
    public static final LlamaVariant BROWN = LlamaVariants.define("brown");
    public static final LlamaVariant GRAY = LlamaVariants.define("gray");

    private LlamaVariants() {
    }

    @ApiStatus.Internal
    public static LlamaVariant define(String name) {
        return REGISTRY.define(name, StaticLlamaVariant::new);
    }

    public static VersionedRegistry<LlamaVariant> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}

