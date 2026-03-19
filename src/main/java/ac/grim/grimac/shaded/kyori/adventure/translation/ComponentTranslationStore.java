/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.translation;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.TranslatableComponent;
import ac.grim.grimac.shaded.kyori.adventure.translation.AbstractTranslationStore;
import java.text.MessageFormat;
import java.util.Locale;

final class ComponentTranslationStore
extends AbstractTranslationStore<Component> {
    ComponentTranslationStore(@NotNull Key name) {
        super(name);
    }

    @Override
    @Nullable
    public MessageFormat translate(@NotNull String key, @NotNull Locale locale) {
        return null;
    }

    @Override
    @Nullable
    public Component translate(@NotNull TranslatableComponent component, @NotNull Locale locale) {
        Component translatedComponent = (Component)this.translationValue(component.key(), locale);
        if (translatedComponent == null) {
            return null;
        }
        return translatedComponent.append(component.children());
    }
}

