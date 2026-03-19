/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.key;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.key.KeyPattern;

public interface Namespaced {
    @KeyPattern.Namespace
    @NotNull
    public String namespace();
}

