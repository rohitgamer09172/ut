/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.predictionengine.predictions;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.predictions.PredictionEngine;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.nmsutil.Collisions;
import ac.grim.grimac.utils.nmsutil.JumpPower;
import java.util.HashSet;
import java.util.OptionalInt;
import java.util.Set;

public class PredictionEngineNormal
extends PredictionEngine {
    public static void staticVectorEndOfTick(GrimPlayer player, Vector3dm vector) {
        double adjustedY = vector.getY();
        OptionalInt levitation = player.compensatedEntities.getPotionLevelForPlayer(PotionTypes.LEVITATION);
        if (levitation.isPresent()) {
            adjustedY += (0.05 * (double)(levitation.getAsInt() + 1) - vector.getY()) * 0.2;
            player.fallDistance = 0.0;
        } else if (player.hasGravity) {
            adjustedY -= player.gravity;
        }
        vector.setX(vector.getX() * (double)player.friction);
        vector.setY(adjustedY * (double)0.98f);
        vector.setZ(vector.getZ() * (double)player.friction);
    }

    @Override
    public void addJumpsToPossibilities(GrimPlayer player, Set<VectorData> existingVelocities) {
        if (player.supportsEndTick() && !player.packetStateData.knownInput.jump()) {
            return;
        }
        for (VectorData vector : new HashSet<VectorData>(existingVelocities)) {
            Vector3dm jump = vector.vector.clone();
            if (!player.isFlying) {
                OptionalInt jumpBoost = player.compensatedEntities.getPotionLevelForPlayer(PotionTypes.JUMP_BOOST);
                if ((jumpBoost.isEmpty() || jumpBoost.getAsInt() >= 0) && player.onGround || !player.lastOnGround) {
                    return;
                }
                JumpPower.jumpFromGround(player, jump);
            } else {
                jump.add(0.0, player.flySpeed * 3.0f, 0.0);
                if (!player.wasFlying) {
                    Vector3dm edgeCaseJump = jump.clone();
                    JumpPower.jumpFromGround(player, edgeCaseJump);
                    existingVelocities.add(vector.returnNewModified(edgeCaseJump, VectorData.VectorType.Jump));
                }
            }
            existingVelocities.add(vector.returnNewModified(jump, VectorData.VectorType.Jump));
        }
    }

    @Override
    public void endOfTick(GrimPlayer player, double delta) {
        super.endOfTick(player, delta);
        boolean walkingOnPowderSnow = false;
        if (!player.inVehicle() && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_17) && player.compensatedWorld.getBlockType(player.x, player.y, player.z) == StateTypes.POWDER_SNOW) {
            ItemStack boots = player.inventory.getBoots();
            walkingOnPowderSnow = boots != null && boots.getType() == ItemTypes.LEATHER_BOOTS;
        }
        player.isClimbing = Collisions.onClimbable(player, player.x, player.y, player.z);
        if (player.lastWasClimbing == 0.0 && (player.pointThreeEstimator.isNearClimbable() || player.isClimbing) && (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14) || !Collisions.isEmpty(player, player.boundingBox.copy().expand(player.clientVelocity.getX(), 0.0, player.clientVelocity.getZ()).expand(0.5, -1.0E-7, 0.5))) || walkingOnPowderSnow) {
            Vector3dm ladderVelocity = player.clientVelocity.clone().setY(0.2);
            PredictionEngineNormal.staticVectorEndOfTick(player, ladderVelocity);
            player.lastWasClimbing = ladderVelocity.getY();
        }
        for (VectorData vector : player.getPossibleVelocitiesMinusKnockback()) {
            PredictionEngineNormal.staticVectorEndOfTick(player, vector.vector);
        }
    }

    @Override
    public Vector3dm handleOnClimbable(Vector3dm vector, GrimPlayer player) {
        if (player.isClimbing) {
            player.fallDistance = 0.0;
            vector.setX(GrimMath.clamp(vector.getX(), (double)-0.15f, (double)0.15f));
            vector.setZ(GrimMath.clamp(vector.getZ(), (double)-0.15f, (double)0.15f));
            vector.setY(Math.max(vector.getY(), (double)-0.15f));
            if (vector.getY() < 0.0 && player.compensatedWorld.getBlockType(player.lastX, player.lastY, player.lastZ) != StateTypes.SCAFFOLDING && player.isSneaking && !player.isFlying) {
                vector.setY(0.0);
            }
        }
        return vector;
    }
}

