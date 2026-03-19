/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import java.util.function.Supplier;

@ApiStatus.NonExtendable
public interface ArgumentQueue {
    public @NotNull Tag.Argument pop();

    public @NotNull Tag.Argument popOr(@NotNull String var1);

    public @NotNull Tag.Argument popOr(@NotNull Supplier<String> var1);

    public @Nullable Tag.Argument peek();

    public boolean hasNext();

    public void reset();
}

