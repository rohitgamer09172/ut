/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelHandler
 *  io.netty.channel.ChannelHandlerContext
 *  io.netty.channel.ChannelOutboundHandlerAdapter
 *  io.netty.channel.ChannelPromise
 *  io.netty.util.ReferenceCountUtil
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.handlers;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.exception.InvalidDisconnectPacketSend;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.exception.PacketProcessException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.ConnectionState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.ExceptionUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisconnect;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.connection.ServerConnectionInitializer;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.viaversion.CustomPipelineUtil;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.viaversion.ViaVersionUtil;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.Queue;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.Nullable;

public class PacketEventsEncoder
extends ChannelOutboundHandlerAdapter {
    public static final Object COMPRESSION_ENABLED_EVENT = PacketEventsEncoder.paperCompressionEnabledEvent();
    private static final boolean NETTY_4_1_0;
    public User user;
    public Player player;
    private boolean handledCompression = COMPRESSION_ENABLED_EVENT != null;
    private ChannelPromise promise;
    private final Queue<QueuedMessage> queuedMessages = new ArrayDeque<QueuedMessage>();
    private boolean hold = false;
    private boolean preVia;

    public PacketEventsEncoder(User user, boolean preVia) {
        this.user = user;
        this.preVia = preVia;
    }

    public PacketEventsEncoder(ChannelHandler encoder) {
        this.user = ((PacketEventsEncoder)encoder).user;
        this.player = ((PacketEventsEncoder)encoder).player;
        this.handledCompression = ((PacketEventsEncoder)encoder).handledCompression;
        this.promise = ((PacketEventsEncoder)encoder).promise;
        this.preVia = ((PacketEventsEncoder)encoder).preVia;
    }

    public void setHold(Channel ch, boolean hold) throws Exception {
        if (this.hold == hold) {
            return;
        }
        this.hold = hold;
        if (!hold && !this.queuedMessages.isEmpty()) {
            QueuedMessage queued;
            ChannelHandlerContext ctx = ch.pipeline().context((ChannelHandler)this);
            while ((queued = this.queuedMessages.poll()) != null) {
                this.write(ctx, queued.message, queued.promise);
            }
        }
    }

    private @Nullable PacketSendEvent handleClientBoundPacket(Channel channel, User user, Object player, ByteBuf buffer, ChannelPromise promise, boolean preVia) throws Exception {
        PacketSendEvent packetSendEvent = PacketEventsImplHelper.handleClientBoundPacket(channel, user, player, buffer, !preVia);
        if (packetSendEvent != null && packetSendEvent.hasTasksAfterSend()) {
            promise.addListener(p -> {
                for (Runnable task : packetSendEvent.getTasksAfterSend()) {
                    task.run();
                }
            });
        }
        return packetSendEvent;
    }

    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ChannelPromise oldPromise;
        if (this.hold && msg instanceof ByteBuf) {
            this.queuedMessages.add(new QueuedMessage(msg, promise));
            return;
        }
        ChannelPromise channelPromise = oldPromise = this.promise != null && !this.promise.isSuccess() ? this.promise : null;
        if (NETTY_4_1_0) {
            promise = promise.unvoid();
        }
        promise.addListener(p -> {
            this.promise = oldPromise;
        });
        this.promise = promise;
        if (msg instanceof ByteBuf) {
            boolean needsRecompression = !this.handledCompression && this.handleCompression(ctx, (ByteBuf)msg);
            this.handleClientBoundPacket(ctx.channel(), this.user, this.player, (ByteBuf)msg, this.promise, this.preVia);
            if (!this.preVia && PacketEvents.getAPI().getSettings().isPreViaInjection() && !ViaVersionUtil.isAvailable()) {
                this.handleClientBoundPacket(ctx.channel(), this.user, this.player, (ByteBuf)msg, this.promise, !this.preVia);
            }
            if (!((ByteBuf)msg).isReadable()) {
                ReferenceCountUtil.release((Object)msg);
                promise.trySuccess();
                return;
            }
            if (needsRecompression) {
                this.compress(ctx, (ByteBuf)msg);
            }
        }
        ctx.write(msg, promise);
    }

    public void handlerRemoved(ChannelHandlerContext ctx) {
        QueuedMessage entry;
        while ((entry = this.queuedMessages.poll()) != null) {
            ReferenceCountUtil.release((Object)entry.message);
            if (!NETTY_4_1_0 || entry.promise == null || entry.promise.isVoid()) continue;
            entry.promise.setFailure((Throwable)new IllegalStateException((Object)((Object)this) + " got dropped from pipeline " + ctx.channel()));
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (ExceptionUtil.isException(cause, InvalidDisconnectPacketSend.class)) {
            return;
        }
        boolean didWeCauseThis = ExceptionUtil.isException(cause, PacketProcessException.class);
        if (didWeCauseThis && (this.user == null || this.user.getEncoderState() != ConnectionState.HANDSHAKING)) {
            if (!SpigotReflectionUtil.isMinecraftServerInstanceDebugging()) {
                if (PacketEvents.getAPI().getSettings().isFullStackTraceEnabled()) {
                    cause.printStackTrace();
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
                catch (Exception exception) {
                    // empty catch block
                }
                ctx.channel().close();
                if (this.player != null) {
                    FoliaScheduler.getEntityScheduler().runDelayed((Entity)this.player, (Plugin)PacketEvents.getAPI().getPlugin(), o -> this.player.kickPlayer("Invalid packet"), null, 1L);
                }
                if (this.user != null && this.user.getProfile().getName() != null) {
                    PacketEvents.getAPI().getLogManager().warn("Disconnected " + this.user.getProfile().getName() + " due to an invalid packet!");
                }
            }
        }
        super.exceptionCaught(ctx, cause);
    }

    private static Object paperCompressionEnabledEvent() {
        try {
            Class<?> eventClass = Class.forName("io.papermc.paper.network.ConnectionEvent");
            return eventClass.getDeclaredField("COMPRESSION_THRESHOLD_SET").get(null);
        }
        catch (ReflectiveOperationException e) {
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void compress(ChannelHandlerContext ctx, ByteBuf input) throws InvocationTargetException {
        ChannelHandler compressor = ctx.pipeline().get("compress");
        ByteBuf temp = ctx.alloc().buffer();
        try {
            if (compressor != null) {
                CustomPipelineUtil.callEncode(compressor, ctx, input, temp);
            }
        }
        finally {
            input.clear().writeBytes(temp);
            temp.release();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void decompress(ChannelHandlerContext ctx, ByteBuf input, ByteBuf output) throws InvocationTargetException {
        ChannelHandler decompressor = ctx.pipeline().get("decompress");
        if (decompressor != null) {
            ByteBuf temp = (ByteBuf)CustomPipelineUtil.callDecode(decompressor, ctx, input).get(0);
            try {
                output.clear().writeBytes(temp);
            }
            finally {
                temp.release();
            }
        }
    }

    private boolean handleCompression(ChannelHandlerContext ctx, ByteBuf buffer) throws InvocationTargetException {
        if (this.handledCompression) {
            return false;
        }
        int compressIndex = ctx.pipeline().names().indexOf("compress");
        if (compressIndex == -1) {
            return false;
        }
        this.handledCompression = true;
        int peEncoderIndex = ctx.pipeline().names().indexOf((this.preVia ? "pre-" : "") + PacketEvents.ENCODER_NAME);
        if (peEncoderIndex == -1) {
            return false;
        }
        if (compressIndex <= peEncoderIndex) {
            return false;
        }
        boolean decompress = false;
        if (!this.preVia || !this.user.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_7_10)) {
            this.decompress(ctx, buffer, buffer);
            decompress = true;
        }
        ServerConnectionInitializer.relocateHandlers(ctx.channel(), this.user, this.preVia, false);
        return decompress;
    }

    static {
        boolean netty410 = false;
        try {
            ChannelPromise.class.getDeclaredMethod("unvoid", new Class[0]);
            netty410 = true;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            // empty catch block
        }
        NETTY_4_1_0 = netty410;
    }

    private static final class QueuedMessage {
        private final Object message;
        private final ChannelPromise promise;

        public QueuedMessage(Object message, ChannelPromise promise) {
            this.message = message;
            this.promise = promise;
        }
    }
}

