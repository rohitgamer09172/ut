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
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.utils.collisions.CollisionData;
import ac.grim.grimac.utils.collisions.blocks.connecting.DynamicConnecting;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.HitBoxFactory;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;

public class DynamicHitboxPane
extends DynamicConnecting
implements HitBoxFactory {
    private static final CollisionBox[] COLLISION_BOXES = DynamicHitboxPane.makeShapes(1.0f, 1.0f, 16.0f, 0.0f, 16.0f, true, 1);

    @Override
    public CollisionBox fetch(GrimPlayer player, StateType item, ClientVersion version, WrappedBlockState block, boolean isTargetBlock, int x, int y, int z) {
        boolean west;
        boolean south;
        boolean north;
        boolean east;
        if (this.isModernVersion(version)) {
            east = block.getEast() != East.FALSE;
            north = block.getNorth() != North.FALSE;
            south = block.getSouth() != South.FALSE;
            west = block.getWest() != West.FALSE;
        } else {
            east = this.connectsTo(player, version, x, y, z, BlockFace.EAST);
            north = this.connectsTo(player, version, x, y, z, BlockFace.NORTH);
            south = this.connectsTo(player, version, x, y, z, BlockFace.SOUTH);
            west = this.connectsTo(player, version, x, y, z, BlockFace.WEST);
        }
        if (this.shouldUseOldPaneShape(version, north, south, east, west)) {
            west = true;
            east = true;
            south = true;
            north = true;
        }
        return version.isNewerThanOrEquals(ClientVersion.V_1_9) ? this.getModernCollisionBox(north, east, south, west) : this.getLegacyCollisionBox(north, east, south, west);
    }

    private boolean isModernVersion(ClientVersion version) {
        return PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13) && version.isNewerThanOrEquals(ClientVersion.V_1_13);
    }

    private boolean shouldUseOldPaneShape(ClientVersion version, boolean north, boolean south, boolean east, boolean west) {
        return !north && !south && !east && !west && (version.isOlderThanOrEquals(ClientVersion.V_1_8) || PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_8_8) && version.isNewerThanOrEquals(ClientVersion.V_1_13));
    }

    private CollisionBox getModernCollisionBox(boolean north, boolean east, boolean south, boolean west) {
        return COLLISION_BOXES[this.getAABBIndex(north, east, south, west)].copy();
    }

    private CollisionBox getLegacyCollisionBox(boolean north, boolean east, boolean south, boolean west) {
        float minX = 0.4375f;
        float maxX = 0.5625f;
        float minZ = 0.4375f;
        float maxZ = 0.5625f;
        if ((!west || !east) && (west || east || north || south)) {
            if (west) {
                minX = 0.0f;
            } else if (east) {
                maxX = 1.0f;
            }
        } else {
            minX = 0.0f;
            maxX = 1.0f;
        }
        if ((!north || !south) && (west || east || north || south)) {
            if (north) {
                minZ = 0.0f;
            } else if (south) {
                maxZ = 1.0f;
            }
        } else {
            minZ = 0.0f;
            maxZ = 1.0f;
        }
        return new SimpleCollisionBox(minX, 0.0, minZ, maxX, 1.0, maxZ);
    }

    @Override
    public boolean canConnectToGlassBlock() {
        return true;
    }

    @Override
    public boolean checkCanConnect(GrimPlayer player, WrappedBlockState state, StateType one, StateType two, BlockFace direction) {
        if (BlockTags.GLASS_PANES.contains(one) || one == StateTypes.IRON_BARS) {
            return true;
        }
        return CollisionData.getData(one).getMovementCollisionBox(player, player.getClientVersion(), state, 0, 0, 0).isSideFullBlock(direction);
    }
}

