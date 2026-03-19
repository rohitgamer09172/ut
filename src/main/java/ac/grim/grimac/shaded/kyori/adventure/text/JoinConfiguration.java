/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.builder.AbstractBuilder;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.ComponentLike;
import ac.grim.grimac.shaded.kyori.adventure.text.JoinConfigurationImpl;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import ac.grim.grimac.shaded.kyori.adventure.util.Buildable;
import ac.grim.grimac.shaded.kyori.examination.Examinable;
import java.util.function.Function;
import java.util.function.Predicate;

@ApiStatus.NonExtendable
public interface JoinConfiguration
extends Buildable<JoinConfiguration, Builder>,
Examinable {
    @NotNull
    public static Builder builder() {
        return new JoinConfigurationImpl.BuilderImpl();
    }

    @NotNull
    public static JoinConfiguration noSeparators() {
        return JoinConfigurationImpl.NULL;
    }

    @NotNull
    public static JoinConfiguration newlines() {
        return JoinConfigurationImpl.STANDARD_NEW_LINES;
    }

    @NotNull
    public static JoinConfiguration spaces() {
        return JoinConfigurationImpl.STANDARD_SPACES;
    }

    @NotNull
    public static JoinConfiguration commas(boolean spaces) {
        return spaces ? JoinConfigurationImpl.STANDARD_COMMA_SPACE_SEPARATED : JoinConfigurationImpl.STANDARD_COMMA_SEPARATED;
    }

    @NotNull
    public static JoinConfiguration arrayLike() {
        return JoinConfigurationImpl.STANDARD_ARRAY_LIKE;
    }

    @NotNull
    public static JoinConfiguration separator(@Nullable ComponentLike separator) {
        if (separator == null) {
            return JoinConfigurationImpl.NULL;
        }
        return (JoinConfiguration)JoinConfiguration.builder().separator(separator).build();
    }

    @NotNull
    public static JoinConfiguration separators(@Nullable ComponentLike separator, @Nullable ComponentLike lastSeparator) {
        if (separator == null && lastSeparator == null) {
            return JoinConfigurationImpl.NULL;
        }
        return (JoinConfiguration)JoinConfiguration.builder().separator(separator).lastSeparator(lastSeparator).build();
    }

    @Nullable
    public Component prefix();

    @Nullable
    public Component suffix();

    @Nullable
    public Component separator();

    @Nullable
    public Component lastSeparator();

    @Nullable
    public Component lastSeparatorIfSerial();

    @NotNull
    public Function<ComponentLike, Component> convertor();

    @NotNull
    public Predicate<ComponentLike> predicate();

    @NotNull
    public Style parentStyle();

    public static interface Builder
    extends AbstractBuilder<JoinConfiguration>,
    Buildable.Builder<JoinConfiguration> {
        @Contract(value="_ -> this")
        @NotNull
        public Builder prefix(@Nullable ComponentLike var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder suffix(@Nullable ComponentLike var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder separator(@Nullable ComponentLike var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder lastSeparator(@Nullable ComponentLike var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder lastSeparatorIfSerial(@Nullable ComponentLike var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder convertor(@NotNull Function<ComponentLike, Component> var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder predicate(@NotNull Predicate<ComponentLike> var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder parentStyle(@NotNull Style var1);
    }
}

