/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.format;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.format.StyleSetter;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextDecoration;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@ApiStatus.NonExtendable
public interface MutableStyleSetter<T extends MutableStyleSetter<?>>
extends StyleSetter<T> {
    @Override
    @Contract(value="_ -> this")
    @NotNull
    default public T decorate(TextDecoration ... decorations) {
        int length = decorations.length;
        for (int i = 0; i < length; ++i) {
            this.decorate(decorations[i]);
        }
        return (T)this;
    }

    @Override
    @Contract(value="_ -> this")
    @NotNull
    default public T decorations(@NotNull Map<TextDecoration, TextDecoration.State> decorations) {
        Objects.requireNonNull(decorations, "decorations");
        for (Map.Entry<TextDecoration, TextDecoration.State> entry : decorations.entrySet()) {
            this.decoration(entry.getKey(), entry.getValue());
        }
        return (T)this;
    }

    @Override
    @Contract(value="_, _ -> this")
    @NotNull
    default public T decorations(@NotNull Set<TextDecoration> decorations, boolean flag) {
        TextDecoration.State state = TextDecoration.State.byBoolean(flag);
        decorations.forEach(decoration -> this.decoration((TextDecoration)decoration, state));
        return (T)this;
    }
}

