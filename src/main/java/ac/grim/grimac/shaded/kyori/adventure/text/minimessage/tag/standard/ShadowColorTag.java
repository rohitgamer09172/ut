/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.format.NamedTextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.format.ShadowColor;
import ac.grim.grimac.shaded.kyori.adventure.text.format.StyleGetter;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.ParsingException;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.ColorTagResolver;

final class ShadowColorTag {
    private static final String SHADOW_COLOR = "shadow";
    private static final String SHADOW_NONE = "!shadow";
    private static final float DEFAULT_ALPHA = 0.25f;
    static final TagResolver RESOLVER = TagResolver.resolver(SerializableResolver.claimingStyle("shadow", ShadowColorTag::create, StyleClaim.claim("shadow", StyleGetter::shadowColor, ShadowColorTag::emit)), TagResolver.resolver("!shadow", Tag.styling(ShadowColor.none())));

    static Tag create(@NotNull ArgumentQueue args, @NotNull Context ctx) throws ParsingException {
        ShadowColor color;
        String colorString = args.popOr("Expected to find a color parameter: #RRGGBBAA").lowerValue();
        if (colorString.startsWith("#") && colorString.length() == 9) {
            color = ShadowColor.fromHexString(colorString);
            if (color == null) {
                throw ctx.newException(String.format("Unable to parse a shadow color from '%s'. Please use #RRGGBBAA formatting.", colorString));
            }
        } else {
            TextColor text = ColorTagResolver.resolveColor(colorString, ctx);
            float alpha = args.hasNext() ? (float)args.pop().asDouble().orElseThrow(() -> ctx.newException("Number was expected to be a double")) : 0.25f;
            color = ShadowColor.shadowColor(text, (int)(alpha * 255.0f));
        }
        return Tag.styling(color);
    }

    static void emit(@NotNull ShadowColor color, @NotNull TokenEmitter emitter) {
        if (ShadowColor.none().equals(color)) {
            emitter.tag(SHADOW_NONE);
            return;
        }
        emitter.tag(SHADOW_COLOR);
        @Nullable NamedTextColor possibleMatch = NamedTextColor.namedColor(TextColor.color(color).value());
        if (possibleMatch != null) {
            emitter.argument(NamedTextColor.NAMES.key(possibleMatch)).argument(Float.toString((float)color.alpha() / 255.0f));
        } else {
            emitter.argument(color.asHexString());
        }
    }

    private ShadowColorTag() {
    }
}

