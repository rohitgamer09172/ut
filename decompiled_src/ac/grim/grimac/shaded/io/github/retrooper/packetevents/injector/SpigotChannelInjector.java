/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelFuture
 *  io.netty.channel.ChannelHandler
 *  io.netty.channel.ChannelPipeline
 *  org.bukkit.entity.Player
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.injector.ChannelInjector;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.ConnectionState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.reflection.ReflectionObject;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.connection.ServerChannelHandler;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.connection.ServerConnectionInitializer;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.handlers.PacketEventsDecoder;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.injector.handlers.PacketEventsEncoder;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.InjectedList;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

@ApiStatus.Internal
public class SpigotChannelInjector
implements ChannelInjector {
    public final Set<Channel> injectedConnectionChannels = new HashSet<Channel>();
    public List<Object> networkManagers;
    private int connectionChannelsListIndex = -1;

    public void updatePlayer(User user, Object player) {
        Object channel = user.getChannel();
        if (channel == null) {
            channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
        }
        this.setPlayer(channel, player);
    }

    @Override
    public boolean isPlayerSet(Object channel) {
        if (channel == null) {
            return false;
        }
        PacketEventsEncoder encoder = this.getEncoder((Channel)channel);
        if (encoder != null && encoder.player != null) {
            return true;
        }
        PacketEventsDecoder decoder = this.getDecoder((Channel)channel);
        return decoder != null && decoder.player != null;
    }

    @Override
    public boolean isServerBound() {
        Object serverConnection = SpigotReflectionUtil.getMinecraftServerConnectionInstance();
        if (serverConnection != null) {
            ReflectionObject reflectServerConnection = new ReflectionObject(serverConnection);
            for (int i = 0; i < 2; ++i) {
                List list = reflectServerConnection.readList(i);
                for (Object value : list) {
                    if (!(value instanceof ChannelFuture)) continue;
                    this.connectionChannelsListIndex = i;
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void inject() {
        Object serverConnection = SpigotReflectionUtil.getMinecraftServerConnectionInstance();
        if (serverConnection != null) {
            ReflectionObject reflectServerConnection = new ReflectionObject(serverConnection);
            List connectionChannelFutures = reflectServerConnection.readList(this.connectionChannelsListIndex);
            InjectedList<ChannelFuture> wrappedList = new InjectedList<ChannelFuture>(connectionChannelFutures, future -> {
                Channel channel = future.channel();
                this.injectServerChannel(channel);
                this.injectedConnectionChannels.add(channel);
            });
            reflectServerConnection.writeList(this.connectionChannelsListIndex, wrappedList);
            if (this.networkManagers == null) {
                this.networkManagers = SpigotReflectionUtil.getNetworkManagers();
            }
            List<Object> list = this.networkManagers;
            synchronized (list) {
                if (!this.networkManagers.isEmpty()) {
                    PacketEvents.getAPI().getLogManager().debug("Late bind not enabled, injecting into existing channel");
                }
                for (Object networkManager : this.networkManagers) {
                    ReflectionObject networkManagerWrapper = new ReflectionObject(networkManager);
                    Channel channel = networkManagerWrapper.readObject(0, Channel.class);
                    if (channel == null) continue;
                    try {
                        ServerConnectionInitializer.initChannel(channel, ConnectionState.PLAY);
                    }
                    catch (Exception e) {
                        PacketEvents.getAPI().getLogManager().severe("PacketEvents Spigot injector failed to inject into an existing channel. If you need assistance, join our Discord server: https://discord.gg/DVHxPPxHZc");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void uninject() {
        ReflectionObject reflectServerConnection;
        List connectionChannelFutures;
        for (Channel connectionChannel : this.injectedConnectionChannels) {
            this.uninjectServerChannel(connectionChannel);
        }
        this.injectedConnectionChannels.clear();
        Object serverConnection = SpigotReflectionUtil.getMinecraftServerConnectionInstance();
        if (serverConnection != null && (connectionChannelFutures = (reflectServerConnection = new ReflectionObject(serverConnection)).readList(this.connectionChannelsListIndex)) instanceof InjectedList) {
            reflectServerConnection.writeList(this.connectionChannelsListIndex, ((InjectedList)connectionChannelFutures).originalList());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void injectServerChannel(Channel serverChannel) {
        ChannelPipeline pipeline = serverChannel.pipeline();
        ChannelHandler connectionHandler = pipeline.get(PacketEvents.CONNECTION_HANDLER_NAME);
        if (connectionHandler != null) {
            pipeline.remove(PacketEvents.CONNECTION_HANDLER_NAME);
        }
        if (pipeline.get("SpigotNettyServerChannelHandler#0") != null) {
            pipeline.addAfter("SpigotNettyServerChannelHandler#0", PacketEvents.CONNECTION_HANDLER_NAME, (ChannelHandler)new ServerChannelHandler());
        } else if (pipeline.get("floodgate-init") != null) {
            pipeline.addAfter("floodgate-init", PacketEvents.CONNECTION_HANDLER_NAME, (ChannelHandler)new ServerChannelHandler());
        } else if (pipeline.get("MinecraftPipeline#0") != null) {
            pipeline.addAfter("MinecraftPipeline#0", PacketEvents.CONNECTION_HANDLER_NAME, (ChannelHandler)new ServerChannelHandler());
        } else {
            pipeline.addFirst(PacketEvents.CONNECTION_HANDLER_NAME, (ChannelHandler)new ServerChannelHandler());
        }
        if (this.networkManagers == null) {
            this.networkManagers = SpigotReflectionUtil.getNetworkManagers();
        }
        List<Object> list = this.networkManagers;
        synchronized (list) {
            for (Object networkManager : this.networkManagers) {
                ReflectionObject networkManagerWrapper = new ReflectionObject(networkManager);
                Channel channel = networkManagerWrapper.readObject(0, Channel.class);
                if (channel == null || !channel.isOpen() || !channel.localAddress().equals(serverChannel.localAddress())) continue;
                channel.close();
            }
        }
    }

    private void uninjectServerChannel(Channel serverChannel) {
        if (serverChannel.pipeline().get(PacketEvents.CONNECTION_HANDLER_NAME) != null) {
            serverChannel.pipeline().remove(PacketEvents.CONNECTION_HANDLER_NAME);
        } else {
            PacketEvents.getAPI().getLogManager().warn("Failed to uninject server channel, handler not found");
        }
    }

    @Override
    public void updateUser(Object channel, User user) {
        PacketEventsDecoder decoder;
        PacketEventsEncoder encoder = this.getEncoder((Channel)channel);
        if (encoder != null) {
            encoder.user = user;
        }
        if ((decoder = this.getDecoder((Channel)channel)) != null) {
            decoder.user = user;
        }
    }

    @Override
    public void setPlayer(Object channel, Object player) {
        PacketEventsDecoder decoder;
        PacketEventsEncoder encoder = this.getEncoder((Channel)channel);
        if (encoder != null) {
            encoder.player = (Player)player;
        }
        if ((decoder = this.getDecoder((Channel)channel)) != null) {
            decoder.player = (Player)player;
            decoder.user.getProfile().setName(((Player)player).getName());
            decoder.user.getProfile().setUUID(((Player)player).getUniqueId());
        }
    }

    public @Nullable PacketEventsEncoder getEncoder(Channel channel) {
        return (PacketEventsEncoder)channel.pipeline().get(PacketEvents.ENCODER_NAME);
    }

    public @Nullable PacketEventsDecoder getDecoder(Channel channel) {
        return (PacketEventsDecoder)channel.pipeline().get(PacketEvents.DECODER_NAME);
    }

    @Override
    public boolean isProxy() {
        return false;
    }
}

