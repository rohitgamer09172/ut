/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.translation;

import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.internal.properties.AdventureProperties;
import ac.grim.grimac.shaded.kyori.adventure.translation.Translator;
import java.util.Locale;
import java.util.function.Supplier;

final class TranslationLocales {
    private static final Supplier<Locale> GLOBAL;

    private TranslationLocales() {
    }

    static Locale global() {
        return GLOBAL.get();
    }

    static {
        @Nullable String property = AdventureProperties.DEFAULT_TRANSLATION_LOCALE.value();
        if (property == null || property.isEmpty()) {
            GLOBAL = () -> Locale.US;
        } else if (property.equals("system")) {
            GLOBAL = Locale::getDefault;
        } else {
            Locale locale = Translator.parseLocale(property);
            GLOBAL = () -> locale;
        }
    }
}

