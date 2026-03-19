/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.AbstractTag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.PreProcess;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.Objects;
import java.util.stream.Stream;

final class PreProcessTagImpl
extends AbstractTag
implements PreProcess {
    private final String value;

    PreProcessTagImpl(String value) {
        this.value = value;
    }

    @Override
    @NotNull
    public String value() {
        return this.value;
    }

    public int hashCode() {
        return Objects.hash(this.value);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PreProcessTagImpl)) {
            return false;
        }
        PreProcessTagImpl that = (PreProcessTagImpl)other;
        return Objects.equals(this.value, that.value);
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("value", this.value));
    }
}

