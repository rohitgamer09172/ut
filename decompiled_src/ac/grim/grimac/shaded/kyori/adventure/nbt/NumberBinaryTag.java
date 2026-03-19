/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.nbt.BinaryTag;
import ac.grim.grimac.shaded.kyori.adventure.nbt.BinaryTagType;

public interface NumberBinaryTag
extends BinaryTag {
    @NotNull
    public BinaryTagType<? extends NumberBinaryTag> type();

    public byte byteValue();

    public double doubleValue();

    public float floatValue();

    public int intValue();

    public long longValue();

    public short shortValue();

    @NotNull
    public Number numberValue();
}

