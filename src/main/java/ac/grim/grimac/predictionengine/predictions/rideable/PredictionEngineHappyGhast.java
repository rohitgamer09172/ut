/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.predictionengine.predictions.rideable;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.predictions.PredictionEngineNormal;
import ac.grim.grimac.predictionengine.predictions.rideable.PredictionEngineRideableUtils;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import java.util.List;
import java.util.Set;
import lombok.Generated;

public class PredictionEngineHappyGhast
extends PredictionEngineNormal {
    private final Vector3dm movementVector;
    private final double multiplier;

    @Override
    public void endOfTick(GrimPlayer player, double delta) {
        for (VectorData vector : player.getPossibleVelocitiesMinusKnockback()) {
            vector.vector.setX(vector.vector.getX() * this.multiplier);
            vector.vector.setY(vector.vector.getY() * this.multiplier);
            vector.vector.setZ(vector.vector.getZ() * this.multiplier);
        }
    }

    @Override
    public List<VectorData> applyInputsToVelocityPossibilities(GrimPlayer player, Set<VectorData> possibleVectors, float speed) {
        return PredictionEngineRideableUtils.applyInputsToVelocityPossibilities(this, this.movementVector, player, possibleVectors, speed);
    }

    @Override
    public Vector3dm getMovementResultFromInput(GrimPlayer player, Vector3dm inputVector, float flyingSpeed, float yRot) {
        float yRotRadians = GrimMath.radians(yRot);
        float sin = player.trigHandler.sin(yRotRadians);
        float cos = player.trigHandler.cos(yRotRadians);
        double xResult = inputVector.getX() * (double)cos - inputVector.getZ() * (double)sin;
        double zResult = inputVector.getZ() * (double)cos + inputVector.getX() * (double)sin;
        return new Vector3dm(xResult * (double)flyingSpeed, inputVector.getY() * (double)flyingSpeed, zResult * (double)flyingSpeed);
    }

    @Generated
    public PredictionEngineHappyGhast(Vector3dm movementVector, double multiplier) {
        this.movementVector = movementVector;
        this.multiplier = multiplier;
    }
}

