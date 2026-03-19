/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.channel.ChannelHandler
 *  io.netty.channel.ChannelHandlerContext
 *  io.netty.channel.ChannelInboundHandlerAdapter
 *  io.netty.channel.ChannelInitializer
 *  io.netty.channel.ChannelPipeline
 *  io.netty.util.internal.logging.InternalLogger
 *  io.netty.util.internal.logging.InternalLoggerFactory
 */
package ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.connection;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.ConnectionState;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.connection.ServerConnectionInitializer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class PreChannelInitializer_v1_12
extends ChannelInboundHandlerAdapter {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(ChannelInitializer.class);

    public void channelRegistered(ChannelHandlerContext ctx) {
        try {
            ServerConnectionInitializer.initChannel(ctx.channel(), ConnectionState.HANDSHAKING);
        }
        catch (Throwable t) {
            this.exceptionCaught(ctx, t);
        }
        finally {
            ChannelPipeline pipeline = ctx.pipeline();
            if (pipeline.context((ChannelHandler)this) != null) {
                pipeline.remove((ChannelHandler)this);
            }
        }
        ctx.pipeline().fireChannelRegistered();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) {
        logger.warn("Failed to initialize a channel. Closing: " + ctx.channel(), t);
        ctx.close();
    }
}

