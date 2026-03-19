/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.scaffolding;

import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockPlaceCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.math.VectorUtils;

@CheckData(name="FarPlace", description="Placing blocks from too far away")
public class FarPlace
extends BlockPlaceCheck {
    public FarPlace(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onBlockPlace(BlockPlace place) {
        double[] possibleEyeHeights;
        if (!this.player.cameraEntity.isSelf() || this.player.inVehicle()) {
            return;
        }
        Vector3i blockPos = place.position;
        if (place.material == StateTypes.SCAFFOLDING) {
            return;
        }
        double min = Double.MAX_VALUE;
        for (double d : possibleEyeHeights = this.player.getPossibleEyeHeights()) {
            SimpleCollisionBox box = new SimpleCollisionBox(blockPos);
            Vector3dm best = VectorUtils.cutBoxToVector(this.player.x, this.player.y + d, this.player.z, box);
            min = Math.min(min, best.distanceSquared(this.player.x, this.player.y + d, this.player.z));
        }
        double maxReach = this.player.compensatedEntities.self.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE);
        double threshold = this.player.getMovementThreshold();
        if (min > (maxReach += Math.hypot(threshold, threshold)) * maxReach && this.flagAndAlert() && this.shouldModifyPackets() && this.shouldCancel()) {
            place.resync();
        }
    }
}

