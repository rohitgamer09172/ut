/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package ac.grim.grimac.api.events;

import ac.grim.grimac.api.GrimUser;
import ac.grim.grimac.api.events.GrimUserEvent;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Deprecated(since="1.2.1.0", forRemoval=true)
public class GrimJoinEvent
extends Event
implements GrimUserEvent {
    private static final HandlerList handlers = new HandlerList();
    private final GrimUser user;

    public GrimJoinEvent(GrimUser user) {
        super(true);
        this.user = user;
    }

    @Override
    public GrimUser getUser() {
        return this.user;
    }

    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

