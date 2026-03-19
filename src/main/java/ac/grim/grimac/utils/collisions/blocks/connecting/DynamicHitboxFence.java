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
import ac.grim.grimac.utils.collisions.datatypes.ComplexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.HitBoxFactory;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;

public class DynamicHitboxFence
extends DynamicConnecting
implements HitBoxFactory {
    private static final CollisionBox[] MODERN_HITBOXES = DynamicHitboxFence.makeShapes(2.0f, 2.0f, 24.0f, 0.0f, 24.0f, true, 1);
    private static final int MAX_MODERN_HITBOX_COMPLEX_COLLISION_BOX_SIZE = 5;
    public static final SimpleCollisionBox[] LEGACY_HITBOXES = new SimpleCollisionBox[]{new SimpleCollisionBox(0.375, 0.0, 0.375, 0.625, 1.0, 0.625), new SimpleCollisionBox(0.375, 0.0, 0.375, 0.625, 1.0, 1.0), new SimpleCollisionBox(0.0, 0.0, 0.375, 0.625, 1.0, 0.625), new SimpleCollisionBox(0.0, 0.0, 0.375, 0.625, 1.0, 1.0), new SimpleCollisionBox(0.375, 0.0, 0.0, 0.625, 1.0, 0.625), new SimpleCollisionBox(0.375, 0.0, 0.0, 0.625, 1.0, 1.0), new SimpleCollisionBox(0.0, 0.0, 0.0, 0.625, 1.0, 0.625), new SimpleCollisionBox(0.0, 0.0, 0.0, 0.625, 1.0, 1.0), new SimpleCollisionBox(0.375, 0.0, 0.375, 1.0, 1.0, 0.625), new SimpleCollisionBox(0.375, 0.0, 0.375, 1.0, 1.0, 1.0), new SimpleCollisionBox(0.0, 0.0, 0.375, 1.0, 1.0, 0.625), new SimpleCollisionBox(0.0, 0.0, 0.375, 1.0, 1.0, 1.0), new SimpleCollisionBox(0.375, 0.0, 0.0, 1.0, 1.0, 0.625), new SimpleCollisionBox(0.375, 0.0, 0.0, 1.0, 1.0, 1.0), new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 0.625), new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)};

    @Override
    public CollisionBox fetch(GrimPlayer player, StateType heldItem, ClientVersion version, WrappedBlockState block, boolean isTargetBlock, int x, int y, int z) {
        boolean west;
        boolean south;
        boolean north;
        boolean east;
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13) && version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
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
        return version.isNewerThanOrEquals(ClientVersion.V_1_12_2) ? this.getModernCollisionBox(north, east, south, west) : this.getLegacyCollisionBox(north, east, south, west);
    }

    private CollisionBox getLegacyCollisionBox(boolean north, boolean east, boolean south, boolean west) {
        return LEGACY_HITBOXES[this.getAABBIndex(north, east, south, west)].copy();
    }

    private CollisionBox getModernCollisionBox(boolean north, boolean east, boolean south, boolean west) {
        return MODERN_HITBOXES[this.getAABBIndex(north, east, south, west)].copy();
    }

    @Override
    public boolean checkCanConnect(GrimPlayer player, WrappedBlockState state, StateType one, StateType two, BlockFace direction) {
        if (BlockTags.FENCES.contains(one)) {
            return one != StateTypes.NETHER_BRICK_FENCE && two != StateTypes.NETHER_BRICK_FENCE;
        }
        return BlockTags.FENCES.contains(one) || CollisionData.getData(one).getMovementCollisionBox(player, player.getClientVersion(), state, 0, 0, 0).isSideFullBlock(direction);
    }

    static {
        SimpleCollisionBox[] boxes = new SimpleCollisionBox[5];
        for (int i = 1; i < MODERN_HITBOXES.length; ++i) {
            CollisionBox collisionBox = MODERN_HITBOXES[i];
            int size = collisionBox.downCast(boxes);
            for (int j = 0; j < size; ++j) {
                if (!(boxes[j].maxY > 1.0)) continue;
                boxes[j].maxY = 1.0;
            }
            DynamicHitboxFence.MODERN_HITBOXES[i] = size == 1 ? boxes[0] : new ComplexCollisionBox(size, boxes);
        }
    }
}

