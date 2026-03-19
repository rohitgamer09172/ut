/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import lombok.Generated;

public final class Riptide {
    @Contract(value="_ -> new")
    @NotNull
    public static Vector3dm getRiptideVelocity(@NotNull GrimPlayer player) {
        int riptideLevel;
        ItemStack main = player.inventory.getHeldItem();
        ItemStack off = player.inventory.getOffHand();
        if (main.getType() == ItemTypes.TRIDENT) {
            riptideLevel = main.getEnchantmentLevel(EnchantmentTypes.RIPTIDE);
        } else if (off.getType() == ItemTypes.TRIDENT) {
            riptideLevel = off.getEnchantmentLevel(EnchantmentTypes.RIPTIDE);
        } else {
            return new Vector3dm();
        }
        float yaw = GrimMath.radians(player.yaw);
        float pitch = GrimMath.radians(player.pitch);
        float pitchCos = player.trigHandler.cos(pitch);
        float x = -player.trigHandler.sin(yaw) * pitchCos;
        float y = -player.trigHandler.sin(pitch);
        float z = player.trigHandler.cos(yaw) * pitchCos;
        float multiplier = 3.0f * ((1.0f + (float)riptideLevel) / 4.0f) / (float)Math.sqrt(x * x + y * y + z * z);
        return new Vector3dm(x * multiplier, player.verticalCollision ? 0.0f : y * multiplier, z * multiplier);
    }

    @Generated
    private Riptide() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

