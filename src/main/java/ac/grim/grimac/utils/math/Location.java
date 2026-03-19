/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.math;

import ac.grim.grimac.platform.api.world.PlatformWorld;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import java.lang.ref.WeakReference;
import java.util.Objects;
import lombok.Generated;

public class Location
implements Cloneable {
    @Nullable
    private WeakReference<PlatformWorld> world;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    public Location(PlatformWorld world, double x, double y, double z) {
        this(world, x, y, z, 0.0f, 0.0f);
    }

    public Location(PlatformWorld world, double x, double y, double z, float yaw, float pitch) {
        if (world != null) {
            this.world = new WeakReference<PlatformWorld>(world);
        }
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public static float normalizeYaw(float yaw) {
        if ((yaw %= 360.0f) >= 180.0f) {
            yaw -= 360.0f;
        } else if (yaw < -180.0f) {
            yaw += 360.0f;
        }
        return yaw;
    }

    public static float normalizePitch(float pitch) {
        if (pitch > 90.0f) {
            pitch = 90.0f;
        } else if (pitch < -90.0f) {
            pitch = -90.0f;
        }
        return pitch;
    }

    public PlatformWorld getWorld() {
        if (this.world == null) {
            return null;
        }
        return (PlatformWorld)this.world.get();
    }

    public void setWorld(@Nullable PlatformWorld world) {
        this.world = world == null ? null : new WeakReference<PlatformWorld>(world);
    }

    @NotNull
    public Location add(@NotNull Location vec) {
        if (Objects.requireNonNull(vec).getWorld() == this.getWorld()) {
            this.x += vec.x;
            this.y += vec.y;
            this.z += vec.z;
            return this;
        }
        throw new IllegalArgumentException("Cannot add Locations of differing worlds");
    }

    @NotNull
    public Location add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    @NotNull
    public Location subtract(@NotNull Location vec) {
        if (Objects.requireNonNull(vec).getWorld() == this.getWorld()) {
            this.x -= vec.x;
            this.y -= vec.y;
            this.z -= vec.z;
            return this;
        }
        throw new IllegalArgumentException("Cannot add Locations of differing worlds");
    }

    @NotNull
    public Location subtract(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public double distance(@NotNull Location o) {
        return Math.sqrt(this.distanceSquared(o));
    }

    public double distanceSquared(@NotNull Location o) {
        if (o.getWorld() != null && this.getWorld() != null) {
            if (o.getWorld() != this.getWorld()) {
                throw new IllegalArgumentException("Cannot measure distance between " + this.getWorld().getName() + " and " + o.getWorld().getName());
            }
            return (this.x - o.x) * (this.x - o.x) + (this.y - o.y) * (this.y - o.y) + (this.z - o.z) * (this.z - o.z);
        }
        throw new IllegalArgumentException("Cannot measure distance to a null world");
    }

    @NotNull
    public Location multiply(double m) {
        this.x *= m;
        this.y *= m;
        this.z *= m;
        return this;
    }

    @NotNull
    public Location zero() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
        return this;
    }

    @NotNull
    public Location set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    @NotNull
    public Location add(@NotNull Location base, double x, double y, double z) {
        return this.set(base.x + x, base.y + y, base.z + z);
    }

    @NotNull
    public Location subtract(@NotNull Location base, double x, double y, double z) {
        return this.set(base.x - x, base.y - y, base.z - z);
    }

    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Location other = (Location)obj;
        return Objects.equals(this.world == null ? null : this.world.get(), other.world == null ? null : other.world.get()) && Double.doubleToLongBits(this.x) == Double.doubleToLongBits(other.x) && Double.doubleToLongBits(this.y) == Double.doubleToLongBits(other.y) && Double.doubleToLongBits(this.z) == Double.doubleToLongBits(other.z) && Float.floatToIntBits(this.pitch) == Float.floatToIntBits(other.pitch) && Float.floatToIntBits(this.yaw) == Float.floatToIntBits(other.yaw);
    }

    public int hashCode() {
        int hash = 3;
        PlatformWorld world = this.world == null ? null : (PlatformWorld)this.world.get();
        hash = 19 * hash + (world != null ? world.hashCode() : 0);
        hash = 19 * hash + Long.hashCode(Double.doubleToLongBits(this.x));
        hash = 19 * hash + Long.hashCode(Double.doubleToLongBits(this.y));
        hash = 19 * hash + Long.hashCode(Double.doubleToLongBits(this.z));
        hash = 19 * hash + Float.floatToIntBits(this.pitch);
        hash = 19 * hash + Float.floatToIntBits(this.yaw);
        return hash;
    }

    public String toString() {
        return "Location{world=" + String.valueOf(this.world == null ? null : (PlatformWorld)this.world.get()) + ",x=" + this.x + ",y=" + this.y + ",z=" + this.z + ",pitch=" + this.pitch + ",yaw=" + this.yaw + "}";
    }

    @NotNull
    public Location clone() {
        try {
            return (Location)super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    public double x() {
        return this.getX();
    }

    public int getBlockX() {
        return GrimMath.mojangFloor(this.x);
    }

    public double y() {
        return this.getY();
    }

    public int getBlockY() {
        return GrimMath.mojangFloor(this.y);
    }

    public double z() {
        return this.getZ();
    }

    public int getBlockZ() {
        return GrimMath.mojangFloor(this.z);
    }

    public boolean isWorldLoaded() {
        if (this.world == null) {
            return false;
        }
        PlatformWorld world = (PlatformWorld)this.world.get();
        return world != null && world.isLoaded();
    }

    public Vector3dm toVector() {
        return new Vector3dm(this.x, this.y, this.z);
    }

    @Generated
    public double getX() {
        return this.x;
    }

    @Generated
    public void setX(double x) {
        this.x = x;
    }

    @Generated
    public double getY() {
        return this.y;
    }

    @Generated
    public void setY(double y) {
        this.y = y;
    }

    @Generated
    public double getZ() {
        return this.z;
    }

    @Generated
    public void setZ(double z) {
        this.z = z;
    }

    @Generated
    public float getPitch() {
        return this.pitch;
    }

    @Generated
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    @Generated
    public float getYaw() {
        return this.yaw;
    }

    @Generated
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}

