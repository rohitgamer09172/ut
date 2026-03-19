/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package ac.grim.grimac.platform.bukkit.utils.anticheat;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.reflection.ReflectionUtils;
import java.lang.reflect.Method;
import org.bukkit.entity.Player;

public class MultiLibUtil {
    public static final Method externalPlayerMethod = ReflectionUtils.getMethod(Player.class, "isExternalPlayer", new Class[0]);
    private static final boolean IS_PRE_1_18 = PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_18);

    public static boolean isExternalPlayer(Player player) {
        if (externalPlayerMethod == null || IS_PRE_1_18) {
            return false;
        }
        try {
            return (Boolean)externalPlayerMethod.invoke((Object)player, new Object[0]);
        }
        catch (Exception e) {
            LogUtil.error("Failed to invoke external player method", e);
            return false;
        }
    }
}

