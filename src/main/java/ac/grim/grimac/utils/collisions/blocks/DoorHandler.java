/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.collisions.blocks;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Half;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Hinge;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.CollisionFactory;
import ac.grim.grimac.utils.collisions.datatypes.HexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.NoCollisionBox;

public class DoorHandler
implements CollisionFactory {
    protected static final CollisionBox SOUTH_AABB = new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 16.0, 3.0);
    protected static final CollisionBox NORTH_AABB = new HexCollisionBox(0.0, 0.0, 13.0, 16.0, 16.0, 16.0);
    protected static final CollisionBox WEST_AABB = new HexCollisionBox(13.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    protected static final CollisionBox EAST_AABB = new HexCollisionBox(0.0, 0.0, 0.0, 3.0, 16.0, 16.0);

    @Override
    public CollisionBox fetch(GrimPlayer player, ClientVersion version, WrappedBlockState block, int x, int y, int z) {
        return switch (this.fetchDirection(player, version, block, x, y, z)) {
            case BlockFace.NORTH -> NORTH_AABB.copy();
            case BlockFace.SOUTH -> SOUTH_AABB.copy();
            case BlockFace.EAST -> EAST_AABB.copy();
            case BlockFace.WEST -> WEST_AABB.copy();
            default -> NoCollisionBox.INSTANCE;
        };
    }

    public BlockFace fetchDirection(GrimPlayer player, ClientVersion version, WrappedBlockState door, int x, int y, int z) {
        boolean isRightHinge;
        boolean isClosed;
        BlockFace facingDirection;
        if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_12_2) || version.isOlderThanOrEquals(ClientVersion.V_1_12_2)) {
            if (door.getHalf() == Half.LOWER) {
                WrappedBlockState above = player.compensatedWorld.getBlock(x, y + 1, z);
                facingDirection = door.getFacing();
                boolean bl = isClosed = !door.isOpen();
                isRightHinge = above.getType() == door.getType() ? above.getHinge() == Hinge.RIGHT : false;
            } else {
                WrappedBlockState below = player.compensatedWorld.getBlock(x, y - 1, z);
                if (below.getType() == door.getType() && below.getHalf() == Half.LOWER) {
                    isClosed = !below.isOpen();
                    facingDirection = below.getFacing();
                    isRightHinge = door.getHinge() == Hinge.RIGHT;
                } else {
                    facingDirection = BlockFace.EAST;
                    isClosed = true;
                    isRightHinge = false;
                }
            }
        } else {
            facingDirection = door.getFacing();
            isClosed = !door.isOpen();
            isRightHinge = door.getHinge() == Hinge.RIGHT;
        }
        return switch (facingDirection) {
            case BlockFace.SOUTH -> {
                if (isClosed) {
                    yield BlockFace.SOUTH;
                }
                if (isRightHinge) {
                    yield BlockFace.EAST;
                }
                yield BlockFace.WEST;
            }
            case BlockFace.WEST -> {
                if (isClosed) {
                    yield BlockFace.WEST;
                }
                if (isRightHinge) {
                    yield BlockFace.SOUTH;
                }
                yield BlockFace.NORTH;
            }
            case BlockFace.NORTH -> {
                if (isClosed) {
                    yield BlockFace.NORTH;
                }
                if (isRightHinge) {
                    yield BlockFace.WEST;
                }
                yield BlockFace.EAST;
            }
            default -> isClosed ? BlockFace.EAST : (isRightHinge ? BlockFace.NORTH : BlockFace.SOUTH);
        };
    }
}

