/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.TextComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.ClaimConsumer;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.QuotingOverride;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

final class MiniMessageSerializer {
    private MiniMessageSerializer() {
    }

    @NotNull
    static String serialize(@NotNull Component component, @NotNull SerializableResolver resolver, boolean strict) {
        StringBuilder sb = new StringBuilder();
        Collector emitter = new Collector(resolver, strict, sb);
        emitter.mark();
        MiniMessageSerializer.visit(component, emitter, resolver, true);
        if (strict) {
            emitter.popAll();
        } else {
            emitter.completeTag();
        }
        return sb.toString();
    }

    private static void visit(@NotNull Component component, Collector emitter, SerializableResolver resolver, boolean lastChild) {
        resolver.handle(component, emitter);
        Component childSource = emitter.flushClaims(component);
        if (childSource == null) {
            childSource = component;
        }
        Iterator<Component> it = childSource.children().iterator();
        while (it.hasNext()) {
            emitter.mark();
            MiniMessageSerializer.visit(it.next(), emitter, resolver, lastChild && !it.hasNext());
        }
        if (!lastChild) {
            emitter.popToMark();
        }
    }

    static final class Collector
    implements TokenEmitter,
    ClaimConsumer {
        private static final String MARK = "__<'\"\\MARK__";
        private static final char[] TEXT_ESCAPES = new char[]{'\\', '<'};
        private static final char[] TAG_TOKENS = new char[]{'>', ':'};
        private static final char[] SINGLE_QUOTED_ESCAPES = new char[]{'\\', '\''};
        private static final char[] DOUBLE_QUOTED_ESCAPES = new char[]{'\\', '\"'};
        private final SerializableResolver resolver;
        private final boolean strict;
        private final StringBuilder consumer;
        private String[] activeTags = new String[4];
        private int tagLevel = 0;
        private TagState tagState = TagState.TEXT;
        @Nullable
        Emitable componentClaim;
        final Set<String> claimedStyleElements = new HashSet<String>();

        Collector(SerializableResolver resolver, boolean strict, StringBuilder consumer) {
            this.resolver = resolver;
            this.strict = strict;
            this.consumer = consumer;
        }

        private void pushActiveTag(String tag) {
            if (this.tagLevel >= this.activeTags.length) {
                this.activeTags = Arrays.copyOf(this.activeTags, this.activeTags.length * 2);
            }
            this.activeTags[this.tagLevel++] = tag;
        }

        private String popTag(boolean allowMarks) {
            if (this.tagLevel-- <= 0) {
                throw new IllegalStateException("Unbalanced tags, tried to pop below depth");
            }
            String tag = this.activeTags[this.tagLevel];
            if (!allowMarks && tag == MARK) {
                throw new IllegalStateException("Tried to pop past mark, tag stack: " + Arrays.toString(this.activeTags) + " @ " + this.tagLevel);
            }
            return tag;
        }

        void mark() {
            this.pushActiveTag(MARK);
        }

        void popToMark() {
            String tag;
            if (this.tagLevel == 0) {
                return;
            }
            while ((tag = this.popTag(true)) != MARK) {
                this.emitClose(tag);
            }
        }

        void popAll() {
            while (this.tagLevel > 0) {
                String tag;
                if ((tag = this.activeTags[--this.tagLevel]) == MARK) continue;
                this.emitClose(tag);
            }
        }

        void completeTag() {
            if (this.tagState.isTag) {
                this.consumer.append('>');
                this.tagState = TagState.TEXT;
            }
        }

        @Override
        @NotNull
        public Collector tag(@NotNull String token) {
            this.completeTag();
            this.consumer.append('<');
            this.escapeTagContent(token, QuotingOverride.UNQUOTED);
            this.tagState = TagState.MID;
            this.pushActiveTag(token);
            return this;
        }

        @Override
        @NotNull
        public TokenEmitter selfClosingTag(@NotNull String token) {
            this.completeTag();
            this.consumer.append('<');
            this.escapeTagContent(token, QuotingOverride.UNQUOTED);
            this.tagState = TagState.MID_SELF_CLOSING;
            return this;
        }

        @Override
        @NotNull
        public TokenEmitter argument(@NotNull String arg) {
            if (!this.tagState.isTag) {
                throw new IllegalStateException("Not within a tag!");
            }
            this.consumer.append(':');
            this.escapeTagContent(arg, null);
            return this;
        }

        @Override
        @NotNull
        public TokenEmitter argument(@NotNull String arg, @NotNull QuotingOverride quotingPreference) {
            if (!this.tagState.isTag) {
                throw new IllegalStateException("Not within a tag!");
            }
            this.consumer.append(':');
            this.escapeTagContent(arg, Objects.requireNonNull(quotingPreference, "quotingPreference"));
            return this;
        }

        @Override
        @NotNull
        public TokenEmitter argument(@NotNull Component arg) {
            String serialized = MiniMessageSerializer.serialize(arg, this.resolver, this.strict);
            return this.argument(serialized, QuotingOverride.QUOTED);
        }

        @Override
        @NotNull
        public Collector text(@NotNull String text) {
            this.completeTag();
            Collector.appendEscaping(this.consumer, text, TEXT_ESCAPES, true);
            return this;
        }

        private void escapeTagContent(String content, @Nullable QuotingOverride preference) {
            boolean mustBeQuoted = preference == QuotingOverride.QUOTED;
            boolean hasSingleQuote = false;
            boolean hasDoubleQuote = false;
            for (int i = 0; i < content.length(); ++i) {
                char active = content.charAt(i);
                if (active == '>' || active == ':' || active == ' ') {
                    mustBeQuoted = true;
                    if (!hasSingleQuote || !hasDoubleQuote) continue;
                    break;
                }
                if (active == '\'') {
                    hasSingleQuote = true;
                    break;
                }
                if (active != '\"') continue;
                hasDoubleQuote = true;
                if (mustBeQuoted && hasSingleQuote) break;
            }
            if (hasSingleQuote) {
                this.consumer.append('\"');
                Collector.appendEscaping(this.consumer, content, DOUBLE_QUOTED_ESCAPES, true);
                this.consumer.append('\"');
            } else if (hasDoubleQuote || mustBeQuoted) {
                this.consumer.append('\'');
                Collector.appendEscaping(this.consumer, content, SINGLE_QUOTED_ESCAPES, true);
                this.consumer.append('\'');
            } else {
                Collector.appendEscaping(this.consumer, content, TAG_TOKENS, false);
            }
        }

        static void appendEscaping(StringBuilder builder, String text, char[] escapeChars, boolean allowEscapes) {
            int startIdx = 0;
            boolean unescapedFound = false;
            for (int i = 0; i < text.length(); ++i) {
                char test = text.charAt(i);
                boolean escaped = false;
                for (char c : escapeChars) {
                    if (test != c) continue;
                    if (!allowEscapes) {
                        throw new IllegalArgumentException("Invalid escapable character '" + test + "' found at index " + i + " in string '" + text + "'");
                    }
                    escaped = true;
                    break;
                }
                if (escaped) {
                    if (unescapedFound) {
                        builder.append(text, startIdx, i);
                    }
                    startIdx = i + 1;
                    builder.append('\\').append(test);
                    continue;
                }
                unescapedFound = true;
            }
            if (startIdx < text.length() && unescapedFound) {
                builder.append(text, startIdx, text.length());
            }
        }

        @Override
        @NotNull
        public Collector pop() {
            this.emitClose(this.popTag(false));
            return this;
        }

        private void emitClose(@NotNull String tag) {
            if (this.tagState.isTag) {
                if (this.tagState == TagState.MID) {
                    this.consumer.append('/');
                }
                this.consumer.append('>');
                this.tagState = TagState.TEXT;
            } else {
                this.consumer.append('<').append('/');
                this.escapeTagContent(tag, QuotingOverride.UNQUOTED);
                this.consumer.append('>');
            }
        }

        @Override
        public void style(@NotNull String claimKey, @NotNull Emitable styleClaim) {
            if (this.claimedStyleElements.add(Objects.requireNonNull(claimKey, "claimKey"))) {
                styleClaim.emit(this);
            }
        }

        @Override
        public boolean component(@NotNull Emitable componentClaim) {
            if (this.componentClaim != null) {
                return false;
            }
            this.componentClaim = Objects.requireNonNull(componentClaim, "componentClaim");
            return true;
        }

        @Override
        public boolean componentClaimed() {
            return this.componentClaim != null;
        }

        @Override
        public boolean styleClaimed(@NotNull String claimId) {
            return this.claimedStyleElements.contains(claimId);
        }

        @Nullable
        Component flushClaims(Component component) {
            Component ret = null;
            if (this.componentClaim != null) {
                this.componentClaim.emit(this);
                ret = this.componentClaim.substitute();
                this.componentClaim = null;
            } else if (component instanceof TextComponent) {
                this.text(((TextComponent)component).content());
            } else {
                throw new IllegalStateException("Unclaimed component " + component);
            }
            this.claimedStyleElements.clear();
            return ret;
        }

        static enum TagState {
            TEXT(false),
            MID(true),
            MID_SELF_CLOSING(true);

            final boolean isTag;

            private TagState(boolean isTag) {
                this.isTag = isTag;
            }
        }
    }
}

