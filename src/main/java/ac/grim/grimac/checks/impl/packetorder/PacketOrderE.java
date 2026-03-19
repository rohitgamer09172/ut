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
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import java.util.ArrayDeque;

@CheckData(name="PacketOrderE", experimental=true)
public class PacketOrderE
extends Check
implements PostPredictionCheck {
    private final ArrayDeque<String> flags = new ArrayDeque();
    private boolean setback;

    public PacketOrderE(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE && (this.player.packetOrderProcessor.isAttacking() || this.player.packetOrderProcessor.isRightClicking() || this.player.packetOrderProcessor.isOpeningInventory() || this.player.packetOrderProcessor.isReleasing() || this.player.packetOrderProcessor.isSneaking() || this.player.packetOrderProcessor.isSprinting() || this.player.packetOrderProcessor.isLeavingBed() || this.player.packetOrderProcessor.isStartingToGlide() || this.player.packetOrderProcessor.isJumpingWithMount())) {
            String verbose = "attacking=" + this.player.packetOrderProcessor.isAttacking() + ", rightClicking=" + this.player.packetOrderProcessor.isRightClicking() + ", openingInventory=" + this.player.packetOrderProcessor.isOpeningInventory() + ", releasing=" + this.player.packetOrderProcessor.isReleasing() + ", sneaking=" + this.player.packetOrderProcessor.isSneaking() + ", sprinting=" + this.player.packetOrderProcessor.isSprinting() + ", bed=" + this.player.packetOrderProcessor.isLeavingBed() + ", sprinting=" + this.player.packetOrderProcessor.isSprinting() + ", gliding=" + this.player.packetOrderProcessor.isStartingToGlide() + ", mountJumping=" + this.player.packetOrderProcessor.isJumpingWithMount();
            if ((this.player.canSkipTicks() && this.flags.add(verbose) || this.flagAndAlert(verbose)) && this.player.packetOrderProcessor.isUsing()) {
                this.setback = true;
            }
        }
    }

    @Override
    public void onPredictionComplete(PredictionComplete predictionComplete) {
        if (!this.player.canSkipTicks()) {
            if (this.setback) {
                this.setback = false;
                this.setbackIfAboveSetbackVL();
            }
            return;
        }
        if (this.player.isTickingReliablyFor(3)) {
            for (String verbose : this.flags) {
                if (!this.flagAndAlert(verbose) || !this.setback) continue;
                this.setback = false;
                this.setbackIfAboveSetbackVL();
            }
        }
        this.setback = false;
        this.flags.clear();
    }
}

