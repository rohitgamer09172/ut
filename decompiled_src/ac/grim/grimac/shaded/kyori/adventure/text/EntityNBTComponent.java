/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.NBTComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.NBTComponentBuilder;
import ac.grim.grimac.shaded.kyori.adventure.text.ScopedComponent;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;

public interface EntityNBTComponent
extends NBTComponent<EntityNBTComponent, Builder>,
ScopedComponent<EntityNBTComponent> {
    @NotNull
    public String selector();

    @Contract(pure=true)
    @NotNull
    public EntityNBTComponent selector(@NotNull String var1);

    @Override
    @NotNull
    default public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.concat(Stream.of(ExaminableProperty.of("selector", this.selector())), NBTComponent.super.examinableProperties());
    }

    public static interface Builder
    extends NBTComponentBuilder<EntityNBTComponent, Builder> {
        @Contract(value="_ -> this")
        @NotNull
        public Builder selector(@NotNull String var1);
    }
}

