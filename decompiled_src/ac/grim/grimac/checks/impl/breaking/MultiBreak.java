/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.breaking;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockBreakCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import ac.grim.grimac.utils.anticheat.update.BlockBreak;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import java.util.ArrayList;
import java.util.List;

@CheckData(name="MultiBreak", experimental=true)
public class MultiBreak
extends Check
implements BlockBreakCheck {
    private final List<String> flags = new ArrayList<String>();
    private boolean hasBroken;
    private BlockFace lastFace;
    private Vector3i lastPos;

    public MultiBreak(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onBlockBreak(BlockBreak blockBreak) {
        if (blockBreak.action == DiggingAction.CANCELLED_DIGGING) {
            return;
        }
        if (this.hasBroken && (blockBreak.face != this.lastFace || !blockBreak.position.equals(this.lastPos))) {
            String verbose = "face=" + String.valueOf((Object)blockBreak.face) + ", lastFace=" + String.valueOf((Object)this.lastFace) + ", pos=" + MessageUtil.toUnlabledString(blockBreak.position) + ", lastPos=" + MessageUtil.toUnlabledString(this.lastPos);
            if (!this.player.canSkipTicks()) {
                if (this.flagAndAlert(verbose) && this.shouldModifyPackets()) {
                    blockBreak.cancel();
                }
            } else {
                this.flags.add(verbose);
            }
        }
        this.lastFace = blockBreak.face;
        this.lastPos = blockBreak.position;
        this.hasBroken = true;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (!this.player.cameraEntity.isSelf() || this.isTickPacket(event.getPacketType())) {
            this.hasBroken = false;
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

