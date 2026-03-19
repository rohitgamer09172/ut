/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.prediction;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;

@CheckData(name="GroundSpoof", setback=10.0, decay=0.01)
public class GroundSpoof
extends Check
implements PostPredictionCheck {
    public GroundSpoof(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPredictionComplete(PredictionComplete predictionComplete) {
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_8) && this.player.gamemode == GameMode.SPECTATOR) {
            return;
        }
        if (this.player.exemptOnGround() || !predictionComplete.isChecked()) {
            return;
        }
        if (this.player.getSetbackTeleportUtil().blockOffsets) {
            return;
        }
        if (this.player.packetStateData.lastPacketWasTeleport) {
            return;
        }
        if (this.player.clientClaimsLastOnGround != this.player.onGround) {
            this.flagAndAlertWithSetback("claimed " + this.player.clientClaimsLastOnGround);
            this.player.checkManager.getNoFall().flipPlayerGroundStatus = true;
        }
    }
}

