/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.jspecify.annotations.Nullable
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.event;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.CancellableEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PlayerEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.UserEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.exception.InvalidDisconnectPacketSend;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.exception.PacketProcessException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.UnpooledByteBufAllocationHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.ConnectionState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.PacketSide;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.UnknownNullability;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class ProtocolPacketEvent
extends PacketEvent
implements PlayerEvent,
CancellableEvent,
UserEvent {
    private final int packetID;
    private final PacketTypeCommon packetType;
    private ServerVersion serverVersion;
    private final Object channel;
    private final ConnectionState connectionState;
    private final User user;
    private @UnknownNullability Object player;
    private Object byteBuf;
    private boolean cancel;
    private @Nullable PacketWrapper<?> lastUsedWrapper;
    private @Nullable List<Runnable> postTasks = null;
    private boolean cloned;
    private boolean needsReEncode = PacketEvents.getAPI().getSettings().reEncodeByDefault();

    public ProtocolPacketEvent(PacketSide packetSide, Object channel, User user, @UnknownNullability Object player, Object byteBuf, boolean autoProtocolTranslation) throws PacketProcessException {
        this.channel = channel;
        this.user = user;
        this.player = player;
        this.serverVersion = autoProtocolTranslation || user.getClientVersion() == null ? PacketEvents.getAPI().getServerManager().getVersion() : user.getClientVersion().toServerVersion();
        this.byteBuf = byteBuf;
        int size = ByteBufHelper.readableBytes(byteBuf);
        if (size == 0) {
            throw new PacketProcessException("Trying to process a packet, but it has no content. (Size=0)");
        }
        try {
            this.packetID = ByteBufHelper.readVarInt(byteBuf);
        }
        catch (Exception e) {
            throw new PacketProcessException("Failed to read the Packet ID of a packet. (Size: " + size + ")");
        }
        ClientVersion version = this.serverVersion.toClientVersion();
        this.connectionState = packetSide == PacketSide.CLIENT ? user.getDecoderState() : user.getEncoderState();
        PacketTypeCommon packetType = PacketType.getById(packetSide, this.connectionState, version, this.packetID);
        if (packetType == null) {
            if (PacketType.getById(packetSide, ConnectionState.PLAY, version, this.packetID) == PacketType.Play.Server.DISCONNECT) {
                throw new InvalidDisconnectPacketSend();
            }
            throw new PacketProcessException("Failed to map the Packet ID " + this.packetID + " to a PacketType constant. Bound: " + (Object)((Object)packetSide.getOpposite()) + ", Connection state: " + (Object)((Object)user.getDecoderState()) + ", Server version: " + this.serverVersion.getReleaseName());
        }
        this.packetType = packetType;
    }

    public ProtocolPacketEvent(int packetID, PacketTypeCommon packetType, ServerVersion serverVersion, Object channel, User user, @UnknownNullability Object player, Object byteBuf) {
        this.channel = channel;
        this.user = user;
        this.player = player;
        this.serverVersion = serverVersion;
        this.byteBuf = byteBuf;
        this.packetID = packetID;
        this.packetType = packetType;
        this.connectionState = packetType != null && packetType.getSide() == PacketSide.SERVER ? user.getEncoderState() : user.getDecoderState();
        this.cloned = true;
    }

    public void markForReEncode(boolean needsReEncode) {
        this.needsReEncode = needsReEncode;
    }

    public boolean needsReEncode() {
        return this.needsReEncode;
    }

    public boolean isClone() {
        return this.cloned;
    }

    public Object getChannel() {
        return this.channel;
    }

    public SocketAddress getAddress() {
        return ChannelHelper.remoteAddress(this.channel);
    }

    public InetSocketAddress getSocketAddress() {
        return (InetSocketAddress)this.getAddress();
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public <T> @UnknownNullability T getPlayer() {
        return (T)this.player;
    }

    @Deprecated
    @ApiStatus.Internal
    public void setPlayer(Object player) {
        this.player = player;
    }

    public ConnectionState getConnectionState() {
        return this.connectionState;
    }

    @Deprecated
    public ClientVersion getClientVersion() {
        return this.user.getClientVersion();
    }

    @Deprecated
    public void setClientVersion(ClientVersion clientVersion) {
        PacketEvents.getAPI().getLogManager().debug("Setting client version with deprecated method " + clientVersion.getReleaseName());
        this.user.setClientVersion(clientVersion);
    }

    public ServerVersion getServerVersion() {
        return this.serverVersion;
    }

    @Deprecated
    public void setServerVersion(ServerVersion serverVersion) {
        this.serverVersion = serverVersion;
    }

    public Object getByteBuf() {
        return this.byteBuf;
    }

    public void setByteBuf(Object byteBuf) {
        this.byteBuf = byteBuf;
    }

    public int getPacketId() {
        return this.packetID;
    }

    public PacketTypeCommon getPacketType() {
        return this.packetType;
    }

    @Deprecated
    public String getPacketName() {
        return ((Enum)((Object)this.packetType)).name();
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean val) {
        this.cancel = val;
    }

    public @Nullable PacketWrapper<?> getLastUsedWrapper() {
        return this.lastUsedWrapper;
    }

    public void setLastUsedWrapper(@Nullable PacketWrapper<?> lastUsedWrapper) {
        this.lastUsedWrapper = lastUsedWrapper;
    }

    public List<Runnable> getPostTasks() {
        if (this.postTasks == null) {
            this.postTasks = new ArrayList<Runnable>();
        }
        return this.postTasks;
    }

    public boolean hasPostTasks() {
        return this.postTasks != null && !this.postTasks.isEmpty();
    }

    public ProtocolPacketEvent clone() {
        return this instanceof PacketReceiveEvent ? ((PacketReceiveEvent)this).clone() : ((PacketSendEvent)this).clone();
    }

    public void cleanUp() {
        if (this.isClone()) {
            ByteBufHelper.release(this.byteBuf);
        }
    }

    public Object getFullBufferClone() {
        byte[] data = ByteBufHelper.copyBytes(this.getByteBuf());
        Object buffer = UnpooledByteBufAllocationHelper.buffer();
        ByteBufHelper.writeVarInt(buffer, this.getPacketId());
        ByteBufHelper.writeBytes(buffer, data);
        return buffer;
    }
}

