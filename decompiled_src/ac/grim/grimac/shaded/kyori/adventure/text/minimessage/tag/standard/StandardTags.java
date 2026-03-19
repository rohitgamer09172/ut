/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextDecoration;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.ClickTag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.ColorTagResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.DecorationTag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.FontTag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.GradientTag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.HoverTag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.InsertionTag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.KeybindTag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.NbtTag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.NewlineTag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.PrideTag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.RainbowTag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.ResetTag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.ScoreTag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.SelectorTag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.SequentialHeadTag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.ShadowColorTag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.SpriteTag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.TransitionTag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.TranslatableFallbackTag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.TranslatableTag;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class StandardTags {
    private static final TagResolver ALL = TagResolver.builder().resolvers(HoverTag.RESOLVER, ClickTag.RESOLVER, ColorTagResolver.INSTANCE, KeybindTag.RESOLVER, TranslatableTag.RESOLVER, TranslatableFallbackTag.RESOLVER, InsertionTag.RESOLVER, FontTag.RESOLVER, DecorationTag.RESOLVER, GradientTag.RESOLVER, RainbowTag.RESOLVER, ResetTag.RESOLVER, NewlineTag.RESOLVER, TransitionTag.RESOLVER, SelectorTag.RESOLVER, ScoreTag.RESOLVER, NbtTag.RESOLVER, PrideTag.RESOLVER, ShadowColorTag.RESOLVER, SpriteTag.RESOLVER, SequentialHeadTag.RESOLVER).build();

    private StandardTags() {
    }

    @NotNull
    public static TagResolver decorations(@NotNull TextDecoration decoration) {
        return Objects.requireNonNull(DecorationTag.RESOLVERS.get(decoration), "No resolver found for decoration (this should not be possible?)");
    }

    @NotNull
    public static TagResolver decorations() {
        return DecorationTag.RESOLVER;
    }

    @NotNull
    public static TagResolver color() {
        return ColorTagResolver.INSTANCE;
    }

    @NotNull
    public static TagResolver hoverEvent() {
        return HoverTag.RESOLVER;
    }

    @NotNull
    public static TagResolver clickEvent() {
        return ClickTag.RESOLVER;
    }

    @NotNull
    public static TagResolver keybind() {
        return KeybindTag.RESOLVER;
    }

    @NotNull
    public static TagResolver sequentialHead() {
        return SequentialHeadTag.RESOLVER;
    }

    @NotNull
    public static TagResolver translatable() {
        return TranslatableTag.RESOLVER;
    }

    @NotNull
    public static TagResolver translatableFallback() {
        return TranslatableFallbackTag.RESOLVER;
    }

    @NotNull
    public static TagResolver insertion() {
        return InsertionTag.RESOLVER;
    }

    @NotNull
    public static TagResolver font() {
        return FontTag.RESOLVER;
    }

    @NotNull
    public static TagResolver gradient() {
        return GradientTag.RESOLVER;
    }

    @NotNull
    public static TagResolver rainbow() {
        return RainbowTag.RESOLVER;
    }

    public static TagResolver transition() {
        return TransitionTag.RESOLVER;
    }

    @NotNull
    public static TagResolver reset() {
        return ResetTag.RESOLVER;
    }

    @NotNull
    public static TagResolver newline() {
        return NewlineTag.RESOLVER;
    }

    @NotNull
    public static TagResolver selector() {
        return SelectorTag.RESOLVER;
    }

    @NotNull
    public static TagResolver score() {
        return ScoreTag.RESOLVER;
    }

    @NotNull
    public static TagResolver nbt() {
        return NbtTag.RESOLVER;
    }

    @NotNull
    public static TagResolver pride() {
        return PrideTag.RESOLVER;
    }

    @NotNull
    public static TagResolver shadowColor() {
        return ShadowColorTag.RESOLVER;
    }

    @NotNull
    public static TagResolver sprite() {
        return SpriteTag.RESOLVER;
    }

    @NotNull
    public static TagResolver defaults() {
        return ALL;
    }

    static Set<String> names(String ... names) {
        return new HashSet<String>(Arrays.asList(names));
    }
}

