/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.translation;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.TranslatableComponent;
import ac.grim.grimac.shaded.kyori.adventure.util.TriState;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;

public interface Translator {
    @Nullable
    public static Locale parseLocale(@NotNull String string) {
        String[] segments = string.split("_", 3);
        int length = segments.length;
        if (length == 1) {
            return new Locale(string);
        }
        if (length == 2) {
            return new Locale(segments[0], segments[1]);
        }
        if (length == 3) {
            return new Locale(segments[0], segments[1], segments[2]);
        }
        return null;
    }

    @NotNull
    public Key name();

    @NotNull
    default public TriState hasAnyTranslations() {
        return TriState.NOT_SET;
    }

    default public boolean canTranslate(@NotNull String key, @NotNull Locale locale) {
        Component translatedValue = this.translate(Component.translatable(Objects.requireNonNull(key, "key")), Objects.requireNonNull(locale, "locale"));
        if (translatedValue != null) {
            return true;
        }
        return this.translate(key, locale) != null;
    }

    @Nullable
    public MessageFormat translate(@NotNull String var1, @NotNull Locale var2);

    @Nullable
    default public Component translate(@NotNull TranslatableComponent component, @NotNull Locale locale) {
        return null;
    }
}

