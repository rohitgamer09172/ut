/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.match;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.Token;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.TokenType;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.match.MatchedTokenConsumer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TokenListProducingMatchedTokenConsumer
extends MatchedTokenConsumer<List<Token>> {
    private List<Token> result = null;

    public TokenListProducingMatchedTokenConsumer(@NotNull String input) {
        super(input);
    }

    @Override
    public void accept(int start, int end, @NotNull TokenType tokenType) {
        super.accept(start, end, tokenType);
        if (this.result == null) {
            this.result = new ArrayList<Token>();
        }
        this.result.add(new Token(start, end, tokenType));
    }

    @Override
    @NotNull
    public List<Token> result() {
        return this.result == null ? Collections.emptyList() : this.result;
    }
}

