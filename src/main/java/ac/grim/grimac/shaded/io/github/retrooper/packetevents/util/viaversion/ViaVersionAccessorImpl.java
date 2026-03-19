/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.viaversion.viaversion.api.Via
 *  com.viaversion.viaversion.api.connection.UserConnection
 *  com.viaversion.viaversion.bukkit.handlers.BukkitDecodeHandler
 *  com.viaversion.viaversion.bukkit.handlers.BukkitEncodeHandler
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelHandler
 *  org.bukkit.entity.Player
 */
package ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.viaversion;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.reflection.Reflection;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.viaversion.ViaVersionAccessor;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.bukkit.handlers.BukkitDecodeHandler;
import com.viaversion.viaversion.bukkit.handlers.BukkitEncodeHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import java.lang.reflect.Field;
import org.bukkit.entity.Player;

public class ViaVersionAccessorImpl
implements ViaVersionAccessor {
    private static Field CONNECTION_FIELD;

    @Override
    public int getProtocolVersion(Player player) {
        return Via.getAPI().getPlayerVersion((Object)player);
    }

    @Override
    public int getProtocolVersion(User user) {
        try {
            ChannelHandler viaEncoder = ((Channel)user.getChannel()).pipeline().get("via-encoder");
            if (CONNECTION_FIELD == null) {
                CONNECTION_FIELD = Reflection.getField(viaEncoder.getClass(), "connection");
            }
            UserConnection connection = (UserConnection)CONNECTION_FIELD.get(viaEncoder);
            return connection.getProtocolInfo().getProtocolVersion();
        }
        catch (IllegalAccessException e) {
            PacketEvents.getAPI().getLogManager().warn("Unable to grab ViaVersion client version for player!");
            return -1;
        }
    }

    @Override
    public Class<?> getUserConnectionClass() {
        return UserConnection.class;
    }

    @Override
    public Class<?> getBukkitDecodeHandlerClass() {
        return BukkitDecodeHandler.class;
    }

    @Override
    public Class<?> getBukkitEncodeHandlerClass() {
        return BukkitEncodeHandler.class;
    }
}

