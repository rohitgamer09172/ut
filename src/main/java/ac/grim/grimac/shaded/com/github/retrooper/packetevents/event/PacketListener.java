/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.event;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerAbstract;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerPriority;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.UserConnectEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.UserDisconnectEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.UserLoginEvent;

public interface PacketListener {
    default public PacketListenerAbstract asAbstract(PacketListenerPriority priority) {
        return new PacketListenerAbstract(priority){

            @Override
            public void onUserConnect(UserConnectEvent event) {
                PacketListener.this.onUserConnect(event);
            }

            @Override
            public void onUserLogin(UserLoginEvent event) {
                PacketListener.this.onUserLogin(event);
            }

            @Override
            public void onUserDisconnect(UserDisconnectEvent event) {
                PacketListener.this.onUserDisconnect(event);
            }

            @Override
            public void onPacketReceive(PacketReceiveEvent event) {
                PacketListener.this.onPacketReceive(event);
            }

            @Override
            public void onPacketSend(PacketSendEvent event) {
                PacketListener.this.onPacketSend(event);
            }

            @Override
            public void onPacketEventExternal(PacketEvent event) {
                PacketListener.this.onPacketEventExternal(event);
            }
        };
    }

    default public void onUserConnect(UserConnectEvent event) {
    }

    default public void onUserLogin(UserLoginEvent event) {
    }

    default public void onUserDisconnect(UserDisconnectEvent event) {
    }

    default public void onPacketReceive(PacketReceiveEvent event) {
    }

    default public void onPacketSend(PacketSendEvent event) {
    }

    default public void onPacketEventExternal(PacketEvent event) {
    }
}

