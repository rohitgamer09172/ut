/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.translation;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.renderer.TranslatableComponentRenderer;
import ac.grim.grimac.shaded.kyori.adventure.translation.GlobalTranslatorImpl;
import ac.grim.grimac.shaded.kyori.adventure.translation.Translator;
import ac.grim.grimac.shaded.kyori.examination.Examinable;
import java.util.Locale;

public interface GlobalTranslator
extends Translator,
Examinable {
    @NotNull
    public static GlobalTranslator translator() {
        return GlobalTranslatorImpl.INSTANCE;
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
    @NotNull
    public static GlobalTranslator get() {
        return GlobalTranslatorImpl.INSTANCE;
    }

    @NotNull
    public static TranslatableComponentRenderer<Locale> renderer() {
        return GlobalTranslatorImpl.INSTANCE.renderer;
    }

    @NotNull
    public static Component render(@NotNull Component component, @NotNull Locale locale) {
        return GlobalTranslator.renderer().render(component, locale);
    }

    @NotNull
    public Iterable<? extends Translator> sources();

    public boolean addSource(@NotNull Translator var1);

    public boolean removeSource(@NotNull Translator var1);
}

