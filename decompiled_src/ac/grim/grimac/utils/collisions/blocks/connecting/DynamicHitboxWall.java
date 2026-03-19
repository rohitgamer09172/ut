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
import ac.grim.grimac.utils.collisions.datatypes.ComplexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.HexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.HitBoxFactory;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;

public class DynamicHitboxWall
extends DynamicConnecting
implements HitBoxFactory {
    @Override
    public CollisionBox fetch(GrimPlayer player, StateType heldItem, ClientVersion version, WrappedBlockState state, boolean isTargetBlock, int x, int y, int z) {
        int[] connections = this.getConnections(player, version, state, x, y, z);
        int north = connections[0];
        int south = connections[1];
        int west = connections[2];
        int east = connections[3];
        int up = connections[4];
        if (version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
            return this.getModernHitBox(north, south, west, east, up);
        }
        return this.getLegacyHitBox(north, south, west, east);
    }

    private int[] getConnections(GrimPlayer player, ClientVersion version, WrappedBlockState state, int x, int y, int z) {
        int up;
        int west;
        int south;
        int east;
        int north;
        if (this.isModernServer()) {
            boolean sixteen = PacketEvents.getAPI().getServerManager().getVersion().isNewerThan(ServerVersion.V_1_16);
            north = this.getConnectionValue(state.getNorth(), sixteen);
            east = this.getConnectionValue(state.getEast(), sixteen);
            south = this.getConnectionValue(state.getSouth(), sixteen);
            west = this.getConnectionValue(state.getWest(), sixteen);
            up = state.isUp() ? 1 : 0;
        } else {
            north = this.connectsTo(player, version, x, y, z, BlockFace.NORTH) ? 1 : 0;
            south = this.connectsTo(player, version, x, y, z, BlockFace.SOUTH) ? 1 : 0;
            west = this.connectsTo(player, version, x, y, z, BlockFace.WEST) ? 1 : 0;
            east = this.connectsTo(player, version, x, y, z, BlockFace.EAST) ? 1 : 0;
            up = 1;
        }
        return new int[]{north, south, west, east, up};
    }

    private boolean isModernServer() {
        return PacketEvents.getAPI().getServerManager().getVersion().isNewerThan(ServerVersion.V_1_12_2);
    }

    private int getConnectionValue(Enum<?> direction, boolean sixteen) {
        if (direction == North.NONE || direction == East.NONE || direction == South.NONE || direction == West.NONE) {
            return 0;
        }
        return direction == North.LOW || direction == East.LOW || direction == South.LOW || direction == West.LOW || sixteen ? 1 : 2;
    }

    private CollisionBox getModernHitBox(int north, int south, int west, int east, int up) {
        ComplexCollisionBox box = new ComplexCollisionBox(5);
        if (up == 1) {
            box.add(new HexCollisionBox(4.0, 0.0, 4.0, 12.0, 16.0, 12.0));
        }
        this.addDirectionalBox(box, north, 5.0, 0.0, 0.0, 11.0, 14.0, 11.0);
        this.addDirectionalBox(box, south, 5.0, 0.0, 5.0, 11.0, 14.0, 16.0);
        this.addDirectionalBox(box, west, 0.0, 0.0, 5.0, 11.0, 14.0, 11.0);
        this.addDirectionalBox(box, east, 5.0, 0.0, 5.0, 16.0, 14.0, 11.0);
        return box;
    }

    private void addDirectionalBox(ComplexCollisionBox box, int direction, double x1, double y1, double z1, double x2, double y2, double z2) {
        if (direction == 1) {
            box.add(new HexCollisionBox(x1, y1, z1, x2, y2, z2));
        } else if (direction == 2) {
            box.add(new HexCollisionBox(x1, y1, z1, x2, 16.0, z2));
        }
    }

    private CollisionBox getLegacyHitBox(int north, int south, int west, int east) {
        float minX = 0.25f;
        float maxX = 0.75f;
        float minZ = 0.25f;
        float maxZ = 0.75f;
        float maxY = 1.0f;
        if (north == 1) {
            minZ = 0.0f;
        }
        if (south == 1) {
            maxZ = 1.0f;
        }
        if (west == 1) {
            minX = 0.0f;
        }
        if (east == 1) {
            maxX = 1.0f;
        }
        if (north == 1 && south == 1 && west == 0 && east == 0) {
            maxY = 0.8125f;
            minX = 0.3125f;
            maxX = 0.6875f;
        } else if (west == 1 && east == 1 && north == 0 && south == 0) {
            maxY = 0.8125f;
            minZ = 0.3125f;
            maxZ = 0.6875f;
        }
        return new SimpleCollisionBox(minX, 0.0, minZ, maxX, maxY, maxZ);
    }

    @Override
    public boolean checkCanConnect(GrimPlayer player, WrappedBlockState state, StateType one, StateType two, BlockFace direction) {
        return BlockTags.WALLS.contains(one) || CollisionData.getData(one).getMovementCollisionBox(player, player.getClientVersion(), state, 0, 0, 0).isSideFullBlock(direction);
    }
}

