/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.GradientTag;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class PrideTag
extends GradientTag {
    private static final String PRIDE = "pride";
    static final TagResolver RESOLVER = TagResolver.resolver("pride", PrideTag::create);
    private static final Map<String, List<TextColor>> FLAGS;
    private final String flag;

    static Tag create(ArgumentQueue args, Context ctx) {
        double phase = 0.0;
        String flag = PRIDE;
        if (args.hasNext()) {
            String value = args.pop().value().toLowerCase(Locale.ROOT);
            if (FLAGS.containsKey(value)) {
                flag = value;
            } else if (!value.isEmpty()) {
                try {
                    phase = Double.parseDouble(value);
                }
                catch (NumberFormatException ex) {
                    throw ctx.newException("Expected phase, got " + value);
                }
                if (phase < -1.0 || phase > 1.0) {
                    throw ctx.newException(String.format("Gradient phase is out of range (%s). Must be in the range [-1.0, 1.0] (inclusive).", phase), args);
                }
            }
        }
        return new PrideTag(phase, FLAGS.get(flag), flag, ctx);
    }

    PrideTag(double phase, @NotNull @NotNull List<@NotNull TextColor> colors, @NotNull String flag, Context ctx) {
        super(phase, colors, ctx);
        this.flag = flag;
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("flag", this.flag), ExaminableProperty.of("phase", this.phase));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.flag, this.phase);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        PrideTag that = (PrideTag)other;
        return this.phase == that.phase && this.flag.equals(that.flag);
    }

    @NotNull
    private static List<TextColor> colors(int ... colors) {
        return Arrays.stream(colors).mapToObj(TextColor::color).collect(Collectors.toList());
    }

    static {
        HashMap<String, List<TextColor>> flags = new HashMap<String, List<TextColor>>();
        flags.put(PRIDE, PrideTag.colors(0xE50000, 16747776, 0xFFEE00, 164129, 19711, 0x770088));
        flags.put("progress", PrideTag.colors(0xFFFFFF, 16756679, 7591918, 6371605, 0, 0xE50000, 16747776, 0xFFEE00, 164129, 19711, 0x770088));
        flags.put("trans", PrideTag.colors(6017019, 16100281, 0xFFFFFF, 16100281, 6017019));
        flags.put("bi", PrideTag.colors(14025328, 10178454, 14504));
        flags.put("pan", PrideTag.colors(16718989, 16766720, 1750015));
        flags.put("nb", PrideTag.colors(16577585, 0xFCFCFC, 10312146, 0x282828));
        flags.put("lesbian", PrideTag.colors(14034944, 16751446, 0xFFFFFF, 13918886, 10748002));
        flags.put("ace", PrideTag.colors(0, 0xA4A4A4, 0xFFFFFF, 0x810081));
        flags.put("agender", PrideTag.colors(0, 0xBABABA, 0xFFFFFF, 12252292, 0xFFFFFF, 0xBABABA, 0));
        flags.put("demisexual", PrideTag.colors(0, 0xFFFFFF, 7209073, 0xD3D3D3));
        flags.put("genderqueer", PrideTag.colors(11894749, 0xFFFFFF, 4817438));
        flags.put("genderfluid", PrideTag.colors(16676514, 0xFFFFFF, 12522199, 0, 3161278));
        flags.put("intersex", PrideTag.colors(16766976, 7930538, 16766976));
        flags.put("aro", PrideTag.colors(3909440, 11064442, 0xFFFFFF, 0xABABAB, 0));
        flags.put("baker", PrideTag.colors(13461247, 16737689, 0xFE0000, 16685312, 0xFFFF01, 39168, 39371, 3473561, 0x990099));
        flags.put("philly", PrideTag.colors(0, 7884567, 0xFE0000, 16616448, 16770304, 1154827, 410803, 12725980));
        flags.put("queer", PrideTag.colors(0, 10148330, 41960, 11920669, 0xFFFFFF, 16763149, 16541287, 16690889, 0));
        flags.put("gay", PrideTag.colors(495216, 2543274, 10021057, 0xFFFFFF, 8105442, 5261771, 4004472));
        flags.put("bigender", PrideTag.colors(12876192, 15509195, 14010344, 0xFFFFFF, 14010344, 10143720, 7111631));
        flags.put("demigender", PrideTag.colors(0x7F7F7F, 0xC3C3C3, 16514932, 0xFFFFFF, 16514932, 0xC3C3C3, 0x7F7F7F));
        FLAGS = Collections.unmodifiableMap(flags);
    }
}

