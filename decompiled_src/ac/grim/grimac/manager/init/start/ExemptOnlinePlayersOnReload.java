/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.manager.init.start;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.manager.init.start.StartableInitable;
import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;

public class ExemptOnlinePlayersOnReload
implements StartableInitable {
    @Override
    public void start() {
        for (PlatformPlayer player : GrimAPI.INSTANCE.getPlatformPlayerFactory().getOnlinePlayers()) {
            User user = PacketEvents.getAPI().getPlayerManager().getUser(player.getNative());
            GrimAPI.INSTANCE.getPlayerDataManager().exemptUsers.add(user);
        }
    }
}

