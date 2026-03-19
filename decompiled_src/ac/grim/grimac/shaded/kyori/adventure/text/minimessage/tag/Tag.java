/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.ComponentLike;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import ac.grim.grimac.shaded.kyori.adventure.text.format.StyleBuilderApplicable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.CallbackStylingTagImpl;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.InsertingImpl;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.PreProcess;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.PreProcessTagImpl;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.StylingTagImpl;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.Consumer;

public interface Tag {
    @NotNull
    public static PreProcess preProcessParsed(@NotNull String content) {
        return new PreProcessTagImpl(Objects.requireNonNull(content, "content"));
    }

    @NotNull
    public static Tag inserting(@NotNull Component content) {
        return new InsertingImpl(true, Objects.requireNonNull(content, "content must not be null"));
    }

    @NotNull
    public static Tag inserting(@NotNull ComponentLike value) {
        return Tag.inserting(Objects.requireNonNull(value, "value").asComponent());
    }

    @NotNull
    public static Tag selfClosingInserting(@NotNull Component content) {
        return new InsertingImpl(false, Objects.requireNonNull(content, "content must not be null"));
    }

    @NotNull
    public static Tag selfClosingInserting(@NotNull ComponentLike value) {
        return Tag.selfClosingInserting(Objects.requireNonNull(value, "value").asComponent());
    }

    @NotNull
    public static Tag styling(Consumer<Style.Builder> styles) {
        return new CallbackStylingTagImpl(styles);
    }

    @NotNull
    public static Tag styling(StyleBuilderApplicable ... actions) {
        Objects.requireNonNull(actions, "actions");
        int length = actions.length;
        for (int i = 0; i < length; ++i) {
            if (actions[i] != null) continue;
            throw new NullPointerException("actions[" + i + "]");
        }
        return new StylingTagImpl(Arrays.copyOf(actions, actions.length));
    }

    @ApiStatus.NonExtendable
    public static interface Argument {
        @NotNull
        public String value();

        @NotNull
        default public String lowerValue() {
            return this.value().toLowerCase(Locale.ROOT);
        }

        default public boolean isTrue() {
            return "true".equals(this.value()) || "on".equals(this.value());
        }

        default public boolean isFalse() {
            return "false".equals(this.value()) || "off".equals(this.value());
        }

        @NotNull
        default public OptionalInt asInt() {
            try {
                return OptionalInt.of(Integer.parseInt(this.value()));
            }
            catch (NumberFormatException ex) {
                return OptionalInt.empty();
            }
        }

        @NotNull
        default public OptionalDouble asDouble() {
            try {
                return OptionalDouble.of(Double.parseDouble(this.value()));
            }
            catch (NumberFormatException ex) {
                return OptionalDouble.empty();
            }
        }
    }
}

