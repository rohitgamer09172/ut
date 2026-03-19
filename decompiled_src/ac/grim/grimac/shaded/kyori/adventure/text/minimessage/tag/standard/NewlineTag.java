/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard;

import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.ParsingException;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard.StandardTags;

final class NewlineTag {
    private static final String BR = "br";
    private static final String NEWLINE = "newline";
    static final TagResolver RESOLVER = SerializableResolver.claimingComponent(StandardTags.names("newline", "br"), NewlineTag::create, NewlineTag::claimComponent);

    private NewlineTag() {
    }

    static Tag create(ArgumentQueue args, Context ctx) throws ParsingException {
        return Tag.selfClosingInserting(Component.newline());
    }

    @Nullable
    static Emitable claimComponent(Component input) {
        if (Component.newline().equals(input)) {
            return emit -> emit.selfClosingTag(BR);
        }
        return null;
    }
}

