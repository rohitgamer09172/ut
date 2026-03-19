/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.ParsingException;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.TagInternals;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.ParsingExceptionImpl;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.Token;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.TokenType;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.match.MatchedTokenConsumer;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.match.StringResolvingMatchedTokenConsumer;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.match.TokenListProducingMatchedTokenConsumer;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node.ElementNode;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node.RootNode;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node.TagNode;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node.TagPart;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node.TextNode;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Inserting;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.ParserDirective;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

@ApiStatus.Internal
public final class TokenParser {
    private static final int MAX_DEPTH = 16;
    public static final char TAG_START = '<';
    public static final char TAG_END = '>';
    public static final char CLOSE_TAG = '/';
    public static final char SEPARATOR = ':';
    public static final char ESCAPE = '\\';

    private TokenParser() {
    }

    public static RootNode parse(@NotNull TagProvider tagProvider, @NotNull Predicate<String> tagNameChecker, @NotNull String message, @NotNull String originalMessage, boolean strict) throws ParsingException {
        List<Token> tokens = TokenParser.tokenize(message, false);
        return TokenParser.buildTree(tagProvider, tagNameChecker, tokens, message, originalMessage, strict);
    }

    public static String resolvePreProcessTags(String message, TagProvider provider) {
        String lastResult;
        int passes = 0;
        String result = message;
        do {
            lastResult = result;
            StringResolvingMatchedTokenConsumer stringTokenResolver = new StringResolvingMatchedTokenConsumer(lastResult, provider);
            TokenParser.parseString(lastResult, false, stringTokenResolver);
            result = stringTokenResolver.result();
        } while (++passes < 16 && !lastResult.equals(result));
        return lastResult;
    }

    public static List<Token> tokenize(String message, boolean lenient) {
        TokenListProducingMatchedTokenConsumer listProducer = new TokenListProducingMatchedTokenConsumer(message);
        TokenParser.parseString(message, lenient, listProducer);
        Object tokens = listProducer.result();
        TokenParser.parseSecondPass(message, (List<Token>)tokens);
        return tokens;
    }

    public static void parseString(String message, boolean lenient, MatchedTokenConsumer<?> consumer) {
        FirstPassState state = FirstPassState.NORMAL;
        boolean escaped = false;
        int currentTokenEnd = 0;
        int marker = -1;
        int currentStringChar = 0;
        int length = message.length();
        for (int i = 0; i < length; ++i) {
            int nextChar;
            int codePoint = message.codePointAt(i);
            if (!lenient && codePoint == 167 && i + 1 < length && ((nextChar = Character.toLowerCase(message.codePointAt(i + 1))) >= 48 && nextChar <= 57 || nextChar >= 97 && nextChar <= 102 || nextChar == 114 || nextChar >= 107 && nextChar <= 111)) {
                throw new ParsingExceptionImpl("Legacy formatting codes have been detected in a MiniMessage string - this is unsupported behaviour. Please refer to the Adventure documentation (https://docs.advntr.dev) for more information.", message, null, true, new Token(i, i + 2, TokenType.TEXT));
            }
            if (!Character.isBmpCodePoint(codePoint)) {
                ++i;
            }
            if (!escaped) {
                if (codePoint == 92 && i + 1 < message.length()) {
                    int nextCodePoint = message.codePointAt(i + 1);
                    switch (state) {
                        case NORMAL: {
                            escaped = nextCodePoint == 60 || nextCodePoint == 92;
                            break;
                        }
                        case STRING: {
                            escaped = currentStringChar == nextCodePoint || nextCodePoint == 92;
                            break;
                        }
                        case TAG: {
                            if (nextCodePoint != 60) break;
                            escaped = true;
                            state = FirstPassState.NORMAL;
                        }
                    }
                    if (escaped) {
                        continue;
                    }
                }
            } else {
                escaped = false;
                continue;
            }
            switch (state) {
                case NORMAL: {
                    if (codePoint != 60) break;
                    marker = i;
                    state = FirstPassState.TAG;
                    break;
                }
                case TAG: {
                    switch (codePoint) {
                        case 62: {
                            if (i == marker + 1) {
                                state = FirstPassState.NORMAL;
                                break;
                            }
                            if (currentTokenEnd != marker) {
                                consumer.accept(currentTokenEnd, marker, TokenType.TEXT);
                            }
                            currentTokenEnd = i + 1;
                            TokenType thisType = TokenType.OPEN_TAG;
                            if (TokenParser.boundsCheck(message, marker, 1) && message.charAt(marker + 1) == '/') {
                                thisType = TokenType.CLOSE_TAG;
                            } else if (TokenParser.boundsCheck(message, marker, 2) && message.charAt(i - 1) == '/') {
                                thisType = TokenType.OPEN_CLOSE_TAG;
                            }
                            consumer.accept(marker, currentTokenEnd, thisType);
                            state = FirstPassState.NORMAL;
                            break;
                        }
                        case 60: {
                            marker = i;
                            break;
                        }
                        case 34: 
                        case 39: {
                            currentStringChar = (char)codePoint;
                            if (message.indexOf(codePoint, i + 1) == -1) break;
                            state = FirstPassState.STRING;
                        }
                    }
                    break;
                }
                case STRING: {
                    if (codePoint != currentStringChar) break;
                    state = FirstPassState.TAG;
                }
            }
            if (i != length - 1 || state != FirstPassState.TAG) continue;
            i = marker;
            state = FirstPassState.NORMAL;
        }
        int end = consumer.lastEndIndex();
        if (end == -1) {
            consumer.accept(0, message.length(), TokenType.TEXT);
        } else if (end != message.length()) {
            consumer.accept(end, message.length(), TokenType.TEXT);
        }
    }

    private static void parseSecondPass(String message, List<Token> tokens) {
        for (Token token : tokens) {
            TokenType type = token.type();
            if (type != TokenType.OPEN_TAG && type != TokenType.OPEN_CLOSE_TAG && type != TokenType.CLOSE_TAG) continue;
            int startIndex = type == TokenType.CLOSE_TAG ? token.startIndex() + 2 : token.startIndex() + 1;
            int endIndex = type == TokenType.OPEN_CLOSE_TAG ? token.endIndex() - 2 : token.endIndex() - 1;
            SecondPassState state = SecondPassState.NORMAL;
            boolean escaped = false;
            int currentStringChar = 0;
            int marker = startIndex;
            block9: for (int i = startIndex; i < endIndex; ++i) {
                int codePoint = message.codePointAt(i);
                if (!Character.isBmpCodePoint(i)) {
                    ++i;
                }
                if (!escaped) {
                    if (codePoint == 92 && i + 1 < message.length()) {
                        int nextCodePoint = message.codePointAt(i + 1);
                        switch (state) {
                            case NORMAL: {
                                escaped = nextCodePoint == 60 || nextCodePoint == 92;
                                break;
                            }
                            case STRING: {
                                boolean bl = escaped = currentStringChar == nextCodePoint || nextCodePoint == 92;
                            }
                        }
                        if (escaped) {
                            continue;
                        }
                    }
                } else {
                    escaped = false;
                    continue;
                }
                switch (state) {
                    case NORMAL: {
                        if (codePoint == 58) {
                            if (TokenParser.boundsCheck(message, i, 2) && message.charAt(i + 1) == '/' && message.charAt(i + 2) == '/') continue block9;
                            if (marker == i) {
                                TokenParser.insert(token, new Token(i, i, TokenType.TAG_VALUE));
                                ++marker;
                                continue block9;
                            }
                            TokenParser.insert(token, new Token(marker, i, TokenType.TAG_VALUE));
                            marker = i + 1;
                            continue block9;
                        }
                        if (codePoint != 39 && codePoint != 34) continue block9;
                        state = SecondPassState.STRING;
                        currentStringChar = (char)codePoint;
                        continue block9;
                    }
                    case STRING: {
                        if (codePoint != currentStringChar) continue block9;
                        state = SecondPassState.NORMAL;
                    }
                }
            }
            if (token.childTokens() == null || token.childTokens().isEmpty()) {
                TokenParser.insert(token, new Token(startIndex, endIndex, TokenType.TAG_VALUE));
                continue;
            }
            int end = token.childTokens().get(token.childTokens().size() - 1).endIndex();
            if (end == endIndex) continue;
            TokenParser.insert(token, new Token(end + 1, endIndex, TokenType.TAG_VALUE));
        }
    }

    private static RootNode buildTree(@NotNull TagProvider tagProvider, @NotNull Predicate<String> tagNameChecker, @NotNull List<Token> tokens, @NotNull String message, @NotNull String originalMessage, boolean strict) throws ParsingException {
        RootNode root = new RootNode(message, originalMessage);
        ElementNode node = root;
        for (Token token : tokens) {
            TokenType type = token.type();
            switch (type) {
                case TEXT: {
                    node.addChild(new TextNode(node, token, message));
                    break;
                }
                case OPEN_TAG: 
                case OPEN_CLOSE_TAG: {
                    Token tagNamePart = token.childTokens().get(0);
                    String tagName = message.substring(tagNamePart.startIndex(), tagNamePart.endIndex());
                    if (!TagInternals.sanitizeAndCheckValidTagName(tagName)) {
                        node.addChild(new TextNode(node, token, message));
                        break;
                    }
                    TagNode tagNode = new TagNode(node, token, message, tagProvider);
                    if (tagNameChecker.test(tagNode.name())) {
                        Tag tag = tagProvider.resolve(tagNode);
                        if (tag == null) {
                            node.addChild(new TextNode(node, token, message));
                            break;
                        }
                        if (tag == ParserDirective.RESET) {
                            if (strict) {
                                throw new ParsingExceptionImpl("<reset> tags are not allowed when strict mode is enabled", message, token);
                            }
                            node = root;
                            break;
                        }
                        tagNode.tag(tag);
                        node.addChild(tagNode);
                        if (type == TokenType.OPEN_CLOSE_TAG || tag instanceof Inserting && !((Inserting)tag).allowsChildren()) break;
                        node = tagNode;
                        break;
                    }
                    node.addChild(new TextNode(node, token, message));
                    break;
                }
                case CLOSE_TAG: {
                    List<Token> childTokens = token.childTokens();
                    if (childTokens.isEmpty()) {
                        throw new IllegalStateException("CLOSE_TAG token somehow has no children - the parser should not allow this. Original text: " + message);
                    }
                    ArrayList<String> closeValues = new ArrayList<String>(childTokens.size());
                    for (Token childToken : childTokens) {
                        closeValues.add(TagPart.unquoteAndEscape(message, childToken.startIndex(), childToken.endIndex()));
                    }
                    String closeTagName = (String)closeValues.get(0);
                    if (tagNameChecker.test(closeTagName)) {
                        Tag tag = tagProvider.resolve(closeTagName);
                        if (tag == ParserDirective.RESET) {
                            break;
                        }
                    } else {
                        node.addChild(new TextNode(node, token, message));
                        break;
                    }
                    ElementNode parentNode = node;
                    while (parentNode instanceof TagNode) {
                        List<TagPart> openParts = ((TagNode)parentNode).parts();
                        if (TokenParser.tagCloses(closeValues, openParts)) {
                            if (parentNode != node && strict) {
                                String msg = "Unclosed tag encountered; " + ((TagNode)node).name() + " is not closed, because " + closeValues.get(0) + " was closed first.";
                                throw new ParsingExceptionImpl(msg, message, parentNode.token(), node.token(), token);
                            }
                            ElementNode par = parentNode.parent();
                            if (par != null) {
                                node = par;
                                break;
                            }
                            throw new IllegalStateException("Root node matched with close tag value, this should not be possible. Original text: " + message);
                        }
                        parentNode = parentNode.parent();
                    }
                    if (parentNode != null && !(parentNode instanceof RootNode)) break;
                    node.addChild(new TextNode(node, token, message));
                    break;
                }
            }
        }
        if (strict && root != node) {
            ArrayList<TagNode> openTags = new ArrayList<TagNode>();
            for (ElementNode n = node; n != null && n instanceof TagNode; n = n.parent()) {
                openTags.add((TagNode)n);
            }
            Token[] errorTokens = new Token[openTags.size()];
            StringBuilder sb = new StringBuilder("All tags must be explicitly closed while in strict mode. End of string found with open tags: ");
            int i = 0;
            ListIterator iter = openTags.listIterator(openTags.size());
            while (iter.hasPrevious()) {
                TagNode n = (TagNode)iter.previous();
                errorTokens[i++] = n.token();
                sb.append(n.name());
                if (!iter.hasPrevious()) continue;
                sb.append(", ");
            }
            throw new ParsingExceptionImpl(sb.toString(), message, errorTokens);
        }
        return root;
    }

    private static boolean tagCloses(List<String> closeParts, List<TagPart> openParts) {
        if (closeParts.size() > openParts.size()) {
            return false;
        }
        if (!closeParts.get(0).equalsIgnoreCase(openParts.get(0).value())) {
            return false;
        }
        for (int i = 1; i < closeParts.size(); ++i) {
            if (closeParts.get(i).equals(openParts.get(i).value())) continue;
            return false;
        }
        return true;
    }

    private static boolean boundsCheck(String text, int index, int length) {
        return index + length < text.length();
    }

    private static void insert(Token token, Token value) {
        if (token.childTokens() == null) {
            token.childTokens(Collections.singletonList(value));
            return;
        }
        if (token.childTokens().size() == 1) {
            ArrayList<Token> list = new ArrayList<Token>(3);
            list.add(token.childTokens().get(0));
            list.add(value);
            token.childTokens(list);
        } else {
            token.childTokens().add(value);
        }
    }

    public static String unescape(String text, int startIndex, int endIndex, IntPredicate escapes) {
        int from = startIndex;
        int i = text.indexOf(92, from);
        if (i == -1 || i >= endIndex) {
            return text.substring(from, endIndex);
        }
        StringBuilder sb = new StringBuilder(endIndex - startIndex);
        while (i != -1 && i + 1 < endIndex) {
            if (escapes.test(text.codePointAt(i + 1))) {
                sb.append(text, from, i);
                if (++i >= endIndex) {
                    from = endIndex;
                    break;
                }
                int codePoint = text.codePointAt(i);
                sb.appendCodePoint(codePoint);
                i = Character.isBmpCodePoint(codePoint) ? ++i : (i += 2);
                if (i >= endIndex) {
                    from = endIndex;
                    break;
                }
            } else {
                sb.append(text, from, ++i);
            }
            from = i;
            i = text.indexOf(92, from);
        }
        sb.append(text, from, endIndex);
        return sb.toString();
    }

    @ApiStatus.Internal
    public static interface TagProvider {
        @Nullable
        public Tag resolve(@NotNull String var1, @NotNull List<? extends Tag.Argument> var2, @Nullable Token var3);

        @Nullable
        default public Tag resolve(@NotNull String name) {
            return this.resolve(name, Collections.emptyList(), null);
        }

        @Nullable
        default public Tag resolve(@NotNull TagNode node) {
            return this.resolve(TagProvider.sanitizePlaceholderName(node.name()), node.parts().subList(1, node.parts().size()), node.token());
        }

        @NotNull
        public static String sanitizePlaceholderName(@NotNull String name) {
            return name.toLowerCase(Locale.ROOT);
        }
    }

    static enum FirstPassState {
        NORMAL,
        TAG,
        STRING;

    }

    static enum SecondPassState {
        NORMAL,
        STRING;

    }
}

