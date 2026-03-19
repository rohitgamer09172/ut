/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.platform.api.player;

import ac.grim.grimac.api.GrimIdentity;

public interface OfflinePlatformPlayer
extends GrimIdentity {
    public boolean isOnline();

    public String getName();
}

