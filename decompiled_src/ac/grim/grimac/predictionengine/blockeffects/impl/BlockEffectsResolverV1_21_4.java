/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.predictionengine.blockeffects.impl;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.blockeffects.BlockEffectsResolver;
import ac.grim.grimac.predictionengine.blockeffects.impl.BlockEffectsResolverV1_21_2;
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

public class BlockEffectsResolverV1_21_4
implements BlockEffectsResolver {
    public static final BlockEffectsResolver INSTANCE = new BlockEffectsResolverV1_21_4();

    @Override
    public void applyEffectsFromBlocks(GrimPlayer player, List<GrimPlayer.Movement> movements) {
        LongSet visitedBlocks = player.visitedBlocks;
        for (GrimPlayer.Movement movement : movements) {
            Vector3d from = movement.from();
            Vector3d to = movement.to();
            SimpleCollisionBox boundingBox = GetBoundingBox.getCollisionBoxForPlayer(player, to.x, to.y, to.z).expand(-1.0E-5f);
            for (Vector3i blockPos : BlockEffectsResolverV1_21_4.boxTraverseBlocks(from, to, boundingBox)) {
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
        Vector3d boxMinPosition = boundingBox.min().toVector3d();
        Vector3d subtractedMinPosition = boxMinPosition.subtract(direction);
        BlockEffectsResolverV1_21_2.addCollisionsAlongTravel(traversedBlocks, subtractedMinPosition, boxMinPosition, boundingBox);
        for (Vector3i blockPos : initialBlocks) {
            traversedBlocks.add(blockPos);
        }
        return traversedBlocks;
    }
}

