/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.multiactions;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.impl.multiactions.MultiActionsC;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;

@CheckData(name="MultiActionsD", description="Closed inventory while moving")
public class MultiActionsD
extends Check
implements PacketCheck {
    public MultiActionsD(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        String verbose;
        if (event.getPacketType() == PacketType.Play.Client.CLOSE_WINDOW && !(verbose = MultiActionsC.getVerbose(this.player)).isEmpty() && this.flagAndAlert(verbose) && this.shouldModifyPackets()) {
            event.setCancelled(true);
            this.player.onPacketCancel();
        }
    }
}

