/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.AbstractIterator
 *  com.google.common.collect.ImmutableList
 */
package ac.grim.grimac.utils.collisions.datatypes;

import ac.grim.grimac.predictionengine.blockeffects.BlockCollisions;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.Direction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.fastutil.doubles.AbstractDoubleList;
import ac.grim.grimac.shaded.fastutil.doubles.DoubleArrayList;
import ac.grim.grimac.shaded.fastutil.doubles.DoubleList;
import ac.grim.grimac.utils.collisions.datatypes.BoundingBox;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.ComplexCollisionBox;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Location;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.nmsutil.Collisions;
import ac.grim.grimac.utils.nmsutil.Ray;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableList;
import java.util.List;

public class SimpleCollisionBox
implements CollisionBox {
    public static final double COLLISION_EPSILON = 1.0E-7;
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;
    private final SimpleCollisionBox[] boxes = new SimpleCollisionBox[15];
    private boolean isFullBlock = false;

    public SimpleCollisionBox() {
        this(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, false);
    }

    public SimpleCollisionBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, boolean fullBlock) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;
        this.isFullBlock = fullBlock;
    }

    public SimpleCollisionBox(Vector3dm min, Vector3dm max) {
        this(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
    }

    public SimpleCollisionBox(Vector3i pos) {
        this(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
    }

    public SimpleCollisionBox(double minX, double minY, double minZ) {
        this(minX, minY, minZ, minX + 1.0, minY + 1.0, minZ + 1.0, true);
    }

    public SimpleCollisionBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;
        if (minX == 0.0 && minY == 0.0 && minZ == 0.0 && maxX == 1.0 && maxY == 1.0 && maxZ == 1.0) {
            this.isFullBlock = true;
        }
    }

    public SimpleCollisionBox(Vector3d min, Vector3d max) {
        this(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
    }

    public SimpleCollisionBox(Location loc, double width, double height) {
        this(loc.toVector(), width, height);
    }

    public SimpleCollisionBox(Vector3dm vec, double width, double height) {
        this(vec.getX(), vec.getY(), vec.getZ(), vec.getX(), vec.getY(), vec.getZ());
        this.expand(width / 2.0, 0.0, width / 2.0);
        this.maxY += height;
    }

    public SimpleCollisionBox(BoundingBox box) {
        this(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }

    public SimpleCollisionBox expand(double x, double y, double z) {
        this.minX -= x;
        this.minY -= y;
        this.minZ -= z;
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
        return this.sort();
    }

    public SimpleCollisionBox sort() {
        double minX = Math.min(this.minX, this.maxX);
        double minY = Math.min(this.minY, this.maxY);
        double minZ = Math.min(this.minZ, this.maxZ);
        double maxX = Math.max(this.minX, this.maxX);
        double maxY = Math.max(this.minY, this.maxY);
        double maxZ = Math.max(this.minZ, this.maxZ);
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        return this;
    }

    public SimpleCollisionBox expandMin(double x, double y, double z) {
        this.minX += x;
        this.minY += y;
        this.minZ += z;
        return this;
    }

    public SimpleCollisionBox expandMax(double x, double y, double z) {
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
        return this;
    }

    public SimpleCollisionBox expand(double value) {
        this.minX -= value;
        this.minY -= value;
        this.minZ -= value;
        this.maxX += value;
        this.maxY += value;
        this.maxZ += value;
        return this;
    }

    public Vector3dm[] corners() {
        Vector3dm[] vectors = new Vector3dm[]{new Vector3dm(this.minX, this.minY, this.minZ), new Vector3dm(this.minX, this.minY, this.maxZ), new Vector3dm(this.maxX, this.minY, this.minZ), new Vector3dm(this.maxX, this.minY, this.maxZ), new Vector3dm(this.minX, this.maxY, this.minZ), new Vector3dm(this.minX, this.maxY, this.maxZ), new Vector3dm(this.maxX, this.maxY, this.minZ), new Vector3dm(this.maxX, this.maxY, this.maxZ)};
        return vectors;
    }

    public CollisionBox encompass(SimpleCollisionBox other) {
        this.minX = Math.min(this.minX, other.minX);
        this.minY = Math.min(this.minY, other.minY);
        this.minZ = Math.min(this.minZ, other.minZ);
        this.maxX = Math.max(this.maxX, other.maxX);
        this.maxY = Math.max(this.maxY, other.maxY);
        this.maxZ = Math.max(this.maxZ, other.maxZ);
        return this;
    }

    public SimpleCollisionBox expandToAbsoluteCoordinates(double x, double y, double z) {
        return this.expandToCoordinate(x - (this.minX + this.maxX) / 2.0, y - (this.minY + this.maxY) / 2.0, z - (this.minZ + this.maxZ) / 2.0);
    }

    public SimpleCollisionBox expandToCoordinate(double x, double y, double z) {
        if (x < 0.0) {
            this.minX += x;
        } else {
            this.maxX += x;
        }
        if (y < 0.0) {
            this.minY += y;
        } else {
            this.maxY += y;
        }
        if (z < 0.0) {
            this.minZ += z;
        } else {
            this.maxZ += z;
        }
        return this;
    }

    public SimpleCollisionBox combineToMinimum(double x, double y, double z) {
        this.minX = Math.min(this.minX, x);
        this.maxX = Math.max(this.maxX, x);
        this.minY = Math.min(this.minY, y);
        this.maxY = Math.max(this.maxY, y);
        this.minZ = Math.min(this.minZ, z);
        this.maxZ = Math.max(this.maxZ, z);
        return this;
    }

    @Override
    public CollisionBox union(SimpleCollisionBox other) {
        return new ComplexCollisionBox(2, this, other);
    }

    @Override
    public boolean isCollided(SimpleCollisionBox other) {
        return other.maxX >= this.minX && other.minX <= this.maxX && other.maxY >= this.minY && other.minY <= this.maxY && other.maxZ >= this.minZ && other.minZ <= this.maxZ;
    }

    @Override
    public boolean isIntersected(SimpleCollisionBox other) {
        return other.maxX - 1.0E-7 > this.minX && other.minX + 1.0E-7 < this.maxX && other.maxY - 1.0E-7 > this.minY && other.minY + 1.0E-7 < this.maxY && other.maxZ - 1.0E-7 > this.minZ && other.minZ + 1.0E-7 < this.maxZ;
    }

    public boolean isIntersected(CollisionBox other) {
        if (other instanceof SimpleCollisionBox) {
            return this.isIntersected((SimpleCollisionBox)other);
        }
        int size = other.downCast(this.boxes);
        for (int i = 0; i < size; ++i) {
            if (!this.isIntersected(this.boxes[i])) continue;
            return true;
        }
        return false;
    }

    public boolean collidesVertically(SimpleCollisionBox other) {
        return other.maxX > this.minX && other.minX < this.maxX && other.maxY >= this.minY && other.minY <= this.maxY && other.maxZ > this.minZ && other.minZ < this.maxZ;
    }

    @Override
    public SimpleCollisionBox copy() {
        return new SimpleCollisionBox(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ, this.isFullBlock);
    }

    @Override
    public SimpleCollisionBox offset(double x, double y, double z) {
        this.minX += x;
        this.minY += y;
        this.minZ += z;
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
        return this;
    }

    @Override
    public void downCast(List<SimpleCollisionBox> list) {
        list.add(this);
    }

    @Override
    public int downCast(SimpleCollisionBox[] list) {
        list[0] = this;
        return 1;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean isFullBlock() {
        return this.isFullBlock;
    }

    @Override
    public boolean isSideFullBlock(BlockFace axis) {
        if (this.isFullBlock) {
            return true;
        }
        BlockFace faceToSourceConnector = axis.getOppositeFace();
        return switch (faceToSourceConnector) {
            case BlockFace.EAST, BlockFace.WEST -> {
                if (this.minX == 0.0 && this.maxX == 1.0) {
                    yield true;
                }
                yield false;
            }
            case BlockFace.UP, BlockFace.DOWN -> {
                if (this.minY == 0.0 && this.maxY == 1.0) {
                    yield true;
                }
                yield false;
            }
            case BlockFace.NORTH, BlockFace.SOUTH -> {
                if (this.minZ == 0.0 && this.maxZ == 1.0) {
                    yield true;
                }
                yield false;
            }
            default -> false;
        };
    }

    public boolean isFullBlockNoCache() {
        return this.minX == 0.0 && this.minY == 0.0 && this.minZ == 0.0 && this.maxX == 1.0 && this.maxY == 1.0 && this.maxZ == 1.0;
    }

    public double collideX(SimpleCollisionBox other, double offsetX) {
        if (offsetX != 0.0 && other.minY - this.maxY < -1.0E-7 && other.maxY - this.minY > 1.0E-7 && other.minZ - this.maxZ < -1.0E-7 && other.maxZ - this.minZ > 1.0E-7) {
            if (offsetX >= 0.0) {
                double max_move = this.minX - other.maxX;
                return max_move < -1.0E-7 ? offsetX : Math.min(max_move, offsetX);
            }
            double max_move = this.maxX - other.minX;
            return max_move > 1.0E-7 ? offsetX : Math.max(max_move, offsetX);
        }
        return offsetX;
    }

    public double collideY(SimpleCollisionBox other, double offsetY) {
        if (offsetY != 0.0 && other.minX - this.maxX < -1.0E-7 && other.maxX - this.minX > 1.0E-7 && other.minZ - this.maxZ < -1.0E-7 && other.maxZ - this.minZ > 1.0E-7) {
            if (offsetY >= 0.0) {
                double max_move = this.minY - other.maxY;
                return max_move < -1.0E-7 ? offsetY : Math.min(max_move, offsetY);
            }
            double max_move = this.maxY - other.minY;
            return max_move > 1.0E-7 ? offsetY : Math.max(max_move, offsetY);
        }
        return offsetY;
    }

    public double collideZ(SimpleCollisionBox other, double offsetZ) {
        if (offsetZ != 0.0 && other.minX - this.maxX < -1.0E-7 && other.maxX - this.minX > 1.0E-7 && other.minY - this.maxY < -1.0E-7 && other.maxY - this.minY > 1.0E-7) {
            if (offsetZ >= 0.0) {
                double max_move = this.minZ - other.maxZ;
                return max_move < -1.0E-7 ? offsetZ : Math.min(max_move, offsetZ);
            }
            double max_move = this.maxZ - other.minZ;
            return max_move > 1.0E-7 ? offsetZ : Math.max(max_move, offsetZ);
        }
        return offsetZ;
    }

    public double distance(SimpleCollisionBox box) {
        double xwidth = (this.maxX - this.minX) / 2.0;
        double zwidth = (this.maxZ - this.minZ) / 2.0;
        double bxwidth = (box.maxX - box.minX) / 2.0;
        double bzwidth = (box.maxZ - box.minZ) / 2.0;
        double hxz = Math.hypot(this.minX - box.minX, this.minZ - box.minZ);
        return hxz - (xwidth + zwidth + bxwidth + bzwidth) / 4.0;
    }

    public double distanceX(double x) {
        return x >= this.minX && x <= this.maxX ? 0.0 : Math.min(Math.abs(x - this.minX), Math.abs(x - this.maxX));
    }

    public double distanceY(double y) {
        return y >= this.minY && y <= this.maxY ? 0.0 : Math.min(Math.abs(y - this.minY), Math.abs(y - this.maxY));
    }

    public double distanceZ(double z) {
        return z >= this.minZ && z <= this.maxZ ? 0.0 : Math.min(Math.abs(z - this.minZ), Math.abs(z - this.maxZ));
    }

    public Vector3dm intersectsRay(Ray ray, float minDist, float maxDist) {
        Vector3dm invDir = new Vector3dm(1.0 / ray.getDirection().getX(), 1.0 / ray.getDirection().getY(), 1.0 / ray.getDirection().getZ());
        boolean signDirX = invDir.getX() < 0.0;
        boolean signDirY = invDir.getY() < 0.0;
        boolean signDirZ = invDir.getZ() < 0.0;
        double tmin = ((signDirX ? this.maxX : this.minX) - ray.getOrigin().getX()) * invDir.getX();
        double tmax = ((signDirX ? this.minX : this.maxX) - ray.getOrigin().getX()) * invDir.getX();
        double tymin = ((signDirY ? this.maxY : this.minY) - ray.getOrigin().getY()) * invDir.getY();
        double tymax = ((signDirY ? this.minY : this.maxY) - ray.getOrigin().getY()) * invDir.getY();
        if (tmin > tymax || tymin > tmax) {
            return null;
        }
        if (tymin > tmin) {
            tmin = tymin;
        }
        if (tymax < tmax) {
            tmax = tymax;
        }
        double tzmin = ((signDirZ ? this.maxZ : this.minZ) - ray.getOrigin().getZ()) * invDir.getZ();
        double tzmax = ((signDirZ ? this.minZ : this.maxZ) - ray.getOrigin().getZ()) * invDir.getZ();
        if (tmin > tzmax || tzmin > tmax) {
            return null;
        }
        if (tzmin > tmin) {
            tmin = tzmin;
        }
        if (tzmax < tmax) {
            tmax = tzmax;
        }
        return tmin < (double)maxDist && tmax > (double)minDist ? ray.getPointAtDistance(tmin) : null;
    }

    public Vector3dm max() {
        return new Vector3dm(this.maxX, this.maxY, this.maxZ);
    }

    public Vector3dm min() {
        return new Vector3dm(this.minX, this.minY, this.minZ);
    }

    public Vector3d getCenter() {
        return new Vector3d(GrimMath.lerp(0.5, this.minX, this.maxX), GrimMath.lerp(0.5, this.minY, this.maxY), GrimMath.lerp(0.5, this.minZ, this.maxZ));
    }

    public DoubleList getYPointPositions() {
        return this.create(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    private DoubleList create(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        if (!(maxX - minX < 1.0E-7 || maxY - minY < 1.0E-7 || maxZ - minZ < 1.0E-7)) {
            int xBits = this.findBits(minX, maxX);
            int yBits = this.findBits(minY, maxY);
            int zBits = this.findBits(minZ, maxZ);
            if (xBits < 0 || yBits < 0 || zBits < 0) {
                return DoubleArrayList.wrap(new double[]{minY, maxY});
            }
            if (xBits == 0 && yBits == 0 && zBits == 0) {
                return DoubleArrayList.wrap(new double[]{0.0, 1.0});
            }
            final int yFactor = 1 << yBits;
            return new AbstractDoubleList(){

                @Override
                public double getDouble(int index) {
                    return (double)index / (double)yFactor;
                }

                @Override
                public int size() {
                    return yFactor + 1;
                }
            };
        }
        return DoubleArrayList.of();
    }

    private int findBits(double min, double max) {
        if (!(min < -1.0E-7) && !(max > 1.0000001)) {
            for (int bitShift = 0; bitShift <= 3; ++bitShift) {
                boolean isMaxAligned;
                int factor = 1 << bitShift;
                double scaledMin = min * (double)factor;
                double scaledMax = max * (double)factor;
                boolean isMinAligned = Math.abs(scaledMin - (double)Math.round(scaledMin)) < 1.0E-7 * (double)factor;
                boolean bl = isMaxAligned = Math.abs(scaledMax - (double)Math.round(scaledMax)) < 1.0E-7 * (double)factor;
                if (!isMinAligned || !isMaxAligned) continue;
                return bitShift;
            }
        }
        return -1;
    }

    public double getXSize() {
        return this.maxX - this.minX;
    }

    public double getYSize() {
        return this.maxY - this.minY;
    }

    public double getZSize() {
        return this.maxZ - this.minZ;
    }

    public SimpleCollisionBox move(Vector3d vector) {
        return this.move(vector.x, vector.y, vector.z);
    }

    public SimpleCollisionBox move(double x, double y, double z) {
        return new SimpleCollisionBox(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
    }

    public boolean intersects(SimpleCollisionBox collisionBox) {
        return this.intersects(collisionBox.minX, collisionBox.minY, collisionBox.minZ, collisionBox.maxX, collisionBox.maxY, collisionBox.maxZ);
    }

    public boolean intersects(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return this.minX < maxX && this.maxX > minX && this.minY < maxY && this.maxY > minY && this.minZ < maxZ && this.maxZ > minZ;
    }

    public boolean intersects(Vector3d min, Vector3d max) {
        return this.intersects(Math.min(min.x, max.x), Math.min(min.y, max.y), Math.min(min.z, max.z), Math.max(min.x, max.x), Math.max(min.y, max.y), Math.max(min.z, max.z));
    }

    public boolean intersects(Vector3i blockPos) {
        return this.intersects(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1);
    }

    public static Iterable<Vector3i> betweenClosed(SimpleCollisionBox box) {
        Vector3i startBlockPos = SimpleCollisionBox.containing(box.minX, box.minY, box.minZ);
        Vector3i endBlockPos = SimpleCollisionBox.containing(box.maxX, box.maxY, box.maxZ);
        return SimpleCollisionBox.betweenClosed(startBlockPos, endBlockPos);
    }

    public static Vector3i containing(double x, double y, double z) {
        return new Vector3i(GrimMath.floor(x), GrimMath.floor(y), GrimMath.floor(z));
    }

    public static Iterable<Vector3i> betweenClosed(Vector3i firstPos, Vector3i secondPos) {
        return SimpleCollisionBox.betweenClosed(Math.min(firstPos.getX(), secondPos.getX()), Math.min(firstPos.getY(), secondPos.getY()), Math.min(firstPos.getZ(), secondPos.getZ()), Math.max(firstPos.getX(), secondPos.getX()), Math.max(firstPos.getY(), secondPos.getY()), Math.max(firstPos.getZ(), secondPos.getZ()));
    }

    public static Iterable<Vector3i> betweenClosed(final int xStart, final int yStart, final int zStart, int xEnd, int yEnd, int zEnd) {
        final int xRange = xEnd - xStart + 1;
        final int yRange = yEnd - yStart + 1;
        int zRange = zEnd - zStart + 1;
        final int totalVectors = xRange * yRange * zRange;
        return () -> new AbstractIterator<Vector3i>(){
            private int index;

            protected Vector3i computeNext() {
                if (this.index == totalVectors) {
                    return (Vector3i)this.endOfData();
                }
                int xOffset = this.index % xRange;
                int yOffset = this.index / xRange;
                int yOffsetMod = yOffset % yRange;
                int zOffset = yOffset / yRange;
                ++this.index;
                return new Vector3i(xStart + xOffset, yStart + yOffsetMod, zStart + zOffset);
            }
        };
    }

    public static Iterable<Vector3i> betweenCornersInDirection(SimpleCollisionBox boundingBox, Vector3d directionVector) {
        Vector3d min = boundingBox.min().toVector3d();
        int minX = GrimMath.floor(min.x);
        int minY = GrimMath.floor(min.y);
        int minZ = GrimMath.floor(min.z);
        Vector3d max = boundingBox.max().toVector3d();
        int maxX = GrimMath.floor(max.x);
        int maxY = GrimMath.floor(max.y);
        int maxZ = GrimMath.floor(max.z);
        return SimpleCollisionBox.betweenCornersInDirection(minX, minY, minZ, maxX, maxY, maxZ, directionVector);
    }

    public static Iterable<Vector3i> betweenCornersInDirection(Vector3i min, Vector3i max, Vector3d directionVector) {
        return SimpleCollisionBox.betweenCornersInDirection(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ(), directionVector);
    }

    public static Iterable<Vector3i> betweenCornersInDirection(int x1, int y1, int z1, int x2, int y2, int z2, Vector3d directionVector) {
        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);
        int maxZ = Math.max(z1, z2);
        int sizeX = maxX - minX;
        int sizeY = maxY - minY;
        int sizeZ = maxZ - minZ;
        final int startX = directionVector.x >= 0.0 ? minX : maxX;
        final int startY = directionVector.y >= 0.0 ? minY : maxY;
        final int startZ = directionVector.z >= 0.0 ? minZ : maxZ;
        ImmutableList<Collisions.Axis> axisOrder = BlockCollisions.axisStepOrder(directionVector);
        Collisions.Axis primaryAxis = (Collisions.Axis)((Object)axisOrder.get(0));
        Collisions.Axis secondaryAxis = (Collisions.Axis)((Object)axisOrder.get(1));
        Collisions.Axis tertiaryAxis = (Collisions.Axis)((Object)axisOrder.get(2));
        final Direction primaryDirection = primaryAxis.get(directionVector) >= 0.0 ? primaryAxis.getPositive() : primaryAxis.getNegative();
        final Direction secondaryDirection = secondaryAxis.get(directionVector) >= 0.0 ? secondaryAxis.getPositive() : secondaryAxis.getNegative();
        final Direction tertiaryDirection = tertiaryAxis.get(directionVector) >= 0.0 ? tertiaryAxis.getPositive() : tertiaryAxis.getNegative();
        final int primaryCount = primaryAxis.choose(sizeX, sizeY, sizeZ);
        final int secondaryCount = secondaryAxis.choose(sizeX, sizeY, sizeZ);
        final int tertiaryCount = tertiaryAxis.choose(sizeX, sizeY, sizeZ);
        return () -> new AbstractIterator<Vector3i>(){
            private int firstIndex;
            private int secondIndex;
            private int thirdIndex;
            private boolean end;
            private final int firstDirX;
            private final int firstDirY;
            private final int firstDirZ;
            private final int secondDirX;
            private final int secondDirY;
            private final int secondDirZ;
            private final int thirdDirX;
            private final int thirdDirY;
            private final int thirdDirZ;
            {
                this.firstDirX = primaryDirection.getVector().getX();
                this.firstDirY = primaryDirection.getVector().getY();
                this.firstDirZ = primaryDirection.getVector().getZ();
                this.secondDirX = secondaryDirection.getVector().getX();
                this.secondDirY = secondaryDirection.getVector().getY();
                this.secondDirZ = secondaryDirection.getVector().getZ();
                this.thirdDirX = tertiaryDirection.getVector().getX();
                this.thirdDirY = tertiaryDirection.getVector().getY();
                this.thirdDirZ = tertiaryDirection.getVector().getZ();
            }

            protected Vector3i computeNext() {
                if (this.end) {
                    return (Vector3i)this.endOfData();
                }
                Vector3i cursor = new Vector3i(startX + this.firstDirX * this.firstIndex + this.secondDirX * this.secondIndex + this.thirdDirX * this.thirdIndex, startY + this.firstDirY * this.firstIndex + this.secondDirY * this.secondIndex + this.thirdDirY * this.thirdIndex, startZ + this.firstDirZ * this.firstIndex + this.secondDirZ * this.secondIndex + this.thirdDirZ * this.thirdIndex);
                if (this.thirdIndex < tertiaryCount) {
                    ++this.thirdIndex;
                } else if (this.secondIndex < secondaryCount) {
                    ++this.secondIndex;
                    this.thirdIndex = 0;
                } else if (this.firstIndex < primaryCount) {
                    ++this.firstIndex;
                    this.thirdIndex = 0;
                    this.secondIndex = 0;
                } else {
                    this.end = true;
                }
                return cursor;
            }
        };
    }

    public String toString() {
        return "SimpleCollisionBox{minX=" + this.minX + ", minY=" + this.minY + ", minZ=" + this.minZ + ", maxX=" + this.maxX + ", maxY=" + this.maxY + ", maxZ=" + this.maxZ + ", isFullBlock=" + this.isFullBlock + "}";
    }
}

