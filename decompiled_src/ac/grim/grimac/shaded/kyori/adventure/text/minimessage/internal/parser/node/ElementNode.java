/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.Token;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.TokenType;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node.TextNode;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tree.Node;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ElementNode
implements Node {
    @Nullable
    private final ElementNode parent;
    @Nullable
    private final Token token;
    private final String sourceMessage;
    private final List<ElementNode> children = new ArrayList<ElementNode>();

    ElementNode(@Nullable ElementNode parent, @Nullable Token token, @NotNull String sourceMessage) {
        this.parent = parent;
        this.token = token;
        this.sourceMessage = sourceMessage;
    }

    @Override
    @Nullable
    public ElementNode parent() {
        return this.parent;
    }

    @Nullable
    public Token token() {
        return this.token;
    }

    @NotNull
    public String sourceMessage() {
        return this.sourceMessage;
    }

    @NotNull
    public List<ElementNode> children() {
        return Collections.unmodifiableList(this.children);
    }

    @NotNull
    public List<ElementNode> unsafeChildren() {
        return this.children;
    }

    public void addChild(@NotNull ElementNode childNode) {
        int last = this.children.size() - 1;
        if (!(childNode instanceof TextNode) || this.children.isEmpty() || !(this.children.get(last) instanceof TextNode)) {
            this.children.add(childNode);
        } else {
            TextNode lastNode = (TextNode)this.children.remove(last);
            if (lastNode.token().endIndex() == childNode.token().startIndex()) {
                Token replace = new Token(lastNode.token().startIndex(), childNode.token().endIndex(), TokenType.TEXT);
                this.children.add(new TextNode(this, replace, lastNode.sourceMessage()));
            } else {
                this.children.add(lastNode);
                this.children.add(childNode);
            }
        }
    }

    @NotNull
    public StringBuilder buildToString(@NotNull StringBuilder sb, int indent) {
        char[] in = this.ident(indent);
        sb.append(in).append("Node {\n");
        for (ElementNode child : this.children) {
            child.buildToString(sb, indent + 1);
        }
        sb.append(in).append("}\n");
        return sb;
    }

    char @NotNull [] ident(int indent) {
        char[] c = new char[indent * 2];
        Arrays.fill(c, ' ');
        return c;
    }

    @Override
    @NotNull
    public String toString() {
        return this.buildToString(new StringBuilder(), 0).toString();
    }
}

