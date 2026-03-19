/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.event;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.CancellableEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.UserEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class UserConnectEvent
extends PacketEvent
implements CancellableEvent,
UserEvent {
    private final User user;
    private boolean cancelled;

    public UserConnectEvent(User user) {
        this.user = user;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public void call(PacketListenerCommon listener) {
        listener.onUserConnect(this);
    }
}

