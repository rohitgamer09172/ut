/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;

@CheckData(name="BadPacketsY", description="Sent out of bounds slot id")
public class BadPacketsY
extends Check
implements PacketCheck {
    public BadPacketsY(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        int slot;
        if (event.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE && ((slot = new WrapperPlayClientHeldItemChange(event).getSlot()) > 8 || slot < 0) && this.flagAndAlert("slot=" + slot) && this.shouldModifyPackets()) {
            event.setCancelled(true);
            this.player.onPacketCancel();
        }
    }
}

