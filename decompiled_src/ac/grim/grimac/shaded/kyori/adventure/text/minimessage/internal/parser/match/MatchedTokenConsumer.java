/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.match;

import ac.grim.grimac.shaded.jetbrains.annotations.MustBeInvokedByOverriders;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.UnknownNullability;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.TokenType;

public abstract class MatchedTokenConsumer<T> {
    protected final String input;
    private int lastIndex = -1;

    public MatchedTokenConsumer(@NotNull String input) {
        this.input = input;
    }

    @MustBeInvokedByOverriders
    public void accept(int start, int end, @NotNull TokenType tokenType) {
        this.lastIndex = end;
    }

    public abstract @UnknownNullability T result();

    public final int lastEndIndex() {
        return this.lastIndex;
    }
}

