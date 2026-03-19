/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.ParsingException;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.ClaimConsumer;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.Arrays;

final class SequentialTagResolver
implements TagResolver,
SerializableResolver {
    final TagResolver[] resolvers;

    SequentialTagResolver(@NotNull @NotNull TagResolver @NotNull [] resolvers) {
        this.resolvers = resolvers;
    }

    @Override
    @Nullable
    public Tag resolve(@NotNull String name, @NotNull ArgumentQueue arguments, @NotNull Context ctx) throws ParsingException {
        ParsingException thrown = null;
        for (TagResolver resolver : this.resolvers) {
            try {
                Tag placeholder;
                if (!resolver.has(name) || (placeholder = resolver.resolve(name, arguments, ctx)) == null) continue;
                return placeholder;
            }
            catch (ParsingException ex) {
                arguments.reset();
                if (thrown == null) {
                    thrown = ex;
                    continue;
                }
                thrown.addSuppressed(ex);
            }
            catch (Exception ex) {
                arguments.reset();
                ParsingException err = ctx.newException("Exception thrown while parsing <" + name + ">", ex, arguments);
                if (thrown == null) {
                    thrown = err;
                    continue;
                }
                thrown.addSuppressed(err);
            }
        }
        if (thrown != null) {
            throw thrown;
        }
        return null;
    }

    @Override
    public boolean has(@NotNull String name) {
        for (TagResolver resolver : this.resolvers) {
            if (!resolver.has(name)) continue;
            return true;
        }
        return false;
    }

    @Override
    public void handle(@NotNull Component serializable, @NotNull ClaimConsumer consumer) {
        for (TagResolver resolver : this.resolvers) {
            if (!(resolver instanceof SerializableResolver)) continue;
            ((SerializableResolver)((Object)resolver)).handle(serializable, consumer);
        }
    }

    public boolean equals(@Nullable Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof SequentialTagResolver)) {
            return false;
        }
        SequentialTagResolver that = (SequentialTagResolver)other;
        return Arrays.equals(this.resolvers, that.resolvers);
    }

    public int hashCode() {
        return Arrays.hashCode(this.resolvers);
    }
}

