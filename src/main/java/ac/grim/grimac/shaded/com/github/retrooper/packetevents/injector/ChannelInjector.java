/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.injector;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.PacketSide;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;

public interface ChannelInjector {
    default public boolean isServerBound() {
        return true;
    }

    public void inject();

    public void uninject();

    public void updateUser(Object var1, User var2);

    public void setPlayer(Object var1, Object var2);

    public boolean isPlayerSet(Object var1);

    public boolean isProxy();

    default public PacketSide getPacketSide() {
        return PacketSide.SERVER;
    }
}

