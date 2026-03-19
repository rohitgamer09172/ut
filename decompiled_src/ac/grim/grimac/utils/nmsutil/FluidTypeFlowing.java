/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.utils.collisions.CollisionData;
import ac.grim.grimac.utils.collisions.blocks.DoorHandler;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.nmsutil.Materials;
import lombok.Generated;

public final class FluidTypeFlowing {
    public static Vector3dm getFlow(GrimPlayer player, int originalX, int originalY, int originalZ) {
        float fluidLevel = (float)Math.min(player.compensatedWorld.getFluidLevelAt(originalX, originalY, originalZ), 0.8888888888888888);
        ClientVersion version = player.getClientVersion();
        if (fluidLevel == 0.0f) {
            return new Vector3dm();
        }
        double d0 = 0.0;
        double d1 = 0.0;
        for (BlockFace enumdirection : new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST}) {
            int modifiedZ;
            int modifiedX = originalX + enumdirection.getModX();
            if (!FluidTypeFlowing.affectsFlow(player, originalX, originalY, originalZ, modifiedX, originalY, modifiedZ = originalZ + enumdirection.getModZ())) continue;
            float f = (float)Math.min(player.compensatedWorld.getFluidLevelAt(modifiedX, originalY, modifiedZ), 0.8888888888888888);
            float f1 = 0.0f;
            if (f == 0.0f) {
                StateType mat = player.compensatedWorld.getBlockType(modifiedX, originalY, modifiedZ);
                if (Materials.isSolidBlockingBlacklist(mat, version) && FluidTypeFlowing.affectsFlow(player, originalX, originalY, originalZ, modifiedX, originalY - 1, modifiedZ) && (f = (float)Math.min(player.compensatedWorld.getFluidLevelAt(modifiedX, originalY - 1, modifiedZ), 0.8888888888888888)) > 0.0f) {
                    f1 = fluidLevel - (f - 0.8888889f);
                }
            } else if (f > 0.0f) {
                f1 = fluidLevel - f;
            }
            if (f1 == 0.0f) continue;
            d0 += (double)((float)enumdirection.getModX() * f1);
            d1 += (double)((float)enumdirection.getModZ() * f1);
        }
        Vector3dm vec3d = new Vector3dm(d0, 0.0, d1);
        WrappedBlockState state = player.compensatedWorld.getBlock(originalX, originalY, originalZ);
        if ((state.getType() == StateTypes.WATER || state.getType() == StateTypes.LAVA) && state.getLevel() >= 8) {
            for (BlockFace enumdirection : new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST}) {
                if (!FluidTypeFlowing.isSolidFace(player, originalX, originalY, originalZ, enumdirection) && !FluidTypeFlowing.isSolidFace(player, originalX, originalY + 1, originalZ, enumdirection)) continue;
                vec3d = FluidTypeFlowing.normalizeVectorWithoutNaN(vec3d).add(0.0, -6.0, 0.0);
                break;
            }
        }
        return FluidTypeFlowing.normalizeVectorWithoutNaN(vec3d);
    }

    private static boolean affectsFlow(GrimPlayer player, int originalX, int originalY, int originalZ, int x2, int y2, int z2) {
        return FluidTypeFlowing.isEmpty(player, x2, y2, z2) || FluidTypeFlowing.isSame(player, originalX, originalY, originalZ, x2, y2, z2);
    }

    private static boolean isSolidFace(GrimPlayer player, int originalX, int y, int originalZ, BlockFace direction) {
        int x = originalX + direction.getModX();
        int z = originalZ + direction.getModZ();
        WrappedBlockState data = player.compensatedWorld.getBlock(x, y, z);
        StateType type = data.getType();
        if (FluidTypeFlowing.isSame(player, x, y, z, originalX, y, originalZ)) {
            return false;
        }
        if (type == StateTypes.ICE) {
            return false;
        }
        if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_12)) {
            if (type == StateTypes.PISTON || type == StateTypes.STICKY_PISTON) {
                return data.getFacing().getOppositeFace() == direction || CollisionData.getData(type).getMovementCollisionBox(player, player.getClientVersion(), data, 0, 0, 0).isFullBlock();
            }
            if (type == StateTypes.PISTON_HEAD) {
                return data.getFacing() == direction;
            }
        }
        if (player.getClientVersion().isOlderThan(ClientVersion.V_1_12)) {
            return !Materials.isSolidBlockingBlacklist(type, player.getClientVersion());
        }
        if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_12) && player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_13_2)) {
            if (Materials.isStairs(type) || Materials.isLeaves(type) || Materials.isShulker(type) || Materials.isGlassBlock(type) || BlockTags.TRAPDOORS.contains(type)) {
                return false;
            }
            if (type == StateTypes.BEACON || BlockTags.CAULDRONS.contains(type) || type == StateTypes.GLOWSTONE || type == StateTypes.SEA_LANTERN || type == StateTypes.CONDUIT) {
                return false;
            }
            if (type == StateTypes.PISTON || type == StateTypes.STICKY_PISTON || type == StateTypes.PISTON_HEAD) {
                return false;
            }
            return type == StateTypes.SOUL_SAND || CollisionData.getData(type).getMovementCollisionBox(player, player.getClientVersion(), data, x, y, z).isFullBlock();
        }
        if (Materials.isLeaves(type)) {
            return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14) && player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_15_2);
        }
        if (type == StateTypes.SNOW) {
            return data.getLayers() == 8;
        }
        if (Materials.isStairs(type)) {
            return data.getFacing() == direction;
        }
        if (type == StateTypes.COMPOSTER) {
            return true;
        }
        if (type == StateTypes.SOUL_SAND) {
            return player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_12_2) || player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_16);
        }
        if (type == StateTypes.LADDER) {
            return data.getFacing().getOppositeFace() == direction;
        }
        if (BlockTags.TRAPDOORS.contains(type)) {
            return data.getFacing().getOppositeFace() == direction && data.isOpen();
        }
        if (BlockTags.DOORS.contains(type)) {
            CollisionData collisionData = CollisionData.getData(type);
            if (collisionData.dynamic instanceof DoorHandler) {
                BlockFace dir = ((DoorHandler)collisionData.dynamic).fetchDirection(player, player.getClientVersion(), data, x, y, z);
                return dir.getOppositeFace() == direction;
            }
        }
        return CollisionData.getData(type).getMovementCollisionBox(player, player.getClientVersion(), data, x, y, z).isFullBlock();
    }

    private static Vector3dm normalizeVectorWithoutNaN(Vector3dm vector) {
        double var0 = vector.length();
        return var0 < 1.0E-4 ? new Vector3dm() : vector.multiply(1.0 / var0);
    }

    public static boolean isEmpty(GrimPlayer player, int x, int y, int z) {
        return player.compensatedWorld.getFluidLevelAt(x, y, z) == 0.0;
    }

    public static boolean isSame(GrimPlayer player, int x1, int y1, int z1, int x2, int y2, int z2) {
        return player.compensatedWorld.getWaterFluidLevelAt(x1, y1, z1) > 0.0 && player.compensatedWorld.getWaterFluidLevelAt(x2, y2, z2) > 0.0 || player.compensatedWorld.getLavaFluidLevelAt(x1, y1, z1) > 0.0 && player.compensatedWorld.getLavaFluidLevelAt(x2, y2, z2) > 0.0;
    }

    @Generated
    private FluidTypeFlowing() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

