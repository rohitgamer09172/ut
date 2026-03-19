/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.data.webhook.discord;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.GrimUser;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record CompiledDiscordTemplate(Segment[] segments) {
    private static final Pattern PLACEHOLDER = Pattern.compile("%([a-zA-Z0-9_]+)%");

    public static CompiledDiscordTemplate compile(String template) {
        ArrayList<Record> parts = new ArrayList<Record>();
        Matcher m = PLACEHOLDER.matcher(template);
        MarkdownContext ctx = MarkdownContext.NORMAL;
        int lastEnd = 0;
        while (m.find()) {
            String gap = template.substring(lastEnd, m.start());
            if (!gap.isEmpty()) {
                parts.add(new Literal(gap));
            }
            ctx = CompiledDiscordTemplate.advanceContext(ctx, gap);
            EscapeMode mode = switch (ctx.ordinal()) {
                default -> throw new IncompatibleClassChangeError();
                case 0 -> EscapeMode.FULL_MARKDOWN;
                case 1, 2 -> EscapeMode.CODE_SPAN;
            };
            parts.add(new Placeholder(m.group(0), mode));
            lastEnd = m.end();
        }
        if (lastEnd < template.length()) {
            parts.add(new Literal(template.substring(lastEnd)));
        }
        return new CompiledDiscordTemplate((Segment[])parts.toArray(Segment[]::new));
    }

    public String render(@NotNull GrimPlayer player, @NotNull Map<String, String> statics, @NotNull Map<String, Function<GrimUser, String>> dynamics, char backtickReplacement) {
        StringBuilder sb = new StringBuilder(this.segments.length * 32);
        for (Segment seg : this.segments) {
            String resolved;
            Function<GrimUser, String> fn;
            if (seg instanceof Literal) {
                Literal l = (Literal)seg;
                sb.append(l.text);
                continue;
            }
            if (!(seg instanceof Placeholder)) continue;
            Placeholder p = (Placeholder)seg;
            String val = statics.get(p.key);
            if (val == null && (fn = dynamics.get(p.key)) != null) {
                val = fn.apply(player);
            }
            if (val == null && !(resolved = GrimAPI.INSTANCE.getMessagePlaceHolderManager().replacePlaceholders(player.platformPlayer, p.key)).equals(p.key)) {
                val = resolved;
            }
            if (val != null) {
                sb.append(CompiledDiscordTemplate.escape(val, p.mode, backtickReplacement));
                continue;
            }
            sb.append(p.key);
        }
        return sb.toString();
    }

    private static String escape(String value, EscapeMode mode, char backtickReplacement) {
        if (mode == EscapeMode.NONE) {
            return value;
        }
        if (mode == EscapeMode.CODE_SPAN) {
            return CompiledDiscordTemplate.escapeCodeSpan(value, backtickReplacement);
        }
        return CompiledDiscordTemplate.escapeMarkdown(value);
    }

    public static String escapeMarkdown(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s.length() + 16);
        block19: for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            switch (c) {
                case '\\': {
                    sb.append("\\\\");
                    continue block19;
                }
                case '`': {
                    sb.append("\\`");
                    continue block19;
                }
                case '*': {
                    sb.append("\\*");
                    continue block19;
                }
                case '_': {
                    sb.append("\\_");
                    continue block19;
                }
                case '~': {
                    sb.append("\\~");
                    continue block19;
                }
                case '|': {
                    sb.append("\\|");
                    continue block19;
                }
                case '[': {
                    sb.append("\\[");
                    continue block19;
                }
                case ']': {
                    sb.append("\\]");
                    continue block19;
                }
                case '(': {
                    sb.append("\\(");
                    continue block19;
                }
                case ')': {
                    sb.append("\\)");
                    continue block19;
                }
                case ':': {
                    sb.append("\\:");
                    continue block19;
                }
                case '<': {
                    sb.append("\\<");
                    continue block19;
                }
                case '#': {
                    sb.append("\\#");
                    continue block19;
                }
                case '>': {
                    sb.append("\\>");
                    continue block19;
                }
                case '-': {
                    sb.append("\\-");
                    continue block19;
                }
                case '.': {
                    sb.append("\\.");
                    continue block19;
                }
                case '\n': {
                    sb.append("\\n");
                    continue block19;
                }
                default: {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    public static String escapeCodeSpan(String s, char replacement) {
        if (s == null || s.isEmpty() || replacement == '`') {
            return s;
        }
        return s.replace('`', replacement);
    }

    private static MarkdownContext advanceContext(MarkdownContext ctx, String text) {
        int i = 0;
        while (i < text.length()) {
            char c = text.charAt(i);
            if (ctx == MarkdownContext.NORMAL) {
                if (c == '\\' && i + 1 < text.length()) {
                    i += 2;
                    continue;
                }
                if (c == '`') {
                    if (i + 2 < text.length() && text.charAt(i + 1) == '`' && text.charAt(i + 2) == '`') {
                        ctx = MarkdownContext.CODE_BLOCK;
                        i += 3;
                        continue;
                    }
                    ctx = MarkdownContext.INLINE_CODE;
                }
            } else if (ctx == MarkdownContext.INLINE_CODE) {
                if (c == '`') {
                    ctx = MarkdownContext.NORMAL;
                }
            } else if (c == '`' && i + 2 < text.length() && text.charAt(i + 1) == '`' && text.charAt(i + 2) == '`') {
                ctx = MarkdownContext.NORMAL;
                i += 3;
                continue;
            }
            ++i;
        }
        return ctx;
    }

    /*
     * Uses 'sealed' constructs - enablewith --sealed true
     */
    static interface Segment {
    }

    private static enum MarkdownContext {
        NORMAL,
        INLINE_CODE,
        CODE_BLOCK;

    }

    record Literal(String text) implements Segment
    {
    }

    public static enum EscapeMode {
        FULL_MARKDOWN,
        CODE_SPAN,
        NONE;

    }

    record Placeholder(String key, EscapeMode mode) implements Segment
    {
    }
}

