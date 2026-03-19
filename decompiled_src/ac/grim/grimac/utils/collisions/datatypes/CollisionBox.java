/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.collisions.datatypes;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import java.util.List;

public interface CollisionBox {
    public CollisionBox union(SimpleCollisionBox var1);

    public boolean isCollided(SimpleCollisionBox var1);

    public boolean isIntersected(SimpleCollisionBox var1);

    public CollisionBox copy();

    public CollisionBox offset(double var1, double var3, double var5);

    public void downCast(List<SimpleCollisionBox> var1);

    public int downCast(SimpleCollisionBox[] var1);

    public boolean isNull();

    public boolean isFullBlock();

    default public boolean isSideFullBlock(BlockFace axis) {
        return this.isFullBlock();
    }
}

