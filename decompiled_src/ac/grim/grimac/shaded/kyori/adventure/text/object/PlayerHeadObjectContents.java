/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.text.object;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.jetbrains.annotations.Unmodifiable;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.text.object.ObjectContents;
import ac.grim.grimac.shaded.kyori.adventure.text.object.PlayerHeadObjectContentsImpl;
import ac.grim.grimac.shaded.kyori.adventure.util.PlatformAPI;
import ac.grim.grimac.shaded.kyori.examination.Examinable;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@ApiStatus.NonExtendable
public interface PlayerHeadObjectContents
extends ObjectContents {
    public static final boolean DEFAULT_HAT = true;

    @Nullable
    public String name();

    @Nullable
    public UUID id();

    public @Unmodifiable @NotNull List<ProfileProperty> profileProperties();

    public boolean hat();

    @Nullable
    public Key texture();

    @Contract(value="-> new", pure=true)
    @NotNull
    public Builder toBuilder();

    @Contract(value="_, _ -> new", pure=true)
    public static ProfileProperty property(@NotNull String name, @NotNull String value) {
        return new PlayerHeadObjectContentsImpl.ProfilePropertyImpl(Objects.requireNonNull(name, "name"), Objects.requireNonNull(value, "value"), null);
    }

    @Contract(value="_, _, _ -> new", pure=true)
    public static ProfileProperty property(@NotNull String name, @NotNull String value, @Nullable String signature) {
        return new PlayerHeadObjectContentsImpl.ProfilePropertyImpl(Objects.requireNonNull(name, "name"), Objects.requireNonNull(value, "value"), signature);
    }

    @Override
    @NotNull
    default public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("name", this.name()), ExaminableProperty.of("id", this.id()), ExaminableProperty.of("profileProperties", this.profileProperties()), ExaminableProperty.of("hat", this.hat()), ExaminableProperty.of("texture", this.texture()));
    }

    public static interface SkinSource {
        @PlatformAPI
        @ApiStatus.Internal
        public void applySkinToPlayerHeadContents(@NotNull Builder var1);
    }

    public static interface Builder {
        @Contract(value="_ -> this")
        @NotNull
        public Builder name(@Nullable String var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder id(@Nullable UUID var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder profileProperty(@NotNull ProfileProperty var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder profileProperties(@NotNull Collection<ProfileProperty> var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder skin(@NotNull SkinSource var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder hat(boolean var1);

        @Contract(value="_ -> this")
        @NotNull
        public Builder texture(@Nullable Key var1);

        @Contract(value="-> new", pure=true)
        @NotNull
        public PlayerHeadObjectContents build();
    }

    public static interface ProfileProperty
    extends Examinable {
        @NotNull
        public String name();

        @NotNull
        public String value();

        @Nullable
        public String signature();

        @Override
        @NotNull
        default public Stream<? extends ExaminableProperty> examinableProperties() {
            return Stream.of(ExaminableProperty.of("name", this.name()), ExaminableProperty.of("value", this.value()), ExaminableProperty.of("signature", this.signature()));
        }
    }
}

