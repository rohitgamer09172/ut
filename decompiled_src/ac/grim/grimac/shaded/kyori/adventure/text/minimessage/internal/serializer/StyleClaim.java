/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.StyleClaimImpl;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface StyleClaim<V> {
    @NotNull
    public static <T> StyleClaim<T> claim(@NotNull String claimKey, @NotNull Function<Style, @Nullable T> lens, @NotNull BiConsumer<T, TokenEmitter> emitable) {
        return StyleClaim.claim(claimKey, lens, $ -> true, emitable);
    }

    @NotNull
    public static <T> StyleClaim<T> claim(@NotNull String claimKey, @NotNull Function<Style, @Nullable T> lens, @NotNull Predicate<T> filter, @NotNull BiConsumer<T, TokenEmitter> emitable) {
        return new StyleClaimImpl<T>(Objects.requireNonNull(claimKey, "claimKey"), Objects.requireNonNull(lens, "lens"), Objects.requireNonNull(filter, "filter"), Objects.requireNonNull(emitable, "emitable"));
    }

    @NotNull
    public String claimKey();

    @Nullable
    public Emitable apply(@NotNull Style var1);
}

