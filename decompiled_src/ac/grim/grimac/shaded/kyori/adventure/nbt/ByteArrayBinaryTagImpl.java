/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.Debug;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.nbt.ArrayBinaryTagImpl;
import ac.grim.grimac.shaded.kyori.adventure.nbt.ByteArrayBinaryTag;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

@Debug.Renderer(text="\"byte[\" + this.value.length + \"]\"", childrenArray="this.value", hasChildren="this.value.length > 0")
final class ByteArrayBinaryTagImpl
extends ArrayBinaryTagImpl
implements ByteArrayBinaryTag {
    final byte[] value;

    ByteArrayBinaryTagImpl(byte[] value) {
        this.value = Arrays.copyOf(value, value.length);
    }

    @Override
    public byte @NotNull [] value() {
        return Arrays.copyOf(this.value, this.value.length);
    }

    @Override
    public int size() {
        return this.value.length;
    }

    @Override
    public byte get(int index) {
        ByteArrayBinaryTagImpl.checkIndex(index, this.value.length);
        return this.value[index];
    }

    static byte[] value(ByteArrayBinaryTag tag) {
        return tag instanceof ByteArrayBinaryTagImpl ? ((ByteArrayBinaryTagImpl)tag).value : tag.value();
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        ByteArrayBinaryTagImpl that = (ByteArrayBinaryTagImpl)other;
        return Arrays.equals(this.value, that.value);
    }

    public int hashCode() {
        return Arrays.hashCode(this.value);
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("value", this.value));
    }

    @Override
    public Iterator<Byte> iterator() {
        return new Iterator<Byte>(){
            private int index;

            @Override
            public boolean hasNext() {
                return this.index < ByteArrayBinaryTagImpl.this.value.length - 1;
            }

            @Override
            public Byte next() {
                return ByteArrayBinaryTagImpl.this.value[this.index++];
            }
        };
    }
}

