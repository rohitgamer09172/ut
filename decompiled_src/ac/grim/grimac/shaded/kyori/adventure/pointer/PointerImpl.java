/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.pointer;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.internal.Internals;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.pointer.Pointer;

final class PointerImpl<T>
implements Pointer<T> {
    private final Class<T> type;
    private final Key key;

    PointerImpl(Class<T> type, Key key) {
        this.type = type;
        this.key = key;
    }

    @Override
    @NotNull
    public Class<T> type() {
        return this.type;
    }

    @Override
    @NotNull
    public Key key() {
        return this.key;
    }

    public String toString() {
        return Internals.toString(this);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        PointerImpl that = (PointerImpl)other;
        return this.type.equals(that.type) && this.key.equals(that.key);
    }

    public int hashCode() {
        int result = this.type.hashCode();
        result = 31 * result + this.key.hashCode();
        return result;
    }
}

