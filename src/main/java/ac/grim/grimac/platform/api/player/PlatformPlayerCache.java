/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.platform.api.player;

import ac.grim.grimac.platform.api.player.PlatformPlayer;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlatformPlayerCache {
    private static final PlatformPlayerCache INSTANCE = new PlatformPlayerCache();
    private final Map<UUID, PlatformPlayer> playerCache = new ConcurrentHashMap<UUID, PlatformPlayer>();

    private PlatformPlayerCache() {
    }

    public static PlatformPlayerCache getInstance() {
        return INSTANCE;
    }

    public PlatformPlayer addOrGetPlayer(UUID uuid, PlatformPlayer player) {
        return this.playerCache.compute(uuid, (key, existing) -> {
            if (existing != null) {
                return existing;
            }
            return player;
        });
    }

    public void removePlayer(UUID uuid) {
        this.playerCache.remove(uuid);
    }

    public PlatformPlayer getPlayer(UUID uuid) {
        return this.playerCache.get(uuid);
    }
}

