/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.event.HandlerList
 */
package ac.grim.grimac.api.events;

import ac.grim.grimac.api.AbstractCheck;
import ac.grim.grimac.api.GrimUser;
import ac.grim.grimac.api.events.FlagEvent;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import lombok.Generated;
import org.bukkit.event.HandlerList;

@Deprecated(since="1.2.1.0", forRemoval=true)
public class CompletePredictionEvent
extends FlagEvent {
    private static final HandlerList handlers = new HandlerList();
    private final double offset;
    private boolean cancelled;

    public CompletePredictionEvent(GrimUser player, AbstractCheck check, String verbose, double offset) {
        super(player, check, verbose);
        this.offset = offset;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Generated
    public double getOffset() {
        return this.offset;
    }
}

