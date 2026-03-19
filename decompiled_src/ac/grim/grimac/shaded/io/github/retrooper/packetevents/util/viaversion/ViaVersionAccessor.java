/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.viaversion;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import org.bukkit.entity.Player;

public interface ViaVersionAccessor {
    public int getProtocolVersion(Player var1);

    public int getProtocolVersion(User var1);

    public Class<?> getUserConnectionClass();

    public Class<?> getBukkitDecodeHandlerClass();

    public Class<?> getBukkitEncodeHandlerClass();
}

