/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.ParsingException;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.Token;
import java.util.Arrays;

@ApiStatus.Internal
public class ParsingExceptionImpl
extends ParsingException {
    private static final long serialVersionUID = 2507190809441787202L;
    private final String originalText;
    private Token @NotNull [] tokens;

    public ParsingExceptionImpl(String message, @Nullable String originalText, Token ... tokens) {
        super(message, null, true, false);
        this.tokens = tokens;
        this.originalText = originalText;
    }

    public ParsingExceptionImpl(String message, @Nullable String originalText, @Nullable Throwable cause, boolean withStackTrace, Token ... tokens) {
        super(message, cause, true, withStackTrace);
        this.tokens = tokens;
        this.originalText = originalText;
    }

    @Override
    public String getMessage() {
        String arrowInfo = this.tokens().length != 0 ? "\n\t" + this.arrow() : "";
        String messageInfo = this.originalText() != null ? "\n\t" + this.originalText() + arrowInfo : "";
        return super.getMessage() + messageInfo;
    }

    @Override
    @Nullable
    public String detailMessage() {
        return super.getMessage();
    }

    @Override
    @Nullable
    public String originalText() {
        return this.originalText;
    }

    @NotNull
    public @NotNull Token @NotNull [] tokens() {
        return this.tokens;
    }

    public void tokens(@NotNull @NotNull Token @NotNull [] tokens) {
        this.tokens = tokens;
    }

    private String arrow() {
        @NotNull Token[] ts = this.tokens();
        char[] chars = new char[ts[ts.length - 1].endIndex()];
        int i = 0;
        for (Token t : ts) {
            Arrays.fill(chars, i, t.startIndex(), ' ');
            chars[t.startIndex()] = 94;
            if (Math.abs(t.startIndex() - t.endIndex()) > 1) {
                Arrays.fill(chars, t.startIndex() + 1, t.endIndex() - 1, '~');
            }
            chars[t.endIndex() - 1] = 94;
            i = t.endIndex();
        }
        return new String(chars);
    }

    @Override
    public int startIndex() {
        if (this.tokens.length == 0) {
            return -1;
        }
        return this.tokens[0].startIndex();
    }

    @Override
    public int endIndex() {
        if (this.tokens.length == 0) {
            return -1;
        }
        return this.tokens[this.tokens.length - 1].endIndex();
    }
}

