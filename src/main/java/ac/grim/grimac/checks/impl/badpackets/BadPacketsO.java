/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientKeepAlive;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerKeepAlive;
import java.util.Iterator;
import java.util.LinkedList;

@CheckData(name="BadPacketsO")
public class BadPacketsO
extends Check
implements PacketCheck {
    private final LinkedList<Long> keepalives = new LinkedList();

    public BadPacketsO(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.KEEP_ALIVE) {
            this.keepalives.add(new WrapperPlayServerKeepAlive(event).getId());
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.KEEP_ALIVE) {
            long id = new WrapperPlayClientKeepAlive(event).getId();
            Iterator iterator = this.keepalives.iterator();
            while (iterator.hasNext()) {
                Long data;
                long keepalive = (Long)iterator.next();
                if (keepalive != id) continue;
                while ((data = this.keepalives.poll()) != null && data != id) {
                }
                return;
            }
            if (this.flagAndAlert("id=" + id) && this.shouldModifyPackets()) {
                event.setCancelled(true);
                this.player.onPacketCancel();
            }
        }
    }
}

