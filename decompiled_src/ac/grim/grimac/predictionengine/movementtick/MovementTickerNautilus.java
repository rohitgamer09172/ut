/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.predictionengine.movementtick;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.movementtick.MovementTickerLivingVehicle;
import ac.grim.grimac.predictionengine.predictions.rideable.PredictionEngineNautilusWater;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.utils.data.packetentity.PacketEntityNautilus;
import ac.grim.grimac.utils.math.Vector3dm;

public class MovementTickerNautilus
extends MovementTickerLivingVehicle {
    public MovementTickerNautilus(GrimPlayer player) {
        super(player);
        PacketEntityNautilus nautilus = (PacketEntityNautilus)player.compensatedEntities.self.getRiding();
        if (!nautilus.hasSaddle()) {
            return;
        }
        player.speed = this.getRiddenSpeed(player);
        float sideways = player.vehicleData.vehicleHorizontal;
        float forward = 0.0f;
        float upAndDown = 0.0f;
        if (player.vehicleData.vehicleForward != 0.0f) {
            float xRot = player.pitch * 2.0f;
            float calcForward = player.trigHandler.cos(xRot * ((float)Math.PI / 180));
            float calcUpAndDown = -player.trigHandler.sin(xRot * ((float)Math.PI / 180));
            if (player.vehicleData.vehicleForward < 0.0f) {
                calcForward *= -0.5f;
                calcUpAndDown *= -0.5f;
            }
            upAndDown = calcUpAndDown;
            forward = calcForward;
        }
        this.movementInput = new Vector3dm(sideways, upAndDown, forward);
        if (this.movementInput.lengthSquared() > 1.0) {
            this.movementInput.normalize();
        }
    }

    @Override
    public void doWaterMove(float swimSpeed, boolean isFalling, float swimFriction) {
        new PredictionEngineNautilusWater(this.movementInput, 0.9).guessBestMovement(this.getRiddenSpeed(this.player), this.player);
    }

    public float getRiddenSpeed(GrimPlayer player) {
        PacketEntityNautilus nautilus = (PacketEntityNautilus)player.compensatedEntities.self.getRiding();
        return player.wasTouchingWater ? 0.0325f * (float)nautilus.getAttributeValue(Attributes.MOVEMENT_SPEED) : 0.02f * (float)nautilus.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }
}

