/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tree.Node;

@ApiStatus.OverrideOnly
public interface Modifying
extends Tag {
    default public void visit(@NotNull Node current, int depth) {
    }

    default public void postVisit() {
    }

    public Component apply(@NotNull Component var1, int var2);
}

