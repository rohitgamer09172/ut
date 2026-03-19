/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.BuildableComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.ComponentBuilder;
import ac.grim.grimac.shaded.kyori.adventure.text.ComponentLike;
import ac.grim.grimac.shaded.kyori.adventure.text.ScopedComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.TranslationArgument;
import ac.grim.grimac.shaded.kyori.adventure.translation.Translatable;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public interface TranslatableComponent
extends BuildableComponent<TranslatableComponent, Builder>,
ScopedComponent<TranslatableComponent> {
    @NotNull
    public String key();

    @Contract(pure=true)
    @NotNull
    default public TranslatableComponent key(@NotNull Translatable translatable) {
        return this.key(Objects.requireNonNull(translatable, "translatable").translationKey());
    }

    @Contract(pure=true)
    @NotNull
    public TranslatableComponent key(@NotNull String var1);

    @Deprecated
    @NotNull
    public List<Component> args();

    @Deprecated
    @Contract(pure=true)
    @NotNull
    default public TranslatableComponent args(ComponentLike ... args) {
        return this.arguments(args);
    }

    @Deprecated
    @Contract(pure=true)
    @NotNull
    default public TranslatableComponent args(@NotNull List<? extends ComponentLike> args) {
        return this.arguments(args);
    }

    @NotNull
    public List<TranslationArgument> arguments();

    @Contract(pure=true)
    @NotNull
    public TranslatableComponent arguments(ComponentLike ... var1);

    @Contract(pure=true)
    @NotNull
    public TranslatableComponent arguments(@NotNull List<? extends ComponentLike> var1);

    @Nullable
    public String fallback();

    @Contract(pure=true)
    @NotNull
    public TranslatableComponent fallback(@Nullable String var1);

    @Override
    @NotNull
    default public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.concat(Stream.of(ExaminableProperty.of("key", this.key()), ExaminableProperty.of("arguments", this.arguments()), ExaminableProperty.of("fallback", this.fallback())), BuildableComponent.super.examinableProperties());
    }

    public static interface Builder
    extends ComponentBuilder<TranslatableComponent, Builder> {
        @Contract(pure=true)
        @NotNull
        default public Builder key(@NotNull Translatable translatable) {
            return this.key(Objects.requireNonNull(translatable, "translatable").translationKey());
        }

        @Contract(value="_ -> this")
        @NotNull
        public Builder key(@NotNull String var1);

        @Deprecated
        @Contract(value="_ -> this")
        @NotNull
        default public Builder args(@NotNull ComponentBuilder<?, ?> arg) {
            return this.arguments(arg);
        }

        @Deprecated
        @Contract(value="_ -> this")
        @NotNull
        default public Builder args(ComponentBuilder<?, ?> ... args) {
            return this.arguments(args);
        }

        @Deprecated
        @Contract(value="_ -> this")
        @NotNull
        default public Builder args(@NotNull Component arg) {
            return this.arguments(arg);
        }

        @Deprecated
        @Contract(value="_ -> this")
        @NotNull
        default public Builder args(ComponentLike ... args) {
            return this.arguments(args);
        }

        @Deprecated
        @Contract(value="_ -> this")
        @NotNull
        default public Builder args(@NotNull List<? extends ComponentLike> args) {
            return this.arguments(args);
        }

        @Contract(value="_ -> this")
        @NotNull
        public Builder arguments(ComponentLike ... var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder arguments(@NotNull List<? extends ComponentLike> var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder fallback(@Nullable String var1);
    }
}

