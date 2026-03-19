/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.ComponentBuilder;
import ac.grim.grimac.shaded.kyori.adventure.text.ComponentLike;
import ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent;
import ac.grim.grimac.shaded.kyori.adventure.text.event.HoverEventSource;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import ac.grim.grimac.shaded.kyori.adventure.text.format.StyleBuilderApplicable;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextDecoration;
import ac.grim.grimac.shaded.kyori.adventure.util.ARGBLike;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public interface ScopedComponent<C extends Component>
extends Component {
    @NotNull
    default public C asComponent() {
        return (C)Component.super.asComponent();
    }

    @NotNull
    public C children(@NotNull List<? extends ComponentLike> var1);

    @NotNull
    public C style(@NotNull Style var1);

    @NotNull
    default public C style(@NotNull Consumer<Style.Builder> style) {
        return (C)Component.super.style(style);
    }

    @NotNull
    default public C style(@NotNull Style.Builder style) {
        return (C)Component.super.style(style);
    }

    @NotNull
    default public C style(@NotNull Consumer<Style.Builder> consumer, @NotNull Style.Merge.Strategy strategy) {
        return (C)Component.super.style(consumer, strategy);
    }

    @NotNull
    default public C mergeStyle(@NotNull Component that) {
        return (C)Component.super.mergeStyle(that);
    }

    @NotNull
    default public C mergeStyle(@NotNull Component that, Style.Merge ... merges) {
        return (C)Component.super.mergeStyle(that, merges);
    }

    @NotNull
    default public C append(@NotNull Component component) {
        return (C)Component.super.append(component);
    }

    @NotNull
    default public C append(@NotNull ComponentLike like) {
        return (C)Component.super.append(like);
    }

    @NotNull
    default public C append(@NotNull ComponentBuilder<?, ?> builder) {
        return (C)Component.super.append(builder);
    }

    @NotNull
    default public C append(@NotNull List<? extends ComponentLike> components) {
        return (C)Component.super.append(components);
    }

    @NotNull
    default public C append(ComponentLike ... components) {
        return (C)Component.super.append(components);
    }

    @NotNull
    default public C appendNewline() {
        return (C)Component.super.appendNewline();
    }

    @NotNull
    default public C appendSpace() {
        return (C)Component.super.appendSpace();
    }

    @NotNull
    default public C applyFallbackStyle(StyleBuilderApplicable ... style) {
        return (C)Component.super.applyFallbackStyle(style);
    }

    @NotNull
    default public C applyFallbackStyle(@NotNull Style style) {
        return (C)Component.super.applyFallbackStyle(style);
    }

    @NotNull
    default public C mergeStyle(@NotNull Component that, @NotNull Set<Style.Merge> merges) {
        return (C)Component.super.mergeStyle(that, merges);
    }

    @Override
    @NotNull
    default public C color(@Nullable TextColor color) {
        return (C)Component.super.color(color);
    }

    @Override
    @NotNull
    default public C colorIfAbsent(@Nullable TextColor color) {
        return (C)Component.super.colorIfAbsent(color);
    }

    @Override
    @NotNull
    default public C shadowColor(@Nullable ARGBLike argb) {
        return (C)Component.super.shadowColor(argb);
    }

    @Override
    @NotNull
    default public C shadowColorIfAbsent(@Nullable ARGBLike argb) {
        return (C)Component.super.shadowColorIfAbsent(argb);
    }

    @Override
    @NotNull
    default public C decorate(@NotNull TextDecoration decoration) {
        return (C)Component.super.decorate(decoration);
    }

    @Override
    @NotNull
    default public C decoration(@NotNull TextDecoration decoration, boolean flag) {
        return (C)Component.super.decoration(decoration, flag);
    }

    @Override
    @NotNull
    default public C decoration(@NotNull TextDecoration decoration, @NotNull TextDecoration.State state) {
        return (C)Component.super.decoration(decoration, state);
    }

    @Override
    @NotNull
    default public C decorationIfAbsent(@NotNull TextDecoration decoration, @NotNull TextDecoration.State state) {
        return (C)Component.super.decorationIfAbsent(decoration, state);
    }

    @Override
    @NotNull
    default public C decorations(@NotNull Map<TextDecoration, TextDecoration.State> decorations) {
        return (C)Component.super.decorations((Map)decorations);
    }

    @Override
    @NotNull
    default public C clickEvent(@Nullable ClickEvent event) {
        return (C)Component.super.clickEvent(event);
    }

    @Override
    @NotNull
    default public C hoverEvent(@Nullable HoverEventSource<?> event) {
        return (C)Component.super.hoverEvent((HoverEventSource)event);
    }

    @Override
    @NotNull
    default public C insertion(@Nullable String insertion) {
        return (C)Component.super.insertion(insertion);
    }

    @Override
    @NotNull
    default public C font(@Nullable Key key) {
        return (C)Component.super.font(key);
    }
}

