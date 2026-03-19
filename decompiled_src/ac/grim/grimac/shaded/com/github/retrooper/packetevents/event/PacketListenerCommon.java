/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.event;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerPriority;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.UserConnectEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.UserDisconnectEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.UserLoginEvent;

public abstract class PacketListenerCommon {
    private final PacketListenerPriority priority;

    public PacketListenerCommon(PacketListenerPriority priority) {
        this.priority = priority;
    }

    public PacketListenerCommon() {
        this.priority = PacketListenerPriority.NORMAL;
    }

    public PacketListenerPriority getPriority() {
        return this.priority;
    }

    public void onUserConnect(UserConnectEvent event) {
    }

    public void onUserLogin(UserLoginEvent event) {
    }

    public void onUserDisconnect(UserDisconnectEvent event) {
    }

    void onPacketReceive(PacketReceiveEvent event) {
    }

    void onPacketSend(PacketSendEvent event) {
    }

    public void onPacketEventExternal(PacketEvent event) {
    }

    public boolean isPreVia() {
        return false;
    }
}

