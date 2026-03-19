/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  protocolsupport.api.ProtocolSupportAPI
 */
package ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.protocolsupport;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.util.protocolsupport.ProtocolSupportState;
import java.net.SocketAddress;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import protocolsupport.api.ProtocolSupportAPI;

public class ProtocolSupportUtil {
    private static ProtocolSupportState available = ProtocolSupportState.UNKNOWN;

    public static boolean isAvailable() {
        if (available == ProtocolSupportState.UNKNOWN) {
            try {
                ClassLoader classLoader = PacketEvents.getAPI().getPlugin().getClass().getClassLoader();
                classLoader.loadClass("protocolsupport.api.ProtocolSupportAPI");
                available = ProtocolSupportState.ENABLED;
                return true;
            }
            catch (Exception e) {
                available = ProtocolSupportState.DISABLED;
                return false;
            }
        }
        return available == ProtocolSupportState.ENABLED;
    }

    public static void checkIfProtocolSupportIsPresent() {
        boolean present = Bukkit.getPluginManager().isPluginEnabled("ProtocolSupport");
        available = present ? ProtocolSupportState.ENABLED : ProtocolSupportState.DISABLED;
    }

    public static int getProtocolVersion(SocketAddress address) {
        return ProtocolSupportAPI.getProtocolVersion((SocketAddress)address).getId();
    }

    public static int getProtocolVersion(Player player) {
        return ProtocolSupportAPI.getProtocolVersion((Player)player).getId();
    }
}

