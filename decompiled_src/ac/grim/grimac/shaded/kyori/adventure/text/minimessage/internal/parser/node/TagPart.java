/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.Token;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.TokenParser;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;

public final class TagPart
implements Tag.Argument {
    private final String value;
    private final Token token;

    public TagPart(@NotNull String sourceMessage, @NotNull Token token, @NotNull TokenParser.TagProvider tagResolver) {
        String v = TagPart.unquoteAndEscape(sourceMessage, token.startIndex(), token.endIndex());
        this.value = v = TokenParser.resolvePreProcessTags(v, tagResolver);
        this.token = token;
    }

    @Override
    @NotNull
    public String value() {
        return this.value;
    }

    @NotNull
    public Token token() {
        return this.token;
    }

    @NotNull
    public static String unquoteAndEscape(@NotNull String text, int start, int end) {
        if (start == end) {
            return "";
        }
        int startIndex = start;
        int endIndex = end;
        char firstChar = text.charAt(startIndex);
        char lastChar = text.charAt(endIndex - 1);
        if (firstChar == '\'' || firstChar == '\"') {
            ++startIndex;
        } else {
            return text.substring(startIndex, endIndex);
        }
        if (lastChar == '\'' || lastChar == '\"') {
            --endIndex;
        }
        if (startIndex > endIndex) {
            return text.substring(start, end);
        }
        return TokenParser.unescape(text, startIndex, endIndex, i -> i == firstChar || i == 92);
    }

    public String toString() {
        return this.value;
    }
}

