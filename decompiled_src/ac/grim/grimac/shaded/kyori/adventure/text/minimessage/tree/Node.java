/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tree;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.List;

@ApiStatus.NonExtendable
public interface Node {
    @NotNull
    public String toString();

    @NotNull
    public List<? extends Node> children();

    @Nullable
    public Node parent();

    @ApiStatus.NonExtendable
    public static interface Root
    extends Node {
        @NotNull
        public String input();
    }
}

