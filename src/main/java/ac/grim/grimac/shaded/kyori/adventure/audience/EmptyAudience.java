/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.audience;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.jetbrains.annotations.UnknownNullability;
import ac.grim.grimac.shaded.kyori.adventure.audience.Audience;
import ac.grim.grimac.shaded.kyori.adventure.audience.MessageType;
import ac.grim.grimac.shaded.kyori.adventure.chat.SignedMessage;
import ac.grim.grimac.shaded.kyori.adventure.identity.Identified;
import ac.grim.grimac.shaded.kyori.adventure.identity.Identity;
import ac.grim.grimac.shaded.kyori.adventure.pointer.Pointer;
import ac.grim.grimac.shaded.kyori.adventure.resource.ResourcePackInfoLike;
import ac.grim.grimac.shaded.kyori.adventure.resource.ResourcePackRequest;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.ComponentLike;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

final class EmptyAudience
implements Audience {
    static final EmptyAudience INSTANCE = new EmptyAudience();

    EmptyAudience() {
    }

    @Override
    @NotNull
    public <T> Optional<T> get(@NotNull Pointer<T> pointer) {
        return Optional.empty();
    }

    @Override
    @Contract(value="_, null -> null; _, !null -> !null")
    @Nullable
    public <T> T getOrDefault(@NotNull Pointer<T> pointer, @Nullable T defaultValue) {
        return defaultValue;
    }

    @Override
    public <T> @UnknownNullability T getOrDefaultFrom(@NotNull Pointer<T> pointer, @NotNull Supplier<? extends T> defaultValue) {
        return defaultValue.get();
    }

    @Override
    @NotNull
    public Audience filterAudience(@NotNull Predicate<? super Audience> filter) {
        return this;
    }

    @Override
    public void forEachAudience(@NotNull Consumer<? super Audience> action) {
    }

    @Override
    public void sendMessage(@NotNull ComponentLike message) {
    }

    @Override
    public void sendMessage(@NotNull Component message) {
    }

    @Override
    @Deprecated
    public void sendMessage(@NotNull Identified source, @NotNull Component message, @NotNull MessageType type) {
    }

    @Override
    @Deprecated
    public void sendMessage(@NotNull Identity source, @NotNull Component message, @NotNull MessageType type) {
    }

    @Override
    public void sendMessage(@NotNull Component message,  @NotNull ChatType.Bound boundChatType) {
    }

    @Override
    public void sendMessage(@NotNull SignedMessage signedMessage,  @NotNull ChatType.Bound boundChatType) {
    }

    @Override
    public void deleteMessage(@NotNull SignedMessage.Signature signature) {
    }

    @Override
    public void sendActionBar(@NotNull ComponentLike message) {
    }

    @Override
    public void sendPlayerListHeader(@NotNull ComponentLike header) {
    }

    @Override
    public void sendPlayerListFooter(@NotNull ComponentLike footer) {
    }

    @Override
    public void sendPlayerListHeaderAndFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer) {
    }

    @Override
    public void openBook( @NotNull Book.Builder book) {
    }

    @Override
    public void sendResourcePacks(@NotNull ResourcePackInfoLike request, ResourcePackInfoLike ... others) {
    }

    @Override
    public void removeResourcePacks(@NotNull ResourcePackRequest request) {
    }

    @Override
    public void removeResourcePacks(@NotNull ResourcePackInfoLike request, ResourcePackInfoLike ... others) {
    }

    public boolean equals(Object that) {
        return this == that;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        return "EmptyAudience";
    }
}

