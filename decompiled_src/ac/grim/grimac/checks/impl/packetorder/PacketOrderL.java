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
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClientStatus;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import java.util.ArrayDeque;

@CheckData(name="PacketOrderL", experimental=true)
public class PacketOrderL
extends Check
implements PostPredictionCheck {
    private final ArrayDeque<String> flags = new ArrayDeque();

    public PacketOrderL(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.CLIENT_STATUS && new WrapperPlayClientClientStatus(event).getAction() == WrapperPlayClientClientStatus.Action.OPEN_INVENTORY_ACHIEVEMENT && this.player.packetOrderProcessor.isDropping()) {
            if (!this.player.canSkipTicks()) {
                if (this.flagAndAlert("inventory") && this.shouldModifyPackets()) {
                    event.setCancelled(true);
                    this.player.onPacketCancel();
                }
            } else {
                this.flags.add("inventory");
            }
        }
        if (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING && new WrapperPlayClientPlayerDigging(event).getAction() == DiggingAction.SWAP_ITEM_WITH_OFFHAND && this.player.packetOrderProcessor.isDropping()) {
            if (!this.player.canSkipTicks()) {
                if (this.flagAndAlert("swap") && this.shouldModifyPackets()) {
                    event.setCancelled(true);
                    this.player.onPacketCancel();
                }
            } else {
                this.flags.add("swap");
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

