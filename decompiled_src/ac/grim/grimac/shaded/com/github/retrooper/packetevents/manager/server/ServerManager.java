/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.SystemOS;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public interface ServerManager {
    public ServerVersion getVersion();

    default public SystemOS getOS() {
        return SystemOS.getOS();
    }

    @Nullable
    default public Object getRegistryCacheKey(User user, ClientVersion version) {
        return null;
    }
}

