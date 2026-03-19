/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.ArgumentQueueImpl;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.ContextImpl;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.ParsingException;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.ParsingExceptionImpl;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.Token;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.TokenParser;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.TokenType;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node.ElementNode;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node.RootNode;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node.TagNode;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node.ValueNode;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Inserting;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Modifying;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import ac.grim.grimac.shaded.kyori.examination.Examinable;
import ac.grim.grimac.shaded.kyori.examination.string.MultiLineStringExaminer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

final class MiniMessageParser {
    final TagResolver tagResolver;

    MiniMessageParser() {
        this.tagResolver = TagResolver.standard();
    }

    MiniMessageParser(TagResolver tagResolver) {
        this.tagResolver = tagResolver;
    }

    @NotNull
    String escapeTokens(@NotNull ContextImpl context) {
        StringBuilder sb = new StringBuilder(context.message().length());
        this.escapeTokens(sb, context);
        return sb.toString();
    }

    void escapeTokens(StringBuilder sb, @NotNull ContextImpl context) {
        this.escapeTokens(sb, context.message(), context);
    }

    private void escapeTokens(StringBuilder sb, String richMessage, ContextImpl context) {
        this.processTokens(sb, richMessage, context, (token, builder) -> {
            builder.append('\\').append('<');
            if (token.type() == TokenType.CLOSE_TAG) {
                builder.append('/');
            }
            List<Token> childTokens = token.childTokens();
            for (int i = 0; i < childTokens.size(); ++i) {
                if (i != 0) {
                    builder.append(':');
                }
                this.escapeTokens((StringBuilder)builder, childTokens.get(i).get(richMessage).toString(), context);
            }
            builder.append('>');
        });
    }

    @NotNull
    String stripTokens(@NotNull ContextImpl context) {
        StringBuilder sb = new StringBuilder(context.message().length());
        this.processTokens(sb, context, (token, builder) -> {});
        return sb.toString();
    }

    private void processTokens(@NotNull StringBuilder sb, @NotNull ContextImpl context, BiConsumer<Token, StringBuilder> tagHandler) {
        this.processTokens(sb, context.message(), context, tagHandler);
    }

    private void processTokens(@NotNull StringBuilder sb, @NotNull String richMessage, @NotNull ContextImpl context, BiConsumer<Token, StringBuilder> tagHandler) {
        TagResolver combinedResolver = TagResolver.resolver(this.tagResolver, context.extraTags());
        List<Token> root = TokenParser.tokenize(richMessage, true);
        block4: for (Token token : root) {
            switch (token.type()) {
                case TEXT: {
                    sb.append(richMessage, token.startIndex(), token.endIndex());
                    continue block4;
                }
                case OPEN_TAG: 
                case CLOSE_TAG: 
                case OPEN_CLOSE_TAG: {
                    if (token.childTokens().isEmpty()) {
                        sb.append(richMessage, token.startIndex(), token.endIndex());
                        continue block4;
                    }
                    String sanitized = TokenParser.TagProvider.sanitizePlaceholderName(token.childTokens().get(0).get(richMessage).toString());
                    if (combinedResolver.has(sanitized)) {
                        tagHandler.accept(token, sb);
                        continue block4;
                    }
                    sb.append(richMessage, token.startIndex(), token.endIndex());
                    continue block4;
                }
            }
            throw new IllegalArgumentException("Unsupported token type " + (Object)((Object)token.type()));
        }
    }

    @NotNull
    RootNode parseToTree(@NotNull ContextImpl context) {
        TagResolver combinedResolver = TagResolver.resolver(this.tagResolver, context.extraTags());
        String processedMessage = (String)context.preProcessor().apply(context.message());
        Consumer<String> debug = context.debugOutput();
        if (debug != null) {
            debug.accept("Beginning parsing message ");
            debug.accept(processedMessage);
            debug.accept("\n");
        }
        TokenParser.TagProvider transformationFactory = debug != null ? (name, args, token) -> {
            try {
                debug.accept("Attempting to match node '");
                debug.accept(name);
                debug.accept("'");
                if (token != null) {
                    debug.accept(" at column ");
                    debug.accept(String.valueOf(token.startIndex()));
                }
                debug.accept("\n");
                @Nullable Tag transformation = combinedResolver.resolve(name, new ArgumentQueueImpl(context, args), context);
                if (transformation == null) {
                    debug.accept("Could not match node '");
                    debug.accept(name);
                    debug.accept("'\n");
                } else {
                    debug.accept("Successfully matched node '");
                    debug.accept(name);
                    debug.accept("' to tag ");
                    debug.accept(transformation instanceof Examinable ? ((Examinable)((Object)transformation)).examinableName() : transformation.getClass().getName());
                    debug.accept("\n");
                }
                return transformation;
            }
            catch (ParsingException e) {
                ParsingExceptionImpl impl;
                if (token != null && e instanceof ParsingExceptionImpl && (impl = (ParsingExceptionImpl)e).tokens().length == 0) {
                    impl.tokens(new Token[]{token});
                }
                debug.accept("Could not match node '");
                debug.accept(name);
                debug.accept("' - ");
                debug.accept(e.getMessage());
                debug.accept("\n");
                return null;
            }
        } : (name, args, token) -> {
            try {
                return combinedResolver.resolve(name, new ArgumentQueueImpl(context, args), context);
            }
            catch (ParsingException ignored) {
                return null;
            }
        };
        Predicate<String> tagNameChecker = name -> {
            String sanitized = TokenParser.TagProvider.sanitizePlaceholderName(name);
            return combinedResolver.has(sanitized);
        };
        String preProcessed = TokenParser.resolvePreProcessTags(processedMessage, transformationFactory);
        context.message(preProcessed);
        RootNode root = TokenParser.parse(transformationFactory, tagNameChecker, preProcessed, processedMessage, context.strict());
        if (debug != null) {
            debug.accept("Text parsed into element tree:\n");
            debug.accept(root.toString());
        }
        return root;
    }

    @NotNull
    Component parseFormat(@NotNull ContextImpl context) {
        RootNode root = this.parseToTree(context);
        return Objects.requireNonNull((Component)context.postProcessor().apply(this.treeToComponent(root, context)), "Post-processor must not return null");
    }

    @NotNull
    Component treeToComponent(@NotNull ElementNode node, @NotNull ContextImpl context) {
        Consumer<String> debug;
        Component comp = Component.empty();
        Tag tag = null;
        if (node instanceof ValueNode) {
            comp = Component.text(((ValueNode)node).value());
        } else if (node instanceof TagNode) {
            TagNode tagNode = (TagNode)node;
            tag = tagNode.tag();
            if (tag instanceof Modifying) {
                Modifying modTransformation = (Modifying)tag;
                this.visitModifying(modTransformation, tagNode, 0);
                modTransformation.postVisit();
            }
            if (tag instanceof Inserting) {
                comp = ((Inserting)tag).value();
            }
        }
        if (!node.unsafeChildren().isEmpty()) {
            ArrayList<Component> children = new ArrayList<Component>(comp.children().size() + node.children().size());
            children.addAll(comp.children());
            for (ElementNode child : node.unsafeChildren()) {
                children.add(this.treeToComponent(child, context));
            }
            comp = comp.children(children);
        }
        if (tag instanceof Modifying) {
            comp = this.handleModifying((Modifying)tag, comp, 0);
        }
        if ((debug = context.debugOutput()) != null) {
            debug.accept("==========\ntreeToComponent \n");
            debug.accept(node.toString());
            debug.accept("\n");
            debug.accept(comp.examine(MultiLineStringExaminer.simpleEscaping()).collect(Collectors.joining("\n")));
            debug.accept("\n==========\n");
        }
        return comp;
    }

    private void visitModifying(Modifying modTransformation, ElementNode node, int depth) {
        modTransformation.visit(node, depth);
        for (ElementNode child : node.unsafeChildren()) {
            this.visitModifying(modTransformation, child, depth + 1);
        }
    }

    private Component handleModifying(Modifying modTransformation, Component current, int depth) {
        Component newComp = modTransformation.apply(current, depth);
        for (Component child : current.children()) {
            newComp = newComp.append(this.handleModifying(modTransformation, child, depth + 1));
        }
        return newComp;
    }
}

