/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.prediction;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.nmsutil.Collisions;
import java.util.ArrayList;

@CheckData(name="Phase", setback=1.0, decay=0.005)
public class Phase
extends Check
implements PostPredictionCheck {
    private SimpleCollisionBox oldBB;

    public Phase(GrimPlayer player) {
        super(player);
        this.oldBB = player.boundingBox;
    }

    @Override
    public void onPredictionComplete(PredictionComplete predictionComplete) {
        if (!this.player.getSetbackTeleportUtil().blockOffsets && !predictionComplete.getData().isTeleport() && predictionComplete.isChecked()) {
            SimpleCollisionBox newBB = this.player.boundingBox;
            ArrayList<SimpleCollisionBox> boxes = new ArrayList<SimpleCollisionBox>();
            Collisions.getCollisionBoxes(this.player, newBB, boxes, false);
            for (SimpleCollisionBox box : boxes) {
                WrappedBlockState state;
                if (!newBB.isIntersected(box) || this.oldBB.isIntersected(box) || this.player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_8) && (BlockTags.ANVIL.contains((state = this.player.compensatedWorld.getBlock((box.minX + box.maxX) / 2.0, (box.minY + box.maxY) / 2.0, (box.minZ + box.maxZ) / 2.0)).getType()) || state.getType() == StateTypes.CHEST || state.getType() == StateTypes.TRAPPED_CHEST)) continue;
                this.flagAndAlertWithSetback();
                return;
            }
        }
        this.oldBB = this.player.boundingBox;
        this.reward();
    }
}

