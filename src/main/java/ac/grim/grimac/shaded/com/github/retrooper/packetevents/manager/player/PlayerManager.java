/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.player;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.ConnectionState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public interface PlayerManager {
    public int getPing(@NotNull Object var1);

    @NotNull
    public ClientVersion getClientVersion(@NotNull Object var1);

    public Object getChannel(@NotNull Object var1);

    public User getUser(@NotNull Object var1);

    @ApiStatus.Obsolete
    default public ConnectionState getConnectionState(@NotNull Object player) throws IllegalStateException {
        return this.getUser(player).getConnectionState();
    }

    default public void sendPacket(@NotNull Object player, @NotNull Object byteBuf) {
        PacketEvents.getAPI().getProtocolManager().sendPacket(this.getChannel(player), byteBuf);
    }

    default public void sendPacket(@NotNull Object player, @NotNull PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().sendPacket(this.getChannel(player), wrapper);
    }

    default public void sendPacketSilently(@NotNull Object player, @NotNull Object byteBuf) {
        PacketEvents.getAPI().getProtocolManager().sendPacketSilently(this.getChannel(player), byteBuf);
    }

    default public void sendPacketSilently(@NotNull Object player, @NotNull PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().sendPacketSilently(this.getChannel(player), wrapper);
    }

    default public void writePacket(@NotNull Object player, @NotNull Object byteBuf) {
        PacketEvents.getAPI().getProtocolManager().writePacket(this.getChannel(player), byteBuf);
    }

    default public void writePacket(@NotNull Object player, @NotNull PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().writePacket(this.getChannel(player), wrapper);
    }

    default public void writePacketSilently(@NotNull Object player, @NotNull Object byteBuf) {
        PacketEvents.getAPI().getProtocolManager().writePacketSilently(this.getChannel(player), byteBuf);
    }

    default public void writePacketSilently(@NotNull Object player, @NotNull PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().writePacketSilently(this.getChannel(player), wrapper);
    }

    default public void receivePacket(Object player, Object byteBuf) {
        PacketEvents.getAPI().getProtocolManager().receivePacket(this.getChannel(player), byteBuf);
    }

    default public void receivePacket(Object player, PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().receivePacket(this.getChannel(player), wrapper);
    }

    default public void receivePacketSilently(Object player, Object byteBuf) {
        PacketEvents.getAPI().getProtocolManager().receivePacketSilently(this.getChannel(player), byteBuf);
    }

    default public void receivePacketSilently(Object player, PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().receivePacketSilently(this.getChannel(player), wrapper);
    }
}

