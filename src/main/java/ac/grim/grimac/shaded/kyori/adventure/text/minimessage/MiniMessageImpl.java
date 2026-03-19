/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.pointer.Pointered;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.ContextImpl;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.MiniMessage;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.MiniMessageParser;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.MiniMessageSerializer;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import ac.grim.grimac.shaded.kyori.adventure.util.Services;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

final class MiniMessageImpl
implements MiniMessage {
    private static final Optional<MiniMessage.Provider> SERVICE = Services.service(MiniMessage.Provider.class);
    static final Consumer<MiniMessage.Builder> BUILDER = SERVICE.map(MiniMessage.Provider::builder).orElseGet(() -> builder -> {});
    static final UnaryOperator<String> DEFAULT_NO_OP = UnaryOperator.identity();
    static final UnaryOperator<Component> DEFAULT_COMPACTING_METHOD = Component::compact;
    private final boolean strict;
    private final boolean emitVirtuals;
    @Nullable
    private final Consumer<String> debugOutput;
    private final UnaryOperator<Component> postProcessor;
    private final UnaryOperator<String> preProcessor;
    final MiniMessageParser parser;

    MiniMessageImpl(@NotNull TagResolver resolver, boolean strict, boolean emitVirtuals, @Nullable Consumer<String> debugOutput, @NotNull UnaryOperator<String> preProcessor, @NotNull UnaryOperator<Component> postProcessor) {
        this.parser = new MiniMessageParser(resolver);
        this.strict = strict;
        this.emitVirtuals = emitVirtuals;
        this.debugOutput = debugOutput;
        this.preProcessor = preProcessor;
        this.postProcessor = postProcessor;
    }

    @Override
    @NotNull
    public Component deserialize(@NotNull String input) {
        return this.parser.parseFormat(this.newContext(input, null, null));
    }

    @Override
    @NotNull
    public Component deserialize(@NotNull String input, @NotNull Pointered target) {
        return this.parser.parseFormat(this.newContext(input, Objects.requireNonNull(target, "target"), null));
    }

    @Override
    @NotNull
    public Component deserialize(@NotNull String input, @NotNull TagResolver tagResolver) {
        return this.parser.parseFormat(this.newContext(input, null, Objects.requireNonNull(tagResolver, "tagResolver")));
    }

    @Override
    @NotNull
    public Component deserialize(@NotNull String input, @NotNull Pointered target, @NotNull TagResolver tagResolver) {
        return this.parser.parseFormat(this.newContext(input, Objects.requireNonNull(target, "target"), Objects.requireNonNull(tagResolver, "tagResolver")));
    }

    @Override
    public  @NotNull Node.Root deserializeToTree(@NotNull String input) {
        return this.parser.parseToTree(this.newContext(input, null, null));
    }

    @Override
    public  @NotNull Node.Root deserializeToTree(@NotNull String input, @NotNull Pointered target) {
        return this.parser.parseToTree(this.newContext(input, Objects.requireNonNull(target, "target"), null));
    }

    @Override
    public  @NotNull Node.Root deserializeToTree(@NotNull String input, @NotNull TagResolver tagResolver) {
        return this.parser.parseToTree(this.newContext(input, null, Objects.requireNonNull(tagResolver, "tagResolver")));
    }

    @Override
    public  @NotNull Node.Root deserializeToTree(@NotNull String input, @NotNull Pointered target, @NotNull TagResolver tagResolver) {
        return this.parser.parseToTree(this.newContext(input, Objects.requireNonNull(target, "target"), Objects.requireNonNull(tagResolver, "tagResolver")));
    }

    @Override
    @NotNull
    public String serialize(@NotNull Component component) {
        return MiniMessageSerializer.serialize(component, this.serialResolver(null), this.strict);
    }

    private SerializableResolver serialResolver(@Nullable TagResolver extraResolver) {
        if (extraResolver == null) {
            if (this.parser.tagResolver instanceof SerializableResolver) {
                return (SerializableResolver)((Object)this.parser.tagResolver);
            }
        } else {
            TagResolver combined = TagResolver.resolver(this.parser.tagResolver, extraResolver);
            if (combined instanceof SerializableResolver) {
                return (SerializableResolver)((Object)combined);
            }
        }
        return (SerializableResolver)((Object)TagResolver.empty());
    }

    @Override
    @NotNull
    public String escapeTags(@NotNull String input) {
        return this.parser.escapeTokens(this.newContext(input, null, null));
    }

    @Override
    @NotNull
    public String escapeTags(@NotNull String input, @NotNull TagResolver tagResolver) {
        return this.parser.escapeTokens(this.newContext(input, null, tagResolver));
    }

    @Override
    @NotNull
    public String stripTags(@NotNull String input) {
        return this.parser.stripTokens(this.newContext(input, null, null));
    }

    @Override
    @NotNull
    public String stripTags(@NotNull String input, @NotNull TagResolver tagResolver) {
        return this.parser.stripTokens(this.newContext(input, null, tagResolver));
    }

    @Override
    public boolean strict() {
        return this.strict;
    }

    @Override
    @NotNull
    public TagResolver tags() {
        return this.parser.tagResolver;
    }

    @NotNull
    private ContextImpl newContext(@NotNull String input, @Nullable Pointered target, @Nullable TagResolver resolver) {
        Objects.requireNonNull(input, "input");
        return new ContextImpl(this.strict, this.emitVirtuals, this.debugOutput, input, this, target, resolver, this.preProcessor, this.postProcessor);
    }

    static /* synthetic */ Optional access$000() {
        return SERVICE;
    }

    static final class BuilderImpl
    implements MiniMessage.Builder {
        private TagResolver tagResolver = TagResolver.standard();
        private boolean strict = false;
        private boolean emitVirtuals = true;
        private Consumer<String> debug = null;
        private UnaryOperator<Component> postProcessor = DEFAULT_COMPACTING_METHOD;
        private UnaryOperator<String> preProcessor = DEFAULT_NO_OP;

        BuilderImpl() {
            BUILDER.accept(this);
        }

        BuilderImpl(MiniMessageImpl serializer) {
            this();
            this.tagResolver = serializer.parser.tagResolver;
            this.strict = serializer.strict;
            this.debug = serializer.debugOutput;
            this.postProcessor = serializer.postProcessor;
            this.preProcessor = serializer.preProcessor;
        }

        @Override
        @NotNull
        public MiniMessage.Builder tags(@NotNull TagResolver tags) {
            this.tagResolver = Objects.requireNonNull(tags, "tags");
            return this;
        }

        @Override
        @NotNull
        public MiniMessage.Builder editTags(@NotNull Consumer<TagResolver.Builder> adder) {
            Objects.requireNonNull(adder, "adder");
            TagResolver.Builder builder = TagResolver.builder().resolver(this.tagResolver);
            adder.accept(builder);
            this.tagResolver = builder.build();
            return this;
        }

        @Override
        @NotNull
        public MiniMessage.Builder strict(boolean strict) {
            this.strict = strict;
            return this;
        }

        @Override
        @NotNull
        public MiniMessage.Builder emitVirtuals(boolean emitVirtuals) {
            this.emitVirtuals = emitVirtuals;
            return this;
        }

        @Override
        @NotNull
        public MiniMessage.Builder debug(@Nullable Consumer<String> debugOutput) {
            this.debug = debugOutput;
            return this;
        }

        @Override
        @NotNull
        public MiniMessage.Builder postProcessor(@NotNull UnaryOperator<Component> postProcessor) {
            this.postProcessor = Objects.requireNonNull(postProcessor, "postProcessor");
            return this;
        }

        @Override
        @NotNull
        public MiniMessage.Builder preProcessor(@NotNull UnaryOperator<String> preProcessor) {
            this.preProcessor = Objects.requireNonNull(preProcessor, "preProcessor");
            return this;
        }

        @Override
        @NotNull
        public MiniMessage build() {
            return new MiniMessageImpl(this.tagResolver, this.strict, this.emitVirtuals, this.debug, this.preProcessor, this.postProcessor);
        }
    }

    static final class Instances {
        static final MiniMessage INSTANCE = MiniMessageImpl.access$000().map(MiniMessage.Provider::miniMessage).orElseGet(() -> new MiniMessageImpl(TagResolver.standard(), false, true, null, DEFAULT_NO_OP, DEFAULT_COMPACTING_METHOD));

        Instances() {
        }
    }
}

