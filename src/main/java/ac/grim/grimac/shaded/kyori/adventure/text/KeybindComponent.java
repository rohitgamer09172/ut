/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.BuildableComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.ComponentBuilder;
import ac.grim.grimac.shaded.kyori.adventure.text.ScopedComponent;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.Objects;
import java.util.stream.Stream;

public interface KeybindComponent
extends BuildableComponent<KeybindComponent, Builder>,
ScopedComponent<KeybindComponent> {
    @NotNull
    public String keybind();

    @Override
    @Contract(pure=true)
    @NotNull
    public KeybindComponent keybind(@NotNull String var1);

    @Override
    @Contract(pure=true)
    @NotNull
    default public KeybindComponent keybind(@NotNull KeybindLike keybind) {
        return this.keybind(Objects.requireNonNull(keybind, "keybind").asKeybind());
    }

    @Override
    @NotNull
    default public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.concat(Stream.of(ExaminableProperty.of("keybind", this.keybind())), BuildableComponent.super.examinableProperties());
    }

    public static interface KeybindLike {
        @NotNull
        public String asKeybind();
    }

    public static interface Builder
    extends ComponentBuilder<KeybindComponent, Builder> {
        @Contract(value="_ -> this")
        @NotNull
        public Builder keybind(@NotNull String var1);

        @Contract(pure=true)
        @NotNull
        default public Builder keybind(@NotNull KeybindLike keybind) {
            return this.keybind(Objects.requireNonNull(keybind, "keybind").asKeybind());
        }
    }
}

