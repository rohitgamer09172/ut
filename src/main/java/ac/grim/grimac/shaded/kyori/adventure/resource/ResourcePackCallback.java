/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.kyori.adventure.resource;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.audience.Audience;
import ac.grim.grimac.shaded.kyori.adventure.resource.ResourcePackCallbacks;
import ac.grim.grimac.shaded.kyori.adventure.resource.ResourcePackStatus;
import java.util.UUID;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface ResourcePackCallback {
    @NotNull
    public static ResourcePackCallback noOp() {
        return ResourcePackCallbacks.NO_OP;
    }

    @NotNull
    public static ResourcePackCallback onTerminal(@NotNull BiConsumer<UUID, Audience> success, @NotNull BiConsumer<UUID, Audience> failure) {
        return (uuid, status, audience) -> {
            if (status == ResourcePackStatus.SUCCESSFULLY_LOADED) {
                success.accept(uuid, audience);
            } else if (!status.intermediate()) {
                failure.accept(uuid, audience);
            }
        };
    }

    public void packEventReceived(@NotNull UUID var1, @NotNull ResourcePackStatus var2, @NotNull Audience var3);
}

