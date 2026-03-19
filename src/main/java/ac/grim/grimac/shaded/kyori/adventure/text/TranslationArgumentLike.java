/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.ComponentLike;
import ac.grim.grimac.shaded.kyori.adventure.text.TranslationArgument;

@FunctionalInterface
public interface TranslationArgumentLike
extends ComponentLike {
    @NotNull
    public TranslationArgument asTranslationArgument();

    @Override
    @NotNull
    default public Component asComponent() {
        return this.asTranslationArgument().asComponent();
    }
}

