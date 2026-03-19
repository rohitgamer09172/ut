/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.Debug;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.nbt.AbstractBinaryTag;
import ac.grim.grimac.shaded.kyori.adventure.nbt.DoubleBinaryTag;
import ac.grim.grimac.shaded.kyori.adventure.nbt.ShadyPines;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;

@Debug.Renderer(text="String.valueOf(this.value) + \"d\"", hasChildren="false")
final class DoubleBinaryTagImpl
extends AbstractBinaryTag
implements DoubleBinaryTag {
    private final double value;

    DoubleBinaryTagImpl(double value) {
        this.value = value;
    }

    @Override
    public double value() {
        return this.value;
    }

    @Override
    public byte byteValue() {
        return (byte)(ShadyPines.floor(this.value) & 0xFF);
    }

    @Override
    public double doubleValue() {
        return this.value;
    }

    @Override
    public float floatValue() {
        return (float)this.value;
    }

    @Override
    public int intValue() {
        return ShadyPines.floor(this.value);
    }

    @Override
    public long longValue() {
        return (long)Math.floor(this.value);
    }

    @Override
    public short shortValue() {
        return (short)(ShadyPines.floor(this.value) & 0xFFFF);
    }

    @Override
    @NotNull
    public Number numberValue() {
        return this.value;
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        DoubleBinaryTagImpl that = (DoubleBinaryTagImpl)other;
        return Double.doubleToLongBits(this.value) == Double.doubleToLongBits(that.value);
    }

    public int hashCode() {
        return Double.hashCode(this.value);
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("value", this.value));
    }
}

