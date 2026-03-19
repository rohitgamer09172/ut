/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.jetbrains.annotations.Range;
import ac.grim.grimac.shaded.kyori.adventure.text.format.NamedTextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.AbstractColorChangingTag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.ColorTagResolver;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.function.Consumer;
import java.util.stream.Stream;

class GradientTag
extends AbstractColorChangingTag {
    private static final String GRADIENT = "gradient";
    private static final TextColor DEFAULT_WHITE = TextColor.color(0xFFFFFF);
    private static final TextColor DEFAULT_BLACK = TextColor.color(0);
    static final TagResolver RESOLVER = SerializableResolver.claimingComponent("gradient", GradientTag::create, AbstractColorChangingTag::claimComponent);
    private int index = 0;
    private double multiplier = 1.0;
    private final TextColor[] colors;
    @Range(from=-1L, to=1L) double phase;
    private final boolean negativePhase;

    static Tag create(ArgumentQueue args, Context ctx) {
        List<TextColor> textColors;
        double phase = 0.0;
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
                    phase = possiblePhase.getAsDouble();
                    if (!(phase < -1.0) && !(phase > 1.0)) break;
                    throw ctx.newException(String.format("Gradient phase is out of range (%s). Must be in the range [-1.0, 1.0] (inclusive).", phase), args);
                }
                throw ctx.newException(String.format("Unable to parse a color from '%s'. Please use named colors or hex (#RRGGBB) colors.", argValue), args);
            }
            if (textColors.size() == 1) {
                throw ctx.newException("Invalid gradient, not enough colors. Gradients must have at least two colors.", args);
            }
        } else {
            textColors = Collections.emptyList();
        }
        return new GradientTag(phase, textColors, ctx);
    }

    GradientTag(double phase, List<TextColor> colors, Context ctx) {
        super(ctx);
        this.colors = colors.isEmpty() ? new TextColor[]{DEFAULT_WHITE, DEFAULT_BLACK} : colors.toArray(new TextColor[0]);
        if (phase < 0.0) {
            this.negativePhase = true;
            this.phase = 1.0 + phase;
            Collections.reverse(Arrays.asList(this.colors));
        } else {
            this.negativePhase = false;
            this.phase = phase;
        }
    }

    @Override
    protected void init() {
        this.multiplier = this.size() == 1 ? 0.0 : (double)(this.colors.length - 1) / (double)(this.size() - 1);
        this.phase *= (double)(this.colors.length - 1);
        this.index = 0;
    }

    @Override
    protected void advanceColor() {
        ++this.index;
    }

    @Override
    protected TextColor color() {
        double position = (double)this.index * this.multiplier + this.phase;
        int lowUnclamped = (int)Math.floor(position);
        int high = (int)Math.ceil(position) % this.colors.length;
        int low = lowUnclamped % this.colors.length;
        return TextColor.lerp((float)position - (float)lowUnclamped, this.colors[low], this.colors[high]);
    }

    @Override
    @NotNull
    protected Consumer<TokenEmitter> preserveData() {
        double phase;
        TextColor[] colors;
        if (this.negativePhase) {
            colors = Arrays.copyOf(this.colors, this.colors.length);
            Collections.reverse(Arrays.asList(colors));
            phase = this.phase - 1.0;
        } else {
            colors = this.colors;
            phase = this.phase;
        }
        return emit -> {
            emit.tag(GRADIENT);
            if (colors.length != 2 || !colors[0].equals(DEFAULT_WHITE) || !colors[1].equals(DEFAULT_BLACK)) {
                for (TextColor color : colors) {
                    if (color instanceof NamedTextColor) {
                        emit.argument(NamedTextColor.NAMES.keyOrThrow((NamedTextColor)color));
                        continue;
                    }
                    emit.argument(color.asHexString());
                }
            }
            if (phase != 0.0) {
                emit.argument(Double.toString(phase));
            }
        };
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("phase", this.phase), ExaminableProperty.of("colors", this.colors));
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        GradientTag that = (GradientTag)other;
        return this.index == that.index && this.phase == that.phase && Arrays.equals(this.colors, that.colors);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(this.index, this.phase);
        result = 31 * result + Arrays.hashCode(this.colors);
        return result;
    }
}

