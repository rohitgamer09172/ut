/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.Pair;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.math.VectorUtils;
import lombok.Generated;

public final class ReachUtils {
    @Contract(value="_, _, _ -> new")
    public static @NotNull Pair<@Nullable Vector3dm, @Nullable BlockFace> calculateIntercept(@NotNull SimpleCollisionBox self, @NotNull Vector3dm origin, @NotNull Vector3dm end) {
        Vector3dm minX = ReachUtils.getIntermediateWithXValue(origin, end, self.minX);
        Vector3dm maxX = ReachUtils.getIntermediateWithXValue(origin, end, self.maxX);
        Vector3dm minY = ReachUtils.getIntermediateWithYValue(origin, end, self.minY);
        Vector3dm maxY = ReachUtils.getIntermediateWithYValue(origin, end, self.maxY);
        Vector3dm minZ = ReachUtils.getIntermediateWithZValue(origin, end, self.minZ);
        Vector3dm maxZ = ReachUtils.getIntermediateWithZValue(origin, end, self.maxZ);
        if (!ReachUtils.isVecInYZ(self, minX)) {
            minX = null;
        }
        if (!ReachUtils.isVecInYZ(self, maxX)) {
            maxX = null;
        }
        if (!ReachUtils.isVecInXZ(self, minY)) {
            minY = null;
        }
        if (!ReachUtils.isVecInXZ(self, maxY)) {
            maxY = null;
        }
        if (!ReachUtils.isVecInXY(self, minZ)) {
            minZ = null;
        }
        if (!ReachUtils.isVecInXY(self, maxZ)) {
            maxZ = null;
        }
        Vector3dm best = null;
        BlockFace bestFace = null;
        if (minX != null) {
            best = minX;
            bestFace = BlockFace.WEST;
        }
        if (maxX != null && (best == null || origin.distanceSquared(maxX) < origin.distanceSquared(best))) {
            best = maxX;
            bestFace = BlockFace.EAST;
        }
        if (minY != null && (best == null || origin.distanceSquared(minY) < origin.distanceSquared(best))) {
            best = minY;
            bestFace = BlockFace.DOWN;
        }
        if (maxY != null && (best == null || origin.distanceSquared(maxY) < origin.distanceSquared(best))) {
            best = maxY;
            bestFace = BlockFace.UP;
        }
        if (minZ != null && (best == null || origin.distanceSquared(minZ) < origin.distanceSquared(best))) {
            best = minZ;
            bestFace = BlockFace.NORTH;
        }
        if (maxZ != null && (best == null || origin.distanceSquared(maxZ) < origin.distanceSquared(best))) {
            best = maxZ;
            bestFace = BlockFace.SOUTH;
        }
        return new Pair<Vector3dm, BlockFace>(best, bestFace);
    }

    @Nullable
    public static Vector3dm getIntermediateWithXValue(@NotNull Vector3dm self, @NotNull Vector3dm other, double x) {
        double deltaX = other.getX() - self.getX();
        double deltaY = other.getY() - self.getY();
        double deltaZ = other.getZ() - self.getZ();
        if (deltaX * deltaX < (double)1.0E-7f) {
            return null;
        }
        double d3 = (x - self.getX()) / deltaX;
        return d3 >= 0.0 && d3 <= 1.0 ? new Vector3dm(self.getX() + deltaX * d3, self.getY() + deltaY * d3, self.getZ() + deltaZ * d3) : null;
    }

    @Nullable
    public static Vector3dm getIntermediateWithYValue(@NotNull Vector3dm self, @NotNull Vector3dm other, double y) {
        double deltaX = other.getX() - self.getX();
        double deltaY = other.getY() - self.getY();
        double deltaZ = other.getZ() - self.getZ();
        if (deltaY * deltaY < (double)1.0E-7f) {
            return null;
        }
        double d3 = (y - self.getY()) / deltaY;
        return d3 >= 0.0 && d3 <= 1.0 ? new Vector3dm(self.getX() + deltaX * d3, self.getY() + deltaY * d3, self.getZ() + deltaZ * d3) : null;
    }

    @Nullable
    public static Vector3dm getIntermediateWithZValue(@NotNull Vector3dm self, @NotNull Vector3dm other, double z) {
        double deltaX = other.getX() - self.getX();
        double deltaY = other.getY() - self.getY();
        double deltaZ = other.getZ() - self.getZ();
        if (deltaZ * deltaZ < (double)1.0E-7f) {
            return null;
        }
        double d3 = (z - self.getZ()) / deltaZ;
        return d3 >= 0.0 && d3 <= 1.0 ? new Vector3dm(self.getX() + deltaX * d3, self.getY() + deltaY * d3, self.getZ() + deltaZ * d3) : null;
    }

    @Contract(value="_, null -> false")
    private static boolean isVecInYZ(@NotNull SimpleCollisionBox self, @Nullable Vector3dm vec) {
        return vec != null && vec.getY() >= self.minY && vec.getY() <= self.maxY && vec.getZ() >= self.minZ && vec.getZ() <= self.maxZ;
    }

    @Contract(value="_, null -> false")
    private static boolean isVecInXZ(@NotNull SimpleCollisionBox self, @Nullable Vector3dm vec) {
        return vec != null && vec.getX() >= self.minX && vec.getX() <= self.maxX && vec.getZ() >= self.minZ && vec.getZ() <= self.maxZ;
    }

    @Contract(value="_, null -> false")
    private static boolean isVecInXY(@NotNull SimpleCollisionBox self, @Nullable Vector3dm vec) {
        return vec != null && vec.getX() >= self.minX && vec.getX() <= self.maxX && vec.getY() >= self.minY && vec.getY() <= self.maxY;
    }

    @Contract(value="_, _, _ -> new")
    @NotNull
    public static Vector3dm getLook(@NotNull GrimPlayer player, float yaw, float pitch) {
        if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_12_2)) {
            float yawRadians = GrimMath.radians(-yaw) - (float)Math.PI;
            float pitchRadians = GrimMath.radians(-pitch);
            float pitchCos = -player.trigHandler.cos(pitchRadians);
            float x = player.trigHandler.sin(yawRadians);
            float y = player.trigHandler.sin(pitchRadians);
            float z = player.trigHandler.cos(yawRadians);
            return new Vector3dm(x * pitchCos, y, z * pitchCos);
        }
        float pitchRadians = GrimMath.radians(pitch);
        float yawRadians = GrimMath.radians(-yaw);
        float pitchCos = player.trigHandler.cos(pitchRadians);
        float x = player.trigHandler.sin(yawRadians);
        float y = player.trigHandler.sin(pitchRadians);
        float z = player.trigHandler.cos(yawRadians);
        return new Vector3dm(x * pitchCos, -y, z * pitchCos);
    }

    public static boolean isVecInside(@NotNull SimpleCollisionBox self, @NotNull Vector3dm vec) {
        return vec.getX() > self.minX && vec.getX() < self.maxX && vec.getY() > self.minY && vec.getY() < self.maxY && vec.getZ() > self.minZ && vec.getZ() < self.maxZ;
    }

    public static double getMinReachToBox(@NotNull GrimPlayer player, @NotNull SimpleCollisionBox targetBox) {
        double[] possibleEyeHeights;
        double lowest = Double.MAX_VALUE;
        for (double eyes : possibleEyeHeights = player.getPossibleEyeHeights()) {
            Vector3dm closestPoint = VectorUtils.cutBoxToVector(player.x, player.y + eyes, player.z, targetBox);
            lowest = Math.min(lowest, closestPoint.distance(player.x, player.y + eyes, player.z));
        }
        return lowest;
    }

    @Generated
    private ReachUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

