/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

class StyleClaimImpl<V>
implements StyleClaim<V> {
    private final String claimKey;
    private final Function<Style, V> lens;
    private final Predicate<V> filter;
    private final BiConsumer<V, TokenEmitter> emitable;

    StyleClaimImpl(String claimKey, Function<Style, @Nullable V> lens, Predicate<V> filter, BiConsumer<V, TokenEmitter> emitable) {
        this.claimKey = claimKey;
        this.lens = lens;
        this.filter = filter;
        this.emitable = emitable;
    }

    @Override
    @NotNull
    public String claimKey() {
        return this.claimKey;
    }

    @Override
    @Nullable
    public Emitable apply(@NotNull Style style) {
        V element = this.lens.apply(style);
        if (element == null || !this.filter.test(element)) {
            return null;
        }
        return emitter -> this.emitable.accept(element, emitter);
    }

    public int hashCode() {
        return Objects.hash(this.claimKey);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof StyleClaimImpl)) {
            return false;
        }
        StyleClaimImpl that = (StyleClaimImpl)other;
        return Objects.equals(this.claimKey, that.claimKey);
    }
}

