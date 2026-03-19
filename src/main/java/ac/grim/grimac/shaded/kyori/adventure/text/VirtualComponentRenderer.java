/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.UnknownNullability;
import ac.grim.grimac.shaded.kyori.adventure.text.ComponentLike;

public interface VirtualComponentRenderer<C> {
    public @UnknownNullability ComponentLike apply(@NotNull C var1);

    @NotNull
    default public String fallbackString() {
        return "";
    }
}

