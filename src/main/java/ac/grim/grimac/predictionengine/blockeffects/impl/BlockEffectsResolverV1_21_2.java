/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.predictionengine.blockeffects.impl;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.blockeffects.BlockCollisions;
import ac.grim.grimac.predictionengine.blockeffects.BlockEffectsResolver;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.fastutil.longs.LongSet;
import ac.grim.grimac.shaded.fastutil.objects.ObjectLinkedOpenHashSet;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.nmsutil.Collisions;
import ac.grim.grimac.utils.nmsutil.GetBoundingBox;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class BlockEffectsResolverV1_21_2
implements BlockEffectsResolver {
    public static BlockEffectsResolver INSTANCE = new BlockEffectsResolverV1_21_2();

    @Override
    public void applyEffectsFromBlocks(GrimPlayer player, List<GrimPlayer.Movement> movements) {
        LongSet visitedBlocks = player.visitedBlocks;
        SimpleCollisionBox boundingBox = (player.inVehicle() ? GetBoundingBox.getCollisionBoxForPlayer(player, player.x, player.y, player.z) : player.boundingBox.copy()).expand(-1.0E-5f);
        for (GrimPlayer.Movement movement : movements) {
            Vector3d from = movement.from();
            Vector3d to = movement.to();
            for (Vector3i blockPos : BlockEffectsResolverV1_21_2.boxTraverseBlocks(from, to, boundingBox)) {
                WrappedBlockState blockState = player.compensatedWorld.getBlock(blockPos);
                StateType blockType = blockState.getType();
                if (blockType.isAir() || !visitedBlocks.add(GrimMath.asLong(blockPos))) continue;
                Collisions.onInsideBlock(player, blockType, blockState, blockPos.x, blockPos.y, blockPos.z, true);
            }
        }
        visitedBlocks.clear();
    }

    private static Iterable<Vector3i> boxTraverseBlocks(Vector3d start, Vector3d end, SimpleCollisionBox boundingBox) {
        Vector3d direction = end.subtract(start);
        Iterable<Vector3i> initialBlocks = SimpleCollisionBox.betweenClosed(boundingBox);
        if (direction.lengthSquared() < (double)GrimMath.square(0.99999f)) {
            return initialBlocks;
        }
        ObjectLinkedOpenHashSet<Vector3i> traversedBlocks = new ObjectLinkedOpenHashSet<Vector3i>();
        Vector3d normalizedDirection = direction.normalize().multiply(1.0E-7);
        Vector3d boxMinPosition = boundingBox.min().toVector3d().add(normalizedDirection);
        Vector3d subtractedMinPosition = boundingBox.min().toVector3d().subtract(direction).subtract(normalizedDirection);
        BlockEffectsResolverV1_21_2.addCollisionsAlongTravel(traversedBlocks, subtractedMinPosition, boxMinPosition, boundingBox);
        for (Vector3i blockPos : initialBlocks) {
            traversedBlocks.add(blockPos);
        }
        return traversedBlocks;
    }

    public static void addCollisionsAlongTravel(Set<Vector3i> output, Vector3d start, Vector3d end, SimpleCollisionBox boundingBox) {
        Vector3d direction = end.subtract(start);
        int currentX = GrimMath.floor(start.x);
        int currentY = GrimMath.floor(start.y);
        int currentZ = GrimMath.floor(start.z);
        int stepX = GrimMath.sign(direction.x);
        int stepY = GrimMath.sign(direction.y);
        int stepZ = GrimMath.sign(direction.z);
        double tMaxX = stepX == 0 ? Double.MAX_VALUE : (double)stepX / direction.x;
        double tMaxY = stepY == 0 ? Double.MAX_VALUE : (double)stepY / direction.y;
        double tMaxZ = stepZ == 0 ? Double.MAX_VALUE : (double)stepZ / direction.z;
        double tDeltaX = tMaxX * (stepX > 0 ? 1.0 - GrimMath.frac(start.x) : GrimMath.frac(start.x));
        double tDeltaY = tMaxY * (stepY > 0 ? 1.0 - GrimMath.frac(start.y) : GrimMath.frac(start.y));
        double tDeltaZ = tMaxZ * (stepZ > 0 ? 1.0 - GrimMath.frac(start.z) : GrimMath.frac(start.z));
        int iterationCount = 0;
        while (tDeltaX <= 1.0 || tDeltaY <= 1.0 || tDeltaZ <= 1.0) {
            if (tDeltaX < tDeltaY) {
                if (tDeltaX < tDeltaZ) {
                    currentX += stepX;
                    tDeltaX += tMaxX;
                } else {
                    currentZ += stepZ;
                    tDeltaZ += tMaxZ;
                }
            } else if (tDeltaY < tDeltaZ) {
                currentY += stepY;
                tDeltaY += tMaxY;
            } else {
                currentZ += stepZ;
                tDeltaZ += tMaxZ;
            }
            if (iterationCount++ > 16) break;
            Optional<Vector3d> collisionPoint = BlockCollisions.clip(currentX, currentY, currentZ, currentX + 1, currentY + 1, currentZ + 1, start, end);
            if (collisionPoint.isEmpty()) continue;
            Vector3d collisionVec = collisionPoint.get();
            double clampedX = GrimMath.clamp(collisionVec.x, (double)((float)currentX + 1.0E-5f), (double)currentX + 1.0 - (double)1.0E-5f);
            double clampedY = GrimMath.clamp(collisionVec.y, (double)((float)currentY + 1.0E-5f), (double)currentY + 1.0 - (double)1.0E-5f);
            double clampedZ = GrimMath.clamp(collisionVec.z, (double)((float)currentZ + 1.0E-5f), (double)currentZ + 1.0 - (double)1.0E-5f);
            int endX = GrimMath.floor(clampedX + boundingBox.getXSize());
            int endY = GrimMath.floor(clampedY + boundingBox.getYSize());
            int endZ = GrimMath.floor(clampedZ + boundingBox.getZSize());
            for (int x = currentX; x <= endX; ++x) {
                for (int y = currentY; y <= endY; ++y) {
                    for (int z = currentZ; z <= endZ; ++z) {
                        output.add(new Vector3i(x, y, z));
                    }
                }
            }
        }
    }
}

