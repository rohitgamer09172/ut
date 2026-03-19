/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.nbt.BinaryTag;
import ac.grim.grimac.shaded.kyori.adventure.nbt.CompoundBinaryTag;
import ac.grim.grimac.shaded.kyori.adventure.nbt.CompoundBinaryTagImpl;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

final class CompoundTagBuilder
implements CompoundBinaryTag.Builder {
    private static final int DEFAULT_CAPACITY = -1;
    @Nullable
    private Map<String, BinaryTag> tags;
    private final int initialCapacity;

    CompoundTagBuilder() {
        this(-1);
    }

    CompoundTagBuilder(int initialCapacity) {
        this.initialCapacity = initialCapacity;
    }

    private Map<String, BinaryTag> tags() {
        if (this.tags == null) {
            if (this.initialCapacity != -1) {
                if (this.initialCapacity < 0) {
                    throw new IllegalArgumentException("initialCapacity cannot be less than 0, was " + this.initialCapacity);
                }
                this.tags = new HashMap<String, BinaryTag>(this.initialCapacity);
            } else {
                this.tags = new HashMap<String, BinaryTag>();
            }
        }
        return this.tags;
    }

    @Override
    public @NotNull CompoundBinaryTag.Builder put(@NotNull String key, @NotNull BinaryTag tag) {
        this.tags().put(key, tag);
        return this;
    }

    @Override
    public @NotNull CompoundBinaryTag.Builder put(@NotNull CompoundBinaryTag tag) {
        Map<String, BinaryTag> tags = this.tags();
        for (String key : tag.keySet()) {
            tags.put(key, tag.get(key));
        }
        return this;
    }

    @Override
    public @NotNull CompoundBinaryTag.Builder put(@NotNull Map<String, ? extends BinaryTag> tags) {
        this.tags().putAll(tags);
        return this;
    }

    @Override
    public @NotNull CompoundBinaryTag.Builder remove(@NotNull String key, @Nullable Consumer<? super BinaryTag> removed) {
        if (this.tags != null) {
            BinaryTag tag = this.tags.remove(key);
            if (removed != null) {
                removed.accept(tag);
            }
        }
        return this;
    }

    @Override
    @NotNull
    public CompoundBinaryTag build() {
        if (this.tags == null) {
            return CompoundBinaryTag.empty();
        }
        return new CompoundBinaryTagImpl(new HashMap<String, BinaryTag>(this.tags));
    }
}

