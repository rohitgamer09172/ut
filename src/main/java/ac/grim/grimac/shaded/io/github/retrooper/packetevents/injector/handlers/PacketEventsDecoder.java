/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  io.netty.channel.ChannelHandlerContext
 *  io.netty.handler.codec.MessageToMessageDecoder
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.handlers;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.exception.PacketProcessException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.ConnectionState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.ExceptionUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisconnect;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.connection.ServerConnectionInitializer;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.handlers.PacketEventsEncoder;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.viaversion.ViaVersionUtil;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PacketEventsDecoder
extends MessageToMessageDecoder<ByteBuf> {
    public User user;
    public Player player;
    public boolean hasBeenRelocated;
    public boolean preViaVersion;

    public PacketEventsDecoder(User user, boolean preViaVersion) {
        this.user = user;
        this.preViaVersion = preViaVersion;
    }

    public PacketEventsDecoder(PacketEventsDecoder decoder) {
        this.user = decoder.user;
        this.player = decoder.player;
        this.hasBeenRelocated = decoder.hasBeenRelocated;
        this.preViaVersion = decoder.preViaVersion;
    }

    public void read(ChannelHandlerContext ctx, ByteBuf input, List<Object> out) throws Exception {
        try {
            if (!this.preViaVersion && PacketEvents.getAPI().getSettings().isPreViaInjection() && !ViaVersionUtil.isAvailable()) {
                PacketEventsImplHelper.handleServerBoundPacket(ctx.channel(), this.user, this.player, input, this.preViaVersion);
            }
            PacketEventsImplHelper.handleServerBoundPacket(ctx.channel(), this.user, this.player, input, !this.preViaVersion);
            out.add(ByteBufHelper.retain(input));
        }
        catch (Throwable e) {
            if (ExceptionUtil.isException(e, PacketProcessException.class)) {
                throw e;
            }
            throw new PacketProcessException(e);
        }
    }

    public void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        if (buffer.isReadable()) {
            this.read(ctx, buffer, out);
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        boolean debug;
        if (!ExceptionUtil.isException(cause, PacketProcessException.class)) {
            super.exceptionCaught(ctx, cause);
            return;
        }
        boolean bl = debug = PacketEvents.getAPI().getSettings().isDebugEnabled() || SpigotReflectionUtil.isMinecraftServerInstanceDebugging();
        if (debug || this.user != null && this.user.getDecoderState() != ConnectionState.HANDSHAKING) {
            if (PacketEvents.getAPI().getSettings().isFullStackTraceEnabled()) {
                String clientVersion;
                String state = this.user != null ? this.user.getDecoderState().name() : "null";
                String string = clientVersion = this.user != null ? this.user.getClientVersion().getReleaseName() : "null";
                String username = this.user != null && this.user.getProfile().getName() != null ? this.user.getProfile().getName() : (this.player != null ? this.player.getName() : "null");
                PacketEvents.getAPI().getLogger().log(Level.WARNING, cause, () -> "An error occurred while processing a packet from " + username + " (state: " + state + ", clientVersion: " + clientVersion + ", serverVersion: " + PacketEvents.getAPI().getServerManager().getVersion().getReleaseName() + ", preVia: " + this.preViaVersion + ")");
            } else {
                PacketEvents.getAPI().getLogManager().warn(cause.getMessage());
            }
        }
        if (PacketEvents.getAPI().getSettings().isKickOnPacketExceptionEnabled()) {
            try {
                if (this.user != null) {
                    this.user.sendPacket(new WrapperPlayServerDisconnect(Component.text("Invalid packet")));
                }
            }
            catch (Exception state) {
                // empty catch block
            }
            ctx.channel().close();
            if (this.player != null) {
                FoliaScheduler.getEntityScheduler().runDelayed((Entity)this.player, (Plugin)PacketEvents.getAPI().getPlugin(), o -> this.player.kickPlayer("Invalid packet"), null, 1L);
            }
            String username = this.user != null && this.user.getProfile().getName() != null ? this.user.getProfile().getName() : (this.player != null ? this.player.getName() : "null");
            PacketEvents.getAPI().getLogManager().warn("Disconnected " + username + " due to an invalid packet!");
        }
    }

    public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {
        if (PacketEventsEncoder.COMPRESSION_ENABLED_EVENT == null || event != PacketEventsEncoder.COMPRESSION_ENABLED_EVENT) {
            super.userEventTriggered(ctx, event);
            return;
        }
        if (!this.preViaVersion) {
            ServerConnectionInitializer.relocateHandlers(ctx.channel(), this.user, false, true);
            if (PacketEvents.getAPI().getSettings().isPreViaInjection() && ViaVersionUtil.isAvailable()) {
                ServerConnectionInitializer.relocateHandlers(ctx.channel(), this.user, true, true);
            }
        }
        super.userEventTriggered(ctx, event);
    }
}

