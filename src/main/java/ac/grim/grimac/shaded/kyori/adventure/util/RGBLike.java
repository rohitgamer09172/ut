/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.util;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Range;
import ac.grim.grimac.shaded.kyori.adventure.util.HSVLike;

public interface RGBLike {
    public @Range(from=0L, to=255L) int red();

    public @Range(from=0L, to=255L) int green();

    public @Range(from=0L, to=255L) int blue();

    @NotNull
    default public HSVLike asHSV() {
        return HSVLike.fromRGB(this.red(), this.green(), this.blue());
    }
}

