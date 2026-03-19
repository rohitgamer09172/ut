/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelHandler
 *  io.netty.channel.ChannelHandlerContext
 *  io.netty.channel.ChannelInboundHandlerAdapter
 *  io.netty.util.Version
 */
package ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.connection;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.PEVersion;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.connection.PreChannelInitializer_v1_12;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.connection.PreChannelInitializer_v1_8;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Version;
import java.util.Map;

public class ServerChannelHandler
extends ChannelInboundHandlerAdapter {
    public static final PEVersion MODERN_NETTY_VERSION = new PEVersion(4, 1, 24);
    public static boolean CHECKED_NETTY_VERSION;
    public static PEVersion NETTY_VERSION;

    private static PEVersion resolveNettyVersion() {
        Map nettyArtifacts = Version.identify();
        Version version = nettyArtifacts.getOrDefault("netty-common", (Version)nettyArtifacts.get("netty-all"));
        if (version == null && !nettyArtifacts.values().isEmpty()) {
            version = (Version)nettyArtifacts.values().iterator().next();
        }
        if (version != null) {
            String stringVersion = version.artifactVersion();
            String[] splitVersion = (stringVersion = stringVersion.replaceAll("[^\\d.]", "")).split("\\.");
            if (splitVersion.length > 3) {
                stringVersion = splitVersion[0] + "." + splitVersion[1] + "." + splitVersion[2];
            }
            stringVersion = stringVersion.endsWith(".") ? stringVersion.substring(0, stringVersion.length() - 1) : stringVersion;
            return PEVersion.fromString(stringVersion);
        }
        return null;
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = (Channel)msg;
        if (NETTY_VERSION == null && !CHECKED_NETTY_VERSION) {
            NETTY_VERSION = ServerChannelHandler.resolveNettyVersion();
            CHECKED_NETTY_VERSION = true;
        }
        if (NETTY_VERSION != null && NETTY_VERSION.isNewerThan(MODERN_NETTY_VERSION) || SpigotReflectionUtil.V_1_12_OR_HIGHER) {
            channel.pipeline().addLast(PacketEvents.SERVER_CHANNEL_HANDLER_NAME, (ChannelHandler)new PreChannelInitializer_v1_12());
        } else {
            channel.pipeline().addFirst(PacketEvents.SERVER_CHANNEL_HANDLER_NAME, (ChannelHandler)new PreChannelInitializer_v1_8());
        }
        super.channelRead(ctx, msg);
    }
}

