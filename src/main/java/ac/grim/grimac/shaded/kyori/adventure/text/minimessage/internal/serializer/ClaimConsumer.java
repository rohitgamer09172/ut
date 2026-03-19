/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.Emitable;

public interface ClaimConsumer {
    public void style(@NotNull String var1, @NotNull Emitable var2);

    public boolean component(@NotNull Emitable var1);

    public boolean styleClaimed(@NotNull String var1);

    public boolean componentClaimed();
}

