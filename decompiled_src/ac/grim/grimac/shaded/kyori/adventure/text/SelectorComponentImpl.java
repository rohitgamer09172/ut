/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.internal.Internals;
import ac.grim.grimac.shaded.kyori.adventure.text.AbstractComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.AbstractComponentBuilder;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.ComponentLike;
import ac.grim.grimac.shaded.kyori.adventure.text.SelectorComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import java.util.List;
import java.util.Objects;

final class SelectorComponentImpl
extends AbstractComponent
implements SelectorComponent {
    private final String pattern;
    @Nullable
    private final Component separator;

    static SelectorComponent create(@NotNull List<? extends ComponentLike> children, @NotNull Style style, @NotNull String pattern, @Nullable ComponentLike separator) {
        return new SelectorComponentImpl(ComponentLike.asComponents(children, IS_NOT_EMPTY), Objects.requireNonNull(style, "style"), Objects.requireNonNull(pattern, "pattern"), ComponentLike.unbox(separator));
    }

    SelectorComponentImpl(@NotNull List<Component> children, @NotNull Style style, @NotNull String pattern, @Nullable Component separator) {
        super(children, style);
        this.pattern = pattern;
        this.separator = separator;
    }

    @Override
    @NotNull
    public String pattern() {
        return this.pattern;
    }

    @Override
    @NotNull
    public SelectorComponent pattern(@NotNull String pattern) {
        if (Objects.equals(this.pattern, pattern)) {
            return this;
        }
        return SelectorComponentImpl.create(this.children, this.style, pattern, this.separator);
    }

    @Override
    @Nullable
    public Component separator() {
        return this.separator;
    }

    @Override
    @NotNull
    public SelectorComponent separator(@Nullable ComponentLike separator) {
        return SelectorComponentImpl.create(this.children, this.style, this.pattern, separator);
    }

    @Override
    @NotNull
    public SelectorComponent children(@NotNull List<? extends ComponentLike> children) {
        return SelectorComponentImpl.create(children, this.style, this.pattern, this.separator);
    }

    @Override
    @NotNull
    public SelectorComponent style(@NotNull Style style) {
        return SelectorComponentImpl.create(this.children, style, this.pattern, this.separator);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SelectorComponent)) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        SelectorComponent that = (SelectorComponent)other;
        return Objects.equals(this.pattern, that.pattern()) && Objects.equals(this.separator, that.separator());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.pattern.hashCode();
        result = 31 * result + Objects.hashCode(this.separator);
        return result;
    }

    @Override
    public String toString() {
        return Internals.toString(this);
    }

    @Override
    @NotNull
    public SelectorComponent.Builder toBuilder() {
        return new BuilderImpl(this);
    }

    static final class BuilderImpl
    extends AbstractComponentBuilder<SelectorComponent, SelectorComponent.Builder>
    implements SelectorComponent.Builder {
        @Nullable
        private String pattern;
        @Nullable
        private Component separator;

        BuilderImpl() {
        }

        BuilderImpl(@NotNull SelectorComponent component) {
            super(component);
            this.pattern = component.pattern();
            this.separator = component.separator();
        }

        @Override
        @NotNull
        public SelectorComponent.Builder pattern(@NotNull String pattern) {
            this.pattern = Objects.requireNonNull(pattern, "pattern");
            return this;
        }

        @Override
        @NotNull
        public SelectorComponent.Builder separator(@Nullable ComponentLike separator) {
            this.separator = ComponentLike.unbox(separator);
            return this;
        }

        @Override
        @NotNull
        public SelectorComponent build() {
            if (this.pattern == null) {
                throw new IllegalStateException("pattern must be set");
            }
            return SelectorComponentImpl.create(this.children, this.buildStyle(), this.pattern, this.separator);
        }
    }
}

