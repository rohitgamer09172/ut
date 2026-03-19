/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.util.concurrent.AtomicDouble
 *  lombok.Generated
 */
package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.MainSupportingBlockData;
import ac.grim.grimac.utils.nmsutil.Collisions;
import com.google.common.util.concurrent.AtomicDouble;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Generated;

public final class MainSupportingBlockPosFinder {
    public static MainSupportingBlockData findMainSupportingBlockPos(GrimPlayer player, MainSupportingBlockData lastSupportingBlock, Vector3d lastMovement, SimpleCollisionBox maxPose, boolean isOnGround) {
        if (!isOnGround) {
            return new MainSupportingBlockData(null, false);
        }
        SimpleCollisionBox slightlyBelowPlayer = new SimpleCollisionBox(maxPose.minX, maxPose.minY - 1.0E-6, maxPose.minZ, maxPose.maxX, maxPose.minY, maxPose.maxZ);
        Vector3i supportingBlock = MainSupportingBlockPosFinder.findSupportingBlock(player, slightlyBelowPlayer);
        if (supportingBlock == null && !lastSupportingBlock.lastOnGroundAndNoBlock()) {
            if (lastMovement != null) {
                SimpleCollisionBox aabb2 = slightlyBelowPlayer.offset(-lastMovement.x, 0.0, -lastMovement.z);
                return new MainSupportingBlockData(MainSupportingBlockPosFinder.findSupportingBlock(player, aabb2), true);
            }
        } else {
            return new MainSupportingBlockData(supportingBlock, true);
        }
        return new MainSupportingBlockData(null, true);
    }

    @Nullable
    private static Vector3i findSupportingBlock(@NotNull GrimPlayer player, @NotNull SimpleCollisionBox searchBox) {
        Vector3d playerPos = new Vector3d(player.x, player.y, player.z);
        AtomicReference bestBlockPos = new AtomicReference();
        AtomicDouble blockPosDistance = new AtomicDouble(Double.MAX_VALUE);
        Collisions.forEachCollisionBox(player, searchBox, pos -> {
            Vector3i blockPos = pos.toVector3i();
            Vector3d blockPosAsVector3d = new Vector3d((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5);
            double distance = playerPos.distanceSquared(blockPosAsVector3d);
            if (distance < blockPosDistance.get() || distance == blockPosDistance.get() && (bestBlockPos.get() == null || MainSupportingBlockPosFinder.firstHasPriorityOverSecond(blockPos, (Vector3i)bestBlockPos.get()))) {
                bestBlockPos.set(blockPos);
                blockPosDistance.set(distance);
            }
        });
        return (Vector3i)bestBlockPos.get();
    }

    private static boolean firstHasPriorityOverSecond(@NotNull Vector3i first, @NotNull Vector3i second) {
        double sumY;
        if (first.getY() < second.getY()) {
            return true;
        }
        double sumX = second.getX() - first.getX();
        double horizontalSumTotal = sumX + (sumY = (double)(second.getZ() - first.getZ()));
        if (horizontalSumTotal == 0.0) {
            return sumX < 0.0;
        }
        return horizontalSumTotal < 0.0;
    }

    @Generated
    private MainSupportingBlockPosFinder() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

