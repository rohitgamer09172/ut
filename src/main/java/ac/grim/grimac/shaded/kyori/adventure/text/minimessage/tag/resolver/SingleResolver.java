/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.MappableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.Map;
import java.util.Objects;

final class SingleResolver
implements TagResolver.Single,
MappableResolver {
    private final String key;
    private final Tag tag;

    SingleResolver(String key, Tag tag) {
        this.key = key;
        this.tag = tag;
    }

    @Override
    @NotNull
    public String key() {
        return this.key;
    }

    @Override
    @NotNull
    public Tag tag() {
        return this.tag;
    }

    @Override
    public boolean has(@NotNull String name) {
        return this.key.equals(name);
    }

    @Override
    public boolean contributeToMap(@NotNull Map<String, Tag> map) {
        map.put(this.key, this.tag);
        return true;
    }

    public int hashCode() {
        return Objects.hash(this.key, this.tag);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (this.getClass() != other.getClass()) {
            return false;
        }
        SingleResolver that = (SingleResolver)other;
        return Objects.equals(this.key, that.key) && Objects.equals(this.tag, that.tag);
    }
}

