/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.event;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerPriority;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;

public abstract class PacketListenerAbstract
extends PacketListenerCommon {
    public PacketListenerAbstract(PacketListenerPriority priority) {
        super(priority);
    }

    public PacketListenerAbstract() {
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
    }
}

