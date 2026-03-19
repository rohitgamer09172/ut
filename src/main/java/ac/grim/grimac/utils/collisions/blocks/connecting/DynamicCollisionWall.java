/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.collisions.blocks.connecting;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.East;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.North;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.South;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.West;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.utils.collisions.CollisionData;
import ac.grim.grimac.utils.collisions.blocks.connecting.DynamicConnecting;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.CollisionFactory;
import ac.grim.grimac.utils.collisions.datatypes.ComplexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.HexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;

public class DynamicCollisionWall
extends DynamicConnecting
implements CollisionFactory {
    private static final CollisionBox[] COLLISION_BOXES = DynamicCollisionWall.makeShapes(4.0f, 3.0f, 24.0f, 0.0f, 24.0f, false, 1);
    private static final boolean isNewServer = PacketEvents.getAPI().getServerManager().getVersion().isNewerThan(ServerVersion.V_1_12_2);

    @Deprecated
    public CollisionBox fetchRegularBox(GrimPlayer player, WrappedBlockState state, ClientVersion version, int x, int y, int z) {
        int up = 0;
        int east = 0;
        int west = 0;
        int south = 0;
        int north = 0;
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThan(ServerVersion.V_1_12_2)) {
            boolean sixteen = PacketEvents.getAPI().getServerManager().getVersion().isNewerThan(ServerVersion.V_1_16);
            if (state.getNorth() != North.NONE) {
                north += state.getNorth() == North.LOW || sixteen ? 1 : 2;
            }
            if (state.getEast() != East.NONE) {
                east += state.getEast() == East.LOW || sixteen ? 1 : 2;
            }
            if (state.getSouth() != South.NONE) {
                south += state.getSouth() == South.LOW || sixteen ? 1 : 2;
            }
            if (state.getWest() != West.NONE) {
                west += state.getWest() == West.LOW || sixteen ? 1 : 2;
            }
            if (state.isUp()) {
                up = 1;
            }
        } else {
            north = this.connectsTo(player, version, x, y, z, BlockFace.NORTH) ? 1 : 0;
            south = this.connectsTo(player, version, x, y, z, BlockFace.SOUTH) ? 1 : 0;
            west = this.connectsTo(player, version, x, y, z, BlockFace.WEST) ? 1 : 0;
            east = this.connectsTo(player, version, x, y, z, BlockFace.EAST) ? 1 : 0;
            up = 1;
        }
        if (version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
            ComplexCollisionBox box = new ComplexCollisionBox(5);
            if (up == 1) {
                box.add(new HexCollisionBox(4.0, 0.0, 4.0, 12.0, 16.0, 12.0));
            }
            if (north == 1) {
                box.add(new HexCollisionBox(5.0, 0.0, 0.0, 11.0, 14.0, 11.0));
            } else if (north == 2) {
                box.add(new HexCollisionBox(5.0, 0.0, 0.0, 11.0, 16.0, 11.0));
            }
            if (south == 1) {
                box.add(new HexCollisionBox(5.0, 0.0, 5.0, 11.0, 14.0, 16.0));
            } else if (south == 2) {
                box.add(new HexCollisionBox(5.0, 0.0, 5.0, 11.0, 16.0, 16.0));
            }
            if (west == 1) {
                box.add(new HexCollisionBox(0.0, 0.0, 5.0, 11.0, 14.0, 11.0));
            } else if (west == 2) {
                box.add(new HexCollisionBox(0.0, 0.0, 5.0, 11.0, 16.0, 11.0));
            }
            if (east == 1) {
                box.add(new HexCollisionBox(5.0, 0.0, 5.0, 16.0, 14.0, 11.0));
            } else if (east == 2) {
                box.add(new HexCollisionBox(5.0, 0.0, 5.0, 16.0, 16.0, 11.0));
            }
            return box;
        }
        float f = 0.25f;
        float f1 = 0.75f;
        float f2 = 0.25f;
        float f3 = 0.75f;
        if (north == 1) {
            f2 = 0.0f;
        }
        if (south == 1) {
            f3 = 1.0f;
        }
        if (west == 1) {
            f = 0.0f;
        }
        if (east == 1) {
            f1 = 1.0f;
        }
        if (north == 1 && south == 1 && west != 0 && east != 0) {
            f = 0.3125f;
            f1 = 0.6875f;
        } else if (north != 1 && south != 1 && west == 0 && east == 0) {
            f2 = 0.3125f;
            f3 = 0.6875f;
        }
        return new SimpleCollisionBox(f, 0.0, f2, f1, 1.0, f3);
    }

    @Override
    public CollisionBox fetch(GrimPlayer player, ClientVersion version, WrappedBlockState block, int x, int y, int z) {
        boolean up;
        boolean east;
        boolean west;
        boolean south;
        boolean north;
        boolean isNewClient = version.isNewerThan(ClientVersion.V_1_12_2);
        if (isNewServer && isNewClient) {
            boolean north2 = block.getNorth() != North.NONE;
            boolean south2 = block.getSouth() != South.NONE;
            boolean west2 = block.getWest() != West.NONE;
            boolean east2 = block.getEast() != East.NONE;
            return block.isUp() ? COLLISION_BOXES[this.getAABBIndex(north2, east2, south2, west2)].copy().union(new HexCollisionBox(4.0, 0.0, 4.0, 12.0, 24.0, 12.0)) : COLLISION_BOXES[this.getAABBIndex(north2, east2, south2, west2)].copy();
        }
        boolean bl = isNewServer ? block.getNorth() != North.NONE : (north = this.connectsTo(player, version, x, y, z, BlockFace.NORTH));
        boolean bl2 = isNewServer ? block.getSouth() != South.NONE : (south = this.connectsTo(player, version, x, y, z, BlockFace.SOUTH));
        boolean bl3 = isNewServer ? block.getWest() != West.NONE : (west = this.connectsTo(player, version, x, y, z, BlockFace.WEST));
        boolean bl4 = isNewServer ? block.getEast() != East.NONE : (east = this.connectsTo(player, version, x, y, z, BlockFace.EAST));
        if (!isNewServer && isNewClient && !(up = this.connectsTo(player, version, x, y, z, BlockFace.UP))) {
            WrappedBlockState currBlock = player.compensatedWorld.getBlock(x, y, z);
            StateType currType = currBlock.getType();
            boolean selfNorth = currType == player.compensatedWorld.getBlock(x, y, z + 1).getType();
            boolean selfSouth = currType == player.compensatedWorld.getBlock(x, y, z - 1).getType();
            boolean selfWest = currType == player.compensatedWorld.getBlock(x - 1, y, z).getType();
            boolean selfEast = currType == player.compensatedWorld.getBlock(x + 1, y, z).getType();
            up = !(selfNorth && selfSouth && !selfWest && !selfEast || selfWest && selfEast && !selfNorth && !selfSouth);
            return up ? COLLISION_BOXES[this.getAABBIndex(north, east, south, west)].copy().union(new HexCollisionBox(4.0, 0.0, 4.0, 12.0, 24.0, 12.0)) : COLLISION_BOXES[this.getAABBIndex(north, east, south, west)].copy();
        }
        float f = 0.25f;
        float f1 = 0.75f;
        float f2 = 0.25f;
        float f3 = 0.75f;
        if (north) {
            f2 = 0.0f;
        }
        if (south) {
            f3 = 1.0f;
        }
        if (west) {
            f = 0.0f;
        }
        if (east) {
            f1 = 1.0f;
        }
        if (north && south && !west && !east) {
            f = 0.3125f;
            f1 = 0.6875f;
        } else if (!north && !south && west && east) {
            f2 = 0.3125f;
            f3 = 0.6875f;
        }
        return new SimpleCollisionBox(f, 0.0, f2, f1, 1.5, f3);
    }

    @Override
    public boolean checkCanConnect(GrimPlayer player, WrappedBlockState state, StateType one, StateType two, BlockFace direction) {
        return BlockTags.WALLS.contains(one) || CollisionData.getData(one).getMovementCollisionBox(player, player.getClientVersion(), state, 0, 0, 0).isSideFullBlock(direction);
    }
}

