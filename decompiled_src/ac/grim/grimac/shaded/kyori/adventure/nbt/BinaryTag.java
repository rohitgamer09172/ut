/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.nbt.BinaryTagLike;
import ac.grim.grimac.shaded.kyori.adventure.nbt.BinaryTagType;
import ac.grim.grimac.shaded.kyori.examination.Examinable;

public interface BinaryTag
extends BinaryTagLike,
Examinable {
    @NotNull
    public BinaryTagType<? extends BinaryTag> type();

    @Override
    @NotNull
    default public BinaryTag asBinaryTag() {
        return this;
    }
}

