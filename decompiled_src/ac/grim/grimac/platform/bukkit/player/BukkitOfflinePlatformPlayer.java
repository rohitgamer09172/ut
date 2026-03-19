/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.OfflinePlayer
 */
package ac.grim.grimac.platform.bukkit.player;

import ac.grim.grimac.platform.api.player.OfflinePlatformPlayer;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.Objects;
import java.util.UUID;
import lombok.Generated;
import org.bukkit.OfflinePlayer;

public class BukkitOfflinePlatformPlayer
implements OfflinePlatformPlayer {
    private final OfflinePlayer offlinePlayer;

    @Override
    public boolean isOnline() {
        return this.offlinePlayer.isOnline();
    }

    @Override
    @NotNull
    public String getName() {
        return Objects.requireNonNull(this.offlinePlayer.getName());
    }

    @Override
    @NotNull
    public UUID getUniqueId() {
        return this.offlinePlayer.getUniqueId();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean equals(Object o) {
        if (!(o instanceof OfflinePlatformPlayer)) return false;
        OfflinePlatformPlayer player = (OfflinePlatformPlayer)o;
        if (!this.getUniqueId().equals(player.getUniqueId())) return false;
        return true;
    }

    @Generated
    public BukkitOfflinePlatformPlayer(OfflinePlayer offlinePlayer) {
        this.offlinePlayer = offlinePlayer;
    }
}

