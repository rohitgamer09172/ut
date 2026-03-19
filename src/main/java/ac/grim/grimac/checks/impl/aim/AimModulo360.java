/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.aim;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.RotationCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.RotationUpdate;

@CheckData(name="AimModulo360", decay=0.005)
public class AimModulo360
extends Check
implements RotationCheck {
    private float lastDeltaYaw;

    public AimModulo360(GrimPlayer playerData) {
        super(playerData);
    }

    @Override
    public void process(RotationUpdate rotationUpdate) {
        if (this.player.packetStateData.lastPacketWasTeleport || this.player.vehicleData.wasVehicleSwitch || this.player.packetStateData.horseInteractCausedForcedRotation) {
            this.lastDeltaYaw = rotationUpdate.getDeltaXRot();
            return;
        }
        if (this.player.yaw < 360.0f && this.player.yaw > -360.0f && Math.abs(rotationUpdate.getDeltaXRot()) > 320.0f && Math.abs(this.lastDeltaYaw) < 30.0f) {
            this.flagAndAlert();
        } else {
            this.reward();
        }
        this.lastDeltaYaw = rotationUpdate.getDeltaXRot();
    }
}

