/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.viaversion;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.viaversion.ViaState;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.viaversion.ViaVersionAccessor;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.viaversion.ViaVersionAccessorImpl;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.viaversion.ViaVersionAccessorImplLegacy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ViaVersionUtil {
    private static ViaState available = ViaState.UNKNOWN;
    private static ViaVersionAccessor viaVersionAccessor;

    private ViaVersionUtil() {
    }

    private static void load() {
        if (viaVersionAccessor == null) {
            ClassLoader classLoader = PacketEvents.getAPI().getPlugin().getClass().getClassLoader();
            try {
                classLoader.loadClass("com.viaversion.viaversion.api.Via");
                viaVersionAccessor = new ViaVersionAccessorImpl();
            }
            catch (Exception e) {
                try {
                    classLoader.loadClass("us.myles.ViaVersion.api.Via");
                    viaVersionAccessor = new ViaVersionAccessorImplLegacy();
                }
                catch (ClassNotFoundException ex) {
                    viaVersionAccessor = null;
                }
            }
        }
    }

    public static void checkIfViaIsPresent() {
        boolean present = Bukkit.getPluginManager().isPluginEnabled("ViaVersion");
        available = present ? ViaState.ENABLED : ViaState.DISABLED;
    }

    public static boolean isAvailable() {
        if (available == ViaState.UNKNOWN) {
            return ViaVersionUtil.getViaVersionAccessor() != null;
        }
        return available == ViaState.ENABLED;
    }

    public static ViaVersionAccessor getViaVersionAccessor() {
        ViaVersionUtil.load();
        return viaVersionAccessor;
    }

    public static int getProtocolVersion(User user) {
        return ViaVersionUtil.getViaVersionAccessor().getProtocolVersion(user);
    }

    public static int getProtocolVersion(Player player) {
        return ViaVersionUtil.getViaVersionAccessor().getProtocolVersion(player);
    }

    public static Class<?> getUserConnectionClass() {
        return ViaVersionUtil.getViaVersionAccessor().getUserConnectionClass();
    }

    public static Class<?> getBukkitDecodeHandlerClass() {
        return ViaVersionUtil.getViaVersionAccessor().getBukkitDecodeHandlerClass();
    }

    public static Class<?> getBukkitEncodeHandlerClass() {
        return ViaVersionUtil.getViaVersionAccessor().getBukkitEncodeHandlerClass();
    }
}

