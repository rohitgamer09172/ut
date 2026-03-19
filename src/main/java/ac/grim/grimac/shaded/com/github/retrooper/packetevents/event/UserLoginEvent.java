/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.event;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.CallableEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PlayerEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.UserEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class UserLoginEvent
extends PacketEvent
implements CallableEvent,
UserEvent,
PlayerEvent {
    private final User user;
    private final Object player;

    public UserLoginEvent(User user, Object player) {
        this.user = user;
        this.player = player;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public <T> T getPlayer() {
        return (T)this.player;
    }

    @Override
    public void call(PacketListenerCommon listener) {
        listener.onUserLogin(this);
    }
}

