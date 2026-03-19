/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.viaversion.viaversion.api.protocol.version.ProtocolVersion
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelHandler
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.viaversion;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.reflection.Reflection;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.viaversion.ViaVersionAccessor;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ViaVersionAccessorImplLegacy
implements ViaVersionAccessor {
    private Class<?> viaClass;
    private Class<?> bukkitDecodeHandlerClass;
    private Class<?> bukkitEncodeHandlerClass;
    private Field viaManagerField;
    private Method apiAccessor;
    private Method getPlayerVersionMethod;
    private Class<?> userConnectionClass;

    private void load() {
        ClassLoader classLoader;
        if (this.viaClass == null) {
            try {
                classLoader = PacketEvents.getAPI().getPlugin().getClass().getClassLoader();
                this.viaClass = classLoader.loadClass("us.myles.ViaVersion.api.Via");
                this.viaManagerField = this.viaClass.getDeclaredField("manager");
                this.bukkitDecodeHandlerClass = classLoader.loadClass("us.myles.ViaVersion.bukkit.handlers.BukkitDecodeHandler");
                this.bukkitEncodeHandlerClass = classLoader.loadClass("us.myles.ViaVersion.bukkit.handlers.BukkitEncodeHandler");
                Class<?> viaAPIClass = classLoader.loadClass("us.myles.ViaVersion.api.ViaAPI");
                this.apiAccessor = this.viaClass.getMethod("getAPI", new Class[0]);
                this.getPlayerVersionMethod = viaAPIClass.getMethod("getPlayerVersion", Object.class);
            }
            catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        if (this.userConnectionClass == null) {
            try {
                classLoader = PacketEvents.getAPI().getPlugin().getClass().getClassLoader();
                this.userConnectionClass = classLoader.loadClass("us.myles.ViaVersion.api.data.UserConnection");
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getProtocolVersion(Player player) {
        this.load();
        try {
            Object viaAPI = this.apiAccessor.invoke(null, new Object[0]);
            return (Integer)this.getPlayerVersionMethod.invoke(viaAPI, player);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int getProtocolVersion(User user) {
        try {
            int version;
            Player player;
            if (user.getUUID() != null && (player = Bukkit.getPlayer((UUID)user.getUUID())) != null && (version = this.getProtocolVersion(player)) != -1) {
                return version;
            }
            ChannelHandler viaEncoder = ((Channel)user.getChannel()).pipeline().get("via-encoder");
            Object connection = Reflection.getField(viaEncoder.getClass(), "connection").get(viaEncoder);
            Object protocolInfo = Reflection.getField(connection.getClass(), "protocolInfo").get(connection);
            Object protocolVersion = Reflection.getField(protocolInfo.getClass(), "protocolVersion").get(protocolInfo);
            return protocolVersion instanceof Integer ? ((Integer)protocolVersion).intValue() : ((ProtocolVersion)protocolVersion).getVersion();
        }
        catch (Exception e) {
            PacketEvents.getAPI().getLogManager().warn("Unable to grab ViaVersion client version for player!");
            return -1;
        }
    }

    @Override
    public Class<?> getUserConnectionClass() {
        this.load();
        return this.userConnectionClass;
    }

    @Override
    public Class<?> getBukkitDecodeHandlerClass() {
        this.load();
        return this.bukkitDecodeHandlerClass;
    }

    @Override
    public Class<?> getBukkitEncodeHandlerClass() {
        this.load();
        return this.bukkitEncodeHandlerClass;
    }
}

