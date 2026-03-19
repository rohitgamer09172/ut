/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.pointer.Pointered;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.ArgumentQueueImpl;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.MiniMessage;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.ParsingException;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.ParsingExceptionImpl;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.Token;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node.TagPart;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

class ContextImpl
implements Context {
    private static final Token[] EMPTY_TOKEN_ARRAY = new Token[0];
    private final boolean strict;
    private final boolean emitVirtuals;
    private final Consumer<String> debugOutput;
    private String message;
    private final MiniMessage miniMessage;
    @Nullable
    private final Pointered target;
    private final TagResolver tagResolver;
    private final UnaryOperator<String> preProcessor;
    private final UnaryOperator<Component> postProcessor;

    ContextImpl(boolean strict, boolean emitVirtuals, Consumer<String> debugOutput, String message, MiniMessage miniMessage, @Nullable Pointered target, @Nullable TagResolver extraTags, @Nullable UnaryOperator<String> preProcessor, @Nullable UnaryOperator<Component> postProcessor) {
        this.strict = strict;
        this.emitVirtuals = emitVirtuals;
        this.debugOutput = debugOutput;
        this.message = message;
        this.miniMessage = miniMessage;
        this.target = target;
        this.tagResolver = extraTags == null ? TagResolver.empty() : extraTags;
        this.preProcessor = preProcessor == null ? UnaryOperator.identity() : preProcessor;
        this.postProcessor = postProcessor == null ? UnaryOperator.identity() : postProcessor;
    }

    public boolean strict() {
        return this.strict;
    }

    @Override
    public boolean emitVirtuals() {
        return this.emitVirtuals;
    }

    public Consumer<String> debugOutput() {
        return this.debugOutput;
    }

    @NotNull
    public String message() {
        return this.message;
    }

    void message(@NotNull String message) {
        this.message = message;
    }

    @NotNull
    public TagResolver extraTags() {
        return this.tagResolver;
    }

    public UnaryOperator<Component> postProcessor() {
        return this.postProcessor;
    }

    public UnaryOperator<String> preProcessor() {
        return this.preProcessor;
    }

    @Override
    @Nullable
    public Pointered target() {
        return this.target;
    }

    @Override
    @NotNull
    public Pointered targetOrThrow() {
        if (this.target == null) {
            throw this.newException("A target is required for this deserialization attempt");
        }
        return this.target;
    }

    @Override
    @NotNull
    public <T extends Pointered> T targetAsType(@NotNull Class<T> targetClass) {
        if (Objects.requireNonNull(targetClass, "targetClass").isInstance(this.target)) {
            return (T)((Pointered)targetClass.cast(this.target));
        }
        throw this.newException("A target with type " + targetClass.getSimpleName() + " is required for this deserialization attempt");
    }

    @Override
    @NotNull
    public Component deserialize(@NotNull String message) {
        return this.deserializeWithOptionalTarget(Objects.requireNonNull(message, "message"), this.tagResolver);
    }

    @Override
    @NotNull
    public Component deserialize(@NotNull String message, @NotNull TagResolver resolver) {
        Objects.requireNonNull(message, "message");
        TagResolver combinedResolver = TagResolver.builder().resolver(this.tagResolver).resolver(resolver).build();
        return this.deserializeWithOptionalTarget(message, combinedResolver);
    }

    @Override
    @NotNull
    public Component deserialize(@NotNull String message, TagResolver ... resolvers) {
        Objects.requireNonNull(message, "message");
        TagResolver combinedResolver = TagResolver.builder().resolver(this.tagResolver).resolvers(resolvers).build();
        return this.deserializeWithOptionalTarget(message, combinedResolver);
    }

    @Override
    @NotNull
    public ParsingException newException(@NotNull String message) {
        return new ParsingExceptionImpl(message, this.message, null, false, EMPTY_TOKEN_ARRAY);
    }

    @Override
    @NotNull
    public ParsingException newException(@NotNull String message, @NotNull ArgumentQueue tags) {
        return new ParsingExceptionImpl(message, this.message, null, false, ContextImpl.tagsToTokens(((ArgumentQueueImpl)tags).args));
    }

    @Override
    @NotNull
    public ParsingException newException(@NotNull String message, @Nullable Throwable cause, @NotNull ArgumentQueue tags) {
        return new ParsingExceptionImpl(message, this.message, cause, false, ContextImpl.tagsToTokens(((ArgumentQueueImpl)tags).args));
    }

    @NotNull
    private Component deserializeWithOptionalTarget(@NotNull String message, @NotNull TagResolver tagResolver) {
        if (this.target != null) {
            return this.miniMessage.deserialize(message, this.target, tagResolver);
        }
        return this.miniMessage.deserialize(message, tagResolver);
    }

    private static Token[] tagsToTokens(List<? extends Tag.Argument> tags) {
        Token[] tokens = new Token[tags.size()];
        int length = tokens.length;
        for (int i = 0; i < length; ++i) {
            tokens[i] = ((TagPart)tags.get(i)).token();
        }
        return tokens;
    }
}

