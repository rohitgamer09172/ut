/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.platform.api.manager;

import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.InteractionHand;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public interface ItemResetHandler {
    public void resetItemUsage(@Nullable PlatformPlayer var1);

    @Contract(value="null -> null")
    @Nullable
    public InteractionHand getItemUsageHand(@Nullable PlatformPlayer var1);

    @Contract(value="null -> false")
    public boolean isUsingItem(@Nullable PlatformPlayer var1);
}

