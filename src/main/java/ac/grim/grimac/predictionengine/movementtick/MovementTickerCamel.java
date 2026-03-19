/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.predictionengine.movementtick;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.movementtick.MovementTickerHorse;
import ac.grim.grimac.utils.data.packetentity.PacketEntityCamel;

public class MovementTickerCamel
extends MovementTickerHorse {
    public MovementTickerCamel(GrimPlayer player) {
        super(player);
    }

    @Override
    public float getExtraSpeed() {
        boolean wantsToJump;
        PacketEntityCamel camel = (PacketEntityCamel)this.player.compensatedEntities.self.getRiding();
        boolean bl = wantsToJump = camel.getJumpPower() > 0.0f && !camel.isJumping() && this.player.lastOnGround;
        if (wantsToJump) {
            return 0.0f;
        }
        return this.player.isSprinting && camel.getDashCooldown() <= 0 && !camel.isDashing() ? 0.1f : 0.0f;
    }
}

