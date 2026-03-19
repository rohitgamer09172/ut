/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard;

import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.ScoreComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.ParsingException;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class ScoreTag {
    public static final String SCORE = "score";
    static final TagResolver RESOLVER = SerializableResolver.claimingComponent("score", ScoreTag::create, ScoreTag::emit);

    private ScoreTag() {
    }

    static Tag create(ArgumentQueue args, Context ctx) throws ParsingException {
        String name = args.popOr("A scoreboard member name is required").value();
        String objective = args.popOr("An objective name is required").value();
        return Tag.inserting(Component.score(name, objective));
    }

    @Nullable
    static Emitable emit(Component component) {
        if (!(component instanceof ScoreComponent)) {
            return null;
        }
        ScoreComponent score = (ScoreComponent)component;
        return emit -> emit.tag(SCORE).argument(score.name()).argument(score.objective());
    }
}

