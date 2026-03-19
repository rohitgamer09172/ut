/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.math;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import lombok.Generated;

public final class VectorUtils {
    @NotNull
    public static Vector3dm cutBoxToVector(@NotNull Vector3dm vectorToCutTo, @NotNull Vector3dm min, @NotNull Vector3dm max) {
        SimpleCollisionBox box = new SimpleCollisionBox(min, max).sort();
        return VectorUtils.cutBoxToVector(vectorToCutTo, box);
    }

    @Contract(value="_, _ -> new")
    @NotNull
    public static Vector3dm cutBoxToVector(@NotNull Vector3dm vectorCutTo, @NotNull SimpleCollisionBox box) {
        return VectorUtils.cutBoxToVector(vectorCutTo.getX(), vectorCutTo.getY(), vectorCutTo.getZ(), box);
    }

    @NotNull
    public static Vector3dm cutBoxToVector(double x, double y, double z, @NotNull SimpleCollisionBox box) {
        return new Vector3dm(GrimMath.clamp(x, box.minX, box.maxX), GrimMath.clamp(y, box.minY, box.maxY), GrimMath.clamp(z, box.minZ, box.maxZ));
    }

    @Contract(value="_ -> new")
    @NotNull
    public static Vector3dm fromVec3d(@NotNull Vector3d vector3d) {
        return new Vector3dm(vector3d.getX(), vector3d.getY(), vector3d.getZ());
    }

    @Contract(value="_ -> new")
    @NotNull
    public static Vector3d clampVector(@NotNull Vector3d toClamp) {
        double x = GrimMath.clamp(toClamp.getX(), -3.0E7, 3.0E7);
        double y = GrimMath.clamp(toClamp.getY(), -2.0E7, 2.0E7);
        double z = GrimMath.clamp(toClamp.getZ(), -3.0E7, 3.0E7);
        return new Vector3d(x, y, z);
    }

    @Generated
    private VectorUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

