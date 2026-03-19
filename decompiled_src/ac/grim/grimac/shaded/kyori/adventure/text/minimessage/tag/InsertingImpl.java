/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.AbstractTag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Inserting;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.Objects;
import java.util.stream.Stream;

final class InsertingImpl
extends AbstractTag
implements Inserting {
    private final boolean allowsChildren;
    private final Component value;

    InsertingImpl(boolean allowsChildren, Component value) {
        this.allowsChildren = allowsChildren;
        this.value = value;
    }

    @Override
    public boolean allowsChildren() {
        return this.allowsChildren;
    }

    @Override
    @NotNull
    public Component value() {
        return this.value;
    }

    public int hashCode() {
        return Objects.hash(this.allowsChildren, this.value);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof InsertingImpl)) {
            return false;
        }
        InsertingImpl that = (InsertingImpl)other;
        return this.allowsChildren == that.allowsChildren && Objects.equals(this.value, that.value);
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("allowsChildren", this.allowsChildren), ExaminableProperty.of("value", this.value));
    }
}

