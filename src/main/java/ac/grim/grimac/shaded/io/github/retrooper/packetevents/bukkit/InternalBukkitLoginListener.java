/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerLoginEvent
 *  org.bukkit.event.player.PlayerLoginEvent$Result
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.io.github.retrooper.packetevents.bukkit;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEventsAPI;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.FakeChannelUtil;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.SpigotChannelInjector;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Internal
public class InternalBukkitLoginListener
implements Listener {
    @EventHandler(priority=EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event) {
        PacketEventsAPI<?> api = PacketEvents.getAPI();
        User user = api.getPlayerManager().getUser(event.getPlayer());
        if (user != null) {
            SpigotChannelInjector injector = (SpigotChannelInjector)api.getInjector();
            injector.updatePlayer(user, event.getPlayer());
            return;
        }
        Object channel = api.getPlayerManager().getChannel(event.getPlayer());
        if (channel != null && FakeChannelUtil.isFakeChannel(channel) || api.isTerminated() && !api.getSettings().isKickIfTerminated()) {
            return;
        }
        event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "PacketEvents failed to inject into a channel");
    }
}

