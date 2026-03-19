/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Inserting;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.ColorTagResolver;
import ac.grim.grimac.shaded.kyori.examination.Examinable;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.stream.Stream;

public final class TransitionTag
implements Inserting,
Examinable {
    public static final String TRANSITION = "transition";
    private final TextColor[] colors;
    private final float phase;
    private final boolean negativePhase;
    static final TagResolver RESOLVER = TagResolver.resolver("transition", TransitionTag::create);

    static Tag create(ArgumentQueue args, Context ctx) {
        List<TextColor> textColors;
        float phase = 0.0f;
        if (args.hasNext()) {
            textColors = new ArrayList();
            while (args.hasNext()) {
                OptionalDouble possiblePhase;
                Tag.Argument arg = args.pop();
                String argValue = arg.value();
                TextColor color = ColorTagResolver.resolveColorOrNull(argValue);
                if (color != null) {
                    textColors.add(color);
                    continue;
                }
                if (!args.hasNext() && (possiblePhase = arg.asDouble()).isPresent()) {
                    phase = (float)possiblePhase.getAsDouble();
                    if (!(phase < -1.0f) && !(phase > 1.0f)) break;
                    throw ctx.newException(String.format("Gradient phase is out of range (%s). Must be in the range [-1.0f, 1.0f] (inclusive).", Float.valueOf(phase)), args);
                }
                throw ctx.newException(String.format("Unable to parse a color from '%s'. Please use named colors or hex (#RRGGBB) colors.", argValue), args);
            }
            if (textColors.size() < 2) {
                throw ctx.newException("Invalid transition, not enough colors. Transitions must have at least two colors.", args);
            }
        } else {
            textColors = Collections.emptyList();
        }
        return new TransitionTag(phase, textColors);
    }

    private TransitionTag(float phase, List<TextColor> colors) {
        if (phase < 0.0f) {
            this.negativePhase = true;
            this.phase = 1.0f + phase;
            Collections.reverse(colors);
        } else {
            this.negativePhase = false;
            this.phase = phase;
        }
        this.colors = colors.isEmpty() ? new TextColor[]{TextColor.color(0xFFFFFF), TextColor.color(0)} : colors.toArray(new TextColor[0]);
    }

    @Override
    @NotNull
    public Component value() {
        return Component.text("", this.color());
    }

    private TextColor color() {
        float steps = 1.0f / (float)(this.colors.length - 1);
        for (int colorIndex = 1; colorIndex < this.colors.length; ++colorIndex) {
            float val = (float)colorIndex * steps;
            if (!(val >= this.phase)) continue;
            float factor = 1.0f + (this.phase - val) * (float)(this.colors.length - 1);
            if (this.negativePhase) {
                return TextColor.lerp(1.0f - factor, this.colors[colorIndex], this.colors[colorIndex - 1]);
            }
            return TextColor.lerp(factor, this.colors[colorIndex - 1], this.colors[colorIndex]);
        }
        return this.colors[0];
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("phase", this.phase), ExaminableProperty.of("colors", this.colors));
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        TransitionTag that = (TransitionTag)other;
        return this.phase == that.phase && Arrays.equals(this.colors, that.colors);
    }

    public int hashCode() {
        int result = Objects.hash(Float.valueOf(this.phase));
        result = 31 * result + Arrays.hashCode(this.colors);
        return result;
    }
}

