/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.type;

import ac.grim.grimac.api.AbstractCheck;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;

public interface PacketCheck
extends AbstractCheck {
    default public void onPacketReceive(PacketReceiveEvent event) {
    }

    default public void onPacketSend(PacketSendEvent event) {
    }
}

