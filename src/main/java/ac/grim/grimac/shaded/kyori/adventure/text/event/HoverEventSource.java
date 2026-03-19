/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.event;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.event.HoverEvent;
import java.util.function.UnaryOperator;

public interface HoverEventSource<V> {
    @Nullable
    public static <V> HoverEvent<V> unbox(@Nullable HoverEventSource<V> source) {
        return source != null ? source.asHoverEvent() : null;
    }

    @NotNull
    default public HoverEvent<V> asHoverEvent() {
        return this.asHoverEvent(UnaryOperator.identity());
    }

    @NotNull
    public HoverEvent<V> asHoverEvent(@NotNull UnaryOperator<V> var1);
}

