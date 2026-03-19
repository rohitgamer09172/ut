/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerLoginEvent
 *  org.bukkit.event.player.PlayerLoginEvent$Result
 *  org.bukkit.plugin.Plugin
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.io.github.retrooper.packetevents.bukkit;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEventsAPI;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.FakeChannelUtil;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.SpigotChannelInjector;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.manager.player.PlayerManagerImpl;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Internal
public class InternalBukkitListener
implements Listener {
    static final String KICK_MESSAGE = "PacketEvents failed to inject into a channel";
    private final Plugin plugin;

    public InternalBukkitListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onLogin(PlayerLoginEvent event) {
        if (event.getResult() == PlayerLoginEvent.Result.ALLOWED) {
            this.onPreJoin(event.getPlayer());
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        this.onPostJoin(event.getPlayer());
    }

    void onPreJoin(Player player) {
        PacketEventsAPI<?> api = PacketEvents.getAPI();
        Map<UUID, WeakReference<Player>> map = ((PlayerManagerImpl)api.getPlayerManager()).joiningPlayers;
        map.put(player.getUniqueId(), new WeakReference<Player>(player));
    }

    void onPostJoin(Player player) {
        PacketEventsAPI<?> api = PacketEvents.getAPI();
        User user = api.getPlayerManager().getUser(player);
        if (user != null) {
            SpigotChannelInjector injector = (SpigotChannelInjector)PacketEvents.getAPI().getInjector();
            injector.setPlayer(user.getChannel(), player);
            ((PlayerManagerImpl)api.getPlayerManager()).joiningPlayers.remove(player.getUniqueId());
            return;
        }
        ((PlayerManagerImpl)api.getPlayerManager()).joiningPlayers.remove(player.getUniqueId());
        Object channel = api.getPlayerManager().getChannel(player);
        if (channel != null && FakeChannelUtil.isFakeChannel(channel) || api.isTerminated() && !api.getSettings().isKickIfTerminated()) {
            return;
        }
        FoliaScheduler.getEntityScheduler().runDelayed((Entity)player, this.plugin, __ -> {
            if (channel != null ? ChannelHelper.isOpen(channel) : player.isOnline()) {
                player.kickPlayer(KICK_MESSAGE);
            }
        }, null, 0L);
    }
}

