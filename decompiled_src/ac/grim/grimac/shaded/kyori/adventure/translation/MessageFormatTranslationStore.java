/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.translation;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.translation.AbstractTranslationStore;
import ac.grim.grimac.shaded.kyori.adventure.translation.TranslationRegistry;
import java.text.MessageFormat;
import java.util.Locale;

final class MessageFormatTranslationStore
extends AbstractTranslationStore.StringBased<MessageFormat>
implements TranslationRegistry {
    MessageFormatTranslationStore(Key name) {
        super(name);
    }

    @Override
    @NotNull
    protected MessageFormat parse(@NotNull String string, @NotNull Locale locale) {
        return new MessageFormat(string, locale);
    }

    @Override
    @Nullable
    public MessageFormat translate(@NotNull String key, @NotNull Locale locale) {
        return (MessageFormat)this.translationValue(key, locale);
    }
}

