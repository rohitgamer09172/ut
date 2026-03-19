/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.ParsingException;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

class ComponentClaimingResolverImpl
implements TagResolver,
SerializableResolver.Single {
    @NotNull
    private final Set<String> names;
    @NotNull
    private final BiFunction<ArgumentQueue, Context, Tag> handler;
    private final @NotNull Function<Component, @Nullable Emitable> componentClaim;

    ComponentClaimingResolverImpl(Set<String> names, BiFunction<ArgumentQueue, Context, Tag> handler, Function<Component, @Nullable Emitable> componentClaim) {
        this.names = names;
        this.handler = handler;
        this.componentClaim = componentClaim;
    }

    @Override
    @Nullable
    public Tag resolve(@NotNull String name, @NotNull ArgumentQueue arguments, @NotNull Context ctx) throws ParsingException {
        if (!this.names.contains(name)) {
            return null;
        }
        return this.handler.apply(arguments, ctx);
    }

    @Override
    public boolean has(@NotNull String name) {
        return this.names.contains(name);
    }

    @Override
    @Nullable
    public Emitable claimComponent(@NotNull Component component) {
        return this.componentClaim.apply(component);
    }
}

