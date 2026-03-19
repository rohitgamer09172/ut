/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.packetorder;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClientStatus;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import java.util.ArrayDeque;

@CheckData(name="PacketOrderK", experimental=true)
public class PacketOrderK
extends Check
implements PostPredictionCheck {
    private final ArrayDeque<String> flags = new ArrayDeque();

    public PacketOrderK(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        Object verbose;
        if (event.getPacketType() == PacketType.Play.Client.CLIENT_STATUS && new WrapperPlayClientClientStatus(event).getAction() == WrapperPlayClientClientStatus.Action.OPEN_INVENTORY_ACHIEVEMENT && (this.player.packetOrderProcessor.isClickingInInventory() || this.player.packetOrderProcessor.isClosingInventory())) {
            verbose = "open, clicking=" + this.player.packetOrderProcessor.isClickingInInventory() + ", closing=" + this.player.packetOrderProcessor.isClosingInventory();
            if (!this.player.canSkipTicks()) {
                this.flagAndAlert((String)verbose);
            } else {
                this.flags.add((String)verbose);
            }
        }
        if ((event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW || event.getPacketType() == PacketType.Play.Client.CLOSE_WINDOW) && this.player.packetOrderProcessor.isOpeningInventory()) {
            Object object = verbose = event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW ? "click" : "close";
            if (!this.player.canSkipTicks()) {
                if (this.flagAndAlert((String)verbose) && this.shouldModifyPackets()) {
                    event.setCancelled(true);
                    this.player.onPacketCancel();
                }
            } else {
                this.flags.add((String)verbose);
            }
        }
    }

    @Override
    public void onPredictionComplete(PredictionComplete predictionComplete) {
        if (!this.player.canSkipTicks()) {
            return;
        }
        if (this.player.isTickingReliablyFor(3)) {
            for (String verbose : this.flags) {
                this.flagAndAlert(verbose);
            }
        }
        this.flags.clear();
    }
}

