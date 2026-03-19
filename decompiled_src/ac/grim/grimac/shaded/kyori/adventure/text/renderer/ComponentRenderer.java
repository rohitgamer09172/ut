/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.renderer;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.function.Function;

public interface ComponentRenderer<C> {
    @NotNull
    public Component render(@NotNull Component var1, @NotNull C var2);

    default public <T> ComponentRenderer<T> mapContext(Function<T, C> transformer) {
        return (component, ctx) -> this.render(component, transformer.apply(ctx));
    }
}

