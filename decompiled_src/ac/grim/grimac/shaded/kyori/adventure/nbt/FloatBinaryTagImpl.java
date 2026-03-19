/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.Debug;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.nbt.AbstractBinaryTag;
import ac.grim.grimac.shaded.kyori.adventure.nbt.FloatBinaryTag;
import ac.grim.grimac.shaded.kyori.adventure.nbt.ShadyPines;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;

@Debug.Renderer(text="String.valueOf(this.value) + \"f\"", hasChildren="false")
final class FloatBinaryTagImpl
extends AbstractBinaryTag
implements FloatBinaryTag {
    private final float value;

    FloatBinaryTagImpl(float value) {
        this.value = value;
    }

    @Override
    public float value() {
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
        return this.value;
    }

    @Override
    public int intValue() {
        return ShadyPines.floor(this.value);
    }

    @Override
    public long longValue() {
        return (long)this.value;
    }

    @Override
    public short shortValue() {
        return (short)(ShadyPines.floor(this.value) & 0xFFFF);
    }

    @Override
    @NotNull
    public Number numberValue() {
        return Float.valueOf(this.value);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        FloatBinaryTagImpl that = (FloatBinaryTagImpl)other;
        return Float.floatToIntBits(this.value) == Float.floatToIntBits(that.value);
    }

    public int hashCode() {
        return Float.hashCode(this.value);
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("value", this.value));
    }
}

