/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.util;

import ac.grim.grimac.shaded.jetbrains.annotations.Range;
import ac.grim.grimac.shaded.kyori.adventure.util.RGBLike;

public interface ARGBLike
extends RGBLike {
    public @Range(from=0L, to=255L) int alpha();
}

