/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.nbt.ArrayBinaryTag;
import ac.grim.grimac.shaded.kyori.adventure.nbt.BinaryTagType;
import ac.grim.grimac.shaded.kyori.adventure.nbt.BinaryTagTypes;
import ac.grim.grimac.shaded.kyori.adventure.nbt.LongArrayBinaryTagImpl;
import java.util.function.LongConsumer;
import java.util.stream.LongStream;

public interface LongArrayBinaryTag
extends ArrayBinaryTag,
Iterable<Long> {
    @NotNull
    public static LongArrayBinaryTag longArrayBinaryTag(long ... value) {
        return new LongArrayBinaryTagImpl(value);
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
    @NotNull
    public static LongArrayBinaryTag of(long ... value) {
        return new LongArrayBinaryTagImpl(value);
    }

    @NotNull
    default public BinaryTagType<LongArrayBinaryTag> type() {
        return BinaryTagTypes.LONG_ARRAY;
    }

    public long @NotNull [] value();

    public int size();

    public long get(int var1);

    public  @NotNull PrimitiveIterator.OfLong iterator();

    public  @NotNull Spliterator.OfLong spliterator();

    @NotNull
    public LongStream stream();

    public void forEachLong(@NotNull LongConsumer var1);
}

