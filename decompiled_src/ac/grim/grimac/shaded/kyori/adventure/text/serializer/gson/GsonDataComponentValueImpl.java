/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonNull
 */
package ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.internal.Internals;
import ac.grim.grimac.shaded.kyori.adventure.text.event.DataComponentValue;
import ac.grim.grimac.shaded.kyori.adventure.text.serializer.gson.GsonDataComponentValue;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import java.util.Objects;
import java.util.stream.Stream;

class GsonDataComponentValueImpl
implements GsonDataComponentValue {
    private final JsonElement element;

    GsonDataComponentValueImpl(@NotNull JsonElement element) {
        this.element = element;
    }

    @Override
    @NotNull
    public JsonElement element() {
        return this.element;
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("element", this.element));
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
        GsonDataComponentValueImpl that = (GsonDataComponentValueImpl)other;
        return Objects.equals(this.element, that.element);
    }

    public int hashCode() {
        return Objects.hashCode(this.element);
    }

    static final class RemovedGsonComponentValueImpl
    extends GsonDataComponentValueImpl
    implements DataComponentValue.Removed {
        static final RemovedGsonComponentValueImpl INSTANCE = new RemovedGsonComponentValueImpl();

        private RemovedGsonComponentValueImpl() {
            super((JsonElement)JsonNull.INSTANCE);
        }
    }
}

