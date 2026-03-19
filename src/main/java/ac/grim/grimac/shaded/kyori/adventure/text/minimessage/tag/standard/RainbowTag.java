/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.AbstractColorChangingTag;
import ac.grim.grimac.shaded.kyori.adventure.util.HSVLike;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

final class RainbowTag
extends AbstractColorChangingTag {
    private static final String REVERSE = "!";
    private static final String RAINBOW = "rainbow";
    static final TagResolver RESOLVER = SerializableResolver.claimingComponent("rainbow", RainbowTag::create, AbstractColorChangingTag::claimComponent);
    private final boolean reversed;
    private final double dividedPhase;
    private int colorIndex = 0;

    static Tag create(ArgumentQueue args, Context ctx) {
        boolean reversed = false;
        int phase = 0;
        if (args.hasNext()) {
            String value = args.pop().value();
            if (value.startsWith(REVERSE)) {
                reversed = true;
                value = value.substring(REVERSE.length());
            }
            if (value.length() > 0) {
                try {
                    phase = Integer.parseInt(value);
                }
                catch (NumberFormatException ex) {
                    throw ctx.newException("Expected phase, got " + value);
                }
            }
        }
        return new RainbowTag(reversed, phase, ctx);
    }

    private RainbowTag(boolean reversed, int phase, Context ctx) {
        super(ctx);
        this.reversed = reversed;
        this.dividedPhase = (double)phase / 10.0;
    }

    @Override
    protected void init() {
        if (this.reversed) {
            this.colorIndex = this.size() - 1;
        }
    }

    @Override
    protected void advanceColor() {
        this.colorIndex = this.reversed ? (this.colorIndex == 0 ? this.size() - 1 : --this.colorIndex) : ++this.colorIndex;
    }

    @Override
    protected TextColor color() {
        float index = this.colorIndex;
        float hue = (float)(((double)(index / (float)this.size()) + this.dividedPhase) % 1.0);
        return TextColor.color(HSVLike.hsvLike(hue, 1.0f, 1.0f));
    }

    @Override
    @NotNull
    protected Consumer<TokenEmitter> preserveData() {
        boolean reversed = this.reversed;
        int phase = (int)Math.round(this.dividedPhase * 10.0);
        return emit -> {
            emit.tag(RAINBOW);
            if (reversed && phase != 0) {
                emit.argument(REVERSE + phase);
            } else if (reversed) {
                emit.argument(REVERSE);
            } else if (phase != 0) {
                emit.argument(Integer.toString(phase));
            }
        };
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("phase", this.dividedPhase));
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        RainbowTag that = (RainbowTag)other;
        return this.colorIndex == that.colorIndex && this.dividedPhase == that.dividedPhase;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.colorIndex, this.dividedPhase);
    }
}

