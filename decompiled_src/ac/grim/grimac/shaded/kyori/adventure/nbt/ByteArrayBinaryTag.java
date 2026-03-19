/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.nbt.ArrayBinaryTag;
import ac.grim.grimac.shaded.kyori.adventure.nbt.BinaryTagType;
import ac.grim.grimac.shaded.kyori.adventure.nbt.BinaryTagTypes;
import ac.grim.grimac.shaded.kyori.adventure.nbt.ByteArrayBinaryTagImpl;

public interface ByteArrayBinaryTag
extends ArrayBinaryTag,
Iterable<Byte> {
    @NotNull
    public static ByteArrayBinaryTag byteArrayBinaryTag(byte ... value) {
        return new ByteArrayBinaryTagImpl(value);
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
    @NotNull
    public static ByteArrayBinaryTag of(byte ... value) {
        return new ByteArrayBinaryTagImpl(value);
    }

    @NotNull
    default public BinaryTagType<ByteArrayBinaryTag> type() {
        return BinaryTagTypes.BYTE_ARRAY;
    }

    public byte @NotNull [] value();

    public int size();

    public byte get(int var1);
}

