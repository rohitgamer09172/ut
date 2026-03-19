/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.predictionengine.predictions;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.predictions.PredictionEngine;
import ac.grim.grimac.predictionengine.predictions.PredictionEngineNormal;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.nmsutil.ReachUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PredictionEngineElytra
extends PredictionEngine {
    public static Vector3dm getElytraMovement(GrimPlayer player, Vector3dm vector, Vector3dm lookVector) {
        double d5;
        float pitchRadians = GrimMath.radians(player.pitch);
        double horizontalSqrt = Math.sqrt(lookVector.getX() * lookVector.getX() + lookVector.getZ() * lookVector.getZ());
        double horizontalLength = vector.clone().setY(0).length();
        double length = lookVector.length();
        double vertCosRotation = player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_18_2) ? Math.cos(pitchRadians) : (double)player.trigHandler.cos(pitchRadians);
        vertCosRotation = (float)(vertCosRotation * vertCosRotation * Math.min(1.0, length / 0.4));
        double recalculatedGravity = player.compensatedEntities.self.getAttributeValue(Attributes.GRAVITY);
        if (player.clientVelocity.getY() <= 0.0 && player.compensatedEntities.getSlowFallingAmplifier().isPresent()) {
            recalculatedGravity = player.getClientVersion().isOlderThan(ClientVersion.V_1_20_5) ? 0.01 : Math.min(recalculatedGravity, 0.01);
        }
        vector.add(0.0, recalculatedGravity * (-1.0 + vertCosRotation * 0.75), 0.0);
        if (vector.getY() < 0.0 && horizontalSqrt > 0.0) {
            d5 = vector.getY() * -0.1 * vertCosRotation;
            vector.add(lookVector.getX() * d5 / horizontalSqrt, d5, lookVector.getZ() * d5 / horizontalSqrt);
        }
        if (pitchRadians < 0.0f && horizontalSqrt > 0.0) {
            d5 = horizontalLength * (double)(-player.trigHandler.sin(pitchRadians)) * 0.04;
            vector.add(-lookVector.getX() * d5 / horizontalSqrt, d5 * 3.2, -lookVector.getZ() * d5 / horizontalSqrt);
        }
        if (horizontalSqrt > 0.0) {
            vector.add((lookVector.getX() / horizontalSqrt * horizontalLength - vector.getX()) * 0.1, 0.0, (lookVector.getZ() / horizontalSqrt * horizontalLength - vector.getZ()) * 0.1);
        }
        return vector;
    }

    @Override
    public List<VectorData> applyInputsToVelocityPossibilities(GrimPlayer player, Set<VectorData> possibleVectors, float speed) {
        ArrayList<VectorData> results = new ArrayList<VectorData>();
        for (int shitmath = 0; shitmath <= 1; ++shitmath) {
            Vector3dm currentLook = ReachUtils.getLook(player, player.yaw, player.pitch);
            for (int applyStuckSpeed = 1; !(applyStuckSpeed < 0 || applyStuckSpeed == 0 && player.isForceStuckSpeed()); --applyStuckSpeed) {
                for (VectorData data : possibleVectors) {
                    Vector3dm elytraResult = PredictionEngineElytra.getElytraMovement(player, data.vector.clone(), currentLook);
                    if (applyStuckSpeed != 0) {
                        elytraResult.multiply(player.stuckSpeedMultiplier);
                    }
                    elytraResult.multiply(0.99f, 0.98f, 0.99f);
                    VectorData modified = data.returnNewModified(elytraResult, VectorData.VectorType.InputResult);
                    modified.input = new Vector3dm(0, 0, 0);
                    results.add(modified);
                }
            }
            player.trigHandler.toggleShitMath();
        }
        return results;
    }

    @Override
    public void addJumpsToPossibilities(GrimPlayer player, Set<VectorData> existingVelocities) {
        new PredictionEngineNormal().addJumpsToPossibilities(player, existingVelocities);
    }
}

