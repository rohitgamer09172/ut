/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.multiactions;

import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockPlaceCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.utils.anticheat.update.BlockBreak;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import java.util.ArrayList;
import java.util.List;

@CheckData(name="MultiActionsF", description="Interacting with a block and an entity in the same tick", experimental=true)
public class MultiActionsF
extends BlockPlaceCheck {
    private final List<String> flags = new ArrayList<String>();
    private boolean entity;
    private boolean block;

    public MultiActionsF(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onBlockPlace(BlockPlace place) {
        this.block = true;
        if (this.entity) {
            if (!this.player.canSkipTicks()) {
                if (this.flagAndAlert("place") && this.shouldModifyPackets() && this.shouldCancel()) {
                    place.resync();
                }
            } else {
                this.flags.add("place");
            }
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            this.entity = true;
            if (this.block) {
                if (!this.player.canSkipTicks()) {
                    if (this.flagAndAlert("entity") && this.shouldModifyPackets()) {
                        event.setCancelled(true);
                        this.player.onPacketCancel();
                    }
                } else {
                    this.flags.add("entity");
                }
            }
        }
        if (this.isTickPacket(event.getPacketType())) {
            this.entity = false;
            this.block = false;
        }
    }

    @Override
    public void onBlockBreak(BlockBreak blockBreak) {
        if (blockBreak.action == DiggingAction.START_DIGGING || blockBreak.action == DiggingAction.FINISHED_DIGGING) {
            this.block = true;
            if (this.entity) {
                if (!this.player.canSkipTicks()) {
                    if (this.flagAndAlert("dig") && this.shouldModifyPackets()) {
                        blockBreak.cancel();
                    }
                } else {
                    this.flags.add("dig");
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

