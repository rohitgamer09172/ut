/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.object;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.internal.Internals;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.text.object.SpriteObjectContents;
import java.util.Objects;

final class SpriteObjectContentsImpl
implements SpriteObjectContents {
    private final Key atlas;
    private final Key sprite;

    SpriteObjectContentsImpl(@NotNull Key atlas, @NotNull Key sprite) {
        this.atlas = atlas;
        this.sprite = sprite;
    }

    @Override
    @NotNull
    public Key atlas() {
        return this.atlas;
    }

    @Override
    @NotNull
    public Key sprite() {
        return this.sprite;
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SpriteObjectContents)) {
            return false;
        }
        SpriteObjectContentsImpl that = (SpriteObjectContentsImpl)other;
        return Objects.equals(this.atlas, that.atlas()) && Objects.equals(this.sprite, that.sprite());
    }

    public int hashCode() {
        int result = this.atlas.hashCode();
        result = 31 * result + this.sprite.hashCode();
        return result;
    }

    public String toString() {
        return Internals.toString(this);
    }
}

