/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.predictionengine.predictions.rideable;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.predictions.PredictionEngine;
import ac.grim.grimac.predictionengine.predictions.PredictionEngineNormal;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.utils.data.Pair;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.data.packetentity.JumpableEntity;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import ac.grim.grimac.utils.math.Vector3dm;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Generated;

public final class PredictionEngineRideableUtils {
    public static Set<VectorData> handleJumps(GrimPlayer player, Set<VectorData> possibleVectors) {
        boolean onGround;
        PacketEntity packetEntity = player.compensatedEntities.self.getRiding();
        if (!(packetEntity instanceof JumpableEntity)) {
            return possibleVectors;
        }
        JumpableEntity jumpable = (JumpableEntity)((Object)packetEntity);
        jumpable.executeJump(player, possibleVectors);
        boolean legacyJumpingMechanics = player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_19_3);
        boolean bl = onGround = legacyJumpingMechanics ? player.clientControlledVerticalCollision : player.lastOnGround;
        if (onGround) {
            if (legacyJumpingMechanics) {
                jumpable.setJumpPower(0.0f);
            }
            jumpable.setJumping(false);
        }
        return possibleVectors;
    }

    public static List<VectorData> applyInputsToVelocityPossibilities(Vector3dm movementVector, GrimPlayer player, Set<VectorData> possibleVectors, float speed) {
        return PredictionEngineRideableUtils.applyInputsToVelocityPossibilities(new PredictionEngine(), movementVector, player, possibleVectors, speed);
    }

    public static List<VectorData> applyInputsToVelocityPossibilities(PredictionEngine predictionEngine, Vector3dm movementVector, GrimPlayer player, Set<VectorData> possibleVectors, float speed) {
        ArrayList<VectorData> returnVectors = new ArrayList<VectorData>();
        for (VectorData possibleLastTickOutput : possibleVectors) {
            for (int applyStuckSpeed = 1; !(applyStuckSpeed < 0 || applyStuckSpeed == 0 && player.isForceStuckSpeed()); --applyStuckSpeed) {
                VectorData result = new VectorData(possibleLastTickOutput.vector.clone().add(predictionEngine.getMovementResultFromInput(player, movementVector, speed, player.yaw)), possibleLastTickOutput, VectorData.VectorType.InputResult);
                result.input = new Vector3dm(player.vehicleData.vehicleForward, 0.0f, player.vehicleData.vehicleHorizontal);
                Vector3dm vector = result.vector.clone();
                if (applyStuckSpeed != 0) {
                    vector.multiply(player.stuckSpeedMultiplier);
                }
                result = result.returnNewModified(vector, VectorData.VectorType.StuckMultiplier);
                result = result.returnNewModified(new PredictionEngineNormal().handleOnClimbable(result.vector.clone(), player), VectorData.VectorType.Climbable);
                returnVectors.add(result);
                result = new VectorData(possibleLastTickOutput.vector.clone(), possibleLastTickOutput, VectorData.VectorType.InputResult);
                result.input = new Vector3dm(player.vehicleData.vehicleForward, 0.0f, player.vehicleData.vehicleHorizontal);
                vector = result.vector.clone();
                if (applyStuckSpeed != 0) {
                    vector.multiply(player.stuckSpeedMultiplier);
                }
                result = result.returnNewModified(vector, VectorData.VectorType.StuckMultiplier);
                result = result.returnNewModified(new PredictionEngineNormal().handleOnClimbable(result.vector.clone(), player), VectorData.VectorType.Climbable);
                returnVectors.add(result);
            }
        }
        return returnVectors;
    }

    public static void applyPendingJumps(GrimPlayer player) {
        Pair<Integer, JumpableEntity> pendingJump;
        while ((pendingJump = player.vehicleData.pendingJumps.poll()) != null) {
            JumpableEntity jumpable = pendingJump.second();
            if (!jumpable.canPlayerJump(player)) continue;
            int jumpBoost = pendingJump.first();
            if (jumpBoost < 0) {
                jumpBoost = 0;
            }
            if (jumpBoost >= 90) {
                jumpable.setJumpPower(1.0f);
                continue;
            }
            jumpable.setJumpPower(0.4f + 0.4f * (float)jumpBoost / 90.0f);
        }
    }

    @Generated
    private PredictionEngineRideableUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

