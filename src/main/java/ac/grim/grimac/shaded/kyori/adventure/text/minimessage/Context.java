/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.pointer.Pointered;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.ParsingException;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

@ApiStatus.NonExtendable
public interface Context {
    @Nullable
    public Pointered target();

    @NotNull
    public Pointered targetOrThrow();

    @NotNull
    public <T extends Pointered> T targetAsType(@NotNull Class<T> var1);

    @NotNull
    public Component deserialize(@NotNull String var1);

    @NotNull
    public Component deserialize(@NotNull String var1, @NotNull TagResolver var2);

    @NotNull
    public Component deserialize(@NotNull String var1, TagResolver ... var2);

    @NotNull
    public ParsingException newException(@NotNull String var1, @NotNull ArgumentQueue var2);

    @NotNull
    public ParsingException newException(@NotNull String var1);

    @NotNull
    public ParsingException newException(@NotNull String var1, @Nullable Throwable var2, @NotNull ArgumentQueue var3);

    public boolean emitVirtuals();
}

