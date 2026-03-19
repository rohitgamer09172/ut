/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import ac.grim.grimac.shaded.kyori.adventure.text.format.StyleBuilderApplicable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.AbstractTag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Inserting;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.Arrays;
import java.util.stream.Stream;

final class StylingTagImpl
extends AbstractTag
implements Inserting {
    private final StyleBuilderApplicable[] styles;

    StylingTagImpl(StyleBuilderApplicable[] styles) {
        this.styles = styles;
    }

    @Override
    @NotNull
    public Component value() {
        return Component.text("", Style.style(this.styles));
    }

    public int hashCode() {
        return 31 + Arrays.hashCode(this.styles);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof StylingTagImpl)) {
            return false;
        }
        StylingTagImpl that = (StylingTagImpl)other;
        return Arrays.equals(this.styles, that.styles);
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("styles", this.styles));
    }
}

