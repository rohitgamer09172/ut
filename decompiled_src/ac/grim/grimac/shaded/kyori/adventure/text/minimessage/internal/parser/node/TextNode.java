/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.Token;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.TokenParser;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node.ElementNode;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node.ValueNode;

public final class TextNode
extends ValueNode {
    private static boolean isEscape(int escape) {
        return escape == 60 || escape == 92;
    }

    public TextNode(@Nullable ElementNode parent, @NotNull Token token, @NotNull String sourceMessage) {
        super(parent, token, sourceMessage, TokenParser.unescape(sourceMessage, token.startIndex(), token.endIndex(), TextNode::isEscape));
    }

    @Override
    String valueName() {
        return "TextNode";
    }
}

