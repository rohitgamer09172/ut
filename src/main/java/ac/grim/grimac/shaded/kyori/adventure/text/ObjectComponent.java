/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.BuildableComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.ComponentBuilder;
import ac.grim.grimac.shaded.kyori.adventure.text.ScopedComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.object.ObjectContents;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;

public interface ObjectComponent
extends BuildableComponent<ObjectComponent, Builder>,
ScopedComponent<ObjectComponent> {
    @NotNull
    public ObjectContents contents();

    @NotNull
    public ObjectComponent contents(@NotNull ObjectContents var1);

    @Override
    @NotNull
    default public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.concat(Stream.of(ExaminableProperty.of("contents", this.contents())), BuildableComponent.super.examinableProperties());
    }

    public static interface Builder
    extends ComponentBuilder<ObjectComponent, Builder> {
        @NotNull
        public Builder contents(@NotNull ObjectContents var1);
    }
}

