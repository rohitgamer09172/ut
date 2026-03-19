/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.MappableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.Map;
import java.util.Objects;

final class MapTagResolver
implements TagResolver.WithoutArguments,
MappableResolver {
    private final Map<String, ? extends Tag> tagMap;

    MapTagResolver(@NotNull Map<String, ? extends Tag> placeholderMap) {
        this.tagMap = placeholderMap;
    }

    @Override
    @Nullable
    public Tag resolve(@NotNull String name) {
        return this.tagMap.get(name);
    }

    @Override
    public boolean has(@NotNull String name) {
        return this.tagMap.containsKey(name);
    }

    @Override
    public boolean contributeToMap(@NotNull Map<String, Tag> map) {
        map.putAll(this.tagMap);
        return true;
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MapTagResolver)) {
            return false;
        }
        MapTagResolver that = (MapTagResolver)other;
        return Objects.equals(this.tagMap, that.tagMap);
    }

    public int hashCode() {
        return Objects.hash(this.tagMap);
    }
}

