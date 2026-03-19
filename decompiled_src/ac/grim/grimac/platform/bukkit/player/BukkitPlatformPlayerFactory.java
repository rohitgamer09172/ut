/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 */
package ac.grim.grimac.platform.bukkit.player;

import ac.grim.grimac.platform.api.player.AbstractPlatformPlayerFactory;
import ac.grim.grimac.platform.api.player.OfflinePlatformPlayer;
import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.platform.bukkit.player.BukkitOfflinePlatformPlayer;
import ac.grim.grimac.platform.bukkit.player.BukkitPlatformPlayer;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class BukkitPlatformPlayerFactory
extends AbstractPlatformPlayerFactory<Player> {
    @Override
    protected Player getNativePlayer(@NotNull UUID uuid) {
        return Bukkit.getPlayer((UUID)uuid);
    }

    @Override
    protected Player getNativePlayer(@NotNull String name) {
        return Bukkit.getPlayer((String)name);
    }

    @Override
    protected PlatformPlayer createPlatformPlayer(@NotNull Player nativePlayer) {
        return new BukkitPlatformPlayer(nativePlayer);
    }

    @Override
    protected UUID getPlayerUUID(@NotNull Player nativePlayer) {
        return nativePlayer.getUniqueId();
    }

    @Override
    protected Collection<Player> getNativeOnlinePlayers() {
        return Bukkit.getOnlinePlayers();
    }

    @Override
    public OfflinePlatformPlayer getOfflineFromUUID(@NotNull UUID uuid) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer((UUID)uuid);
        return new BukkitOfflinePlatformPlayer(offlinePlayer);
    }

    @Override
    public OfflinePlatformPlayer getOfflineFromName(@NotNull String name) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer((String)name);
        return new BukkitOfflinePlatformPlayer(offlinePlayer);
    }

    @Override
    public Collection<OfflinePlatformPlayer> getOfflinePlayers() {
        OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
        BukkitOfflinePlatformPlayer[] offlinePlatformPlayers = new BukkitOfflinePlatformPlayer[offlinePlayers.length];
        for (int i = 0; i < offlinePlayers.length; ++i) {
            offlinePlatformPlayers[i] = new BukkitOfflinePlatformPlayer(offlinePlayers[i]);
        }
        return Arrays.asList(offlinePlatformPlayers);
    }
}

