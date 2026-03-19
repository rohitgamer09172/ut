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

@CheckData(name="PacketOrderF", experimental=true)
public class PacketOrderF
extends Check
implements PostPredictionCheck {
    private final ArrayDeque<String> flags = new ArrayDeque();

    public PacketOrderF(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if ((event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY || event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT || event.getPacketType() == PacketType.Play.Client.USE_ITEM || event.getPacketType() == PacketType.Play.Client.PICK_ITEM || event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING || event.getPacketType() == PacketType.Play.Client.CLIENT_STATUS && new WrapperPlayClientClientStatus(event).getAction() == WrapperPlayClientClientStatus.Action.OPEN_INVENTORY_ACHIEVEMENT) && (this.player.packetOrderProcessor.isSprinting() || this.player.packetOrderProcessor.isSneaking())) {
            String verbose = "action=" + (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY ? "interact" : (event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT ? "place" : (event.getPacketType() == PacketType.Play.Client.USE_ITEM ? "use" : (event.getPacketType() == PacketType.Play.Client.PICK_ITEM ? "pick" : (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING ? "dig" : "openInventory"))))) + ", sprinting=" + this.player.packetOrderProcessor.isSprinting() + ", sneaking=" + this.player.packetOrderProcessor.isSneaking();
            if (!this.player.canSkipTicks()) {
                if (this.flagAndAlert(verbose) && this.shouldModifyPackets()) {
                    if (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING && new WrapperPlayClientPlayerDigging(event).getAction() == DiggingAction.RELEASE_USE_ITEM) {
                        return;
                    }
                    event.setCancelled(true);
                    this.player.onPacketCancel();
                }
            } else {
                this.flags.add(verbose);
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

