/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.object;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.text.object.PlayerHeadObjectContents;
import ac.grim.grimac.shaded.kyori.adventure.text.object.PlayerHeadObjectContentsImpl;
import ac.grim.grimac.shaded.kyori.adventure.text.object.SpriteObjectContents;
import ac.grim.grimac.shaded.kyori.adventure.text.object.SpriteObjectContentsImpl;
import ac.grim.grimac.shaded.kyori.examination.Examinable;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

@ApiStatus.NonExtendable
public interface ObjectContents
extends Examinable {
    @Contract(value="_, _ -> new", pure=true)
    @NotNull
    public static SpriteObjectContents sprite(@NotNull Key atlas, @NotNull Key sprite) {
        return new SpriteObjectContentsImpl(Objects.requireNonNull(atlas, "atlas"), Objects.requireNonNull(sprite, "sprite"));
    }

    @Contract(value="_ -> new", pure=true)
    @NotNull
    public static SpriteObjectContents sprite(@NotNull Key sprite) {
        return new SpriteObjectContentsImpl(SpriteObjectContents.DEFAULT_ATLAS, Objects.requireNonNull(sprite, "sprite"));
    }

    @Contract(value="-> new", pure=true)
    public static @NotNull PlayerHeadObjectContents.Builder playerHead() {
        return new PlayerHeadObjectContentsImpl.BuilderImpl();
    }

    @Contract(value="_ -> new", pure=true)
    @NotNull
    public static PlayerHeadObjectContents playerHead(@NotNull String name) {
        return new PlayerHeadObjectContentsImpl(name, null, Collections.emptyList(), true, null);
    }

    @Contract(value="_ -> new", pure=true)
    @NotNull
    public static PlayerHeadObjectContents playerHead(@NotNull UUID id) {
        return new PlayerHeadObjectContentsImpl(null, id, Collections.emptyList(), true, null);
    }

    @Contract(value="_ -> new", pure=true)
    @NotNull
    public static PlayerHeadObjectContents playerHead(@NotNull PlayerHeadObjectContents.SkinSource skinSource) {
        return ObjectContents.playerHead().skin(skinSource).build();
    }
}

