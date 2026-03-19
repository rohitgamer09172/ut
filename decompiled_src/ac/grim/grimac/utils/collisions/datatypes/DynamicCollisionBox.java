/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.collisions.datatypes;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.CollisionFactory;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import java.util.List;
import lombok.Generated;

public class DynamicCollisionBox
implements CollisionBox {
    private final GrimPlayer player;
    private final CollisionFactory box;
    private ClientVersion version;
    private WrappedBlockState block;
    private int x;
    private int y;
    private int z;

    public DynamicCollisionBox(GrimPlayer player, ClientVersion version, CollisionFactory box, WrappedBlockState block) {
        this.player = player;
        this.version = version;
        this.box = box;
        this.block = block;
    }

    @Override
    public CollisionBox union(SimpleCollisionBox other) {
        CollisionBox dynamicBox = this.box.fetch(this.player, this.version, this.block, this.x, this.y, this.z).offset(this.x, this.y, this.z);
        return dynamicBox.union(other);
    }

    @Override
    public boolean isCollided(SimpleCollisionBox other) {
        return this.box.fetch(this.player, this.version, this.block, this.x, this.y, this.z).offset(this.x, this.y, this.z).isCollided(other);
    }

    @Override
    public boolean isIntersected(SimpleCollisionBox other) {
        return this.box.fetch(this.player, this.version, this.block, this.x, this.y, this.z).offset(this.x, this.y, this.z).isIntersected(other);
    }

    @Override
    public CollisionBox copy() {
        return new DynamicCollisionBox(this.player, this.version, this.box, this.block).offset(this.x, this.y, this.z);
    }

    @Override
    public CollisionBox offset(double x, double y, double z) {
        this.x = (int)((double)this.x + x);
        this.y = (int)((double)this.y + y);
        this.z = (int)((double)this.z + z);
        return this;
    }

    @Override
    public int downCast(SimpleCollisionBox[] list) {
        return this.box.fetch(this.player, this.version, this.block, this.x, this.y, this.z).offset(this.x, this.y, this.z).downCast(list);
    }

    @Override
    public void downCast(List<SimpleCollisionBox> list) {
        this.box.fetch(this.player, this.version, this.block, this.x, this.y, this.z).offset(this.x, this.y, this.z).downCast(list);
    }

    @Override
    public boolean isNull() {
        return this.box.fetch(this.player, this.version, this.block, this.x, this.y, this.z).isNull();
    }

    @Override
    public boolean isFullBlock() {
        return this.box.fetch(this.player, this.version, this.block, this.x, this.y, this.z).offset(this.x, this.y, this.z).isFullBlock();
    }

    @Generated
    public void setVersion(ClientVersion version) {
        this.version = version;
    }

    @Generated
    public void setBlock(WrappedBlockState block) {
        this.block = block;
    }
}

