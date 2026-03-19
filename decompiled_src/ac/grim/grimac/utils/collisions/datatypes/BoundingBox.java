/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.collisions.datatypes;

import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.math.Vector3dm;

public class BoundingBox {
    public final float minX;
    public final float minY;
    public final float minZ;
    public final float maxX;
    public final float maxY;
    public final float maxZ;

    public BoundingBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public BoundingBox(Vector3dm min, Vector3dm max) {
        this.minX = (float)Math.min(min.getX(), max.getX());
        this.minY = (float)Math.min(min.getY(), max.getY());
        this.minZ = (float)Math.min(min.getZ(), max.getZ());
        this.maxX = (float)Math.max(min.getX(), max.getX());
        this.maxY = (float)Math.max(min.getY(), max.getY());
        this.maxZ = (float)Math.max(min.getZ(), max.getZ());
    }

    public BoundingBox(BoundingBox one, BoundingBox two) {
        this.minX = Math.min(one.minX, two.minX);
        this.minY = Math.min(one.minY, two.minY);
        this.minZ = Math.min(one.minZ, two.minZ);
        this.maxX = Math.max(one.maxX, two.maxX);
        this.maxY = Math.max(one.maxY, two.maxY);
        this.maxZ = Math.max(one.maxZ, two.maxZ);
    }

    public BoundingBox add(float x, float y, float z) {
        return new BoundingBox(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
    }

    public BoundingBox add(Vector3dm vector) {
        return this.add((float)vector.getX(), (float)vector.getY(), (float)vector.getZ());
    }

    public BoundingBox grow(float x, float y, float z) {
        return new BoundingBox(this.minX - x, this.minY - y, this.minZ - z, this.maxX + x, this.maxY + y, this.maxZ + z);
    }

    public BoundingBox shrink(float x, float y, float z) {
        return new BoundingBox(this.minX + x, this.minY + y, this.minZ + z, this.maxX - x, this.maxY - y, this.maxZ - z);
    }

    public BoundingBox add(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        return new BoundingBox(this.minX + minX, this.minY + minY, this.minZ + minZ, this.maxX + maxX, this.maxY + maxY, this.maxZ + maxZ);
    }

    public BoundingBox subtract(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        return new BoundingBox(this.minX - minX, this.minY - minY, this.minZ - minZ, this.maxX - maxX, this.maxY - maxY, this.maxZ - maxZ);
    }

    public boolean intersectsWithBox(Vector3dm vector) {
        return vector.getX() > (double)this.minX && vector.getX() < (double)this.maxX && vector.getY() > (double)this.minY && vector.getY() < (double)this.maxY && vector.getZ() > (double)this.minZ && vector.getZ() < (double)this.maxZ;
    }

    public Vector3dm getMinimum() {
        return new Vector3dm(this.minX, this.minY, this.minZ);
    }

    public Vector3dm getMaximum() {
        return new Vector3dm(this.maxX, this.maxY, this.maxZ);
    }

    public boolean collides(Vector3dm vector) {
        return vector.getX() >= (double)this.minX && vector.getX() <= (double)this.maxX && vector.getY() >= (double)this.minY && vector.getY() <= (double)this.maxY && vector.getZ() >= (double)this.minZ && vector.getZ() <= (double)this.maxZ;
    }

    public boolean collidesHorizontally(Vector3dm vector) {
        return vector.getX() >= (double)this.minX && vector.getX() <= (double)this.maxX && vector.getY() > (double)this.minY && vector.getY() < (double)this.maxY && vector.getZ() >= (double)this.minZ && vector.getZ() <= (double)this.maxZ;
    }

    public boolean collidesVertically(Vector3dm vector) {
        return vector.getX() > (double)this.minX && vector.getX() < (double)this.maxX && vector.getY() >= (double)this.minY && vector.getY() <= (double)this.maxY && vector.getZ() > (double)this.minZ && vector.getZ() < (double)this.maxZ;
    }

    public double calculateXOffset(BoundingBox other, double offsetX) {
        if (other.maxY > this.minY && other.minY < this.maxY && other.maxZ > this.minZ && other.minZ < this.maxZ) {
            double offset;
            if (offsetX > 0.0 && other.maxX <= this.minX) {
                double offset2 = this.minX - other.maxX;
                if (offset2 < offsetX) {
                    return offset2;
                }
            } else if (offsetX < 0.0 && other.minX >= this.maxX && (offset = (double)(this.maxX - other.minX)) > offsetX) {
                return offset;
            }
        }
        return offsetX;
    }

    public double calculateYOffset(BoundingBox other, double offsetY) {
        if (other.maxX > this.minX && other.minX < this.maxX && other.maxZ > this.minZ && other.minZ < this.maxZ) {
            double offset;
            if (offsetY > 0.0 && other.maxY <= this.minY) {
                double offset2 = this.minY - other.maxY;
                if (offset2 < offsetY) {
                    return offset2;
                }
            } else if (offsetY < 0.0 && other.minY >= this.maxY && (offset = (double)(this.maxY - other.minY)) > offsetY) {
                return offset;
            }
        }
        return offsetY;
    }

    public double calculateZOffset(BoundingBox other, double offsetZ) {
        if (other.maxX > this.minX && other.minX < this.maxX && other.maxY > this.minY && other.minY < this.maxY) {
            double offset;
            if (offsetZ > 0.0 && other.maxZ <= this.minZ) {
                double offset2 = this.minZ - other.maxZ;
                if (offset2 < offsetZ) {
                    return offset2;
                }
            } else if (offsetZ < 0.0 && other.minZ >= this.maxZ && (offset = (double)(this.maxZ - other.minZ)) > offsetZ) {
                return offset;
            }
        }
        return offsetZ;
    }

    public BoundingBox addCoord(float x, float y, float z) {
        return new BoundingBox(x < 0.0f ? this.minX + x : this.minX, y < 0.0f ? this.minY + y : this.minY, z < 0.0f ? this.minZ + z : this.minZ, x > 0.0f ? this.maxX + x : this.maxX, y > 0.0f ? this.maxY + y : this.maxY, z > 0.0f ? this.maxZ + z : this.maxZ);
    }

    public SimpleCollisionBox toCollisionBox() {
        return new SimpleCollisionBox(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    public String toString() {
        return "[" + this.minX + ", " + this.minY + ", " + this.minZ + ", " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }
}

