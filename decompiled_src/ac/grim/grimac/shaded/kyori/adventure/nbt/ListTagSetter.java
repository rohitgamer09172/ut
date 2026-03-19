/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.nbt.BinaryTag;

public interface ListTagSetter<R, T extends BinaryTag> {
    @NotNull
    public R add(T var1);

    @NotNull
    public R add(Iterable<? extends T> var1);
}

