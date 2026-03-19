/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.viaversion.viaversion.api.Via
 */
package ac.grim.grimac.utils.collisions;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.movementtick.MovementTickerStrider;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Attachment;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Axis;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Face;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Half;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Part;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Thickness;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Tilt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Type;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.VerticalDirection;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.utils.collisions.blocks.DoorHandler;
import ac.grim.grimac.utils.collisions.blocks.DynamicChest;
import ac.grim.grimac.utils.collisions.blocks.DynamicChorusPlant;
import ac.grim.grimac.utils.collisions.blocks.DynamicStair;
import ac.grim.grimac.utils.collisions.blocks.PistonBaseCollision;
import ac.grim.grimac.utils.collisions.blocks.PistonHeadCollision;
import ac.grim.grimac.utils.collisions.blocks.TrapDoorHandler;
import ac.grim.grimac.utils.collisions.blocks.connecting.DynamicCollisionFence;
import ac.grim.grimac.utils.collisions.blocks.connecting.DynamicCollisionPane;
import ac.grim.grimac.utils.collisions.blocks.connecting.DynamicCollisionWall;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.CollisionFactory;
import ac.grim.grimac.utils.collisions.datatypes.ComplexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.DynamicCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.HexCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.HexOffsetCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.NoCollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.packetentity.PacketEntityStrider;
import ac.grim.grimac.utils.nmsutil.Materials;
import ac.grim.grimac.utils.viaversion.ViaVersionUtil;
import com.viaversion.viaversion.api.Via;
import java.util.Arrays;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.stream.Stream;

public enum CollisionData implements CollisionFactory
{
    LAVA((player, version, block, x, y, z) -> {
        if (MovementTickerStrider.isAbove(player) && player.compensatedEntities.self.getRiding() instanceof PacketEntityStrider && block.getLevel() == 0) {
            return new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
        }
        return NoCollisionBox.INSTANCE;
    }, StateTypes.LAVA),
    BREWING_STAND((player, version, block, x, y, z) -> {
        int base = 0;
        int maxIndex = 3;
        if (version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
            maxIndex = 2;
            base = 1;
        }
        return new ComplexCollisionBox(maxIndex, new HexCollisionBox(base, 0.0, base, 16 - base, 2.0, 16 - base), new SimpleCollisionBox(0.4375, 0.0, 0.4375, 0.5625, 0.875, 0.5625, false));
    }, StateTypes.BREWING_STAND),
    BAMBOO((player, version, block, x, y, z) -> {
        if (version.isOlderThan(ClientVersion.V_1_14)) {
            return NoCollisionBox.INSTANCE;
        }
        return new HexOffsetCollisionBox(block.getType(), 6.5, 0.0, 6.5, 9.5, 16.0, 9.5);
    }, StateTypes.BAMBOO),
    COMPOSTER((player, version, block, x, y, z) -> {
        double height = 0.125;
        if (version.isOlderThanOrEquals(ClientVersion.V_1_13_2)) {
            height = 0.25;
        }
        if (version.isOlderThanOrEquals(ClientVersion.V_1_12_2)) {
            height = 0.3125;
        }
        return new ComplexCollisionBox(5, new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, height, 1.0, false), new SimpleCollisionBox(0.0, height, 0.0, 0.125, 1.0, 1.0, false), new SimpleCollisionBox(0.875, height, 0.0, 1.0, 1.0, 1.0, false), new SimpleCollisionBox(0.0, height, 0.0, 1.0, 1.0, 0.125, false), new SimpleCollisionBox(0.0, height, 0.875, 1.0, 1.0, 1.0, false));
    }, StateTypes.COMPOSTER),
    RAIL(new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 0.125, 0.0, false), StateTypes.RAIL, StateTypes.ACTIVATOR_RAIL, StateTypes.DETECTOR_RAIL, StateTypes.POWERED_RAIL),
    ANVIL((player, version, data, x, y, z) -> {
        BlockFace face = data.getFacing();
        if (version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
            ComplexCollisionBox complexAnvil = new ComplexCollisionBox(4);
            complexAnvil.add(new HexCollisionBox(2.0, 0.0, 2.0, 14.0, 4.0, 14.0));
            if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {
                complexAnvil.add(new HexCollisionBox(4.0, 4.0, 3.0, 12.0, 5.0, 13.0));
                complexAnvil.add(new HexCollisionBox(6.0, 5.0, 4.0, 10.0, 10.0, 12.0));
                complexAnvil.add(new HexCollisionBox(3.0, 10.0, 0.0, 13.0, 16.0, 16.0));
            } else {
                complexAnvil.add(new HexCollisionBox(3.0, 4.0, 4.0, 13.0, 5.0, 12.0));
                complexAnvil.add(new HexCollisionBox(4.0, 5.0, 6.0, 12.0, 10.0, 10.0));
                complexAnvil.add(new HexCollisionBox(0.0, 10.0, 3.0, 16.0, 16.0, 13.0));
            }
            return complexAnvil;
        }
        if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {
            return new SimpleCollisionBox(0.125, 0.0, 0.0, 0.875, 1.0, 1.0, false);
        }
        return new SimpleCollisionBox(0.0, 0.0, 0.125, 1.0, 1.0, 0.875, false);
    }, BlockTags.ANVIL.getStates().toArray(new StateType[0])),
    WALL(new DynamicCollisionWall(), BlockTags.WALLS.getStates().toArray(new StateType[0])),
    SLAB((player, version, data, x, y, z) -> {
        Type slabType = data.getTypeData();
        if (slabType == Type.DOUBLE) {
            return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
        }
        if (slabType == Type.BOTTOM) {
            return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 0.5, 1.0, false);
        }
        return new SimpleCollisionBox(0.0, 0.5, 0.0, 1.0, 1.0, 1.0, false);
    }, BlockTags.SLABS.getStates().toArray(new StateType[0])),
    SKULL(new SimpleCollisionBox(0.25, 0.0, 0.25, 0.75, 0.5, 0.75, false), StateTypes.CREEPER_HEAD, StateTypes.ZOMBIE_HEAD, StateTypes.DRAGON_HEAD, StateTypes.PLAYER_HEAD, StateTypes.SKELETON_SKULL, StateTypes.WITHER_SKELETON_SKULL, StateTypes.HEAVY_CORE),
    PIGLIN_HEAD(new HexCollisionBox(3.0, 0.0, 3.0, 13.0, 8.0, 13.0), StateTypes.PIGLIN_HEAD),
    WALL_SKULL((player, version, data, x, y, z) -> switch (data.getFacing()) {
        case BlockFace.SOUTH -> new SimpleCollisionBox(0.25, 0.25, 0.0, 0.75, 0.75, 0.5, false);
        case BlockFace.WEST -> new SimpleCollisionBox(0.5, 0.25, 0.25, 1.0, 0.75, 0.75, false);
        case BlockFace.EAST -> new SimpleCollisionBox(0.0, 0.25, 0.25, 0.5, 0.75, 0.75, false);
        default -> new SimpleCollisionBox(0.25, 0.25, 0.5, 0.75, 0.75, 1.0, false);
    }, StateTypes.CREEPER_WALL_HEAD, StateTypes.DRAGON_WALL_HEAD, StateTypes.PLAYER_WALL_HEAD, StateTypes.ZOMBIE_WALL_HEAD, StateTypes.SKELETON_WALL_SKULL, StateTypes.WITHER_SKELETON_WALL_SKULL),
    PIGLIN_WALL_HEAD((player, version, data, x, y, z) -> switch (data.getFacing()) {
        case BlockFace.SOUTH -> new HexCollisionBox(3.0, 4.0, 0.0, 13.0, 12.0, 8.0);
        case BlockFace.EAST -> new HexCollisionBox(0.0, 4.0, 3.0, 8.0, 12.0, 13.0);
        case BlockFace.WEST -> new HexCollisionBox(8.0, 4.0, 3.0, 16.0, 12.0, 13.0);
        default -> new HexCollisionBox(3.0, 4.0, 8.0, 13.0, 12.0, 16.0);
    }, StateTypes.PIGLIN_WALL_HEAD),
    DOOR(new DoorHandler(), BlockTags.DOORS.getStates().toArray(new StateType[0])),
    HOPPER((player, version, data, x, y, z) -> {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
            ComplexCollisionBox hopperBox = new ComplexCollisionBox(7);
            switch (data.getFacing()) {
                case DOWN: {
                    hopperBox.add(new HexCollisionBox(6.0, 0.0, 6.0, 10.0, 4.0, 10.0));
                    break;
                }
                case EAST: {
                    hopperBox.add(new HexCollisionBox(12.0, 4.0, 6.0, 16.0, 8.0, 10.0));
                    break;
                }
                case NORTH: {
                    hopperBox.add(new HexCollisionBox(6.0, 4.0, 0.0, 10.0, 8.0, 4.0));
                    break;
                }
                case SOUTH: {
                    hopperBox.add(new HexCollisionBox(6.0, 4.0, 12.0, 10.0, 8.0, 16.0));
                    break;
                }
                case WEST: {
                    hopperBox.add(new HexCollisionBox(0.0, 4.0, 6.0, 4.0, 8.0, 10.0));
                }
            }
            hopperBox.add(new SimpleCollisionBox(0.0, 0.625, 0.0, 1.0, 0.6875, 1.0, false));
            hopperBox.add(new SimpleCollisionBox(0.0, 0.6875, 0.0, 0.125, 1.0, 1.0, false));
            hopperBox.add(new SimpleCollisionBox(0.125, 0.6875, 0.0, 1.0, 1.0, 0.125, false));
            hopperBox.add(new SimpleCollisionBox(0.125, 0.6875, 0.875, 1.0, 1.0, 1.0, false));
            hopperBox.add(new SimpleCollisionBox(0.25, 0.25, 0.25, 0.75, 0.625, 0.75, false));
            hopperBox.add(new SimpleCollisionBox(0.875, 0.6875, 0.125, 1.0, 1.0, 0.875, false));
            return hopperBox;
        }
        double height = 0.625;
        return new ComplexCollisionBox(5, new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, height, 1.0, false), new SimpleCollisionBox(0.0, height, 0.0, 0.125, 1.0, 1.0, false), new SimpleCollisionBox(0.875, height, 0.0, 1.0, 1.0, 1.0, false), new SimpleCollisionBox(0.0, height, 0.0, 1.0, 1.0, 0.125, false), new SimpleCollisionBox(0.0, height, 0.875, 1.0, 1.0, 1.0, false));
    }, StateTypes.HOPPER),
    CAKE((player, version, data, x, y, z) -> {
        double height = 0.5;
        if (version.isOlderThan(ClientVersion.V_1_8)) {
            height = 0.4375;
        }
        double eatenPosition = (double)(1 + data.getBites() * 2) / 16.0;
        return new SimpleCollisionBox(eatenPosition, 0.0, 0.0625, 0.9375, height, 0.9375, false);
    }, StateTypes.CAKE),
    COCOA_BEANS((player, version, data, x, y, z) -> CollisionData.getCocoa(version, data.getAge(), data.getFacing()), StateTypes.COCOA),
    STONE_CUTTER((player, version, data, x, y, z) -> {
        if (version.isOlderThanOrEquals(ClientVersion.V_1_13_2)) {
            return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
        }
        return new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 9.0, 16.0);
    }, StateTypes.STONECUTTER),
    CORAL_FAN(NoCollisionBox.INSTANCE, BlockTags.CORALS.getStates().toArray(new StateType[0])),
    RAILS(NoCollisionBox.INSTANCE, BlockTags.RAILS.getStates().toArray(new StateType[0])),
    BANNER(NoCollisionBox.INSTANCE, BlockTags.BANNERS.getStates().toArray(new StateType[0])),
    SMALL_FLOWER(NoCollisionBox.INSTANCE, BlockTags.SMALL_FLOWERS.getStates().toArray(new StateType[0])),
    TALL_FLOWER(NoCollisionBox.INSTANCE, BlockTags.TALL_FLOWERS.getStates().toArray(new StateType[0])),
    SAPLING(NoCollisionBox.INSTANCE, BlockTags.SAPLINGS.getStates().toArray(new StateType[0])),
    BUTTON(NoCollisionBox.INSTANCE, BlockTags.BUTTONS.getStates().toArray(new StateType[0])),
    NO_COLLISION(NoCollisionBox.INSTANCE, StateTypes.TWISTING_VINES_PLANT, StateTypes.WEEPING_VINES_PLANT, StateTypes.TWISTING_VINES, StateTypes.WEEPING_VINES, StateTypes.CAVE_VINES, StateTypes.CAVE_VINES_PLANT, StateTypes.TALL_SEAGRASS, StateTypes.SEAGRASS, StateTypes.SHORT_GRASS, StateTypes.FERN, StateTypes.NETHER_SPROUTS, StateTypes.DEAD_BUSH, StateTypes.SUGAR_CANE, StateTypes.SWEET_BERRY_BUSH, StateTypes.WARPED_ROOTS, StateTypes.CRIMSON_ROOTS, StateTypes.TORCHFLOWER_CROP, StateTypes.PINK_PETALS, StateTypes.TALL_GRASS, StateTypes.LARGE_FERN, StateTypes.BAMBOO_SAPLING, StateTypes.HANGING_ROOTS, StateTypes.VINE, StateTypes.SMALL_DRIPLEAF, StateTypes.END_PORTAL, StateTypes.LEVER, StateTypes.PUMPKIN_STEM, StateTypes.MELON_STEM, StateTypes.ATTACHED_MELON_STEM, StateTypes.ATTACHED_PUMPKIN_STEM, StateTypes.BEETROOTS, StateTypes.POTATOES, StateTypes.WHEAT, StateTypes.CARROTS, StateTypes.NETHER_WART, StateTypes.MOVING_PISTON, StateTypes.AIR, StateTypes.CAVE_AIR, StateTypes.VOID_AIR, StateTypes.LIGHT, StateTypes.WATER, StateTypes.BUBBLE_COLUMN, StateTypes.FIRE, StateTypes.SOUL_FIRE),
    KELP(new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 9.0, 16.0), StateTypes.KELP),
    BELL((player, version, data, x, y, z) -> {
        if (version.isOlderThanOrEquals(ClientVersion.V_1_13_2)) {
            return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
        }
        BlockFace direction = data.getFacing();
        if (data.getAttachment() == Attachment.FLOOR) {
            return direction != BlockFace.NORTH && direction != BlockFace.SOUTH ? new HexCollisionBox(4.0, 0.0, 0.0, 12.0, 16.0, 16.0) : new HexCollisionBox(0.0, 0.0, 4.0, 16.0, 16.0, 12.0);
        }
        ComplexCollisionBox complex = new ComplexCollisionBox(3, new HexCollisionBox(5.0, 6.0, 5.0, 11.0, 13.0, 11.0), new HexCollisionBox(4.0, 4.0, 4.0, 12.0, 6.0, 12.0));
        if (data.getAttachment() == Attachment.CEILING) {
            complex.add(new HexCollisionBox(7.0, 13.0, 7.0, 9.0, 16.0, 9.0));
        } else if (data.getAttachment() == Attachment.DOUBLE_WALL) {
            if (direction != BlockFace.NORTH && direction != BlockFace.SOUTH) {
                complex.add(new HexCollisionBox(0.0, 13.0, 7.0, 16.0, 15.0, 9.0));
            } else {
                complex.add(new HexCollisionBox(7.0, 13.0, 0.0, 9.0, 15.0, 16.0));
            }
        } else if (direction == BlockFace.NORTH) {
            complex.add(new HexCollisionBox(7.0, 13.0, 0.0, 9.0, 15.0, 13.0));
        } else if (direction == BlockFace.SOUTH) {
            complex.add(new HexCollisionBox(7.0, 13.0, 3.0, 9.0, 15.0, 16.0));
        } else if (direction == BlockFace.EAST) {
            complex.add(new HexCollisionBox(3.0, 13.0, 7.0, 16.0, 15.0, 9.0));
        } else {
            complex.add(new HexCollisionBox(0.0, 13.0, 7.0, 13.0, 15.0, 9.0));
        }
        return complex;
    }, StateTypes.BELL),
    SCAFFOLDING((player, version, data, x, y, z) -> {
        if (version.isOlderThanOrEquals(ClientVersion.V_1_13_2)) {
            return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
        }
        if (player.lastY > (double)(y + 1) - 1.0E-5 && !player.isSneaking) {
            return new ComplexCollisionBox(5, new HexCollisionBox(0.0, 14.0, 0.0, 16.0, 16.0, 16.0), new HexCollisionBox(0.0, 0.0, 0.0, 2.0, 16.0, 2.0), new HexCollisionBox(14.0, 0.0, 0.0, 16.0, 16.0, 2.0), new HexCollisionBox(0.0, 0.0, 14.0, 2.0, 16.0, 16.0), new HexCollisionBox(14.0, 0.0, 14.0, 16.0, 16.0, 16.0));
        }
        return data.getDistance() != 0 && data.isBottom() && player.lastY > (double)y - 1.0E-5 ? new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 2.0, 16.0) : NoCollisionBox.INSTANCE;
    }, StateTypes.SCAFFOLDING),
    LADDER((player, version, data, x, y, z) -> {
        int width = 3;
        if (version.isOlderThanOrEquals(ClientVersion.V_1_8)) {
            width = 2;
        }
        return switch (data.getFacing()) {
            case BlockFace.NORTH -> new HexCollisionBox(0.0, 0.0, 16.0 - (double)width, 16.0, 16.0, 16.0);
            case BlockFace.SOUTH -> new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 16.0, width);
            case BlockFace.WEST -> new HexCollisionBox(16.0 - (double)width, 0.0, 0.0, 16.0, 16.0, 16.0);
            default -> new HexCollisionBox(0.0, 0.0, 0.0, width, 16.0, 16.0);
        };
    }, StateTypes.LADDER),
    CAMPFIRE((player, version, data, x, y, z) -> {
        if (version.isOlderThanOrEquals(ClientVersion.V_1_13_2)) {
            if (data.isLit()) {
                return NoCollisionBox.INSTANCE;
            }
            return new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
        }
        return new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 7.0, 16.0);
    }, StateTypes.CAMPFIRE, StateTypes.SOUL_CAMPFIRE),
    LANTERN((player, version, data, x, y, z) -> {
        if (version.isOlderThanOrEquals(ClientVersion.V_1_12_2)) {
            return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
        }
        if (data.isHanging()) {
            return new ComplexCollisionBox(2, new HexCollisionBox(5.0, 1.0, 5.0, 11.0, 8.0, 11.0), new HexCollisionBox(6.0, 8.0, 6.0, 10.0, 10.0, 10.0));
        }
        return new ComplexCollisionBox(2, new HexCollisionBox(5.0, 0.0, 5.0, 11.0, 7.0, 11.0), new HexCollisionBox(6.0, 7.0, 6.0, 10.0, 9.0, 10.0));
    }, BlockTags.LANTERNS.getStates().toArray(new StateType[0])),
    LECTERN((player, version, data, x, y, z) -> {
        if (version.isOlderThanOrEquals(ClientVersion.V_1_13_2)) {
            return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
        }
        return new ComplexCollisionBox(2, new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), new HexCollisionBox(4.0, 2.0, 4.0, 12.0, 14.0, 12.0));
    }, StateTypes.LECTERN),
    HONEY_BLOCK((player, version, data, x, y, z) -> {
        if (version.isOlderThanOrEquals(ClientVersion.V_1_14_4)) {
            return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
        }
        return new HexCollisionBox(1.0, 0.0, 1.0, 15.0, 15.0, 15.0);
    }, StateTypes.HONEY_BLOCK),
    DRAGON_EGG_BLOCK(new HexCollisionBox(1.0, 0.0, 1.0, 15.0, 16.0, 15.0), StateTypes.DRAGON_EGG),
    GRINDSTONE((player, version, data, x, y, z) -> {
        BlockFace facing = data.getFacing();
        if (version.isOlderThanOrEquals(ClientVersion.V_1_12_2)) {
            if (facing == BlockFace.NORTH || facing == BlockFace.SOUTH) {
                return new SimpleCollisionBox(0.125, 0.0, 0.0, 0.875, 1.0, 1.0, false);
            }
            return new SimpleCollisionBox(0.0, 0.0, 0.125, 1.0, 1.0, 0.875, false);
        }
        if (version.isOlderThanOrEquals(ClientVersion.V_1_13_2)) {
            ComplexCollisionBox complexAnvil = new ComplexCollisionBox(4);
            complexAnvil.add(new HexCollisionBox(2.0, 0.0, 2.0, 14.0, 4.0, 14.0));
            if (facing == BlockFace.NORTH || facing == BlockFace.SOUTH) {
                complexAnvil.add(new HexCollisionBox(4.0, 4.0, 3.0, 12.0, 5.0, 13.0));
                complexAnvil.add(new HexCollisionBox(6.0, 5.0, 4.0, 10.0, 10.0, 12.0));
                complexAnvil.add(new HexCollisionBox(3.0, 10.0, 0.0, 13.0, 16.0, 16.0));
            } else {
                complexAnvil.add(new HexCollisionBox(3.0, 4.0, 4.0, 13.0, 5.0, 12.0));
                complexAnvil.add(new HexCollisionBox(4.0, 5.0, 6.0, 12.0, 10.0, 10.0));
                complexAnvil.add(new HexCollisionBox(0.0, 10.0, 3.0, 16.0, 16.0, 13.0));
            }
            return complexAnvil;
        }
        Face attachment = data.getFace();
        if (attachment == Face.FLOOR) {
            if (facing == BlockFace.NORTH || facing == BlockFace.SOUTH) {
                return new ComplexCollisionBox(5, new HexCollisionBox(2.0, 0.0, 6.0, 4.0, 7.0, 10.0), new HexCollisionBox(12.0, 0.0, 6.0, 14.0, 7.0, 10.0), new HexCollisionBox(2.0, 7.0, 5.0, 4.0, 13.0, 11.0), new HexCollisionBox(12.0, 7.0, 5.0, 14.0, 13.0, 11.0), new HexCollisionBox(4.0, 4.0, 2.0, 12.0, 16.0, 14.0));
            }
            return new ComplexCollisionBox(5, new HexCollisionBox(6.0, 0.0, 2.0, 10.0, 7.0, 4.0), new HexCollisionBox(6.0, 0.0, 12.0, 10.0, 7.0, 14.0), new HexCollisionBox(5.0, 7.0, 2.0, 11.0, 13.0, 4.0), new HexCollisionBox(5.0, 7.0, 12.0, 11.0, 13.0, 14.0), new HexCollisionBox(2.0, 4.0, 4.0, 14.0, 16.0, 12.0));
        }
        if (attachment == Face.WALL) {
            switch (facing) {
                case NORTH: {
                    return new ComplexCollisionBox(5, new HexCollisionBox(2.0, 6.0, 7.0, 4.0, 10.0, 16.0), new HexCollisionBox(12.0, 6.0, 7.0, 14.0, 10.0, 16.0), new HexCollisionBox(2.0, 5.0, 3.0, 4.0, 11.0, 9.0), new HexCollisionBox(12.0, 5.0, 3.0, 14.0, 11.0, 9.0), new HexCollisionBox(4.0, 2.0, 0.0, 12.0, 14.0, 12.0));
                }
                case WEST: {
                    return new ComplexCollisionBox(5, new HexCollisionBox(7.0, 6.0, 2.0, 16.0, 10.0, 4.0), new HexCollisionBox(7.0, 6.0, 12.0, 16.0, 10.0, 14.0), new HexCollisionBox(3.0, 5.0, 2.0, 9.0, 11.0, 4.0), new HexCollisionBox(3.0, 5.0, 12.0, 9.0, 11.0, 14.0), new HexCollisionBox(0.0, 2.0, 4.0, 12.0, 14.0, 12.0));
                }
                case SOUTH: {
                    return new ComplexCollisionBox(5, new HexCollisionBox(2.0, 6.0, 0.0, 4.0, 10.0, 7.0), new HexCollisionBox(12.0, 6.0, 0.0, 14.0, 10.0, 7.0), new HexCollisionBox(2.0, 5.0, 7.0, 4.0, 11.0, 13.0), new HexCollisionBox(12.0, 5.0, 7.0, 14.0, 11.0, 13.0), new HexCollisionBox(4.0, 2.0, 4.0, 12.0, 14.0, 16.0));
                }
                case EAST: {
                    return new ComplexCollisionBox(5, new HexCollisionBox(0.0, 6.0, 2.0, 9.0, 10.0, 4.0), new HexCollisionBox(0.0, 6.0, 12.0, 9.0, 10.0, 14.0), new HexCollisionBox(7.0, 5.0, 2.0, 13.0, 11.0, 4.0), new HexCollisionBox(7.0, 5.0, 12.0, 13.0, 11.0, 14.0), new HexCollisionBox(4.0, 2.0, 4.0, 16.0, 14.0, 12.0));
                }
            }
        } else {
            if (facing == BlockFace.NORTH || facing == BlockFace.SOUTH) {
                return new ComplexCollisionBox(5, new HexCollisionBox(2.0, 9.0, 6.0, 4.0, 16.0, 10.0), new HexCollisionBox(12.0, 9.0, 6.0, 14.0, 16.0, 10.0), new HexCollisionBox(2.0, 3.0, 5.0, 4.0, 9.0, 11.0), new HexCollisionBox(12.0, 3.0, 5.0, 14.0, 9.0, 11.0), new HexCollisionBox(4.0, 0.0, 2.0, 12.0, 12.0, 14.0));
            }
            return new ComplexCollisionBox(5, new HexCollisionBox(6.0, 9.0, 2.0, 10.0, 16.0, 4.0), new HexCollisionBox(6.0, 9.0, 12.0, 10.0, 16.0, 14.0), new HexCollisionBox(5.0, 3.0, 2.0, 11.0, 9.0, 4.0), new HexCollisionBox(5.0, 3.0, 12.0, 11.0, 9.0, 14.0), new HexCollisionBox(2.0, 0.0, 4.0, 14.0, 12.0, 12.0));
        }
        return NoCollisionBox.INSTANCE;
    }, StateTypes.GRINDSTONE),
    PANE(new DynamicCollisionPane(), Materials.getPanes().toArray(new StateType[0])),
    CHAIN_BLOCK((player, version, data, x, y, z) -> {
        if (version.isOlderThan(ClientVersion.V_1_16)) {
            return PANE.fetch(player, version, data, x, y, z);
        }
        if (data.getAxis() == Axis.X) {
            return new HexCollisionBox(0.0, 6.5, 6.5, 16.0, 9.5, 9.5);
        }
        if (data.getAxis() == Axis.Y) {
            return new HexCollisionBox(6.5, 0.0, 6.5, 9.5, 16.0, 9.5);
        }
        return new HexCollisionBox(6.5, 6.5, 0.0, 9.5, 9.5, 16.0);
    }, Materials.getChains().toArray(new StateType[0])),
    CHORUS_PLANT(new DynamicChorusPlant(), StateTypes.CHORUS_PLANT),
    FENCE_GATE((player, version, data, x, y, z) -> {
        if (data.isOpen()) {
            return NoCollisionBox.INSTANCE;
        }
        return switch (data.getFacing()) {
            case BlockFace.NORTH, BlockFace.SOUTH -> new SimpleCollisionBox(0.0, 0.0, 0.375, 1.0, 1.5, 0.625, false);
            case BlockFace.EAST, BlockFace.WEST -> new SimpleCollisionBox(0.375, 0.0, 0.0, 0.625, 1.5, 1.0, false);
            default -> NoCollisionBox.INSTANCE;
        };
    }, BlockTags.FENCE_GATES.getStates().toArray(new StateType[0])),
    FENCE(new DynamicCollisionFence(), BlockTags.FENCES.getStates().toArray(new StateType[0])),
    SNOW((player, version, data, x, y, z) -> {
        int layers = data.getLayers();
        if (layers == 1 && version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
            if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13) || !ViaVersionUtil.isAvailable || !Via.getConfig().isSnowCollisionFix()) {
                return NoCollisionBox.INSTANCE;
            }
            ++layers;
        }
        return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, (double)(layers - 1) * 0.125, 1.0);
    }, StateTypes.SNOW),
    STAIR(new DynamicStair(), BlockTags.STAIRS.getStates().toArray(new StateType[0])),
    CHEST(new DynamicChest(), Materials.getChests().toArray(new StateType[0])),
    ENDER_CHEST(new SimpleCollisionBox(0.0625, 0.0, 0.0625, 0.9375, 0.875, 0.9375, false), StateTypes.ENDER_CHEST),
    ENCHANTING_TABLE(new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 0.75, 1.0, false), StateTypes.ENCHANTING_TABLE),
    FRAME((player, version, data, x, y, z) -> {
        ComplexCollisionBox complexCollisionBox = new ComplexCollisionBox(2, new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 13.0, 16.0));
        if (data.isEye()) {
            if (version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
                complexCollisionBox.add(new HexCollisionBox(4.0, 13.0, 4.0, 12.0, 16.0, 12.0));
            } else {
                complexCollisionBox.add(new HexCollisionBox(5.0, 13.0, 5.0, 11.0, 16.0, 11.0));
            }
        }
        return complexCollisionBox;
    }, StateTypes.END_PORTAL_FRAME),
    CARPET((player, version, data, x, y, z) -> {
        if (version.isOlderThanOrEquals(ClientVersion.V_1_7_10)) {
            return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 0.0, 1.0, false);
        }
        return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0, false);
    }, BlockTags.WOOL_CARPETS.getStates().toArray(new StateType[0])),
    MOSS_CARPET(CARPET, StateTypes.MOSS_CARPET),
    PALE_MOSS_CARPET((player, version, data, x, y, z) -> {
        if (!data.isBottom()) {
            return NoCollisionBox.INSTANCE;
        }
        if (version.isOlderThan(ClientVersion.V_1_21_2)) {
            return MOSS_CARPET.fetch(player, version, data, x, y, z);
        }
        return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0, false);
    }, StateTypes.PALE_MOSS_CARPET),
    DAYLIGHT(new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 0.375, 1.0, false), StateTypes.DAYLIGHT_DETECTOR),
    FARMLAND((player, version, data, x, y, z) -> {
        if (version == ClientVersion.V_1_10) {
            if (Math.abs(player.y % 1.0) < 0.001) {
                return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
            }
            return new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 15.0, 16.0);
        }
        if (version.isNewerThanOrEquals(ClientVersion.V_1_10)) {
            return new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 15.0, 16.0);
        }
        return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
    }, StateTypes.FARMLAND),
    GRASS_PATH((player, version, data, x, y, z) -> {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_9)) {
            return new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 15.0, 16.0);
        }
        return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
    }, StateTypes.DIRT_PATH, StateTypes.GRASS_PATH),
    LILYPAD((player, version, data, x, y, z) -> {
        if (player.inVehicle() && player.compensatedEntities.self.getRiding().isBoat && version.isOlderThanOrEquals(ClientVersion.V_1_12_2)) {
            return NoCollisionBox.INSTANCE;
        }
        if (version.isOlderThan(ClientVersion.V_1_9)) {
            return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 0.015625, 1.0, false);
        }
        return new HexCollisionBox(1.0, 0.0, 1.0, 15.0, 1.5, 15.0);
    }, StateTypes.LILY_PAD),
    BED((player, version, data, x, y, z) -> {
        if (version.isOlderThan(ClientVersion.V_1_14)) {
            return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 0.5625, 1.0, false);
        }
        ComplexCollisionBox baseBox = new ComplexCollisionBox(3, new HexCollisionBox(0.0, 3.0, 0.0, 16.0, 9.0, 16.0));
        BlockFace facing = data.getPart() == Part.HEAD ? data.getFacing() : data.getFacing().getOppositeFace();
        switch (facing) {
            case NORTH: {
                baseBox.add(new HexCollisionBox(0.0, 0.0, 0.0, 3.0, 3.0, 3.0));
                baseBox.add(new HexCollisionBox(13.0, 0.0, 0.0, 16.0, 3.0, 3.0));
                break;
            }
            case SOUTH: {
                baseBox.add(new HexCollisionBox(0.0, 0.0, 13.0, 3.0, 3.0, 16.0));
                baseBox.add(new HexCollisionBox(13.0, 0.0, 13.0, 16.0, 3.0, 16.0));
                break;
            }
            case WEST: {
                baseBox.add(new HexCollisionBox(0.0, 0.0, 0.0, 3.0, 3.0, 3.0));
                baseBox.add(new HexCollisionBox(0.0, 0.0, 13.0, 3.0, 3.0, 16.0));
                break;
            }
            case EAST: {
                baseBox.add(new HexCollisionBox(13.0, 0.0, 0.0, 16.0, 3.0, 3.0));
                baseBox.add(new HexCollisionBox(13.0, 0.0, 13.0, 16.0, 3.0, 16.0));
            }
        }
        return baseBox;
    }, BlockTags.BEDS.getStates().toArray(new StateType[0])),
    TRAPDOOR(new TrapDoorHandler(), BlockTags.TRAPDOORS.getStates().toArray(new StateType[0])),
    DIODES(new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 0.125, 1.0, false), StateTypes.REPEATER, StateTypes.COMPARATOR),
    STRUCTURE_VOID(new SimpleCollisionBox(0.375, 0.375, 0.375, 0.625, 0.625, 0.625, false), StateTypes.STRUCTURE_VOID),
    END_ROD((player, version, data, x, y, z) -> CollisionData.getEndRod(version, data.getFacing()), Materials.getRods().toArray(new StateType[0])),
    CAULDRON((player, version, data, x, y, z) -> {
        if (version.isNewerThan(ClientVersion.V_1_13_2)) {
            return new ComplexCollisionBox(15, new SimpleCollisionBox(0.0, 0.0, 0.0, 0.125, 1.0, 0.25, false), new SimpleCollisionBox(0.0, 0.0, 0.75, 0.125, 1.0, 1.0, false), new SimpleCollisionBox(0.125, 0.0, 0.0, 0.25, 1.0, 0.125, false), new SimpleCollisionBox(0.125, 0.0, 0.875, 0.25, 1.0, 1.0, false), new SimpleCollisionBox(0.75, 0.0, 0.0, 1.0, 1.0, 0.125, false), new SimpleCollisionBox(0.75, 0.0, 0.875, 1.0, 1.0, 1.0, false), new SimpleCollisionBox(0.875, 0.0, 0.125, 1.0, 1.0, 0.25, false), new SimpleCollisionBox(0.875, 0.0, 0.75, 1.0, 1.0, 0.875, false), new SimpleCollisionBox(0.0, 0.1875, 0.25, 1.0, 0.25, 0.75, false), new SimpleCollisionBox(0.125, 0.1875, 0.125, 0.875, 0.25, 0.25, false), new SimpleCollisionBox(0.125, 0.1875, 0.75, 0.875, 0.25, 0.875, false), new SimpleCollisionBox(0.25, 0.1875, 0.0, 0.75, 1.0, 0.125, false), new SimpleCollisionBox(0.25, 0.1875, 0.875, 0.75, 1.0, 1.0, false), new SimpleCollisionBox(0.0, 0.25, 0.25, 0.125, 1.0, 0.75, false), new SimpleCollisionBox(0.875, 0.25, 0.25, 1.0, 1.0, 0.75, false));
        }
        double height = 0.25;
        if (version.isOlderThan(ClientVersion.V_1_13)) {
            height = 0.3125;
        }
        return new ComplexCollisionBox(5, new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, height, 1.0, false), new SimpleCollisionBox(0.0, height, 0.0, 0.125, 1.0, 1.0, false), new SimpleCollisionBox(0.875, height, 0.0, 1.0, 1.0, 1.0, false), new SimpleCollisionBox(0.0, height, 0.0, 1.0, 1.0, 0.125, false), new SimpleCollisionBox(0.0, height, 0.875, 1.0, 1.0, 1.0, false));
    }, BlockTags.CAULDRONS.getStates().toArray(new StateType[0])),
    CACTUS(new SimpleCollisionBox(0.0625, 0.0, 0.0625, 0.9375, 0.9375, 0.9375, false), StateTypes.CACTUS),
    PISTON_BASE(new PistonBaseCollision(), StateTypes.PISTON, StateTypes.STICKY_PISTON),
    PISTON_HEAD(new PistonHeadCollision(), StateTypes.PISTON_HEAD),
    SOULSAND(new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 0.875, 1.0, false), StateTypes.SOUL_SAND),
    PICKLE((player, version, data, x, y, z) -> CollisionData.getPicklesBox(version, data.getPickles()), StateTypes.SEA_PICKLE),
    TURTLEEGG((player, version, data, x, y, z) -> {
        if (version.isOlderThanOrEquals(ClientVersion.V_1_12_2)) {
            return CollisionData.getCocoa(version, data.getEggs(), BlockFace.WEST);
        }
        if (data.getEggs() == 1) {
            return new HexCollisionBox(3.0, 0.0, 3.0, 12.0, 7.0, 12.0);
        }
        return new HexCollisionBox(1.0, 0.0, 1.0, 15.0, 7.0, 15.0);
    }, StateTypes.TURTLE_EGG),
    CONDUIT((player, version, data, x, y, z) -> {
        if (version.isOlderThanOrEquals(ClientVersion.V_1_12_2)) {
            return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
        }
        return new HexCollisionBox(5.0, 5.0, 5.0, 11.0, 11.0, 11.0);
    }, StateTypes.CONDUIT),
    POT(new HexCollisionBox(5.0, 0.0, 5.0, 11.0, 6.0, 11.0), BlockTags.FLOWER_POTS.getStates().toArray(new StateType[0])),
    WALL_SIGN((player, version, data, x, y, z) -> switch (data.getFacing()) {
        case BlockFace.NORTH -> new HexCollisionBox(0.0, 4.5, 14.0, 16.0, 12.5, 16.0);
        case BlockFace.SOUTH -> new HexCollisionBox(0.0, 4.5, 0.0, 16.0, 12.5, 2.0);
        case BlockFace.WEST -> new HexCollisionBox(14.0, 4.5, 0.0, 16.0, 12.5, 16.0);
        case BlockFace.EAST -> new HexCollisionBox(0.0, 4.5, 0.0, 2.0, 12.5, 16.0);
        default -> NoCollisionBox.INSTANCE;
    }, BlockTags.WALL_SIGNS.getStates().toArray(new StateType[0])),
    WALL_FAN((player, version, data, x, y, z) -> switch (data.getFacing()) {
        case BlockFace.NORTH -> new HexCollisionBox(0.0, 4.0, 5.0, 16.0, 12.0, 16.0);
        case BlockFace.SOUTH -> new HexCollisionBox(0.0, 4.0, 0.0, 16.0, 12.0, 11.0);
        case BlockFace.WEST -> new HexCollisionBox(5.0, 4.0, 0.0, 16.0, 12.0, 16.0);
        default -> new HexCollisionBox(0.0, 4.0, 0.0, 11.0, 12.0, 16.0);
    }, BlockTags.WALL_CORALS.getStates().toArray(new StateType[0])),
    CORAL_PLANT((player, version, data, x, y, z) -> new HexCollisionBox(2.0, 0.0, 2.0, 14.0, 15.0, 14.0), (StateType[])Stream.concat(Arrays.stream(BlockTags.CORAL_PLANTS.getStates().toArray(new StateType[0])), Stream.of(StateTypes.DEAD_HORN_CORAL, StateTypes.DEAD_TUBE_CORAL, StateTypes.DEAD_BRAIN_CORAL, StateTypes.DEAD_BUBBLE_CORAL, StateTypes.DEAD_FIRE_CORAL)).distinct().toArray(StateType[]::new)),
    SIGN(new SimpleCollisionBox(0.25, 0.0, 0.25, 0.75, 1.0, 0.75, false), BlockTags.STANDING_SIGNS.getStates().toArray(new StateType[0])),
    STONE_PRESSURE_PLATE((player, version, data, x, y, z) -> {
        if (data.isPowered()) {
            return new HexCollisionBox(1.0, 0.0, 1.0, 15.0, 0.5, 15.0);
        }
        return new HexCollisionBox(1.0, 0.0, 1.0, 15.0, 1.0, 15.0);
    }, BlockTags.STONE_PRESSURE_PLATES.getStates().toArray(new StateType[0])),
    WOOD_PRESSURE_PLATE((player, version, data, x, y, z) -> {
        if (data.isPowered()) {
            return new HexCollisionBox(1.0, 0.0, 1.0, 15.0, 0.5, 15.0);
        }
        return new HexCollisionBox(1.0, 0.0, 1.0, 15.0, 1.0, 15.0);
    }, BlockTags.WOODEN_PRESSURE_PLATES.getStates().toArray(new StateType[0])),
    OTHER_PRESSURE_PLATE((player, version, data, x, y, z) -> {
        if (data.getPower() > 0) {
            return new HexCollisionBox(1.0, 0.0, 1.0, 15.0, 0.5, 15.0);
        }
        return new HexCollisionBox(1.0, 0.0, 1.0, 15.0, 1.0, 15.0);
    }, StateTypes.LIGHT_WEIGHTED_PRESSURE_PLATE, StateTypes.HEAVY_WEIGHTED_PRESSURE_PLATE),
    TRIPWIRE((player, version, data, x, y, z) -> {
        if (data.isAttached()) {
            return new HexCollisionBox(0.0, 1.0, 0.0, 16.0, 2.5, 16.0);
        }
        return new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    }, StateTypes.TRIPWIRE),
    TRIPWIRE_HOOK((player, version, data, x, y, z) -> switch (data.getFacing()) {
        case BlockFace.NORTH -> new HexCollisionBox(5.0, 0.0, 10.0, 11.0, 10.0, 16.0);
        case BlockFace.SOUTH -> new HexCollisionBox(5.0, 0.0, 0.0, 11.0, 10.0, 6.0);
        case BlockFace.WEST -> new HexCollisionBox(10.0, 0.0, 5.0, 16.0, 10.0, 11.0);
        default -> new HexCollisionBox(0.0, 0.0, 5.0, 6.0, 10.0, 11.0);
    }, StateTypes.TRIPWIRE_HOOK),
    TORCH(new HexCollisionBox(6.0, 0.0, 6.0, 10.0, 10.0, 10.0), StateTypes.TORCH, StateTypes.REDSTONE_TORCH, StateTypes.COPPER_TORCH),
    WALL_TORCH((player, version, data, x, y, z) -> switch (data.getFacing()) {
        case BlockFace.NORTH -> new HexCollisionBox(5.5, 3.0, 11.0, 10.5, 13.0, 16.0);
        case BlockFace.SOUTH -> new HexCollisionBox(5.5, 3.0, 0.0, 10.5, 13.0, 5.0);
        case BlockFace.WEST -> new HexCollisionBox(11.0, 3.0, 5.5, 16.0, 13.0, 10.5);
        case BlockFace.EAST -> new HexCollisionBox(0.0, 3.0, 5.5, 5.0, 13.0, 10.5);
        default -> new HexCollisionBox(6.0, 0.0, 6.0, 10.0, 10.0, 10.0);
    }, StateTypes.WALL_TORCH, StateTypes.REDSTONE_WALL_TORCH, StateTypes.COPPER_WALL_TORCH),
    CANDLE((player, version, data, x, y, z) -> {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_17)) {
            return switch (data.getCandles()) {
                case 1 -> new HexCollisionBox(7.0, 0.0, 7.0, 9.0, 6.0, 9.0);
                case 2 -> new HexCollisionBox(5.0, 0.0, 6.0, 11.0, 6.0, 9.0);
                case 3 -> new HexCollisionBox(5.0, 0.0, 6.0, 10.0, 6.0, 11.0);
                default -> new HexCollisionBox(5.0, 0.0, 5.0, 11.0, 6.0, 10.0);
            };
        }
        return CollisionData.getPicklesBox(version, data.getCandles());
    }, BlockTags.CANDLES.getStates().toArray(new StateType[0])),
    CANDLE_CAKE((player, version, data, x, y, z) -> {
        HexCollisionBox cake = new HexCollisionBox(1.0, 0.0, 1.0, 15.0, 8.0, 15.0);
        if (version.isOlderThan(ClientVersion.V_1_17)) {
            return cake;
        }
        return new ComplexCollisionBox(2, cake, new HexCollisionBox(7.0, 8.0, 7.0, 9.0, 14.0, 9.0));
    }, BlockTags.CANDLE_CAKES.getStates().toArray(new StateType[0])),
    SCULK_SENSOR(new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 8.0, 16.0), StateTypes.SCULK_SENSOR, StateTypes.CALIBRATED_SCULK_SENSOR),
    DECORATED_POT((player, version, data, x, y, z) -> {
        if (version.isNewerThan(ClientVersion.V_1_19_3)) {
            return new HexCollisionBox(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);
        }
        return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
    }, StateTypes.DECORATED_POT),
    BIG_DRIPLEAF((player, version, data, x, y, z) -> {
        Tilt tilt = data.getTilt();
        if (version.isOlderThanOrEquals(ClientVersion.V_1_16_4)) {
            if (tilt == Tilt.FULL) {
                return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 0.5, 1.0, false);
            }
            return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
        }
        if (tilt == Tilt.NONE || tilt == Tilt.UNSTABLE) {
            return new HexCollisionBox(0.0, 11.0, 0.0, 16.0, 15.0, 16.0);
        }
        if (tilt == Tilt.PARTIAL) {
            return new HexCollisionBox(0.0, 11.0, 0.0, 16.0, 13.0, 16.0);
        }
        return NoCollisionBox.INSTANCE;
    }, StateTypes.BIG_DRIPLEAF),
    POINTED_DRIPSTONE((player, version, data, x, y, z) -> {
        if (version.isOlderThan(ClientVersion.V_1_17)) {
            return CollisionData.getEndRod(version, BlockFace.UP);
        }
        HexOffsetCollisionBox box = data.getThickness() == Thickness.TIP_MERGE ? new HexOffsetCollisionBox(data.getType(), 5.0, 0.0, 5.0, 11.0, 16.0, 11.0) : (data.getThickness() == Thickness.TIP ? (data.getVerticalDirection() == VerticalDirection.DOWN ? new HexOffsetCollisionBox(data.getType(), 5.0, 5.0, 5.0, 11.0, 16.0, 11.0) : new HexOffsetCollisionBox(data.getType(), 5.0, 0.0, 5.0, 11.0, 11.0, 11.0)) : (data.getThickness() == Thickness.FRUSTUM ? new HexOffsetCollisionBox(data.getType(), 4.0, 0.0, 4.0, 12.0, 16.0, 12.0) : (data.getThickness() == Thickness.MIDDLE ? new HexOffsetCollisionBox(data.getType(), 3.0, 0.0, 3.0, 13.0, 16.0, 13.0) : new HexOffsetCollisionBox(data.getType(), 2.0, 0.0, 2.0, 14.0, 16.0, 14.0))));
        return box;
    }, StateTypes.POINTED_DRIPSTONE),
    POWDER_SNOW((player, version, data, x, y, z) -> {
        if (version.isOlderThanOrEquals(ClientVersion.V_1_16_4)) {
            return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
        }
        if (player.fallDistance > 2.5) {
            return player.getClientVersion() == ClientVersion.V_1_21_4 ? new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true) : new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 0.9, 1.0, false);
        }
        ItemStack boots = player.inventory.getBoots();
        if (player.lastY > (double)(y + 1) - 1.0E-5 && boots != null && boots.getType() == ItemTypes.LEATHER_BOOTS && !player.isSneaking && !player.inVehicle()) {
            return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
        }
        return NoCollisionBox.INSTANCE;
    }, StateTypes.POWDER_SNOW),
    NETHER_PORTAL((player, version, data, x, y, z) -> {
        if (data.getAxis() == Axis.X) {
            return new HexCollisionBox(0.0, 0.0, 6.0, 16.0, 16.0, 10.0);
        }
        return new HexCollisionBox(6.0, 0.0, 0.0, 10.0, 16.0, 16.0);
    }, StateTypes.NETHER_PORTAL),
    AZALEA((player, version, data, x, y, z) -> new ComplexCollisionBox(2, new HexCollisionBox(0.0, 8.0, 0.0, 16.0, 16.0, 16.0), new HexCollisionBox(6.0, 0.0, 6.0, 10.0, 8.0, 10.0)), StateTypes.AZALEA, StateTypes.FLOWERING_AZALEA),
    AMETHYST_CLUSTER((player, version, data, x, y, z) -> CollisionData.getAmethystBox(version, data.getFacing(), 7, 3), StateTypes.AMETHYST_CLUSTER),
    SMALL_AMETHYST_BUD((player, version, data, x, y, z) -> CollisionData.getAmethystBox(version, data.getFacing(), 3, 4), StateTypes.SMALL_AMETHYST_BUD),
    MEDIUM_AMETHYST_BUD((player, version, data, x, y, z) -> CollisionData.getAmethystBox(version, data.getFacing(), 4, 3), StateTypes.MEDIUM_AMETHYST_BUD),
    LARGE_AMETHYST_BUD((player, version, data, x, y, z) -> CollisionData.getAmethystBox(version, data.getFacing(), 5, 3), StateTypes.LARGE_AMETHYST_BUD),
    MUD_BLOCK((player, version, data, x, y, z) -> {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_19)) {
            return new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 14.0, 16.0);
        }
        return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
    }, StateTypes.MUD),
    MANGROVE_PROPAGULE_BLOCK((player, version, data, x, y, z) -> {
        if (!data.isHanging()) {
            return new HexCollisionBox(7.0, 0.0, 7.0, 9.0, 16.0, 9.0);
        }
        return switch (data.getAge()) {
            case 0 -> new HexCollisionBox(7.0, 13.0, 7.0, 9.0, 16.0, 9.0);
            case 1 -> new HexCollisionBox(7.0, 10.0, 7.0, 9.0, 16.0, 9.0);
            case 2 -> new HexCollisionBox(7.0, 7.0, 7.0, 9.0, 16.0, 9.0);
            case 3 -> new HexCollisionBox(7.0, 3.0, 7.0, 9.0, 16.0, 9.0);
            default -> new HexCollisionBox(7.0, 0.0, 7.0, 9.0, 16.0, 9.0);
        };
    }, StateTypes.MANGROVE_PROPAGULE),
    SCULK_SHRIKER((player, version, data, x, y, z) -> {
        if (version.isNewerThan(ClientVersion.V_1_18_2)) {
            return new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
        }
        return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
    }, StateTypes.SCULK_SHRIEKER),
    SNIFFER_EGG((player, version, data, x, y, z) -> {
        if (version.isNewerThan(ClientVersion.V_1_19_4)) {
            return new HexCollisionBox(1.0, 0.0, 2.0, 15.0, 16.0, 14.0);
        }
        return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
    }, StateTypes.SNIFFER_EGG),
    PITCHER_CROP((player, version, data, x, y, z) -> {
        if (version.isNewerThan(ClientVersion.V_1_19_4)) {
            HexCollisionBox COLLISION_SHAPE_BULB = new HexCollisionBox(5.0, -1.0, 5.0, 11.0, 3.0, 11.0);
            HexCollisionBox COLLISION_SHAPE_CROP = new HexCollisionBox(3.0, -1.0, 3.0, 13.0, 5.0, 13.0);
            if (data.getAge() == 0) {
                return COLLISION_SHAPE_BULB;
            }
            return data.getHalf() == Half.LOWER ? COLLISION_SHAPE_CROP : NoCollisionBox.INSTANCE;
        }
        return NoCollisionBox.INSTANCE;
    }, StateTypes.PITCHER_CROP),
    WALL_HANGING_SIGNS((player, version, data, x, y, z) -> switch (data.getFacing()) {
        case BlockFace.NORTH, BlockFace.SOUTH -> new HexCollisionBox(0.0, 14.0, 6.0, 16.0, 16.0, 10.0);
        case BlockFace.EAST, BlockFace.WEST -> new HexCollisionBox(6.0, 14.0, 0.0, 10.0, 16.0, 16.0);
        default -> NoCollisionBox.INSTANCE;
    }, BlockTags.WALL_HANGING_SIGNS.getStates().toArray(new StateType[0])),
    DRIED_GHAST((player, version, data, x, y, z) -> {
        if (player.getClientVersion().isNewerThan(ClientVersion.V_1_21_5)) {
            return new HexCollisionBox(3.0, 0.0, 3.0, 13.0, 10.0, 13.0);
        }
        if (player.getClientVersion().isNewerThan(ClientVersion.V_1_12_2)) {
            return new ComplexCollisionBox(2, new SimpleCollisionBox(0.1875, 0.1875, 0.1875, 0.8125, 0.8125, 0.8125), new SimpleCollisionBox(0.1875, 0.0, 0.1875, 0.8125, 0.8125, 0.8125));
        }
        if (player.getClientVersion().isNewerThan(ClientVersion.V_1_8)) {
            return new SimpleCollisionBox(0.1875, 0.0, 0.1875, 0.8125, 0.8125, 0.8125);
        }
        return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
    }, StateTypes.DRIED_GHAST),
    SHELF((player, version, data, x, y, z) -> {
        if (version.isOlderThan(ClientVersion.V_1_21_9)) {
            return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
        }
        return switch (data.getFacing()) {
            case BlockFace.NORTH -> new ComplexCollisionBox(3, new HexCollisionBox(0.0, 12.0, 11.0, 16.0, 16.0, 13.0), new HexCollisionBox(0.0, 0.0, 13.0, 16.0, 16.0, 16.0), new HexCollisionBox(0.0, 0.0, 11.0, 16.0, 4.0, 13.0));
            case BlockFace.SOUTH -> new ComplexCollisionBox(3, new HexCollisionBox(0.0, 12.0, 3.0, 16.0, 16.0, 5.0), new HexCollisionBox(0.0, 0.0, 0.0, 16.0, 16.0, 3.0), new HexCollisionBox(0.0, 0.0, 3.0, 16.0, 4.0, 5.0));
            case BlockFace.WEST -> new ComplexCollisionBox(3, new HexCollisionBox(11.0, 12.0, 0.0, 13.0, 16.0, 16.0), new HexCollisionBox(13.0, 0.0, 0.0, 16.0, 16.0, 16.0), new HexCollisionBox(11.0, 0.0, 0.0, 13.0, 4.0, 16.0));
            case BlockFace.EAST -> new ComplexCollisionBox(3, new HexCollisionBox(3.0, 12.0, 0.0, 5.0, 16.0, 16.0), new HexCollisionBox(0.0, 0.0, 0.0, 3.0, 16.0, 16.0), new HexCollisionBox(3.0, 0.0, 0.0, 5.0, 4.0, 16.0));
            default -> throw new IllegalStateException("Unexpected value: " + String.valueOf((Object)data.getFacing()));
        };
    }, BlockTags.WOODEN_SHELVES.getStates().toArray(new StateType[0])),
    COPPER_GOLEM_STATUE((player, version, data, x, y, z) -> {
        if (version.isOlderThan(ClientVersion.V_1_21_9)) {
            return new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true);
        }
        return new HexCollisionBox(3.0, 0.0, 3.0, 13.0, 14.0, 13.0);
    }, BlockTags.COPPER_GOLEM_STATUES.getStates().toArray(new StateType[0])),
    DEFAULT(new SimpleCollisionBox(0.0, 0.0, 0.0, 1.0, 1.0, 1.0, true), StateTypes.STONE);

    private static final Map<StateType, CollisionData> rawLookupMap;
    public final StateType[] materials;
    public CollisionBox box;
    public CollisionFactory dynamic;

    private CollisionData(CollisionBox box, StateType ... states) {
        this.box = box;
        HashSet<StateType> mList = new HashSet<StateType>(Arrays.asList(states));
        mList.remove(null);
        this.materials = mList.toArray(new StateType[0]);
    }

    private CollisionData(CollisionFactory dynamic, StateType ... states) {
        this.dynamic = dynamic;
        HashSet<StateType> mList = new HashSet<StateType>(Arrays.asList(states));
        mList.remove(null);
        this.materials = mList.toArray(new StateType[0]);
    }

    private static CollisionBox getAmethystBox(ClientVersion version, BlockFace facing, int param_0, int param_1) {
        if (version.isOlderThanOrEquals(ClientVersion.V_1_16_4)) {
            return NoCollisionBox.INSTANCE;
        }
        return switch (facing) {
            case BlockFace.DOWN -> new HexCollisionBox(param_1, 16 - param_0, param_1, 16 - param_1, 16.0, 16 - param_1);
            case BlockFace.NORTH -> new HexCollisionBox(param_1, param_1, 16 - param_0, 16 - param_1, 16 - param_1, 16.0);
            case BlockFace.SOUTH -> new HexCollisionBox(param_1, param_1, 0.0, 16 - param_1, 16 - param_1, param_0);
            case BlockFace.EAST -> new HexCollisionBox(0.0, param_1, param_1, param_0, 16 - param_1, 16 - param_1);
            case BlockFace.WEST -> new HexCollisionBox(16 - param_0, param_1, param_1, 16.0, 16 - param_1, 16 - param_1);
            default -> new HexCollisionBox(param_1, 0.0, param_1, 16 - param_1, param_0, 16 - param_1);
        };
    }

    private static CollisionBox getPicklesBox(ClientVersion version, int pickles) {
        if (version.isOlderThanOrEquals(ClientVersion.V_1_12_2)) {
            return CollisionData.getCocoa(version, pickles, BlockFace.WEST);
        }
        return switch (pickles) {
            case 1 -> new HexCollisionBox(6.0, 0.0, 6.0, 10.0, 6.0, 10.0);
            case 2 -> new HexCollisionBox(3.0, 0.0, 3.0, 13.0, 6.0, 13.0);
            case 3 -> new HexCollisionBox(2.0, 0.0, 2.0, 14.0, 6.0, 14.0);
            case 4 -> new HexCollisionBox(2.0, 0.0, 2.0, 14.0, 7.0, 14.0);
            default -> NoCollisionBox.INSTANCE;
        };
    }

    public static CollisionBox getCocoa(ClientVersion version, int age, BlockFace direction) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_9_1) && version.isOlderThan(ClientVersion.V_1_11)) {
            age = Math.min(age, 1);
        }
        switch (direction) {
            case EAST: {
                switch (age) {
                    case 0: {
                        return new HexCollisionBox(11.0, 7.0, 6.0, 15.0, 12.0, 10.0);
                    }
                    case 1: {
                        return new HexCollisionBox(9.0, 5.0, 5.0, 15.0, 12.0, 11.0);
                    }
                    case 2: {
                        return new HexCollisionBox(7.0, 3.0, 4.0, 15.0, 12.0, 12.0);
                    }
                }
            }
            case WEST: {
                switch (age) {
                    case 0: {
                        return new HexCollisionBox(1.0, 7.0, 6.0, 5.0, 12.0, 10.0);
                    }
                    case 1: {
                        return new HexCollisionBox(1.0, 5.0, 5.0, 7.0, 12.0, 11.0);
                    }
                    case 2: {
                        return new HexCollisionBox(1.0, 3.0, 4.0, 9.0, 12.0, 12.0);
                    }
                }
            }
            case NORTH: {
                switch (age) {
                    case 0: {
                        return new HexCollisionBox(6.0, 7.0, 1.0, 10.0, 12.0, 5.0);
                    }
                    case 1: {
                        return new HexCollisionBox(5.0, 5.0, 1.0, 11.0, 12.0, 7.0);
                    }
                    case 2: {
                        return new HexCollisionBox(4.0, 3.0, 1.0, 12.0, 12.0, 9.0);
                    }
                }
            }
            case SOUTH: {
                switch (age) {
                    case 0: {
                        return new HexCollisionBox(6.0, 7.0, 11.0, 10.0, 12.0, 15.0);
                    }
                    case 1: {
                        return new HexCollisionBox(5.0, 5.0, 9.0, 11.0, 12.0, 15.0);
                    }
                    case 2: {
                        return new HexCollisionBox(4.0, 3.0, 7.0, 12.0, 12.0, 15.0);
                    }
                }
            }
        }
        return NoCollisionBox.INSTANCE;
    }

    private static CollisionBox getEndRod(ClientVersion version, BlockFace face) {
        if (version.isOlderThan(ClientVersion.V_1_9)) {
            return NoCollisionBox.INSTANCE;
        }
        return switch (face) {
            case BlockFace.NORTH, BlockFace.SOUTH -> new HexCollisionBox(6.0, 6.0, 0.0, 10.0, 10.0, 16.0);
            case BlockFace.EAST, BlockFace.WEST -> new HexCollisionBox(0.0, 6.0, 6.0, 16.0, 10.0, 10.0);
            default -> new HexCollisionBox(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);
        };
    }

    public static CollisionData getData(StateType state) {
        return state.isSolid() || state == StateTypes.LAVA || state == StateTypes.SCAFFOLDING || state == StateTypes.PITCHER_CROP || state == StateTypes.HEAVY_CORE || state == StateTypes.PALE_MOSS_CARPET || BlockTags.WALL_HANGING_SIGNS.contains(state) || BlockTags.COPPER_GOLEM_STATUES.contains(state) ? rawLookupMap.getOrDefault(state, DEFAULT) : NO_COLLISION;
    }

    public static CollisionData getRawData(StateType state) {
        return rawLookupMap.getOrDefault(state, DEFAULT);
    }

    public CollisionBox getMovementCollisionBox(GrimPlayer player, ClientVersion version, WrappedBlockState block, int x, int y, int z) {
        return this.fetch(player, version, block, x, y, z).offset(x, y, z);
    }

    public CollisionBox getMovementCollisionBox(GrimPlayer player, ClientVersion version, WrappedBlockState block) {
        if (this.box != null) {
            return this.box.copy();
        }
        return new DynamicCollisionBox(player, version, this.dynamic, block);
    }

    @Override
    public CollisionBox fetch(GrimPlayer player, ClientVersion version, WrappedBlockState block, int x, int y, int z) {
        return this.box != null ? this.box.copy() : new DynamicCollisionBox(player, version, this.dynamic, block);
    }

    static {
        rawLookupMap = new IdentityHashMap<StateType, CollisionData>();
        for (CollisionData data2 : CollisionData.values()) {
            for (StateType type : data2.materials) {
                rawLookupMap.put(type, data2);
            }
        }
    }
}

