/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelHandler
 *  io.netty.channel.ChannelInitializer
 */
package ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.connection;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.ConnectionState;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.connection.ServerConnectionInitializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;

public class PreChannelInitializer_v1_8
extends ChannelInitializer<Channel> {
    protected void initChannel(Channel channel) {
        channel.pipeline().addLast(new ChannelHandler[]{new ChannelInitializer<Channel>(){

            protected void initChannel(Channel channel) {
                ServerConnectionInitializer.initChannel(channel, ConnectionState.HANDSHAKING);
            }
        }});
    }
}

