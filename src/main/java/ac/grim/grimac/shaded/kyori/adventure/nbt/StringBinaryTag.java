/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.nbt.BinaryTag;
import ac.grim.grimac.shaded.kyori.adventure.nbt.BinaryTagType;
import ac.grim.grimac.shaded.kyori.adventure.nbt.BinaryTagTypes;
import ac.grim.grimac.shaded.kyori.adventure.nbt.StringBinaryTagImpl;

public interface StringBinaryTag
extends BinaryTag {
    @NotNull
    public static StringBinaryTag stringBinaryTag(@NotNull String value) {
        return new StringBinaryTagImpl(value);
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
    @NotNull
    public static StringBinaryTag of(@NotNull String value) {
        return new StringBinaryTagImpl(value);
    }

    @NotNull
    default public BinaryTagType<StringBinaryTag> type() {
        return BinaryTagTypes.STRING;
    }

    @NotNull
    public String value();
}

