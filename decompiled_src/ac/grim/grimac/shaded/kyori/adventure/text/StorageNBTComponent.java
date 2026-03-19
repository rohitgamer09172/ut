/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.text.NBTComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.NBTComponentBuilder;
import ac.grim.grimac.shaded.kyori.adventure.text.ScopedComponent;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;

public interface StorageNBTComponent
extends NBTComponent<StorageNBTComponent, Builder>,
ScopedComponent<StorageNBTComponent> {
    @NotNull
    public Key storage();

    @Contract(pure=true)
    @NotNull
    public StorageNBTComponent storage(@NotNull Key var1);

    @Override
    @NotNull
    default public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.concat(Stream.of(ExaminableProperty.of("storage", this.storage())), NBTComponent.super.examinableProperties());
    }

    public static interface Builder
    extends NBTComponentBuilder<StorageNBTComponent, Builder> {
        @Contract(value="_ -> this")
        @NotNull
        public Builder storage(@NotNull Key var1);
    }
}

