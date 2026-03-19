/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.intellij.lang.annotations.RegExp;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.builder.AbstractBuilder;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.ComponentLike;
import ac.grim.grimac.shaded.kyori.adventure.text.PatternReplacementResult;
import ac.grim.grimac.shaded.kyori.adventure.text.TextComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.TextReplacementConfigImpl;
import ac.grim.grimac.shaded.kyori.adventure.util.Buildable;
import ac.grim.grimac.shaded.kyori.adventure.util.IntFunction2;
import ac.grim.grimac.shaded.kyori.examination.Examinable;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public interface TextReplacementConfig
extends Buildable<TextReplacementConfig, Builder>,
Examinable {
    @NotNull
    public static Builder builder() {
        return new TextReplacementConfigImpl.Builder();
    }

    @NotNull
    public Pattern matchPattern();

    @FunctionalInterface
    public static interface Condition {
        @NotNull
        public PatternReplacementResult shouldReplace(@NotNull MatchResult var1, int var2, int var3);
    }

    public static interface Builder
    extends AbstractBuilder<TextReplacementConfig>,
    Buildable.Builder<TextReplacementConfig> {
        @Contract(value="_ -> this")
        default public Builder matchLiteral(String literal) {
            return this.match(Pattern.compile(literal, 16));
        }

        @Contract(value="_ -> this")
        @NotNull
        default public Builder match(@NotNull @RegExp String pattern) {
            return this.match(Pattern.compile(pattern));
        }

        @Contract(value="_ -> this")
        @NotNull
        public Builder match(@NotNull Pattern var1);

        @NotNull
        default public Builder once() {
            return this.times(1);
        }

        @Contract(value="_ -> this")
        @NotNull
        default public Builder times(int times) {
            return this.condition((int index, int replaced) -> replaced < times ? PatternReplacementResult.REPLACE : PatternReplacementResult.STOP);
        }

        @Contract(value="_ -> this")
        @NotNull
        default public Builder condition(@NotNull IntFunction2<PatternReplacementResult> condition) {
            return this.condition((MatchResult result, int matchCount, int replaced) -> (PatternReplacementResult)((Object)((Object)condition.apply(matchCount, replaced))));
        }

        @Contract(value="_ -> this")
        @NotNull
        public Builder condition(@NotNull Condition var1);

        @Contract(value="_ -> this")
        @NotNull
        default public Builder replacement(@NotNull String replacement) {
            Objects.requireNonNull(replacement, "replacement");
            return this.replacement((TextComponent.Builder builder) -> builder.content(replacement));
        }

        @Contract(value="_ -> this")
        @NotNull
        default public Builder replacement(@Nullable ComponentLike replacement) {
            @Nullable Component baked = ComponentLike.unbox(replacement);
            return this.replacement((MatchResult result, TextComponent.Builder input) -> baked);
        }

        @Contract(value="_ -> this")
        @NotNull
        default public Builder replacement(@NotNull Function<TextComponent.Builder, @Nullable ComponentLike> replacement) {
            Objects.requireNonNull(replacement, "replacement");
            return this.replacement((MatchResult result, TextComponent.Builder input) -> (ComponentLike)replacement.apply((TextComponent.Builder)input));
        }

        @Contract(value="_ -> this")
        @NotNull
        public Builder replacement(@NotNull BiFunction<MatchResult, TextComponent.Builder, @Nullable ComponentLike> var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder replaceInsideHoverEvents(boolean var1);
    }
}

