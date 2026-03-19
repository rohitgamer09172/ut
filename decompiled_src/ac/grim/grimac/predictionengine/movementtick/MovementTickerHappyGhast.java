/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.predictionengine.movementtick;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.movementtick.MovementTickerLivingVehicle;
import ac.grim.grimac.predictionengine.predictions.rideable.PredictionEngineHappyGhast;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.utils.data.packetentity.PacketEntityHappyGhast;
import ac.grim.grimac.utils.math.Vector3dm;

public class MovementTickerHappyGhast
extends MovementTickerLivingVehicle {
    public MovementTickerHappyGhast(GrimPlayer player) {
        super(player);
        PacketEntityHappyGhast happyGhastPacket = (PacketEntityHappyGhast)player.compensatedEntities.self.getRiding();
        if (!happyGhastPacket.isControllingPassenger()) {
            return;
        }
        player.speed = (float)happyGhastPacket.getAttributeValue(Attributes.FLYING_SPEED) * 5.0f / 3.0f;
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
        if (player.lastJumping) {
            upAndDown += 0.5f;
        }
        this.movementInput = new Vector3dm(sideways, upAndDown, forward).multiply((double)3.9f * happyGhastPacket.getAttributeValue(Attributes.FLYING_SPEED));
        if (this.movementInput.lengthSquared() > 1.0) {
            this.movementInput.normalize();
        }
    }

    @Override
    public void doNormalMove(float blockFriction) {
        PacketEntityHappyGhast happyGhastPacket = (PacketEntityHappyGhast)this.player.compensatedEntities.self.getRiding();
        float flyingSpeed = (float)happyGhastPacket.getAttributeValue(Attributes.FLYING_SPEED) * 5.0f / 3.0f;
        new PredictionEngineHappyGhast(this.movementInput, 0.91f).guessBestMovement(flyingSpeed, this.player);
    }

    @Override
    public void doLavaMove() {
        PacketEntityHappyGhast happyGhastPacket = (PacketEntityHappyGhast)this.player.compensatedEntities.self.getRiding();
        float flyingSpeed = (float)happyGhastPacket.getAttributeValue(Attributes.FLYING_SPEED) * 5.0f / 3.0f;
        new PredictionEngineHappyGhast(this.movementInput, 0.5).guessBestMovement(flyingSpeed, this.player);
    }

    @Override
    public void doWaterMove(float swimSpeed, boolean isFalling, float swimFriction) {
        PacketEntityHappyGhast happyGhastPacket = (PacketEntityHappyGhast)this.player.compensatedEntities.self.getRiding();
        float flyingSpeed = (float)happyGhastPacket.getAttributeValue(Attributes.FLYING_SPEED) * 5.0f / 3.0f;
        new PredictionEngineHappyGhast(this.movementInput, 0.8f).guessBestMovement(flyingSpeed, this.player);
    }
}

