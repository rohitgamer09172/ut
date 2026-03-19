/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.QuotingOverride;

public interface TokenEmitter {
    @NotNull
    public TokenEmitter tag(@NotNull String var1);

    @NotNull
    public TokenEmitter selfClosingTag(@NotNull String var1);

    @NotNull
    default public TokenEmitter arguments(String ... args) {
        for (String arg : args) {
            this.argument(arg);
        }
        return this;
    }

    @NotNull
    public TokenEmitter argument(@NotNull String var1);

    @NotNull
    public TokenEmitter argument(@NotNull String var1, @NotNull QuotingOverride var2);

    @NotNull
    public TokenEmitter argument(@NotNull Component var1);

    @NotNull
    public TokenEmitter text(@NotNull String var1);

    @NotNull
    public TokenEmitter pop();
}

