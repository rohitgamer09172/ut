/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node.ElementNode;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tree.Node;

public final class RootNode
extends ElementNode
implements Node.Root {
    private final String beforePreprocessing;

    public RootNode(@NotNull String sourceMessage, @NotNull String beforePreprocessing) {
        super(null, null, sourceMessage);
        this.beforePreprocessing = beforePreprocessing;
    }

    @Override
    @NotNull
    public String input() {
        return this.beforePreprocessing;
    }
}

