/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.anticheat.update;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.utils.collisions.HitboxData;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import java.util.ArrayList;
import lombok.Generated;

public final class BlockBreak {
    public final Vector3i position;
    public final BlockFace face;
    public final int faceId;
    public final DiggingAction action;
    public final int sequence;
    public final WrappedBlockState block;
    private final GrimPlayer player;
    private boolean cancelled;

    public BlockBreak(GrimPlayer player, Vector3i position, BlockFace face, int faceId, DiggingAction action, int sequence, WrappedBlockState block) {
        this.player = player;
        this.position = position;
        this.face = face;
        this.faceId = faceId;
        this.action = action;
        this.sequence = sequence;
        this.block = block;
    }

    public void cancel() {
        this.cancelled = true;
    }

    public SimpleCollisionBox getCombinedBox() {
        CollisionBox placedOn = HitboxData.getBlockHitbox(this.player, this.player.inventory.getHeldItem().getType().getPlacedType(), this.player.getClientVersion(), this.block, true, this.position.x, this.position.y, this.position.z);
        ArrayList<SimpleCollisionBox> boxes = new ArrayList<SimpleCollisionBox>();
        placedOn.downCast(boxes);
        SimpleCollisionBox combined = new SimpleCollisionBox(this.position.x, (double)this.position.y, (double)this.position.z);
        for (SimpleCollisionBox box : boxes) {
            double minX = Math.max(box.minX, combined.minX);
            double minY = Math.max(box.minY, combined.minY);
            double minZ = Math.max(box.minZ, combined.minZ);
            double maxX = Math.min(box.maxX, combined.maxX);
            double maxY = Math.min(box.maxY, combined.maxY);
            double maxZ = Math.min(box.maxZ, combined.maxZ);
            combined = new SimpleCollisionBox(minX, minY, minZ, maxX, maxY, maxZ);
        }
        return combined;
    }

    @Generated
    public boolean isCancelled() {
        return this.cancelled;
    }
}

