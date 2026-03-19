/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.chat;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.chat.SignedMessageImpl;
import ac.grim.grimac.shaded.kyori.adventure.identity.Identified;
import ac.grim.grimac.shaded.kyori.adventure.identity.Identity;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.ComponentLike;
import ac.grim.grimac.shaded.kyori.examination.Examinable;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.time.Instant;
import java.util.stream.Stream;

@ApiStatus.NonExtendable
public interface SignedMessage
extends Identified,
Examinable {
    @Contract(value="_ -> new", pure=true)
    @NotNull
    public static Signature signature(byte[] signature) {
        return new SignedMessageImpl.SignatureImpl(signature);
    }

    @Contract(value="_, _ -> new", pure=true)
    @NotNull
    public static SignedMessage system(@NotNull String message, @Nullable ComponentLike unsignedContent) {
        return new SignedMessageImpl(message, ComponentLike.unbox(unsignedContent));
    }

    @Contract(pure=true)
    @NotNull
    public Instant timestamp();

    @Contract(pure=true)
    public long salt();

    @Contract(pure=true)
    @Nullable
    public Signature signature();

    @Contract(pure=true)
    @Nullable
    public Component unsignedContent();

    @Contract(pure=true)
    @NotNull
    public String message();

    @Contract(pure=true)
    default public boolean isSystem() {
        return this.identity() == Identity.nil();
    }

    @Contract(pure=true)
    default public boolean canDelete() {
        return this.signature() != null;
    }

    @Override
    @NotNull
    default public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("timestamp", this.timestamp()), ExaminableProperty.of("salt", this.salt()), ExaminableProperty.of("signature", this.signature()), ExaminableProperty.of("unsignedContent", this.unsignedContent()), ExaminableProperty.of("message", this.message()));
    }

    @ApiStatus.NonExtendable
    public static interface Signature
    extends Examinable {
        @Contract(pure=true)
        public byte[] bytes();

        @Override
        @NotNull
        default public Stream<? extends ExaminableProperty> examinableProperties() {
            return Stream.of(ExaminableProperty.of("bytes", this.bytes()));
        }
    }
}

