/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.internal.Internals;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.TranslationArgument;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.Objects;
import java.util.stream.Stream;

final class TranslationArgumentImpl
implements TranslationArgument {
    private static final Component TRUE = Component.text("true");
    private static final Component FALSE = Component.text("false");
    private final Object value;

    TranslationArgumentImpl(Object value) {
        this.value = value;
    }

    @Override
    @NotNull
    public Object value() {
        return this.value;
    }

    @Override
    @NotNull
    public Component asComponent() {
        if (this.value instanceof Component) {
            return (Component)this.value;
        }
        if (this.value instanceof Boolean) {
            return (Boolean)this.value != false ? TRUE : FALSE;
        }
        return Component.text(String.valueOf(this.value));
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        TranslationArgumentImpl that = (TranslationArgumentImpl)other;
        return Objects.equals(this.value, that.value);
    }

    public int hashCode() {
        return Objects.hash(this.value);
    }

    public String toString() {
        return Internals.toString(this);
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("value", this.value));
    }
}

