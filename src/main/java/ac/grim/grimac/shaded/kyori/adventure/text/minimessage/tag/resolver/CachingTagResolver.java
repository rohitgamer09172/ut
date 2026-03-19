/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.ClaimConsumer;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.MappableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

final class CachingTagResolver
implements TagResolver.WithoutArguments,
MappableResolver,
SerializableResolver {
    private static final Tag NULL_REPLACEMENT = () -> {
        throw new UnsupportedOperationException("no-op null tag");
    };
    private final Map<String, Tag> cache = new HashMap<String, Tag>();
    private final TagResolver.WithoutArguments resolver;

    CachingTagResolver(TagResolver.WithoutArguments resolver) {
        this.resolver = resolver;
    }

    private Tag query(@NotNull String key) {
        return this.cache.computeIfAbsent(key, k -> {
            @Nullable Tag result = this.resolver.resolve((String)k);
            return result == null ? NULL_REPLACEMENT : result;
        });
    }

    @Override
    @Nullable
    public Tag resolve(@NotNull String name) {
        Tag potentialValue = this.query(name);
        return potentialValue == NULL_REPLACEMENT ? null : potentialValue;
    }

    @Override
    public boolean has(@NotNull String name) {
        return this.query(name) != NULL_REPLACEMENT;
    }

    @Override
    public boolean contributeToMap(@NotNull Map<String, Tag> map) {
        if (this.resolver instanceof MappableResolver) {
            return ((MappableResolver)((Object)this.resolver)).contributeToMap(map);
        }
        return false;
    }

    @Override
    public void handle(@NotNull Component serializable, @NotNull ClaimConsumer consumer) {
        if (this.resolver instanceof SerializableResolver) {
            ((SerializableResolver)((Object)this.resolver)).handle(serializable, consumer);
        }
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CachingTagResolver)) {
            return false;
        }
        CachingTagResolver that = (CachingTagResolver)other;
        return Objects.equals(this.resolver, that.resolver);
    }

    public int hashCode() {
        return Objects.hash(this.resolver);
    }
}

