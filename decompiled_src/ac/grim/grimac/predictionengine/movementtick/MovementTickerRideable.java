/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.predictionengine.movementtick;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.movementtick.MovementTickerLivingVehicle;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.utils.data.packetentity.PacketEntityRideable;
import ac.grim.grimac.utils.nmsutil.Collisions;

public class MovementTickerRideable
extends MovementTickerLivingVehicle {
    public MovementTickerRideable(GrimPlayer player) {
        super(player);
        float f = this.getSteeringSpeed();
        PacketEntityRideable boost = (PacketEntityRideable)player.compensatedEntities.self.getRiding();
        if (boost.currentBoostTime++ < boost.boostTimeMax) {
            f += f * 1.15f * player.trigHandler.sin((float)boost.currentBoostTime / (float)boost.boostTimeMax * (float)Math.PI);
        }
        player.speed = f;
    }

    public float getSteeringSpeed() {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public void livingEntityTravel() {
        super.livingEntityTravel();
        if (this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_17)) {
            Collisions.handleInsideBlocks(this.player);
        }
    }
}

