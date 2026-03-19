/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelFutureListener
 *  io.netty.channel.ChannelHandler
 *  io.netty.util.concurrent.GenericFutureListener
 */
package ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.connection;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.UserConnectEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.ConnectionState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.UserProfile;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.FakeChannelUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.handlers.PacketEventsDecoder;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.handlers.PacketEventsEncoder;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.viaversion.ViaVersionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.List;
import java.util.NoSuchElementException;

public class ServerConnectionInitializer {
    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void initChannel(Object ch, ConnectionState connectionState) {
        Channel channel = (Channel)ch;
        if (FakeChannelUtil.isFakeChannel(channel)) {
            return;
        }
        User user = new User(channel, connectionState, null, new UserProfile(null, null));
        if (connectionState == ConnectionState.PLAY) {
            user.setClientVersion(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
            PacketEvents.getAPI().getLogManager().warn("Late injection detected, we missed packets so some functionality may break!");
        }
        Channel channel2 = channel;
        synchronized (channel2) {
            if (channel.pipeline().get("splitter") == null) {
                channel.close();
                return;
            }
            UserConnectEvent connectEvent = new UserConnectEvent(user);
            PacketEvents.getAPI().getEventManager().callEvent(connectEvent);
            if (connectEvent.isCancelled()) {
                channel.unsafe().closeForcibly();
                return;
            }
            ServerConnectionInitializer.relocateHandlers(channel, user, false, false);
            if (PacketEvents.getAPI().getSettings().isPreViaInjection() && ViaVersionUtil.isAvailable()) {
                ServerConnectionInitializer.relocateHandlers(channel, user, true, false);
            }
            channel.closeFuture().addListener((GenericFutureListener)((ChannelFutureListener)future -> PacketEventsImplHelper.handleDisconnection(user.getChannel(), user.getUUID())));
            PacketEvents.getAPI().getProtocolManager().setUser(channel, user);
        }
    }

    public static void destroyHandlers(Object ch) {
        Channel channel = (Channel)ch;
        if (channel.pipeline().get(PacketEvents.DECODER_NAME) != null) {
            channel.pipeline().remove(PacketEvents.DECODER_NAME);
        } else {
            PacketEvents.getAPI().getLogger().warning("Could not find decoder handler in channel pipeline!");
        }
        if (channel.pipeline().get(PacketEvents.ENCODER_NAME) != null) {
            channel.pipeline().remove(PacketEvents.ENCODER_NAME);
        } else {
            PacketEvents.getAPI().getLogger().warning("Could not find encoder handler in channel pipeline!");
        }
    }

    public static void relocateHandlers(Channel ctx, User user, boolean preVia, boolean force) {
        try {
            PacketEventsEncoder encoder;
            PacketEventsDecoder decoder;
            PacketEventsDecoder existingDecoder;
            String targetDecoderName;
            String targetEncoderName;
            String decoderName;
            if (PacketEvents.getAPI().getSettings().isDebugEnabled()) {
                PacketEvents.getAPI().getLogManager().debug("Pre relocate, preVia: " + preVia + ", " + ChannelHelper.pipelineHandlerNamesAsString(ctx));
            }
            String encoderName = preVia ? "pre-" + PacketEvents.ENCODER_NAME : PacketEvents.ENCODER_NAME;
            String string = decoderName = preVia ? "pre-" + PacketEvents.DECODER_NAME : PacketEvents.DECODER_NAME;
            if (preVia) {
                targetEncoderName = "via-encoder";
                targetDecoderName = "via-decoder";
            } else {
                targetDecoderName = ctx.pipeline().names().contains("inbound_config") ? "inbound_config" : "decoder";
                String string2 = targetEncoderName = ctx.pipeline().names().contains("outbound_config") ? "outbound_config" : "encoder";
            }
            if (force) {
                boolean decoderGood = ServerConnectionInitializer.isAlreadyBefore(ctx, decoderName, targetDecoderName);
                boolean encoderGood = ServerConnectionInitializer.isAlreadyBefore(ctx, encoderName, targetEncoderName);
                if (decoderGood && encoderGood) {
                    return;
                }
            }
            if ((existingDecoder = (PacketEventsDecoder)ctx.pipeline().get(decoderName)) != null) {
                if (existingDecoder.hasBeenRelocated && !force) {
                    return;
                }
                existingDecoder.hasBeenRelocated = true;
                decoder = new PacketEventsDecoder((PacketEventsDecoder)ctx.pipeline().remove(decoderName));
                encoder = new PacketEventsEncoder(ctx.pipeline().remove(encoderName));
            } else {
                encoder = new PacketEventsEncoder(user, preVia);
                decoder = new PacketEventsDecoder(user, preVia);
            }
            if (PacketEvents.getAPI().getSettings().isDebugEnabled()) {
                PacketEvents.getAPI().getLogManager().debug("After remove, preVia: " + preVia + ", " + ChannelHelper.pipelineHandlerNamesAsString(ctx));
            }
            if (preVia) {
                ctx.pipeline().addBefore("via-encoder", encoderName, (ChannelHandler)encoder).addBefore("via-decoder", decoderName, (ChannelHandler)decoder);
            } else {
                ctx.pipeline().addBefore(targetDecoderName, decoderName, (ChannelHandler)decoder).addBefore(targetEncoderName, encoderName, (ChannelHandler)encoder);
            }
            if (PacketEvents.getAPI().getSettings().isDebugEnabled()) {
                PacketEvents.getAPI().getLogManager().debug("After add, preVia: " + preVia + ", " + ChannelHelper.pipelineHandlerNamesAsString(ctx));
            }
        }
        catch (NoSuchElementException ex) {
            String handlers = ChannelHelper.pipelineHandlerNamesAsString(ctx);
            throw new IllegalStateException("PacketEvents failed to add a decoder to the netty pipeline. Pipeline handlers: " + handlers, ex);
        }
    }

    private static boolean isAlreadyBefore(Channel ctx, String myHandler, String targetHandler) {
        List names = ctx.pipeline().names();
        int myIndex = names.indexOf(myHandler);
        int targetIndex = names.indexOf(targetHandler);
        if (myIndex == -1) {
            return false;
        }
        if (targetIndex == -1) {
            return true;
        }
        return myIndex < targetIndex;
    }
}

