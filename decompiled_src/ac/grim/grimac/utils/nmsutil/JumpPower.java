/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.nmsutil.BlockProperties;
import java.util.OptionalInt;
import lombok.Generated;

public final class JumpPower {
    public static void jumpFromGround(@NotNull GrimPlayer player, @NotNull Vector3dm vector) {
        float jumpPower = JumpPower.getJumpPower(player);
        OptionalInt jumpBoost = player.compensatedEntities.getPotionLevelForPlayer(PotionTypes.JUMP_BOOST);
        if (jumpBoost.isPresent()) {
            jumpPower += 0.1f * (float)(jumpBoost.getAsInt() + 1);
        }
        if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_20_5) && jumpPower <= 1.0E-5f) {
            return;
        }
        vector.setY(player.getClientVersion().isOlderThan(ClientVersion.V_1_21_2) ? (double)jumpPower : Math.max((double)jumpPower, vector.getY()));
        if (player.isSprinting) {
            float radRotation = GrimMath.radians(player.yaw);
            if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
                vector.add((double)(-player.trigHandler.sin(radRotation)) * 0.2, 0.0, (double)player.trigHandler.cos(radRotation) * 0.2);
            } else {
                vector.add(-player.trigHandler.sin(radRotation) * 0.2f, 0.0, player.trigHandler.cos(radRotation) * 0.2f);
            }
        }
    }

    public static float getJumpPower(@NotNull GrimPlayer player) {
        return (float)player.compensatedEntities.self.getAttributeValue(Attributes.JUMP_STRENGTH) * JumpPower.getPlayerJumpFactor(player);
    }

    public static float getPlayerJumpFactor(@NotNull GrimPlayer player) {
        return BlockProperties.onHoneyBlock(player, player.mainSupportingBlockData, new Vector3d(player.lastX, player.lastY, player.lastZ)) ? 0.5f : 1.0f;
    }

    @Generated
    private JumpPower() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

