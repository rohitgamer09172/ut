/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.combat;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3f;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import java.util.ArrayList;

@CheckData(name="MultiInteractB", experimental=true)
public class MultiInteractB
extends Check
implements PostPredictionCheck {
    private final ArrayList<String> flags = new ArrayList();
    private Vector3f lastPos;
    private boolean hasInteracted = false;

    public MultiInteractB(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            Vector3f pos = new WrapperPlayClientInteractEntity(event).getTarget().orElse(null);
            if (pos == null) {
                return;
            }
            if (this.hasInteracted && !pos.equals(this.lastPos)) {
                String verbose = "pos=" + MessageUtil.toUnlabledString(pos) + ", lastPos=" + MessageUtil.toUnlabledString(this.lastPos);
                if (!this.player.canSkipTicks()) {
                    if (this.flagAndAlert(verbose) && this.shouldModifyPackets()) {
                        event.setCancelled(true);
                        this.player.onPacketCancel();
                    }
                } else {
                    this.flags.add(verbose);
                }
            }
            this.lastPos = pos;
            this.hasInteracted = true;
        }
        if (!this.player.cameraEntity.isSelf() || this.isTickPacket(event.getPacketType())) {
            this.hasInteracted = false;
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

