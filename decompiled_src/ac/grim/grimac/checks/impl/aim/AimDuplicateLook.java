/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.aim;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.RotationCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.RotationUpdate;

@CheckData(name="AimDuplicateLook")
public class AimDuplicateLook
extends Check
implements RotationCheck {
    private boolean exempt;

    public AimDuplicateLook(GrimPlayer playerData) {
        super(playerData);
    }

    @Override
    public void process(RotationUpdate rotationUpdate) {
        if (this.player.packetStateData.lastPacketWasTeleport || this.player.packetStateData.lastPacketWasOnePointSeventeenDuplicate || this.player.compensatedEntities.self.getRiding() != null) {
            this.exempt = true;
            return;
        }
        if (this.exempt) {
            this.exempt = false;
            return;
        }
        if (rotationUpdate.getFrom().equals(rotationUpdate.getTo())) {
            this.flagAndAlert();
        }
    }
}

