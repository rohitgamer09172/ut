/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.block.BlockFace
 *  org.bukkit.permissions.PermissionDefault
 */
package ac.grim.grimac.platform.bukkit.utils.convert;

import ac.grim.grimac.platform.api.permissions.PermissionDefaultValue;
import ac.grim.grimac.platform.bukkit.world.BukkitPlatformWorld;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.utils.math.Location;
import org.bukkit.permissions.PermissionDefault;

public class BukkitConversionUtils {
    @Contract(value="null -> null; !null -> new")
    public static org.bukkit.Location toBukkitLocation(Location location) {
        if (location == null) {
            return null;
        }
        return new org.bukkit.Location(((BukkitPlatformWorld)location.getWorld()).getBukkitWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    @Contract(value="null -> null; !null -> !null", pure=true)
    @Nullable
    public static PermissionDefault toBukkitPermissionDefault(@Nullable PermissionDefaultValue permissionDefaultValue) {
        if (permissionDefaultValue == null) {
            return null;
        }
        return switch (permissionDefaultValue) {
            default -> throw new IncompatibleClassChangeError();
            case PermissionDefaultValue.TRUE -> PermissionDefault.TRUE;
            case PermissionDefaultValue.FALSE -> PermissionDefault.FALSE;
            case PermissionDefaultValue.OP -> PermissionDefault.OP;
            case PermissionDefaultValue.NOT_OP -> PermissionDefault.NOT_OP;
        };
    }

    public static BlockFace fromBukkitFace(org.bukkit.block.BlockFace face) {
        return switch (face) {
            case org.bukkit.block.BlockFace.NORTH -> BlockFace.NORTH;
            case org.bukkit.block.BlockFace.SOUTH -> BlockFace.SOUTH;
            case org.bukkit.block.BlockFace.WEST -> BlockFace.WEST;
            case org.bukkit.block.BlockFace.EAST -> BlockFace.EAST;
            case org.bukkit.block.BlockFace.UP -> BlockFace.UP;
            case org.bukkit.block.BlockFace.DOWN -> BlockFace.DOWN;
            default -> BlockFace.OTHER;
        };
    }
}

