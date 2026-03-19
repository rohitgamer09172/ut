/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextDecoration;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class DecorationTag {
    private static final String B = "b";
    private static final String I = "i";
    private static final String EM = "em";
    private static final String OBF = "obf";
    private static final String ST = "st";
    private static final String U = "u";
    public static final String REVERT = "!";
    static final Map<TextDecoration, TagResolver> RESOLVERS = Stream.of(DecorationTag.resolvers(TextDecoration.OBFUSCATED, "obf", new String[0]), DecorationTag.resolvers(TextDecoration.BOLD, "b", new String[0]), DecorationTag.resolvers(TextDecoration.STRIKETHROUGH, "st", new String[0]), DecorationTag.resolvers(TextDecoration.UNDERLINED, "u", new String[0]), DecorationTag.resolvers(TextDecoration.ITALIC, "em", "i")).collect(Collectors.toMap(Map.Entry::getKey, ent -> ((Stream)ent.getValue()).collect(TagResolver.toTagResolver()), (l, r) -> TagResolver.builder().resolver((TagResolver)l).resolver((TagResolver)r).build(), LinkedHashMap::new));
    static final TagResolver RESOLVER = TagResolver.resolver(RESOLVERS.values());

    static Map.Entry<TextDecoration, Stream<TagResolver>> resolvers(TextDecoration decoration, @Nullable String shortName, String ... secondaryAliases) {
        String canonicalName = TextDecoration.NAMES.key(decoration);
        HashSet<String> names = new HashSet<String>();
        names.add(canonicalName);
        if (shortName != null) {
            names.add(shortName);
        }
        Collections.addAll(names, secondaryAliases);
        return new AbstractMap.SimpleImmutableEntry<TextDecoration, Stream<TagResolver>>(decoration, Stream.concat(Stream.of(SerializableResolver.claimingStyle(names, (args, ctx) -> DecorationTag.create(decoration, args, ctx), DecorationTag.claim(decoration, (state, emitter) -> DecorationTag.emit(canonicalName, shortName == null ? canonicalName : shortName, state, emitter)))), names.stream().map(name -> TagResolver.resolver(REVERT + name, DecorationTag.createNegated(decoration)))));
    }

    private DecorationTag() {
    }

    static Tag create(TextDecoration toApply, ArgumentQueue args, Context ctx) {
        boolean flag = !args.hasNext() || !args.pop().isFalse();
        return Tag.styling(toApply.withState(flag));
    }

    static Tag createNegated(TextDecoration toApply) {
        return Tag.styling(toApply.withState(false));
    }

    @NotNull
    static StyleClaim<TextDecoration.State> claim(@NotNull TextDecoration decoration, @NotNull BiConsumer<TextDecoration.State, TokenEmitter> emitable) {
        Objects.requireNonNull(decoration, "decoration");
        return StyleClaim.claim("decoration_" + TextDecoration.NAMES.key(decoration), style -> style.decoration(decoration), state -> state != TextDecoration.State.NOT_SET, emitable);
    }

    static void emit(@NotNull String longName, @NotNull String shortName, @NotNull TextDecoration.State state, @NotNull TokenEmitter emitter) {
        if (state == TextDecoration.State.FALSE) {
            emitter.tag(REVERT + longName);
        } else {
            emitter.tag(longName);
        }
    }
}

