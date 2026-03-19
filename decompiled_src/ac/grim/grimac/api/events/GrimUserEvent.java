/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.api.events;

import ac.grim.grimac.api.GrimUser;

@Deprecated(since="1.2.1.0", forRemoval=true)
public interface GrimUserEvent {
    public GrimUser getUser();

    default public GrimUser getPlayer() {
        return this.getUser();
    }
}

