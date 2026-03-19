/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.jetbrains.annotations.UnknownNullability;
import ac.grim.grimac.shaded.kyori.adventure.internal.Internals;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.ComponentLike;
import ac.grim.grimac.shaded.kyori.adventure.text.TextComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.VirtualComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.VirtualComponentRenderer;
import ac.grim.grimac.shaded.kyori.adventure.text.flattener.ComponentFlattener;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node.TagNode;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node.ValueNode;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Inserting;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Modifying;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tree.Node;
import ac.grim.grimac.shaded.kyori.examination.Examinable;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.Collections;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

abstract class AbstractColorChangingTag
implements Modifying,
Examinable {
    private static final ComponentFlattener LENGTH_CALCULATOR = (ComponentFlattener)ComponentFlattener.builder().mapper(TextComponent.class, TextComponent::content).unknownMapper(x -> "_").build();
    private boolean visited;
    private int size = 0;
    private int disableApplyingColorDepth = -1;
    private final boolean emitVirtuals;

    AbstractColorChangingTag(Context ctx) {
        this.emitVirtuals = ctx.emitVirtuals();
    }

    protected final int size() {
        return this.size;
    }

    @Override
    public final void visit(@NotNull Node current, int depth) {
        TagNode tag;
        if (this.visited) {
            throw new IllegalStateException("Color changing tag instances cannot be re-used, return a new one for each resolve");
        }
        if (current instanceof ValueNode) {
            String value = ((ValueNode)current).value();
            this.size += value.codePointCount(0, value.length());
        } else if (current instanceof TagNode && (tag = (TagNode)current).tag() instanceof Inserting) {
            LENGTH_CALCULATOR.flatten(((Inserting)tag.tag()).value(), s -> this.size += s.codePointCount(0, s.length()));
        }
    }

    @Override
    public final void postVisit() {
        this.visited = true;
        this.init();
    }

    @Override
    public final Component apply(@NotNull Component current, int depth) {
        if (this.emitVirtuals && depth == 0) {
            return Component.virtual(Void.class, new TagInfoHolder(this.preserveData(), current), current.style());
        }
        if (this.disableApplyingColorDepth != -1 && depth > this.disableApplyingColorDepth || current.style().color() != null) {
            if (this.disableApplyingColorDepth == -1 || depth < this.disableApplyingColorDepth) {
                this.disableApplyingColorDepth = depth;
            }
            if (current instanceof TextComponent) {
                this.skipColorForLengthOf(((TextComponent)current).content());
            }
            return current.children(Collections.emptyList());
        }
        this.disableApplyingColorDepth = -1;
        if (current instanceof VirtualComponent) {
            this.skipColorForLengthOf(((VirtualComponent)current).content());
            return current.children(Collections.emptyList());
        }
        if (current instanceof TextComponent && ((TextComponent)current).content().length() > 0) {
            TextComponent textComponent = (TextComponent)current;
            String content = textComponent.content();
            TextComponent.Builder parent = Component.text();
            int[] holder = new int[1];
            PrimitiveIterator.OfInt it = content.codePoints().iterator();
            while (it.hasNext()) {
                holder[0] = it.nextInt();
                TextComponent comp = Component.text(new String(holder, 0, 1), current.style().color(this.color()));
                this.advanceColor();
                parent.append((Component)comp);
            }
            return parent.build();
        }
        if (!(current instanceof TextComponent)) {
            Component ret = current.children(Collections.emptyList()).colorIfAbsent(this.color());
            this.advanceColor();
            return ret;
        }
        return Component.empty().mergeStyle(current);
    }

    private void skipColorForLengthOf(String content) {
        int len = content.codePointCount(0, content.length());
        for (int i = 0; i < len; ++i) {
            this.advanceColor();
        }
    }

    protected abstract void init();

    protected abstract void advanceColor();

    protected abstract TextColor color();

    @NotNull
    protected abstract Consumer<TokenEmitter> preserveData();

    @Override
    @NotNull
    public abstract Stream<? extends ExaminableProperty> examinableProperties();

    @NotNull
    public final String toString() {
        return Internals.toString(this);
    }

    public abstract boolean equals(@Nullable Object var1);

    public abstract int hashCode();

    @Nullable
    static Emitable claimComponent(Component comp) {
        if (!(comp instanceof VirtualComponent)) {
            return null;
        }
        VirtualComponentRenderer<?> holder = ((VirtualComponent)comp).renderer();
        if (!(holder instanceof TagInfoHolder)) {
            return null;
        }
        return (TagInfoHolder)holder;
    }

    static final class TagInfoHolder
    implements VirtualComponentRenderer<Void>,
    Emitable {
        private final Consumer<TokenEmitter> output;
        private final Component originalComp;

        TagInfoHolder(Consumer<TokenEmitter> output, Component originalComp) {
            this.output = output;
            this.originalComp = originalComp;
        }

        @Override
        public @UnknownNullability ComponentLike apply(@NotNull Void context) {
            return this.originalComp;
        }

        @Override
        @NotNull
        public String fallbackString() {
            return "";
        }

        @Override
        public void emit(@NotNull TokenEmitter emitter) {
            this.output.accept(emitter);
        }

        @Override
        @Nullable
        public Component substitute() {
            return this.originalComp;
        }
    }
}

