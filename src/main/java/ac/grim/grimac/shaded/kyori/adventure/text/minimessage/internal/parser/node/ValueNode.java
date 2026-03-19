/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.Token;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node.ElementNode;
import java.util.Objects;

public abstract class ValueNode
extends ElementNode {
    private final String value;

    ValueNode(@Nullable ElementNode parent, @Nullable Token token, @NotNull String sourceMessage, @NotNull String value) {
        super(parent, token, sourceMessage);
        this.value = value;
    }

    abstract String valueName();

    @NotNull
    public String value() {
        return this.value;
    }

    @Override
    @NotNull
    public Token token() {
        return Objects.requireNonNull(super.token(), "token is not set");
    }

    @Override
    @NotNull
    public StringBuilder buildToString(@NotNull StringBuilder sb, int indent) {
        char[] in = this.ident(indent);
        sb.append(in).append(this.valueName()).append("('").append(this.value).append("')\n");
        return sb;
    }
}

