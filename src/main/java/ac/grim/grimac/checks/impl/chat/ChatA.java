/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.chat;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientTabComplete;

@CheckData(name="ChatA", experimental=true)
public class ChatA
extends Check
implements PacketCheck {
    public ChatA(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        WrapperPlayClientTabComplete wrapper;
        String text;
        if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE && PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13) && ((text = (wrapper = new WrapperPlayClientTabComplete(event)).getText()).equals("/") || text.trim().isEmpty()) && this.flagAndAlert("")) {
            event.setCancelled(true);
            this.player.onPacketCancel();
        }
    }
}

