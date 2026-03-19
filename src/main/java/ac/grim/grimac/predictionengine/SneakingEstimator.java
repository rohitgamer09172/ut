/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.predictionengine;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.VectorData;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;

public class SneakingEstimator
extends Check
implements PostPredictionCheck {
    private SimpleCollisionBox sneakingPotentialHiddenVelocity = new SimpleCollisionBox();
    private List<VectorData> possible = new ArrayList<VectorData>();

    public SneakingEstimator(GrimPlayer player) {
        super(player);
    }

    public void storePossibleVelocities(List<VectorData> possible) {
        this.possible = possible;
    }

    @Override
    public void onPredictionComplete(PredictionComplete predictionComplete) {
        double trueFriction;
        if (!predictionComplete.isChecked()) {
            return;
        }
        double d = trueFriction = this.player.lastOnGround ? (double)this.player.friction * 0.91 : 0.91;
        if (this.player.wasTouchingLava) {
            trueFriction = 0.5;
        }
        if (this.player.wasTouchingWater) {
            trueFriction = 0.96;
        }
        if (this.player.isGliding) {
            trueFriction = 0.99;
        }
        if (!this.player.uncertaintyHandler.stuckOnEdge.hasOccurredSince(0)) {
            this.sneakingPotentialHiddenVelocity = new SimpleCollisionBox();
            return;
        }
        for (VectorData data : this.possible) {
            if (data.isJump() != this.player.predictedVelocity.isJump() || data.isKnockback() != this.player.predictedVelocity.isKnockback() || data.isExplosion() != this.player.predictedVelocity.isExplosion()) continue;
            if (this.player.uncertaintyHandler.lastStuckWest.hasOccurredSince(0) || this.player.uncertaintyHandler.lastStuckNorth.hasOccurredSince(0)) {
                this.sneakingPotentialHiddenVelocity.minX = Math.min(this.sneakingPotentialHiddenVelocity.minX, data.vector.getX());
                this.sneakingPotentialHiddenVelocity.minZ = Math.min(this.sneakingPotentialHiddenVelocity.minZ, data.vector.getZ());
            }
            if (!this.player.uncertaintyHandler.lastStuckEast.hasOccurredSince(0) && !this.player.uncertaintyHandler.lastStuckSouth.hasOccurredSince(0)) continue;
            this.sneakingPotentialHiddenVelocity.maxX = Math.max(this.sneakingPotentialHiddenVelocity.maxX, data.vector.getX());
            this.sneakingPotentialHiddenVelocity.maxZ = Math.max(this.sneakingPotentialHiddenVelocity.maxZ, data.vector.getZ());
        }
        this.sneakingPotentialHiddenVelocity.minX *= trueFriction;
        this.sneakingPotentialHiddenVelocity.minZ *= trueFriction;
        this.sneakingPotentialHiddenVelocity.maxX *= trueFriction;
        this.sneakingPotentialHiddenVelocity.maxZ *= trueFriction;
        this.sneakingPotentialHiddenVelocity.minX = Math.min(-0.15, this.sneakingPotentialHiddenVelocity.minX);
        this.sneakingPotentialHiddenVelocity.minZ = Math.min(-0.15, this.sneakingPotentialHiddenVelocity.minZ);
        this.sneakingPotentialHiddenVelocity.maxX = Math.max(0.15, this.sneakingPotentialHiddenVelocity.maxX);
        this.sneakingPotentialHiddenVelocity.maxZ = Math.max(0.15, this.sneakingPotentialHiddenVelocity.maxZ);
        if (!this.player.uncertaintyHandler.lastStuckEast.hasOccurredSince(0)) {
            this.sneakingPotentialHiddenVelocity.maxX = 0.0;
        }
        if (!this.player.uncertaintyHandler.lastStuckWest.hasOccurredSince(0)) {
            this.sneakingPotentialHiddenVelocity.minX = 0.0;
        }
        if (!this.player.uncertaintyHandler.lastStuckNorth.hasOccurredSince(0)) {
            this.sneakingPotentialHiddenVelocity.minZ = 0.0;
        }
        if (!this.player.uncertaintyHandler.lastStuckSouth.hasOccurredSince(0)) {
            this.sneakingPotentialHiddenVelocity.maxZ = 0.0;
        }
    }

    @Generated
    public SimpleCollisionBox getSneakingPotentialHiddenVelocity() {
        return this.sneakingPotentialHiddenVelocity;
    }
}

