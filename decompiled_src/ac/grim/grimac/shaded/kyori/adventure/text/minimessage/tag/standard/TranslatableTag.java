/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard;

import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.TranslatableComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.TranslationArgument;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.ParsingException;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class TranslatableTag {
    private static final String TR = "tr";
    private static final String TRANSLATE = "translate";
    private static final String LANG = "lang";
    static final TagResolver RESOLVER = SerializableResolver.claimingComponent(StandardTags.names("lang", "translate", "tr"), TranslatableTag::create, TranslatableTag::claim);

    private TranslatableTag() {
    }

    static Tag create(ArgumentQueue args, Context ctx) throws ParsingException {
        List with;
        String key = args.popOr("A translation key is required").value();
        if (args.hasNext()) {
            with = new ArrayList();
            while (args.hasNext()) {
                with.add(ctx.deserialize(args.pop().value()));
            }
        } else {
            with = Collections.emptyList();
        }
        return Tag.inserting(Component.translatable(key, with));
    }

    @Nullable
    static Emitable claim(Component input) {
        if (!(input instanceof TranslatableComponent) || ((TranslatableComponent)input).fallback() != null) {
            return null;
        }
        TranslatableComponent tr = (TranslatableComponent)input;
        return emit -> {
            emit.tag(LANG);
            emit.argument(tr.key());
            for (TranslationArgument with : tr.arguments()) {
                emit.argument(with.asComponent());
            }
        };
    }
}

