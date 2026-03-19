/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.event;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerCommon;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface CallableEvent {
    default public void call(PacketListenerCommon listener) {
        listener.onPacketEventExternal((PacketEvent)this);
    }
}

