/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.blockstate.helper;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import lombok.Generated;

public final class BlockFaceHelper {
    @Contract(pure=true)
    public static boolean isFaceVertical(@Nullable BlockFace face) {
        return face == BlockFace.UP || face == BlockFace.DOWN;
    }

    @Contract(pure=true)
    public static boolean isFaceHorizontal(@Nullable BlockFace face) {
        return face == BlockFace.NORTH || face == BlockFace.EAST || face == BlockFace.SOUTH || face == BlockFace.WEST;
    }

    @Contract(pure=true)
    public static BlockFace getClockWise(@NotNull BlockFace face) {
        return switch (face) {
            case BlockFace.NORTH -> BlockFace.EAST;
            case BlockFace.SOUTH -> BlockFace.WEST;
            case BlockFace.WEST -> BlockFace.NORTH;
            default -> BlockFace.SOUTH;
        };
    }

    @Contract(pure=true)
    public static BlockFace getPEClockWise(@NotNull BlockFace face) {
        return switch (face) {
            case BlockFace.NORTH -> BlockFace.EAST;
            case BlockFace.SOUTH -> BlockFace.WEST;
            case BlockFace.WEST -> BlockFace.NORTH;
            default -> BlockFace.SOUTH;
        };
    }

    @Contract(pure=true)
    public static BlockFace getCounterClockwise(@NotNull BlockFace face) {
        return switch (face) {
            case BlockFace.NORTH -> BlockFace.WEST;
            case BlockFace.SOUTH -> BlockFace.EAST;
            case BlockFace.WEST -> BlockFace.SOUTH;
            default -> BlockFace.NORTH;
        };
    }

    @Generated
    private BlockFaceHelper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

