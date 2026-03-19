/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.TagInternals;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.EmptyTagResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.MapTagResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.MappableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.SequentialTagResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;

final class TagResolverBuilderImpl
implements TagResolver.Builder {
    static final Collector<TagResolver, TagResolver.Builder, TagResolver> COLLECTOR = Collector.of(TagResolver::builder, TagResolver.Builder::resolver, (left, right) -> TagResolver.builder().resolvers(left.build(), right.build()), TagResolver.Builder::build, new Collector.Characteristics[0]);
    private final Map<String, Tag> replacements = new HashMap<String, Tag>();
    private final List<TagResolver> resolvers = new ArrayList<TagResolver>();

    TagResolverBuilderImpl() {
    }

    @Override
    public @NotNull TagResolver.Builder tag(@NotNull String name, @NotNull Tag tag) {
        TagInternals.assertValidTagName(Objects.requireNonNull(name, "name"));
        this.replacements.put(name, Objects.requireNonNull(tag, "tag"));
        return this;
    }

    @Override
    public @NotNull TagResolver.Builder resolver(@NotNull TagResolver resolver) {
        if (resolver instanceof SequentialTagResolver) {
            this.resolvers(((SequentialTagResolver)resolver).resolvers, false);
        } else if (!this.consumePotentialMappable(resolver)) {
            this.popMap();
            this.resolvers.add(Objects.requireNonNull(resolver, "resolver"));
        }
        return this;
    }

    @Override
    public @NotNull TagResolver.Builder resolvers(TagResolver ... resolvers) {
        return this.resolvers(resolvers, true);
    }

    private @NotNull TagResolver.Builder resolvers(@NotNull @NotNull TagResolver @NotNull [] resolvers, boolean forwards) {
        boolean popped = false;
        Objects.requireNonNull(resolvers, "resolvers");
        if (forwards) {
            for (TagResolver resolver : resolvers) {
                popped = this.single(resolver, popped);
            }
        } else {
            for (int i = resolvers.length - 1; i >= 0; --i) {
                popped = this.single(resolvers[i], popped);
            }
        }
        return this;
    }

    @Override
    public @NotNull TagResolver.Builder resolvers(@NotNull Iterable<? extends TagResolver> resolvers) {
        boolean popped = false;
        for (TagResolver tagResolver : Objects.requireNonNull(resolvers, "resolvers")) {
            popped = this.single(tagResolver, popped);
        }
        return this;
    }

    private boolean single(TagResolver resolver, boolean popped) {
        if (resolver instanceof SequentialTagResolver) {
            this.resolvers(((SequentialTagResolver)resolver).resolvers, false);
        } else if (!this.consumePotentialMappable(resolver)) {
            if (!popped) {
                this.popMap();
            }
            this.resolvers.add(Objects.requireNonNull(resolver, "resolvers[?]"));
            return true;
        }
        return false;
    }

    private void popMap() {
        if (!this.replacements.isEmpty()) {
            this.resolvers.add(new MapTagResolver(new HashMap<String, Tag>(this.replacements)));
            this.replacements.clear();
        }
    }

    private boolean consumePotentialMappable(TagResolver resolver) {
        if (resolver instanceof MappableResolver) {
            return ((MappableResolver)((Object)resolver)).contributeToMap(this.replacements);
        }
        return false;
    }

    @Override
    @NotNull
    public TagResolver build() {
        this.popMap();
        if (this.resolvers.size() == 0) {
            return EmptyTagResolver.INSTANCE;
        }
        if (this.resolvers.size() == 1) {
            return this.resolvers.get(0);
        }
        TagResolver[] resolvers = this.resolvers.toArray(new TagResolver[0]);
        Collections.reverse(Arrays.asList(resolvers));
        return new SequentialTagResolver(resolvers);
    }
}

