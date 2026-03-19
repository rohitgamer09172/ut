/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.event;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.UserEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class UserDisconnectEvent
extends PacketEvent
implements UserEvent {
    private final User user;

    public UserDisconnectEvent(User user) {
        this.user = user;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public void call(PacketListenerCommon listener) {
        listener.onUserDisconnect(this);
    }
}

