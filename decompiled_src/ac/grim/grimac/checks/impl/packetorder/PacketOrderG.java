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

@CheckData(name="PacketOrderG", experimental=true)
public class PacketOrderG
extends Check
implements PostPredictionCheck {
    private final ArrayDeque<String> flags = new ArrayDeque();

    public PacketOrderG(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING || event.getPacketType() == PacketType.Play.Client.CLIENT_STATUS && new WrapperPlayClientClientStatus(event).getAction() == WrapperPlayClientClientStatus.Action.OPEN_INVENTORY_ACHIEVEMENT) {
            DiggingAction action = null;
            if (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING && ((action = new WrapperPlayClientPlayerDigging(event).getAction()) == DiggingAction.RELEASE_USE_ITEM || action == DiggingAction.START_DIGGING || action == DiggingAction.CANCELLED_DIGGING || action == DiggingAction.FINISHED_DIGGING)) {
                return;
            }
            if (this.player.packetOrderProcessor.isAttacking() || this.player.packetOrderProcessor.isReleasing() || this.player.packetOrderProcessor.isRightClicking() || this.player.packetOrderProcessor.isPicking() || this.player.packetOrderProcessor.isDigging()) {
                String verbose = "action=" + (action == null ? "openInventory" : (action == DiggingAction.SWAP_ITEM_WITH_OFFHAND ? "swap" : "drop")) + ", attacking=" + this.player.packetOrderProcessor.isAttacking() + ", releasing=" + this.player.packetOrderProcessor.isReleasing() + ", rightClicking=" + this.player.packetOrderProcessor.isRightClicking() + ", picking=" + this.player.packetOrderProcessor.isPicking() + ", digging=" + this.player.packetOrderProcessor.isDigging();
                if (!this.player.canSkipTicks()) {
                    if (this.flagAndAlert(verbose) && this.shouldModifyPackets()) {
                        event.setCancelled(true);
                        this.player.onPacketCancel();
                    }
                } else {
                    this.flags.add(verbose);
                }
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

