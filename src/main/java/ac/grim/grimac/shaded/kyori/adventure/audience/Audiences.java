/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.audience;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.audience.Audience;
import ac.grim.grimac.shaded.kyori.adventure.audience.ForwardingAudience;
import ac.grim.grimac.shaded.kyori.adventure.resource.ResourcePackCallback;
import ac.grim.grimac.shaded.kyori.adventure.text.ComponentLike;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class Audiences {
    static final Collector<? super Audience, ?, ForwardingAudience> COLLECTOR = Collectors.collectingAndThen(Collectors.toCollection(ArrayList::new), audiences -> Audience.audience(Collections.unmodifiableCollection(audiences)));

    private Audiences() {
    }

    @NotNull
    public static Consumer<? super Audience> sendingMessage(@NotNull ComponentLike message) {
        return audience -> audience.sendMessage(message);
    }

    @NotNull
    static ResourcePackCallback unwrapCallback(Audience forwarding, Audience dest, @NotNull ResourcePackCallback cb) {
        if (cb == ResourcePackCallback.noOp()) {
            return cb;
        }
        return (uuid, status, audience) -> cb.packEventReceived(uuid, status, audience == dest ? forwarding : audience);
    }
}

