/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.manager.tick.impl;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.manager.tick.Tickable;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.channel.ChannelHelper;

public class ClientVersionSetter
implements Tickable {
    @Override
    public void tick() {
        for (GrimPlayer player : GrimAPI.INSTANCE.getPlayerDataManager().getEntries()) {
            if (!ChannelHelper.isOpen(player.user.getChannel())) {
                GrimAPI.INSTANCE.getPlayerDataManager().onDisconnect(player.user);
                continue;
            }
            player.pollData();
        }
    }
}

