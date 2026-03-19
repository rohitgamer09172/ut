/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.predictionengine;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;

public class GhostBlockDetector
extends Check
implements PostPredictionCheck {
    public GhostBlockDetector(GrimPlayer player) {
        super(player);
    }

    public static boolean isGhostBlock(GrimPlayer player) {
        if (player.uncertaintyHandler.isOrWasNearGlitchyBlock) {
            return true;
        }
        if (player.getClientVersion().isOlderThan(ClientVersion.V_1_9)) {
            SimpleCollisionBox largeExpandedBB = player.boundingBox.copy().expand(12.0, 0.5, 12.0);
            for (PacketEntity entity : player.compensatedEntities.entityMap.values()) {
                if (!entity.isBoat || !entity.getPossibleCollisionBoxes().isIntersected(largeExpandedBB)) continue;
                return true;
            }
        }
        return false;
    }

    @Override
    public void onPredictionComplete(PredictionComplete predictionComplete) {
        if (predictionComplete.getOffset() < 0.001 && (this.player.clientClaimsLastOnGround == this.player.onGround || this.player.inVehicle())) {
            return;
        }
        boolean shouldResync = GhostBlockDetector.isGhostBlock(this.player);
        if (shouldResync) {
            if (this.player.clientClaimsLastOnGround != this.player.onGround) {
                this.player.onGround = this.player.clientClaimsLastOnGround;
            }
            predictionComplete.setOffset(0.0);
            this.player.getSetbackTeleportUtil().executeForceResync();
        }
    }
}

