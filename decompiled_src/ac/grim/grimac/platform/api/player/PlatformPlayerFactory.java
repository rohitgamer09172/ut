/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.platform.api.player;

import ac.grim.grimac.platform.api.player.OfflinePlatformPlayer;
import ac.grim.grimac.platform.api.player.PlatformPlayer;
import java.util.Collection;
import java.util.UUID;

public interface PlatformPlayerFactory {
    public OfflinePlatformPlayer getOfflineFromUUID(UUID var1);

    public OfflinePlatformPlayer getOfflineFromName(String var1);

    public Collection<OfflinePlatformPlayer> getOfflinePlayers();

    public PlatformPlayer getFromName(String var1);

    public PlatformPlayer getFromUUID(UUID var1);

    public PlatformPlayer getFromNativePlayerType(Object var1);

    public void invalidatePlayer(UUID var1);

    public Collection<PlatformPlayer> getOnlinePlayers();
}

