/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.utils.math.Vector3dm;

public record HitData(Vector3i position, Vector3dm blockHitLocation, BlockFace closestDirection, WrappedBlockState state) {
    public Vector3d getRelativeBlockHitLocation() {
        return new Vector3d(this.blockHitLocation.getX() - (double)this.position.getX(), this.blockHitLocation.getY() - (double)this.position.getY(), this.blockHitLocation.getZ() - (double)this.position.getZ());
    }
}

