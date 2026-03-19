/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.match;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.TagInternals;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.Token;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.TokenParser;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.TokenType;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.match.MatchedTokenConsumer;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node.TagPart;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.PreProcess;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class StringResolvingMatchedTokenConsumer
extends MatchedTokenConsumer<String> {
    private final StringBuilder builder;
    private final TokenParser.TagProvider tagProvider;

    public StringResolvingMatchedTokenConsumer(@NotNull String input, @NotNull TokenParser.TagProvider tagProvider) {
        super(input);
        this.builder = new StringBuilder(input.length());
        this.tagProvider = tagProvider;
    }

    @Override
    public void accept(int start, int end, @NotNull TokenType tokenType) {
        super.accept(start, end, tokenType);
        if (tokenType != TokenType.OPEN_TAG) {
            this.builder.append(this.input, start, end);
        } else {
            String tag;
            String match = this.input.substring(start, end);
            String cleanup = this.input.substring(start + 1, end - 1);
            int index = cleanup.indexOf(58);
            String string = tag = index == -1 ? cleanup : cleanup.substring(0, index);
            if (TagInternals.sanitizeAndCheckValidTagName(tag)) {
                Tag replacement;
                List<Token> childs;
                List<Token> tokens = TokenParser.tokenize(match, false);
                ArrayList<TagPart> parts = new ArrayList<TagPart>();
                List<Token> list = childs = tokens.isEmpty() ? null : tokens.get(0).childTokens();
                if (childs != null) {
                    for (int i = 1; i < childs.size(); ++i) {
                        parts.add(new TagPart(match, childs.get(i), this.tagProvider));
                    }
                }
                if ((replacement = this.tagProvider.resolve(TokenParser.TagProvider.sanitizePlaceholderName(tag), parts, tokens.get(0))) instanceof PreProcess) {
                    this.builder.append(Objects.requireNonNull(((PreProcess)replacement).value(), "PreProcess replacements cannot return null"));
                    return;
                }
            }
            this.builder.append(match);
        }
    }

    @Override
    @NotNull
    public String result() {
        return this.builder.toString();
    }
}

