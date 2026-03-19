/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.TextComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.VirtualComponentRenderer;

@ApiStatus.NonExtendable
public interface VirtualComponent
extends TextComponent {
    @NotNull
    public Class<?> contextType();

    @NotNull
    public VirtualComponentRenderer<?> renderer();
}

