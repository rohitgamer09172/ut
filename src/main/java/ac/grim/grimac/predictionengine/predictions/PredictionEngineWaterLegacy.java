/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.predictionengine.predictions;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.predictions.PredictionEngine;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import java.util.HashSet;
import java.util.Set;

public class PredictionEngineWaterLegacy
extends PredictionEngine {
    private float swimmingSpeed;
    private float swimmingFriction;

    public void guessBestMovement(float swimmingSpeed, GrimPlayer player, float swimmingFriction) {
        this.swimmingSpeed = swimmingSpeed;
        this.swimmingFriction = swimmingFriction;
        super.guessBestMovement(swimmingSpeed, player);
    }

    @Override
    public Vector3dm getMovementResultFromInput(GrimPlayer player, Vector3dm inputVector, float f, float f2) {
        float lengthSquared = (float)inputVector.lengthSquared();
        if (lengthSquared >= 1.0E-4f) {
            if ((lengthSquared = (float)Math.sqrt(lengthSquared)) < 1.0f) {
                lengthSquared = 1.0f;
            }
            lengthSquared = this.swimmingSpeed / lengthSquared;
            inputVector.multiply(lengthSquared);
            float yawRadians = GrimMath.radians(player.yaw);
            float sinResult = player.trigHandler.sin(yawRadians);
            float cosResult = player.trigHandler.cos(yawRadians);
            return new Vector3dm(inputVector.getX() * (double)cosResult - inputVector.getZ() * (double)sinResult, inputVector.getY(), inputVector.getZ() * (double)cosResult + inputVector.getX() * (double)sinResult);
        }
        return new Vector3dm();
    }

    @Override
    public void addJumpsToPossibilities(GrimPlayer player, Set<VectorData> existingVelocities) {
        for (VectorData vector : new HashSet<VectorData>(existingVelocities)) {
            existingVelocities.add(new VectorData(vector.vector.clone().add(0.0, 0.04f, 0.0), vector, VectorData.VectorType.Jump));
            if (!player.skippedTickInActualMovement) continue;
            existingVelocities.add(new VectorData(vector.vector.clone().add(0.0, 0.02f, 0.0), vector, VectorData.VectorType.Jump));
        }
    }

    @Override
    public void endOfTick(GrimPlayer player, double playerGravity) {
        super.endOfTick(player, playerGravity);
        for (VectorData vector : player.getPossibleVelocitiesMinusKnockback()) {
            vector.vector.multiply(this.swimmingFriction, 0.8f, this.swimmingFriction);
            vector.vector.setY(vector.vector.getY() - 0.02);
        }
    }
}

