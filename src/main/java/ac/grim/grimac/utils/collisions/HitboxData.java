/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.collisions;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.East;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Face;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Half;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Leaves;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.North;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Shape;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.South;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Tilt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.West;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateValue;
import ac.grim.grimac.utils.collisions.CollisionData;
import ac.grim.grimac.utils.collisions.blocks.connecting.DynamicHitboxFence;
import ac.grim.grimac.utils.collisions.blocks.connecting.DynamicHitboxPane;
import ac.grim.grimac.utils.collisions.blocks.connecting.DynamicHitboxWall;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.ComplexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.HexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.HexOffsetCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.HitBoxFactory;
import ac.grim.grimac.utils.collisions.datatypes.NoCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.OffsetCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.lists.ArrayUtils;
import ac.grim.grimac.utils.nmsutil.Materials;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public enum HitboxData implements HitBoxFactory
{
    VINE((player, item, version, data, isTargetBlock, x, y, z) -> {
        ComplexCollisionBox boxes = new ComplexCollisionBox(5);
        if (data.getWest() == West.TRUE) {
            boxes.add(new HexCollisionBox(0.0, 0.0, 0.0, 1.0, 16.0, 16.0));
        }
        if (data.getEast() == East.TRUE) {
            boxes.add(new HexCollisionBox(15.0, 0.0, 0.0, 16.0, 16.0, 16.0));
        }
        if (data.getNorth() == North.TRUE) {
            boxes.add(new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 16.0, 1.0));
        }
        if (data.getSouth() == South.TRUE) {
            boxes.add(new HexCollisionBox(0.0, 0.0, 15.0, 16.0, 16.0, 16.0));
        }
        if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_12_2) && boxes.size() > 1) {
            return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
        }
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13) && data.isUp()) {
            boxes.add(new HexCollisionBox(0.0, 15.0, 0.0, 16.0, 16.0, 16.0));
        }
        return boxes;
    }, StateTypes.VINE),
    RAILS((player, item, version, data, isTargetBlock, x, y, z) -> switch (data.getShape()) {
        case Shape.ASCENDING_NORTH, Shape.ASCENDING_SOUTH, Shape.ASCENDING_EAST, Shape.ASCENDING_WEST -> {
            if (version.isOlderThan(ClientVersion.V_1_8)) {
                StateType railType = data.getType();
                if (railType == StateTypes.ACTIVATOR_RAIL || railType == StateTypes.POWERED_RAIL && data.isPowered()) {
                    yield new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 0.125, 1.0, false);
                }
                yield new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 0.625, 1.0, false);
            }
            if (version.isOlderThan(ClientVersion.V_1_9)) {
                yield new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 0.625, 1.0, false);
            }
            if (version.isNewerThanOrEquals(ClientVersion.V_1_9) && version.isOlderThan(ClientVersion.V_1_10)) {
                yield new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 0.15625, 1.0, false);
            }
            if (version.isOlderThan(ClientVersion.V_1_11)) {
                yield new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
            }
            yield new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
        }
        default -> new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
    }, BlockTags.RAILS.getStates().toArray(new StateType[0])),
    END_PORTAL((player, item, version, data, isTargetBlock, x, y, z) -> {
        if (version.isOlderThan(ClientVersion.V_1_9)) {
            return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0);
        }
        if (version.isOlderThan(ClientVersion.V_1_17)) {
            return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 0.75, 1.0);
        }
        return new HexCollisionBox(0.0, 6.0, 0.0, 16.0, 12.0, 16.0);
    }, StateTypes.END_PORTAL),
    FENCE_GATE((player, item, version, data, isTargetBlock, x, y, z) -> {
        boolean isInWall;
        boolean isXAxis;
        boolean bl = isXAxis = data.getFacing() == BlockFace.WEST || data.getFacing() == BlockFace.EAST;
        if (isXAxis) {
            boolean zPosWall = Materials.isWall(player.compensatedWorld.getBlockType(x, y, z + 1));
            boolean zNegWall = Materials.isWall(player.compensatedWorld.getBlockType(x, y, z - 1));
            isInWall = zPosWall || zNegWall;
        } else {
            boolean xPosWall = Materials.isWall(player.compensatedWorld.getBlockType(x + 1, y, z));
            boolean xNegWall = Materials.isWall(player.compensatedWorld.getBlockType(x - 1, y, z));
            boolean bl2 = isInWall = xPosWall || xNegWall;
        }
        if (isInWall) {
            return isXAxis ? new HexCollisionBox(6.0, 0.0, 0.0, 10.0, 13.0, 16.0) : new HexCollisionBox(0.0, 0.0, 6.0, 16.0, 13.0, 10.0);
        }
        return isXAxis ? new HexCollisionBox(6.0, 0.0, 0.0, 10.0, 16.0, 16.0) : new HexCollisionBox(0.0, 0.0, 6.0, 16.0, 16.0, 10.0);
    }, BlockTags.FENCE_GATES.getStates().toArray(new StateType[0])),
    FENCE(new DynamicHitboxFence(), BlockTags.FENCES.getStates().toArray(new StateType[0])),
    PANE(new DynamicHitboxPane(), Materials.getPanes().toArray(new StateType[0])),
    LEVER((player, item, version, data, isTargetBlock, x, y, z) -> {
        Face face = data.getFace();
        BlockFace facing = data.getFacing();
        if (version.isOlderThan(ClientVersion.V_1_13)) {
            double f = 0.1875;
            switch (face) {
                case WALL: {
                    switch (facing) {
                        case WEST: {
                            return new SimpleCollisionBox(1.0 - f * 2.0, 0.2, 0.5 - f, 1.0, 0.8, 0.5 + f, false);
                        }
                        case EAST: {
                            return new SimpleCollisionBox(0.0, 0.2, 0.5 - f, f * 2.0, 0.8, 0.5 + f, false);
                        }
                        case NORTH: {
                            return new SimpleCollisionBox(0.5 - f, 0.2, 1.0 - f * 2.0, 0.5 + f, 0.8, 1.0, false);
                        }
                        case SOUTH: {
                            return new SimpleCollisionBox(0.5 - f, 0.2, 0.0, 0.5 + f, 0.8, f * 2.0, false);
                        }
                    }
                }
                case CEILING: {
                    return new SimpleCollisionBox(0.25, 0.4, 0.25, 0.75, 1.0, 0.75, false);
                }
                case FLOOR: {
                    return new SimpleCollisionBox(0.25, 0.0, 0.25, 0.75, 0.6, 0.75, false);
                }
            }
        }
        return switch (face) {
            case Face.FLOOR -> {
                if (facing == BlockFace.EAST || facing == BlockFace.WEST) {
                    yield new SimpleCollisionBox(0.25, 0.0, 0.3125, 0.75, 0.375, 0.6875, false);
                }
                yield new SimpleCollisionBox(0.3125, 0.0, 0.25, 0.6875, 0.375, 0.75, false);
            }
            case Face.WALL -> {
                switch (facing) {
                    case EAST: {
                        yield new SimpleCollisionBox(0.0, 0.25, 0.3125, 0.375, 0.75, 0.6875, false);
                    }
                    case WEST: {
                        yield new SimpleCollisionBox(0.625, 0.25, 0.3125, 1.0, 0.75, 0.6875, false);
                    }
                    case SOUTH: {
                        yield new SimpleCollisionBox(0.3125, 0.25, 0.0, 0.6875, 0.75, 0.375, false);
                    }
                }
                yield new SimpleCollisionBox(0.3125, 0.25, 0.625, 0.6875, 0.75, 1.0, false);
            }
            default -> facing == BlockFace.EAST || facing == BlockFace.WEST ? new SimpleCollisionBox(0.25, 0.625, 0.3125, 0.75, 1.0, 0.6875, false) : new SimpleCollisionBox(0.3125, 0.625, 0.25, 0.6875, 1.0, 0.75, false);
        };
    }, StateTypes.LEVER),
    BUTTON((player, item, version, data, isTargetBlock, x, y, z) -> {
        Face face = data.getFace();
        BlockFace facing = data.getFacing();
        boolean powered = data.isPowered();
        if (version.isOlderThan(ClientVersion.V_1_13)) {
            double f2 = (double)(data.isPowered() ? 1 : 2) / 16.0;
            switch (face) {
                case WALL: {
                    switch (facing) {
                        case WEST: {
                            return new SimpleCollisionBox(1.0 - f2, 0.375, 0.3125, 1.0, 0.625, 0.6875, false);
                        }
                        case EAST: {
                            return new SimpleCollisionBox(0.0, 0.375, 0.3125, f2, 0.625, 0.6875, false);
                        }
                        case NORTH: {
                            return new SimpleCollisionBox(0.3125, 0.375, 1.0 - f2, 0.6875, 0.625, 1.0, false);
                        }
                        case SOUTH: {
                            return new SimpleCollisionBox(0.3125, 0.375, 0.0, 0.6875, 0.625, f2, false);
                        }
                    }
                }
                case CEILING: {
                    return new SimpleCollisionBox(0.3125, 1.0 - f2, 0.375, 0.6875, 1.0, 0.625, false);
                }
                case FLOOR: {
                    return new SimpleCollisionBox(0.3125, 0.0, 0.375, 0.6875, 0.0 + f2, 0.625, false);
                }
            }
        }
        switch (face) {
            case WALL: {
                return switch (facing) {
                    case BlockFace.EAST -> {
                        if (powered) {
                            yield new HexCollisionBox(0.0, 6.0, 5.0, 1.0, 10.0, 11.0);
                        }
                        yield new HexCollisionBox(0.0, 6.0, 5.0, 2.0, 10.0, 11.0);
                    }
                    case BlockFace.WEST -> {
                        if (powered) {
                            yield new HexCollisionBox(15.0, 6.0, 5.0, 16.0, 10.0, 11.0);
                        }
                        yield new HexCollisionBox(14.0, 6.0, 5.0, 16.0, 10.0, 11.0);
                    }
                    case BlockFace.SOUTH -> {
                        if (powered) {
                            yield new HexCollisionBox(5.0, 6.0, 0.0, 11.0, 10.0, 1.0);
                        }
                        yield new HexCollisionBox(5.0, 6.0, 0.0, 11.0, 10.0, 2.0);
                    }
                    case BlockFace.NORTH, BlockFace.UP, BlockFace.DOWN -> {
                        if (powered) {
                            yield new HexCollisionBox(5.0, 6.0, 15.0, 11.0, 10.0, 16.0);
                        }
                        yield new HexCollisionBox(5.0, 6.0, 14.0, 11.0, 10.0, 16.0);
                    }
                    default -> NoCollisionBox.INSTANCE;
                };
            }
            case CEILING: {
                if (player.getClientVersion().isOlderThan(ClientVersion.V_1_8)) {
                    return HitboxData.LEVER.dynamic.fetch(player, item, version, data, isTargetBlock, x, y, z);
                }
                if (facing == BlockFace.EAST || facing == BlockFace.WEST) {
                    return powered ? new HexCollisionBox(6.0, 15.0, 5.0, 10.0, 16.0, 11.0) : new HexCollisionBox(6.0, 14.0, 5.0, 10.0, 16.0, 11.0);
                }
                return powered ? new HexCollisionBox(5.0, 15.0, 6.0, 11.0, 16.0, 10.0) : new HexCollisionBox(5.0, 14.0, 6.0, 11.0, 16.0, 10.0);
            }
            case FLOOR: {
                if (player.getClientVersion().isOlderThan(ClientVersion.V_1_8)) {
                    return HitboxData.LEVER.dynamic.fetch(player, item, version, data, isTargetBlock, x, y, z);
                }
                if (facing == BlockFace.EAST || facing == BlockFace.WEST) {
                    return powered ? new HexCollisionBox(6.0, 0.0, 5.0, 10.0, 1.0, 11.0) : new HexCollisionBox(6.0, 0.0, 5.0, 10.0, 2.0, 11.0);
                }
                return powered ? new HexCollisionBox(5.0, 0.0, 6.0, 11.0, 1.0, 10.0) : new HexCollisionBox(5.0, 0.0, 6.0, 11.0, 2.0, 10.0);
            }
        }
        throw new IllegalStateException();
    }, BlockTags.BUTTONS.getStates().toArray(new StateType[0])),
    WALL(new DynamicHitboxWall(), BlockTags.WALLS.getStates().toArray(new StateType[0])),
    WALL_SIGN((player, item, version, data, isTargetBlock, x, y, z) -> switch (data.getFacing()) {
        case BlockFace.NORTH -> new HexCollisionBox(0.0, 4.5, 14.0, 16.0, 12.5, 16.0);
        case BlockFace.SOUTH -> new HexCollisionBox(0.0, 4.5, 0.0, 16.0, 12.5, 2.0);
        case BlockFace.EAST -> new HexCollisionBox(0.0, 4.5, 0.0, 2.0, 12.5, 16.0);
        case BlockFace.WEST -> new HexCollisionBox(14.0, 4.5, 0.0, 16.0, 12.5, 16.0);
        default -> NoCollisionBox.INSTANCE;
    }, BlockTags.WALL_SIGNS.getStates().toArray(new StateType[0])),
    CEILING_HANGING_SIGNS((player, item, version, data, isTargetBlock, x, y, z) -> switch (data.getRotation()) {
        case 0, 8 -> new HexCollisionBox(1.0, 0.0, 7.0, 15.0, 10.0, 9.0);
        case 4, 12 -> new HexCollisionBox(7.0, 0.0, 1.0, 9.0, 10.0, 15.0);
        default -> new HexCollisionBox(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);
    }, BlockTags.CEILING_HANGING_SIGNS.getStates().toArray(new StateType[0])),
    WALL_HANGING_SIGN((player, item, version, data, isTargetBlock, x, y, z) -> switch (data.getFacing()) {
        case BlockFace.SOUTH, BlockFace.NORTH -> new ComplexCollisionBox(2, new HexCollisionBox(0.0, 14.0, 6.0, 16.0, 16.0, 10.0), new HexCollisionBox(1.0, 0.0, 7.0, 15.0, 10.0, 9.0));
        default -> new ComplexCollisionBox(2, new HexCollisionBox(6.0, 14.0, 0.0, 10.0, 16.0, 16.0), new HexCollisionBox(7.0, 0.0, 1.0, 9.0, 10.0, 15.0));
    }, BlockTags.WALL_HANGING_SIGNS.getStates().toArray(new StateType[0])),
    STANDING_SIGN((player, item, version, data, isTargetBlock, x, y, z) -> new HexCollisionBox(4.0, 0.0, 4.0, 12.0, 16.0, 12.0), BlockTags.STANDING_SIGNS.getStates().toArray(new StateType[0])),
    SAPLING(new HexCollisionBox(2.0, 0.0, 2.0, 14.0, 12.0, 14.0), (StateType[])BlockTags.SAPLINGS.getStates().stream().filter(s -> s != StateTypes.AZALEA && s != StateTypes.FLOWERING_AZALEA).toArray(StateType[]::new)),
    ROOTS(new HexCollisionBox(2.0, 0.0, 2.0, 14.0, 13.0, 14.0), StateTypes.WARPED_ROOTS, StateTypes.CRIMSON_ROOTS),
    BANNER(new HexCollisionBox(4.0, 0.0, 4.0, 12.0, 16.0, 12.0), StateTypes.WHITE_BANNER, StateTypes.ORANGE_BANNER, StateTypes.MAGENTA_BANNER, StateTypes.LIGHT_BLUE_BANNER, StateTypes.YELLOW_BANNER, StateTypes.LIME_BANNER, StateTypes.PINK_BANNER, StateTypes.GRAY_BANNER, StateTypes.LIGHT_GRAY_BANNER, StateTypes.CYAN_BANNER, StateTypes.PURPLE_BANNER, StateTypes.BLUE_BANNER, StateTypes.BROWN_BANNER, StateTypes.GREEN_BANNER, StateTypes.RED_BANNER, StateTypes.BLACK_BANNER),
    WALL_BANNER((player, item, version, data, isTargetBlock, x, y, z) -> {
        if (version.isOlderThan(ClientVersion.V_1_8)) {
            return HitboxData.WALL_SIGN.dynamic.fetch(player, item, version, data, isTargetBlock, x, y, z);
        }
        return switch (data.getFacing()) {
            case BlockFace.NORTH -> new HexCollisionBox(0.0, 0.0, 14.0, 16.0, 12.5, 16.0);
            case BlockFace.EAST -> new HexCollisionBox(0.0, 0.0, 0.0, 2.0, 12.5, 16.0);
            case BlockFace.WEST -> new HexCollisionBox(14.0, 0.0, 0.0, 16.0, 12.5, 16.0);
            case BlockFace.SOUTH -> new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 12.5, 2.0);
            default -> throw new IllegalStateException("Impossible Banner Facing State; Something very wrong is going on");
        };
    }, StateTypes.WHITE_WALL_BANNER, StateTypes.ORANGE_WALL_BANNER, StateTypes.MAGENTA_WALL_BANNER, StateTypes.LIGHT_BLUE_WALL_BANNER, StateTypes.YELLOW_WALL_BANNER, StateTypes.LIME_WALL_BANNER, StateTypes.PINK_WALL_BANNER, StateTypes.GRAY_WALL_BANNER, StateTypes.LIGHT_GRAY_WALL_BANNER, StateTypes.CYAN_WALL_BANNER, StateTypes.PURPLE_WALL_BANNER, StateTypes.BLUE_WALL_BANNER, StateTypes.BROWN_WALL_BANNER, StateTypes.GREEN_WALL_BANNER, StateTypes.RED_WALL_BANNER, StateTypes.BLACK_WALL_BANNER),
    BREWING_STAND((player, item, version, block, isTargetBlock, x, y, z) -> {
        if (version.isOlderThan(ClientVersion.V_1_13)) {
            if (isTargetBlock && block.getType() == StateTypes.BREWING_STAND && player.getClientVersion().equals((Object)ClientVersion.V_1_8)) {
                return new ComplexCollisionBox(2, new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 0.125, 1.0), new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true));
            }
            return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
        }
        return new ComplexCollisionBox(2, new HexCollisionBox(1.0, 0.0, 1.0, 15.0, 2.0, 15.0), new SimpleCollisionBox(0.4375, 0.0, 0.4375, 0.5625, 0.875, 0.5625, false));
    }, StateTypes.BREWING_STAND),
    SMALL_FLOWER((player, item, version, data, isTargetBlock, x, y, z) -> player.getClientVersion().isOlderThan(ClientVersion.V_1_13) ? new SimpleCollisionBox(0.3125, 0.0, 0.3125, 0.6875, 0.625, 0.6875) : new OffsetCollisionBox(data.getType(), 0.3125, 0.0, 0.3125, 0.6875, 0.625, 0.6875), BlockTags.SMALL_FLOWERS.getStates().toArray(new StateType[0])),
    TALL_FLOWERS(new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true), BlockTags.TALL_FLOWERS.getStates().toArray(new StateType[0])),
    FIRE((player, item, version, data, isTargetBlock, x, y, z) -> {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_16)) {
            ComplexCollisionBox boxes = new ComplexCollisionBox(5);
            if (data.getWest() == West.TRUE) {
                boxes.add(new HexCollisionBox(0.0, 0.0, 0.0, 1.0, 16.0, 16.0));
            }
            if (data.getEast() == East.TRUE) {
                boxes.add(new HexCollisionBox(15.0, 0.0, 0.0, 16.0, 16.0, 16.0));
            }
            if (data.getNorth() == North.TRUE) {
                boxes.add(new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 16.0, 1.0));
            }
            if (data.getSouth() == South.TRUE) {
                boxes.add(new HexCollisionBox(0.0, 0.0, 15.0, 16.0, 16.0, 16.0));
            }
            if (data.hasProperty(StateValue.UP) && data.isUp()) {
                boxes.add(new HexCollisionBox(0.0, 15.0, 0.0, 16.0, 16.0, 16.0));
            }
            if (boxes.isNull()) {
                return new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
            }
            return boxes;
        }
        return NoCollisionBox.INSTANCE;
    }, BlockTags.FIRE.getStates().toArray(new StateType[0])),
    HONEY_BLOCK(new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true), StateTypes.HONEY_BLOCK),
    POWDER_SNOW(new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true), StateTypes.POWDER_SNOW),
    SOUL_SAND(new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true), StateTypes.SOUL_SAND),
    CACTUS((player, item, version, data, isTargetBlock, x, y, z) -> {
        if (version.isOlderThan(ClientVersion.V_1_13)) {
            return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
        }
        return new HexCollisionBox(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);
    }, StateTypes.CACTUS),
    SNOW((player, item, version, data, isTargetBlock, x, y, z) -> new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, (double)data.getLayers() * 0.125, 1.0), StateTypes.SNOW),
    LECTERN_BLOCK((player, item, version, data, isTargetBlock, x, y, z) -> {
        ComplexCollisionBox common = new ComplexCollisionBox(5, new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), new HexCollisionBox(4.0, 2.0, 4.0, 12.0, 14.0, 12.0));
        if (data.getFacing() == BlockFace.WEST) {
            common.add(new HexCollisionBox(1.0, 10.0, 0.0, 5.333333, 14.0, 16.0));
            common.add(new HexCollisionBox(5.333333, 12.0, 0.0, 9.666667, 16.0, 16.0));
            common.add(new HexCollisionBox(9.666667, 14.0, 0.0, 14.0, 18.0, 16.0));
        } else if (data.getFacing() == BlockFace.NORTH) {
            common.add(new HexCollisionBox(0.0, 10.0, 1.0, 16.0, 14.0, 5.333333));
            common.add(new HexCollisionBox(0.0, 12.0, 5.333333, 16.0, 16.0, 9.666667));
            common.add(new HexCollisionBox(0.0, 14.0, 9.666667, 16.0, 18.0, 14.0));
        } else if (data.getFacing() == BlockFace.EAST) {
            common.add(new HexCollisionBox(10.666667, 10.0, 0.0, 15.0, 14.0, 16.0));
            common.add(new HexCollisionBox(6.333333, 12.0, 0.0, 10.666667, 16.0, 16.0));
            common.add(new HexCollisionBox(2.0, 14.0, 0.0, 6.333333, 18.0, 16.0));
        } else {
            common.add(new HexCollisionBox(0.0, 10.0, 10.666667, 16.0, 14.0, 15.0));
            common.add(new HexCollisionBox(0.0, 12.0, 6.333333, 16.0, 16.0, 10.666667));
            common.add(new HexCollisionBox(0.0, 14.0, 2.0, 16.0, 18.0, 6.333333));
        }
        return common;
    }, StateTypes.LECTERN),
    GLOW_LICHEN_SCULK_VEIN((player, item, version, data, isTargetBlock, x, y, z) -> {
        if (version.isNewerThan(ClientVersion.V_1_16_4)) {
            ComplexCollisionBox box = new ComplexCollisionBox(6);
            if (data.isUp()) {
                box.add(new HexCollisionBox(0.0, 15.0, 0.0, 16.0, 16.0, 16.0));
            }
            if (data.isDown()) {
                box.add(new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 1.0, 16.0));
            }
            if (data.getWest() == West.TRUE) {
                box.add(new HexCollisionBox(0.0, 0.0, 0.0, 1.0, 16.0, 16.0));
            }
            if (data.getEast() == East.TRUE) {
                box.add(new HexCollisionBox(15.0, 0.0, 0.0, 16.0, 16.0, 16.0));
            }
            if (data.getNorth() == North.TRUE) {
                box.add(new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 16.0, 1.0));
            }
            if (data.getSouth() == South.TRUE) {
                box.add(new HexCollisionBox(0.0, 0.0, 15.0, 16.0, 16.0, 16.0));
            }
            return box;
        }
        return NoCollisionBox.INSTANCE;
    }, StateTypes.GLOW_LICHEN, StateTypes.SCULK_VEIN, StateTypes.RESIN_CLUMP),
    SPORE_BLOSSOM((player, item, version, data, isTargetBlock, x, y, z) -> {
        if (version.isNewerThan(ClientVersion.V_1_16_4)) {
            return new HexCollisionBox(2.0, 13.0, 2.0, 14.0, 16.0, 14.0);
        }
        return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
    }, StateTypes.SPORE_BLOSSOM),
    PITCHER_CROP((player, item, version, data, isTargetBlock, x, y, z) -> {
        if (version.isNewerThan(ClientVersion.V_1_19_4)) {
            HexCollisionBox FULL_UPPER_SHAPE = new HexCollisionBox(3.0, 0.0, 3.0, 13.0, 15.0, 13.0);
            HexCollisionBox FULL_LOWER_SHAPE = new HexCollisionBox(3.0, -1.0, 3.0, 13.0, 16.0, 13.0);
            HexCollisionBox COLLISION_SHAPE_BULB = new HexCollisionBox(5.0, -1.0, 5.0, 11.0, 3.0, 11.0);
            HexCollisionBox COLLISION_SHAPE_CROP = new HexCollisionBox(3.0, -1.0, 3.0, 13.0, 5.0, 13.0);
            SimpleCollisionBox[] UPPER_SHAPE_BY_AGE = new SimpleCollisionBox[]{new HexCollisionBox(3.0, 0.0, 3.0, 13.0, 11.0, 13.0), FULL_UPPER_SHAPE};
            SimpleCollisionBox[] LOWER_SHAPE_BY_AGE = new SimpleCollisionBox[]{COLLISION_SHAPE_BULB, new HexCollisionBox(3.0, -1.0, 3.0, 13.0, 14.0, 13.0), FULL_LOWER_SHAPE, FULL_LOWER_SHAPE, FULL_LOWER_SHAPE};
            return data.getHalf() == Half.UPPER ? UPPER_SHAPE_BY_AGE[Math.min(Math.abs(4 - (data.getAge() + 1)), UPPER_SHAPE_BY_AGE.length - 1)] : LOWER_SHAPE_BY_AGE[data.getAge()];
        }
        return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
    }, StateTypes.PITCHER_CROP),
    WHEAT_BEETROOTS((player, item, version, data, isTargetBlock, x, y, z) -> new HexCollisionBox(0.0, 0.0, 0.0, 16.0, (data.getAge() + 1) * 2, 16.0), StateTypes.WHEAT, StateTypes.BEETROOTS),
    CARROT_POTATOES((player, item, version, data, isTargetBlock, x, y, z) -> new HexCollisionBox(0.0, 0.0, 0.0, 16.0, data.getAge() + 2, 16.0), StateTypes.CARROTS, StateTypes.POTATOES),
    NETHER_WART((player, item, version, data, isTargetBlock, x, y, z) -> new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 5 + data.getAge() * 3, 16.0), StateTypes.NETHER_WART),
    ATTACHED_PUMPKIN_STEM((player, item, version, data, isTargetBlock, x, y, z) -> {
        if (version.isOlderThan(ClientVersion.V_1_13)) {
            return new HexCollisionBox(7.0, 0.0, 7.0, 9.0, 16.0, 9.0);
        }
        return switch (data.getFacing()) {
            case BlockFace.SOUTH -> new HexCollisionBox(6.0, 0.0, 6.0, 10.0, 10.0, 16.0);
            case BlockFace.WEST -> new HexCollisionBox(0.0, 0.0, 6.0, 10.0, 10.0, 10.0);
            case BlockFace.NORTH -> new HexCollisionBox(6.0, 0.0, 0.0, 10.0, 10.0, 10.0);
            default -> new HexCollisionBox(6.0, 0.0, 6.0, 16.0, 10.0, 10.0);
        };
    }, StateTypes.ATTACHED_MELON_STEM, StateTypes.ATTACHED_PUMPKIN_STEM),
    PUMPKIN_STEM((player, item, version, data, isTargetBlock, x, y, z) -> new HexCollisionBox(7.0, 0.0, 7.0, 9.0, 2 * (data.getAge() + 1), 9.0), StateTypes.PUMPKIN_STEM, StateTypes.MELON_STEM),
    COCOA_BEANS((player, item, version, data, isTargetBlock, x, y, z) -> CollisionData.getCocoa(version, data.getAge(), data.getFacing()), StateTypes.COCOA),
    REDSTONE_WIRE(NoCollisionBox.INSTANCE, StateTypes.REDSTONE_WIRE),
    SWEET_BERRY((player, item, version, data, isTargetBlock, x, y, z) -> {
        if (data.getAge() == 0) {
            return new HexCollisionBox(3.0, 0.0, 3.0, 13.0, 8.0, 13.0);
        }
        if (data.getAge() < 3) {
            return new HexCollisionBox(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);
        }
        return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
    }, StateTypes.SWEET_BERRY_BUSH),
    CORAL_PLANTS(new HexCollisionBox(2.0, 0.0, 2.0, 14.0, 15.0, 14.0), ArrayUtils.combine(BlockTags.CORAL_PLANTS.getStates(), StateTypes.DEAD_TUBE_CORAL, StateTypes.DEAD_BRAIN_CORAL, StateTypes.DEAD_BUBBLE_CORAL, StateTypes.DEAD_FIRE_CORAL, StateTypes.DEAD_HORN_CORAL)),
    CORAL_FAN(new HexCollisionBox(2.0, 0.0, 2.0, 14.0, 4.0, 14.0), StateTypes.TUBE_CORAL_FAN, StateTypes.BRAIN_CORAL_FAN, StateTypes.BUBBLE_CORAL_FAN, StateTypes.FIRE_CORAL_FAN, StateTypes.HORN_CORAL_FAN, StateTypes.DEAD_TUBE_CORAL_FAN, StateTypes.DEAD_BRAIN_CORAL_FAN, StateTypes.DEAD_BUBBLE_CORAL_FAN, StateTypes.DEAD_FIRE_CORAL_FAN, StateTypes.DEAD_HORN_CORAL_FAN),
    CORAL_WALL_FAN((player, item, version, data, isTargetBlock, x, y, z) -> switch (data.getFacing()) {
        case BlockFace.NORTH -> new HexCollisionBox(0.0, 4.0, 5.0, 16.0, 12.0, 16.0);
        case BlockFace.SOUTH -> new HexCollisionBox(0.0, 4.0, 0.0, 16.0, 12.0, 11.0);
        case BlockFace.WEST -> new HexCollisionBox(5.0, 4.0, 0.0, 16.0, 12.0, 16.0);
        case BlockFace.EAST -> new HexCollisionBox(0.0, 4.0, 0.0, 11.0, 12.0, 16.0);
        default -> throw new UnsupportedOperationException();
    }, ArrayUtils.combine(BlockTags.WALL_CORALS.getStates(), StateTypes.DEAD_TUBE_CORAL_WALL_FAN, StateTypes.DEAD_BRAIN_CORAL_WALL_FAN, StateTypes.DEAD_BUBBLE_CORAL_WALL_FAN, StateTypes.DEAD_FIRE_CORAL_WALL_FAN, StateTypes.DEAD_HORN_CORAL_WALL_FAN)),
    TORCHFLOWER_CROP((player, item, version, data, isTargetBlock, x, y, z) -> {
        if (data.getAge() == 0) {
            return new HexCollisionBox(5.0, 0.0, 5.0, 11.0, 6.0, 11.0);
        }
        return new HexCollisionBox(5.0, 0.0, 5.0, 11.0, 10.0, 11.0);
    }, StateTypes.TORCHFLOWER_CROP),
    DEAD_BUSH(new HexCollisionBox(2.0, 0.0, 2.0, 14.0, 13.0, 14.0), StateTypes.DEAD_BUSH),
    SUGARCANE(new HexCollisionBox(2.0, 0.0, 2.0, 14.0, 16.0, 14.0), StateTypes.SUGAR_CANE),
    NETHER_SPROUTS(new HexCollisionBox(2.0, 0.0, 2.0, 14.0, 3.0, 14.0), StateTypes.NETHER_SPROUTS),
    HANGING_ROOTS(new HexCollisionBox(2.0, 10.0, 2.0, 14.0, 16.0, 14.0), StateTypes.HANGING_ROOTS),
    HANGING_MOSS((player, item, version, data, isTargetBlock, x, y, z) -> {
        if (version.isOlderThan(ClientVersion.V_1_21_2)) {
            return HANGING_ROOTS.fetch(player, item, version, data, isTargetBlock, x, y, z);
        }
        return new HexCollisionBox(1.0, data.isTip() ? 2.0 : 0.0, 1.0, 15.0, 16.0, 15.0);
    }, StateTypes.PALE_HANGING_MOSS),
    GRASS_FERN((player, item, version, data, isTargetBlock, x, y, z) -> {
        if (version.isOlderThan(ClientVersion.V_1_13)) {
            return new SimpleCollisionBox(0.1f, 0.0, 0.1f, 0.9f, 0.8f, 0.9f);
        }
        return new HexCollisionBox(2.0, 0.0, 2.0, 14.0, 13.0, 14.0);
    }, StateTypes.SHORT_GRASS, StateTypes.FERN),
    SEA_GRASS(new HexCollisionBox(2.0, 0.0, 2.0, 14.0, 12.0, 14.0), StateTypes.SEAGRASS),
    TALL_SEAGRASS(new HexCollisionBox(2.0, 0.0, 2.0, 14.0, 16.0, 14.0), StateTypes.TALL_SEAGRASS),
    SMALL_DRIPLEAF(new HexCollisionBox(2.0, 0.0, 2.0, 14.0, 13.0, 14.0), StateTypes.SMALL_DRIPLEAF),
    CAVE_VINES(new HexCollisionBox(1.0, 0.0, 1.0, 15.0, 16.0, 15.0), StateTypes.CAVE_VINES, StateTypes.CAVE_VINES_PLANT),
    MUSHROOM(new HexCollisionBox(5.0, 0.0, 5.0, 11.0, 6.0, 11.0), StateTypes.BROWN_MUSHROOM, StateTypes.RED_MUSHROOM),
    FUNGUS(new HexCollisionBox(4.0, 0.0, 4.0, 12.0, 9.0, 12.0), StateTypes.CRIMSON_FUNGUS, StateTypes.WARPED_FUNGUS),
    TWISTING_VINES_BLOCK((player, item, version, data, isTargetBlock, x, y, z) -> HitboxData.getVineCollisionBox(version, false, true), StateTypes.TWISTING_VINES),
    WEEPING_VINES_BLOCK((player, item, version, data, isTargetBlock, x, y, z) -> HitboxData.getVineCollisionBox(version, true, true), StateTypes.WEEPING_VINES),
    TWISTING_VINES((player, item, version, data, isTargetBlock, x, y, z) -> HitboxData.getVineCollisionBox(version, false, false), StateTypes.TWISTING_VINES_PLANT),
    WEEPING_VINES((player, item, version, data, isTargetBlock, x, y, z) -> HitboxData.getVineCollisionBox(version, true, false), StateTypes.WEEPING_VINES_PLANT),
    TALL_PLANT(new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true), StateTypes.TALL_GRASS, StateTypes.LARGE_FERN),
    BAMBOO((player, item, version, data, isTargetBlock, x, y, z) -> data.getLeaves() == Leaves.LARGE ? new HexOffsetCollisionBox(data.getType(), 3.0, 0.0, 3.0, 13.0, 16.0, 13.0) : new HexOffsetCollisionBox(data.getType(), 5.0, 0.0, 5.0, 11.0, 16.0, 11.0), StateTypes.BAMBOO),
    BAMBOO_SAPLING((player, item, version, data, isTargetBlock, x, y, z) -> new HexOffsetCollisionBox(data.getType(), 4.0, 0.0, 4.0, 12.0, 12.0, 12.0), StateTypes.BAMBOO_SAPLING),
    SCAFFOLDING((player, item, version, data, isTargetBlock, x, y, z) -> {
        if (item == StateTypes.SCAFFOLDING || version.isOlderThan(ClientVersion.V_1_14)) {
            return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
        }
        ComplexCollisionBox box = new ComplexCollisionBox(9, new HexCollisionBox(0.0, 14.0, 0.0, 16.0, 16.0, 16.0), new HexCollisionBox(0.0, 0.0, 0.0, 2.0, 16.0, 2.0), new HexCollisionBox(14.0, 0.0, 0.0, 16.0, 16.0, 2.0), new HexCollisionBox(0.0, 0.0, 14.0, 2.0, 16.0, 16.0), new HexCollisionBox(14.0, 0.0, 14.0, 16.0, 16.0, 16.0));
        if (data.isBottom()) {
            box.add(new HexCollisionBox(0.0, 0.0, 0.0, 2.0, 2.0, 16.0));
            box.add(new HexCollisionBox(14.0, 0.0, 0.0, 16.0, 2.0, 16.0));
            box.add(new HexCollisionBox(0.0, 0.0, 14.0, 16.0, 2.0, 16.0));
            box.add(new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 2.0, 2.0));
        }
        return box;
    }, StateTypes.SCAFFOLDING),
    DRIPLEAF((player, item, version, data, isTargetBlock, x, y, z) -> {
        if (version.isOlderThanOrEquals(ClientVersion.V_1_16_4)) {
            return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
        }
        ComplexCollisionBox box = new ComplexCollisionBox(2);
        if (data.getFacing() == BlockFace.NORTH) {
            box.add(new HexCollisionBox(5.0, 0.0, 9.0, 11.0, 15.0, 15.0));
        } else if (data.getFacing() == BlockFace.SOUTH) {
            box.add(new HexCollisionBox(5.0, 0.0, 1.0, 11.0, 15.0, 7.0));
        } else if (data.getFacing() == BlockFace.EAST) {
            box.add(new HexCollisionBox(1.0, 0.0, 5.0, 7.0, 15.0, 11.0));
        } else {
            box.add(new HexCollisionBox(9.0, 0.0, 5.0, 15.0, 15.0, 11.0));
        }
        if (data.getTilt() == Tilt.NONE || data.getTilt() == Tilt.UNSTABLE) {
            box.add(new HexCollisionBox(0.0, 11.0, 0.0, 16.0, 15.0, 16.0));
        } else if (data.getTilt() == Tilt.PARTIAL) {
            box.add(new HexCollisionBox(0.0, 11.0, 0.0, 16.0, 13.0, 16.0));
        }
        return box;
    }, StateTypes.BIG_DRIPLEAF),
    PINK_PETALS_BLOCK((player, item, version, data, isTargetBlock, x, y, z) -> {
        if (version.isNewerThan(ClientVersion.V_1_20_2)) {
            return HitboxData.getSegmentedHitBox(data.getFlowerAmount(), data.getFacing(), 3);
        }
        if (version.isNewerThan(ClientVersion.V_1_19_3)) {
            return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 0.1875, 1.0);
        }
        if (version.isNewerThan(ClientVersion.V_1_12_2)) {
            return HitboxData.CORAL_FAN.box.copy();
        }
        return HitboxData.GRASS_FERN.dynamic.fetch(player, item, version, data, isTargetBlock, x, y, z);
    }, StateTypes.PINK_PETALS),
    MANGROVE_PROPAGULE((player, item, version, data, isTargetBlock, x, y, z) -> {
        if (data.isHanging()) {
            return new HexOffsetCollisionBox(data.getType(), 7.0, 0.0, 7.0, 9.0, 16.0, 9.0);
        }
        return new HexOffsetCollisionBox(data.getType(), 7.0, (double)HitboxData.getPropaguleMinHeight(data.getAge()), 7.0, 9.0, 16.0, 9.0);
    }, StateTypes.MANGROVE_PROPAGULE),
    FROGSPAWN((player, item, version, data, isTargetBlock, x, y, z) -> {
        if (version.isNewerThan(ClientVersion.V_1_18_2)) {
            return new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 1.5, 16.0);
        }
        return NoCollisionBox.INSTANCE;
    }, StateTypes.FROGSPAWN),
    BUSH((player, heldItem, version, block, isTargetBlock, x, y, z) -> version.isNewerThan(ClientVersion.V_1_21_4) ? new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 0.8125, 1.0) : HitboxData.GRASS_FERN.dynamic.fetch(player, heldItem, version, block, isTargetBlock, x, y, z), StateTypes.BUSH),
    SHORT_DRY_GRASS((player, heldItem, version, block, isTargetBlock, x, y, z) -> version.isNewerThan(ClientVersion.V_1_21_4) ? new SimpleCollisionBox(0.125, 0.0, 0.125, 0.875, 0.625, 0.875) : HitboxData.GRASS_FERN.dynamic.fetch(player, heldItem, version, block, isTargetBlock, x, y, z), StateTypes.SHORT_DRY_GRASS),
    TALL_DRY_GRASS((player, heldItem, version, block, isTargetBlock, x, y, z) -> version.isNewerThan(ClientVersion.V_1_21_4) ? new SimpleCollisionBox(0.0625, 0.0, 0.0625, 0.9375, 1.0, 0.9375) : HitboxData.GRASS_FERN.dynamic.fetch(player, heldItem, version, block, isTargetBlock, x, y, z), StateTypes.TALL_DRY_GRASS),
    LEAF_LITTER((player, item, version, data, isTargetBlock, x, y, z) -> version.isNewerThan(ClientVersion.V_1_21_4) ? HitboxData.getSegmentedHitBox(data.getSegmentAmount(), data.getFacing(), 1) : new HexCollisionBox(0.0, 15.0, 0.0, 16.0, 16.0, 16.0), StateTypes.LEAF_LITTER),
    WILDFLOWERS((player, item, version, data, isTargetBlock, x, y, z) -> version.isNewerThan(ClientVersion.V_1_21_4) ? HitboxData.getSegmentedHitBox(data.getFlowerAmount(), data.getFacing(), 3) : new HexCollisionBox(0.0, 15.0, 0.0, 16.0, 16.0, 16.0), StateTypes.WILDFLOWERS),
    CACTUS_FLOWER((player, item, version, data, isTargetBlock, x, y, z) -> version.isNewerThan(ClientVersion.V_1_21_4) ? new SimpleCollisionBox(0.0625, 0.0, 0.0625, 0.9375, 0.75, 0.9375) : HitboxData.CORAL_FAN.box.copy(), StateTypes.CACTUS_FLOWER),
    SCULK_SHRIEKER(new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true), StateTypes.SCULK_SHRIEKER);

    private static final Map<StateType, HitboxData> lookup;
    private final StateType[] materials;
    private CollisionBox box;
    private HitBoxFactory dynamic;

    private HitboxData(CollisionBox box, StateType ... materials) {
        this.box = box;
        HashSet<StateType> mList = new HashSet<StateType>(Arrays.asList(materials));
        mList.remove(null);
        this.materials = mList.toArray(new StateType[0]);
    }

    private HitboxData(HitBoxFactory dynamic, StateType ... materials) {
        this.dynamic = dynamic;
        HashSet<StateType> mList = new HashSet<StateType>(Arrays.asList(materials));
        mList.remove(null);
        this.materials = mList.toArray(new StateType[0]);
    }

    @Override
    public CollisionBox fetch(GrimPlayer player, StateType heldItem, ClientVersion version, WrappedBlockState block, boolean isTargetBlock, int x, int y, int z) {
        return this.box != null ? this.box.copy() : this.dynamic.fetch(player, heldItem, version, block, isTargetBlock, x, y, z);
    }

    public static HitboxData getData(StateType material) {
        return lookup.get(material);
    }

    public static CollisionBox getBlockHitbox(GrimPlayer player, StateType heldItem, ClientVersion version, WrappedBlockState block, boolean isTargetBlock, int x, int y, int z) {
        HitboxData data = HitboxData.getData(block.getType());
        if (data == null) {
            return CollisionData.getRawData(block.getType()).getMovementCollisionBox(player, version, block, x, y, z);
        }
        return data.fetch(player, heldItem, version, block, isTargetBlock, x, y, z).offset(x, y, z);
    }

    private static int getPropaguleMinHeight(int age) {
        return switch (age) {
            case 0, 1, 2 -> 13 - age * 3;
            case 3, 4 -> (4 - age) * 3;
            default -> throw new IllegalStateException("Impossible Propagule Height");
        };
    }

    private static CollisionBox getVineCollisionBox(ClientVersion version, boolean isWeeping, boolean isBlock) {
        if (version.isNewerThan(ClientVersion.V_1_15_2)) {
            return isWeeping ? (isBlock ? new SimpleCollisionBox(0.25, 0.5625, 0.25, 0.75, 1.0, 0.75) : new SimpleCollisionBox(0.0625, 0.0, 0.0625, 0.9375, 1.0, 0.9375)) : new SimpleCollisionBox(0.25, 0.0, 0.25, 0.75, isBlock ? 0.9375 : 1.0, 0.75);
        }
        return new ComplexCollisionBox(4, new SimpleCollisionBox(0.0, 0.0, 0.0, 0.0625, 1.0, 1.0), new SimpleCollisionBox(0.9375, 0.0, 0.0, 1.0, 1.0, 1.0), new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 0.0625), new SimpleCollisionBox(0.0, 0.0, 0.9375, 1.0, 1.0, 1.0));
    }

    private static CollisionBox getSegmentedHitBox(int segments, BlockFace facing, int height) {
        return switch (segments) {
            case 0 -> NoCollisionBox.INSTANCE;
            case 1 -> {
                switch (facing) {
                    case SOUTH: {
                        yield new SimpleCollisionBox(0.5, 0.0, 0.5, 1.0, (double)height / 16.0, 1.0, false);
                    }
                    case WEST: {
                        yield new SimpleCollisionBox(0.5, 0.0, 0.0, 1.0, (double)height / 16.0, 0.5, false);
                    }
                    case NORTH: {
                        yield new SimpleCollisionBox(0.0, 0.0, 0.0, 0.5, (double)height / 16.0, 0.5, false);
                    }
                    case EAST: {
                        yield new SimpleCollisionBox(0.0, 0.0, 0.5, 0.5, (double)height / 16.0, 1.0, false);
                    }
                }
                throw new IllegalStateException("Unexpected value: " + String.valueOf((Object)facing));
            }
            case 2 -> {
                switch (facing) {
                    case SOUTH: {
                        yield new SimpleCollisionBox(0.5, 0.0, 0.0, 1.0, (double)height / 16.0, 1.0, false);
                    }
                    case WEST: {
                        yield new SimpleCollisionBox(0.0, 0.0, 0.5, 1.0, (double)height / 16.0, 1.0, false);
                    }
                    case NORTH: {
                        yield new SimpleCollisionBox(0.0, 0.0, 0.0, 0.5, (double)height / 16.0, 1.0, false);
                    }
                    case EAST: {
                        yield new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, (double)height / 16.0, 0.5, false);
                    }
                }
                throw new IllegalStateException("Unexpected value: " + String.valueOf((Object)facing));
            }
            case 3, 4 -> new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, (double)height / 16.0, 1.0, false);
            default -> throw new IllegalStateException("Unexpected value: " + segments);
        };
    }

    static {
        lookup = new HashMap<StateType, HitboxData>();
        for (HitboxData data2 : HitboxData.values()) {
            for (StateType type : data2.materials) {
                lookup.put(type, data2);
            }
        }
    }
}

