/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.ParsingExceptionImpl;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.Token;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.TokenParser;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node.ElementNode;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node.TagPart;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class TagNode
extends ElementNode {
    private final List<TagPart> parts;
    @Nullable
    private Tag tag = null;

    public TagNode(@NotNull ElementNode parent, @NotNull Token token, @NotNull String sourceMessage, @NotNull TokenParser.TagProvider tagProvider) {
        super(parent, token, sourceMessage);
        this.parts = TagNode.genParts(token, sourceMessage, tagProvider);
        if (this.parts.isEmpty()) {
            throw new ParsingExceptionImpl("Tag has no parts? " + this, this.sourceMessage(), this.token());
        }
    }

    @NotNull
    private static List<TagPart> genParts(@NotNull Token token, @NotNull String sourceMessage, @NotNull TokenParser.TagProvider tagProvider) {
        ArrayList<TagPart> parts = new ArrayList<TagPart>();
        if (token.childTokens() != null) {
            for (Token childToken : token.childTokens()) {
                parts.add(new TagPart(sourceMessage, childToken, tagProvider));
            }
        }
        return parts;
    }

    @NotNull
    public List<TagPart> parts() {
        return this.parts;
    }

    @NotNull
    public String name() {
        return this.parts.get(0).value();
    }

    @Override
    @NotNull
    public Token token() {
        return Objects.requireNonNull(super.token(), "token is not set");
    }

    @NotNull
    public Tag tag() {
        return Objects.requireNonNull(this.tag, "no tag set");
    }

    public void tag(@NotNull Tag tag) {
        this.tag = tag;
    }

    @Override
    @NotNull
    public StringBuilder buildToString(@NotNull StringBuilder sb, int indent) {
        char[] in = this.ident(indent);
        sb.append(in).append("TagNode(");
        int size = this.parts.size();
        for (int i = 0; i < size; ++i) {
            TagPart part = this.parts.get(i);
            sb.append('\'').append(part.value()).append('\'');
            if (i == size - 1) continue;
            sb.append(", ");
        }
        sb.append(") {\n");
        for (ElementNode child : this.children()) {
            child.buildToString(sb, indent + 1);
        }
        sb.append(in).append("}\n");
        return sb;
    }
}

