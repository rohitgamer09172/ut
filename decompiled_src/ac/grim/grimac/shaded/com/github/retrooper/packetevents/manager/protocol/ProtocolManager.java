/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.protocol;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.ProtocolVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.PacketTransformationUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public interface ProtocolManager {
    @ApiStatus.Internal
    public static final Map<UUID, Object> CHANNELS = new ConcurrentHashMap<UUID, Object>();
    @ApiStatus.Internal
    public static final Map<Object, User> USERS = new ConcurrentHashMap<Object, User>();

    default public Collection<User> getUsers() {
        return USERS.values();
    }

    default public Collection<Object> getChannels() {
        return CHANNELS.values();
    }

    public ProtocolVersion getPlatformVersion();

    public void sendPacket(Object var1, Object var2);

    public void sendPacketSilently(Object var1, Object var2);

    public void writePacket(Object var1, Object var2);

    public void writePacketSilently(Object var1, Object var2);

    public void receivePacket(Object var1, Object var2);

    public void receivePacketSilently(Object var1, Object var2);

    public ClientVersion getClientVersion(Object var1);

    default public void sendPackets(Object channel, Object ... byteBuf) {
        for (Object buf : byteBuf) {
            this.sendPacket(channel, buf);
        }
    }

    default public void sendPacketsSilently(Object channel, Object ... byteBuf) {
        for (Object buf : byteBuf) {
            this.sendPacketSilently(channel, buf);
        }
    }

    default public void writePackets(Object channel, Object ... byteBuf) {
        for (Object buf : byteBuf) {
            this.writePacket(channel, buf);
        }
    }

    default public void writePacketsSilently(Object channel, Object ... byteBuf) {
        for (Object buf : byteBuf) {
            this.writePacketSilently(channel, buf);
        }
    }

    default public void receivePackets(Object channel, Object ... byteBuf) {
        for (Object buf : byteBuf) {
            this.receivePacket(channel, buf);
        }
    }

    default public void receivePacketsSilently(Object channel, Object ... byteBuf) {
        for (Object buf : byteBuf) {
            this.receivePacketSilently(channel, buf);
        }
    }

    default public void setClientVersion(Object channel, ClientVersion version) {
        this.getUser(channel).setClientVersion(version);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @ApiStatus.Internal
    default public Object[] transformWrappers(PacketWrapper<?> wrapper, Object channel, boolean outgoing) {
        PacketWrapper<?>[] wrappers = PacketTransformationUtil.transform(wrapper);
        Object[] buffers = new Object[wrappers.length];
        for (int i = 0; i < wrappers.length; ++i) {
            PacketWrapper<?> wrappper = wrappers[i];
            Object object = wrappper.bufferLock;
            synchronized (object) {
                wrappper.prepareForSend(channel, outgoing);
                buffers[i] = wrappper.buffer;
                wrappper.buffer = null;
                continue;
            }
        }
        return buffers;
    }

    default public void sendPacket(Object channel, PacketWrapper<?> wrapper) {
        Object[] transformed = this.transformWrappers(wrapper, channel, true);
        this.sendPackets(channel, transformed);
    }

    default public void sendPacketSilently(Object channel, PacketWrapper<?> wrapper) {
        Object[] transformed = this.transformWrappers(wrapper, channel, true);
        this.sendPacketsSilently(channel, transformed);
    }

    default public void writePacket(Object channel, PacketWrapper<?> wrapper) {
        Object[] transformed = this.transformWrappers(wrapper, channel, true);
        this.writePackets(channel, transformed);
    }

    default public void writePacketSilently(Object channel, PacketWrapper<?> wrapper) {
        Object[] transformed = this.transformWrappers(wrapper, channel, true);
        this.writePacketsSilently(channel, transformed);
    }

    default public void receivePacket(Object channel, PacketWrapper<?> wrapper) {
        Object[] transformed = this.transformWrappers(wrapper, channel, false);
        this.receivePackets(channel, transformed);
    }

    default public void receivePacketSilently(Object channel, PacketWrapper<?> wrapper) {
        Object[] transformed = this.transformWrappers(wrapper, channel, false);
        this.receivePacketsSilently(channel, transformed);
    }

    default public User getUser(Object channel) {
        Object pipeline = ChannelHelper.getPipeline(channel);
        return USERS.get(pipeline);
    }

    @ApiStatus.Internal
    default public User removeUser(Object channel) {
        Object pipeline = ChannelHelper.getPipeline(channel);
        return USERS.remove(pipeline);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @ApiStatus.Internal
    default public void setUser(Object channel, User user) {
        Object object = channel;
        synchronized (object) {
            Object pipeline = ChannelHelper.getPipeline(channel);
            USERS.put(pipeline, user);
        }
        PacketEvents.getAPI().getInjector().updateUser(channel, user);
    }

    default public Object getChannel(UUID uuid) {
        return CHANNELS.get(uuid);
    }

    @ApiStatus.Internal
    default public void setChannel(UUID uuid, Object channel) {
        CHANNELS.put(uuid, channel);
    }

    @ApiStatus.Internal
    default public void removeChannel(Object channel) {
        CHANNELS.values().remove(channel);
    }

    @ApiStatus.Internal
    default public void removeChannelById(UUID uuid) {
        CHANNELS.remove(uuid);
    }

    default public boolean hasChannel(Object channel) {
        return CHANNELS.containsValue(channel);
    }
}

