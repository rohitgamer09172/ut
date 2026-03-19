/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.object;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.text.object.ObjectContents;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;

@ApiStatus.NonExtendable
public interface SpriteObjectContents
extends ObjectContents {
    public static final Key DEFAULT_ATLAS = Key.key("minecraft:blocks");

    @NotNull
    public Key atlas();

    @NotNull
    public Key sprite();

    @Override
    @NotNull
    default public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("atlas", this.atlas()), ExaminableProperty.of("sprite", this.sprite()));
    }
}

