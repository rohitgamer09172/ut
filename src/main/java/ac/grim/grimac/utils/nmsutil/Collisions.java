/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.events.packets.PacketWorldBorder;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.blockeffects.BlockEffectsResolver;
import ac.grim.grimac.predictionengine.blockeffects.impl.BlockEffectsResolverV1_21_10;
import ac.grim.grimac.predictionengine.blockeffects.impl.BlockEffectsResolverV1_21_2;
import ac.grim.grimac.predictionengine.blockeffects.impl.BlockEffectsResolverV1_21_4;
import ac.grim.grimac.predictionengine.blockeffects.impl.BlockEffectsResolverV1_21_5;
import ac.grim.grimac.predictionengine.blockeffects.impl.BlockEffectsResolverV1_21_6;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.Direction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.fastutil.doubles.DoubleListIterator;
import ac.grim.grimac.shaded.fastutil.floats.FloatArraySet;
import ac.grim.grimac.shaded.fastutil.floats.FloatArrays;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.chunks.Column;
import ac.grim.grimac.utils.collisions.CollisionData;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.Pair;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.data.tags.SyncedTags;
import ac.grim.grimac.utils.latency.CompensatedWorld;
import ac.grim.grimac.utils.math.Location;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.math.VectorUtils;
import ac.grim.grimac.utils.nmsutil.CheckIfChunksLoaded;
import ac.grim.grimac.utils.nmsutil.GetBoundingBox;
import ac.grim.grimac.utils.nmsutil.Materials;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import lombok.Generated;

public final class Collisions {
    public static final double COLLISION_EPSILON = 1.0E-7;
    private static final boolean IS_FOURTEEN = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_14);
    private static final List<List<Axis>> allAxisCombinations = Arrays.asList(Arrays.asList(Axis.Y, Axis.X, Axis.Z), Arrays.asList(Axis.Y, Axis.Z, Axis.X), Arrays.asList(Axis.X, Axis.Y, Axis.Z), Arrays.asList(Axis.X, Axis.Z, Axis.Y), Arrays.asList(Axis.Z, Axis.X, Axis.Y), Arrays.asList(Axis.Z, Axis.Y, Axis.X));
    private static final List<List<Axis>> nonStupidityCombinations = Arrays.asList(Arrays.asList(Axis.Y, Axis.X, Axis.Z), Arrays.asList(Axis.Y, Axis.Z, Axis.X));

    public static boolean slowCouldPointThreeHitGround(GrimPlayer player, double x, double y, double z) {
        SimpleCollisionBox oldBB = player.boundingBox;
        player.boundingBox = GetBoundingBox.getBoundingBoxFromPosAndSize(player, x, y, z, 0.6f, 0.06f);
        double movementThreshold = player.getMovementThreshold();
        double posXZ = Collisions.collide(player, movementThreshold, -movementThreshold, movementThreshold).getY();
        double negXNegZ = Collisions.collide(player, -movementThreshold, -movementThreshold, -movementThreshold).getY();
        double posXNegZ = Collisions.collide(player, movementThreshold, -movementThreshold, -movementThreshold).getY();
        double posZNegX = Collisions.collide(player, -movementThreshold, -movementThreshold, movementThreshold).getY();
        player.boundingBox = oldBB;
        return negXNegZ != -movementThreshold || posXNegZ != -movementThreshold || posXZ != -movementThreshold || posZNegX != -movementThreshold;
    }

    public static Vector3dm collide(GrimPlayer player, double desiredX, double desiredY, double desiredZ) {
        return Collisions.collide(player, desiredX, desiredY, desiredZ, desiredY, null);
    }

    public static Vector3dm collide(GrimPlayer player, double desiredX, double desiredY, double desiredZ, double clientVelY, VectorData data) {
        if (desiredX == 0.0 && desiredY == 0.0 && desiredZ == 0.0) {
            return new Vector3dm();
        }
        SimpleCollisionBox grabBoxesBB = player.boundingBox.copy();
        double stepUpHeight = player.getMaxUpStep();
        if (desiredX == 0.0 && desiredZ == 0.0) {
            if (desiredY > 0.0) {
                grabBoxesBB.maxY += desiredY;
            } else {
                grabBoxesBB.minY += desiredY;
            }
        } else if (stepUpHeight > 0.0 && (player.lastOnGround || desiredY < 0.0 || clientVelY < 0.0)) {
            if (desiredY <= 0.0) {
                grabBoxesBB.expandToCoordinate(desiredX, desiredY, desiredZ);
                grabBoxesBB.maxY += stepUpHeight;
            } else {
                grabBoxesBB.expandToCoordinate(desiredX, Math.max(stepUpHeight, desiredY), desiredZ);
            }
        } else {
            grabBoxesBB.expandToCoordinate(desiredX, desiredY, desiredZ);
        }
        ArrayList<SimpleCollisionBox> desiredMovementCollisionBoxes = new ArrayList<SimpleCollisionBox>();
        Collisions.getCollisionBoxes(player, grabBoxesBB, desiredMovementCollisionBoxes, false);
        double bestInput = Double.MAX_VALUE;
        Vector3dm bestOrderResult = null;
        Vector3dm bestTheoreticalCollisionResult = VectorUtils.cutBoxToVector(player.actualMovement, new SimpleCollisionBox(0.0, Math.min(0.0, desiredY), 0.0, desiredX, Math.max(stepUpHeight, desiredY), desiredZ).sort());
        int zeroCount = (desiredX == 0.0 ? 1 : 0) + (desiredY == 0.0 ? 1 : 0) + (desiredZ == 0.0 ? 1 : 0);
        for (List<Axis> order : data != null && data.isZeroPointZeroThree() ? allAxisCombinations : nonStupidityCombinations) {
            boolean disallowStepping;
            Vector3dm collisionResult = Collisions.collideBoundingBoxLegacy(new Vector3dm(desiredX, desiredY, desiredZ), player.boundingBox, desiredMovementCollisionBoxes, order);
            boolean movingIntoGroundReal = player.pointThreeEstimator.closeEnoughToGroundToStepWithPointThree(data, clientVelY) || collisionResult.getY() != desiredY && (desiredY < 0.0 || clientVelY < 0.0);
            boolean movingIntoGround = player.lastOnGround || movingIntoGroundReal;
            boolean bl = disallowStepping = player.getSetbackTeleportUtil().getRequiredSetBack() != null && player.getSetbackTeleportUtil().getRequiredSetBack().getTicksComplete() == 1;
            if (!disallowStepping && stepUpHeight > 0.0 && movingIntoGround && (collisionResult.getX() != desiredX || collisionResult.getZ() != desiredZ)) {
                player.uncertaintyHandler.isStepMovement = true;
                if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21)) {
                    float[] stepHeights;
                    SimpleCollisionBox startingOffsetBox = movingIntoGroundReal ? player.boundingBox.copy().offset(0.0, collisionResult.getY(), 0.0) : player.boundingBox.copy();
                    SimpleCollisionBox offsetByHorizAndStepBox = startingOffsetBox.copy().expandToCoordinate(desiredX, stepUpHeight, desiredZ);
                    if (!movingIntoGroundReal) {
                        offsetByHorizAndStepBox = offsetByHorizAndStepBox.copy().expandToCoordinate(0.0, -1.0E-5f, 0.0);
                    }
                    ArrayList<SimpleCollisionBox> stepCollisions = new ArrayList<SimpleCollisionBox>();
                    Collisions.getCollisionBoxes(player, offsetByHorizAndStepBox, stepCollisions, false);
                    for (float stepHeight : stepHeights = Collisions.collectStepHeights(startingOffsetBox, stepCollisions, (float)stepUpHeight, (float)collisionResult.getY())) {
                        Vector3dm vec3d2 = Collisions.collideBoundingBoxLegacy(new Vector3dm(desiredX, (double)stepHeight, desiredZ), startingOffsetBox, stepCollisions, order);
                        if (!(Collisions.getHorizontalDistanceSqr(vec3d2) > Collisions.getHorizontalDistanceSqr(collisionResult))) continue;
                        double d = player.boundingBox.minY - startingOffsetBox.minY;
                        collisionResult = vec3d2.add(new Vector3dm(0.0, -d, 0.0));
                        break;
                    }
                } else {
                    Vector3dm stepUpBugFixResult;
                    Vector3dm stepUpBugFix;
                    Vector3dm regularStepUp = Collisions.collideBoundingBoxLegacy(new Vector3dm(desiredX, stepUpHeight, desiredZ), player.boundingBox, desiredMovementCollisionBoxes, order);
                    if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_8) && (stepUpBugFix = Collisions.collideBoundingBoxLegacy(new Vector3dm(0.0, stepUpHeight, 0.0), player.boundingBox.copy().expandToCoordinate(desiredX, 0.0, desiredZ), desiredMovementCollisionBoxes, order)).getY() < stepUpHeight && Collisions.getHorizontalDistanceSqr(stepUpBugFixResult = Collisions.collideBoundingBoxLegacy(new Vector3dm(desiredX, 0.0, desiredZ), player.boundingBox.copy().offset(0.0, stepUpBugFix.getY(), 0.0), desiredMovementCollisionBoxes, order).add(stepUpBugFix)) > Collisions.getHorizontalDistanceSqr(regularStepUp)) {
                        regularStepUp = stepUpBugFixResult;
                    }
                    if (Collisions.getHorizontalDistanceSqr(regularStepUp) > Collisions.getHorizontalDistanceSqr(collisionResult)) {
                        collisionResult = regularStepUp.add(Collisions.collideBoundingBoxLegacy(new Vector3dm(0.0, -regularStepUp.getY() + (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14) ? desiredY : 0.0), 0.0), player.boundingBox.copy().offset(regularStepUp.getX(), regularStepUp.getY(), regularStepUp.getZ()), desiredMovementCollisionBoxes, order));
                    }
                }
            }
            double resultAccuracy = collisionResult.distanceSquared(bestTheoreticalCollisionResult);
            if (player.wouldCollisionResultFlagGroundSpoof(desiredY, collisionResult.getY())) {
                resultAccuracy += 1.0;
            }
            if (!(resultAccuracy < bestInput)) continue;
            bestOrderResult = collisionResult;
            bestInput = resultAccuracy;
            if (!(resultAccuracy < 1.0000000000000002E-10) && zeroCount < 2) continue;
            break;
        }
        return bestOrderResult;
    }

    private static float[] collectStepHeights(SimpleCollisionBox collisionBox, List<SimpleCollisionBox> collisions, float stepHeight, float collideY) {
        FloatArraySet floatSet = new FloatArraySet(4);
        block0: for (SimpleCollisionBox blockBox : collisions) {
            DoubleListIterator doubleListIterator = blockBox.getYPointPositions().iterator();
            while (doubleListIterator.hasNext()) {
                double possibleStepY = (Double)doubleListIterator.next();
                float yDiff = (float)(possibleStepY - collisionBox.minY);
                if (yDiff < 0.0f || yDiff == collideY) continue;
                if (yDiff > stepHeight) continue block0;
                floatSet.add(yDiff);
            }
        }
        float[] fs = floatSet.toFloatArray();
        FloatArrays.unstableSort(fs);
        return fs;
    }

    public static boolean addWorldBorder(GrimPlayer player, SimpleCollisionBox wantedBB, List<SimpleCollisionBox> listOfBlocks, boolean onlyCheckCollide) {
        if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_8)) {
            double toMaxZ;
            double toMinZ;
            double minimumInZDirection;
            PacketWorldBorder border = player.checkManager.getPacketCheck(PacketWorldBorder.class);
            double minX = Math.floor(border.getMinX());
            double minZ = Math.floor(border.getMinZ());
            double maxX = Math.ceil(border.getMaxX());
            double maxZ = Math.ceil(border.getMaxZ());
            double toMinX = player.lastX - minX;
            double toMaxX = maxX - player.lastX;
            double minimumInXDirection = Math.min(toMinX, toMaxX);
            double distanceToBorder = Math.min(minimumInXDirection, minimumInZDirection = Math.min(toMinZ = player.lastZ - minZ, toMaxZ = maxZ - player.lastZ));
            if (distanceToBorder < 16.0 && player.lastX > minX && player.lastX < maxX && player.lastZ > minZ && player.lastZ < maxZ) {
                if (listOfBlocks == null) {
                    listOfBlocks = new ArrayList<SimpleCollisionBox>();
                }
                listOfBlocks.add(new SimpleCollisionBox(minX - 10.0, Double.NEGATIVE_INFINITY, maxZ, maxX + 10.0, Double.POSITIVE_INFINITY, maxZ, false));
                listOfBlocks.add(new SimpleCollisionBox(minX - 10.0, Double.NEGATIVE_INFINITY, minZ, maxX + 10.0, Double.POSITIVE_INFINITY, minZ, false));
                listOfBlocks.add(new SimpleCollisionBox(maxX, Double.NEGATIVE_INFINITY, minZ - 10.0, maxX, Double.POSITIVE_INFINITY, maxZ + 10.0, false));
                listOfBlocks.add(new SimpleCollisionBox(minX, Double.NEGATIVE_INFINITY, minZ - 10.0, minX, Double.POSITIVE_INFINITY, maxZ + 10.0, false));
                if (onlyCheckCollide) {
                    for (SimpleCollisionBox box : listOfBlocks) {
                        if (!box.isIntersected(wantedBB)) continue;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean getCollisionBoxes(GrimPlayer player, SimpleCollisionBox wantedBB, List<SimpleCollisionBox> listOfBlocks, boolean onlyCheckCollide) {
        SimpleCollisionBox expandedBB = wantedBB.copy();
        boolean collided = Collisions.addWorldBorder(player, wantedBB, listOfBlocks, onlyCheckCollide);
        if (onlyCheckCollide && collided) {
            return true;
        }
        int minBlockX = (int)Math.floor(expandedBB.minX - 1.0E-7) - 1;
        int maxBlockX = (int)Math.floor(expandedBB.maxX + 1.0E-7) + 1;
        int minBlockY = (int)Math.floor(expandedBB.minY - 1.0E-7) - 1;
        int maxBlockY = (int)Math.floor(expandedBB.maxY + 1.0E-7) + 1;
        int minBlockZ = (int)Math.floor(expandedBB.minZ - 1.0E-7) - 1;
        int maxBlockZ = (int)Math.floor(expandedBB.maxZ + 1.0E-7) + 1;
        int minSection = player.compensatedWorld.getMinHeight() >> 4;
        int minBlock = minSection << 4;
        int maxBlock = player.compensatedWorld.getMaxHeight() - 1;
        int minChunkX = minBlockX >> 4;
        int maxChunkX = maxBlockX >> 4;
        int minChunkZ = minBlockZ >> 4;
        int maxChunkZ = maxBlockZ >> 4;
        int minYIterate = Math.max(minBlock, minBlockY);
        int maxYIterate = Math.min(maxBlock, maxBlockY);
        for (int currChunkZ = minChunkZ; currChunkZ <= maxChunkZ; ++currChunkZ) {
            int minZ = currChunkZ == minChunkZ ? minBlockZ & 0xF : 0;
            int maxZ = currChunkZ == maxChunkZ ? maxBlockZ & 0xF : 15;
            for (int currChunkX = minChunkX; currChunkX <= maxChunkX; ++currChunkX) {
                int minX = currChunkX == minChunkX ? minBlockX & 0xF : 0;
                int maxX = currChunkX == maxChunkX ? maxBlockX & 0xF : 15;
                int chunkXGlobalPos = currChunkX << 4;
                int chunkZGlobalPos = currChunkZ << 4;
                Column chunk = player.compensatedWorld.getChunk(currChunkX, currChunkZ);
                if (chunk == null) continue;
                BaseChunk[] sections = chunk.chunks();
                for (int y = minYIterate; y <= maxYIterate; ++y) {
                    int sectionIndex = (y >> 4) - minSection;
                    BaseChunk section = sections[sectionIndex];
                    if (section == null || IS_FOURTEEN && section.isEmpty()) {
                        y = (y & 0xFFFFFFF0) + 15;
                        continue;
                    }
                    for (int currZ = minZ; currZ <= maxZ; ++currZ) {
                        for (int currX = minX; currX <= maxX; ++currX) {
                            int x = currX | chunkXGlobalPos;
                            int z = currZ | chunkZGlobalPos;
                            WrappedBlockState data = section.get(CompensatedWorld.blockVersion, x & 0xF, y & 0xF, z & 0xF, false);
                            if (data.getGlobalId() == 0) continue;
                            int edgeCount = (x == minBlockX || x == maxBlockX ? 1 : 0) + (y == minBlockY || y == maxBlockY ? 1 : 0) + (z == minBlockZ || z == maxBlockZ ? 1 : 0);
                            StateType type = data.getType();
                            if (edgeCount == 3 || edgeCount == 1 && !Materials.isShapeExceedsCube(type) || edgeCount == 2 && type != StateTypes.PISTON_HEAD) continue;
                            CollisionBox collisionBox = CollisionData.getData(type).getMovementCollisionBox(player, player.getClientVersion(), data, x, y, z);
                            if (!onlyCheckCollide) {
                                collisionBox.downCast(listOfBlocks);
                                continue;
                            }
                            if (!collisionBox.isCollided(wantedBB)) continue;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static Vector3dm collideBoundingBoxLegacy(Vector3dm toCollide, SimpleCollisionBox box, List<SimpleCollisionBox> desiredMovementCollisionBoxes, List<Axis> order) {
        double x = toCollide.getX();
        double y = toCollide.getY();
        double z = toCollide.getZ();
        SimpleCollisionBox setBB = box.copy();
        for (Axis axis : order) {
            if (axis == Axis.X) {
                for (SimpleCollisionBox bb : desiredMovementCollisionBoxes) {
                    x = bb.collideX(setBB, x);
                }
                setBB.offset(x, 0.0, 0.0);
                continue;
            }
            if (axis == Axis.Y) {
                for (SimpleCollisionBox bb : desiredMovementCollisionBoxes) {
                    y = bb.collideY(setBB, y);
                }
                setBB.offset(0.0, y, 0.0);
                continue;
            }
            if (axis != Axis.Z) continue;
            for (SimpleCollisionBox bb : desiredMovementCollisionBoxes) {
                z = bb.collideZ(setBB, z);
            }
            setBB.offset(0.0, 0.0, z);
        }
        return new Vector3dm(x, y, z);
    }

    public static boolean isEmpty(GrimPlayer player, SimpleCollisionBox playerBB) {
        return !Collisions.getCollisionBoxes(player, playerBB, null, true);
    }

    public static double getHorizontalDistanceSqr(Vector3dm vector) {
        return vector.getX() * vector.getX() + vector.getZ() * vector.getZ();
    }

    public static Vector3dm maybeBackOffFromEdge(Vector3dm vec3, GrimPlayer player, boolean overrideVersion) {
        if (!player.isFlying && player.isSneaking && Collisions.isAboveGround(player)) {
            double maxStepDown;
            double x = vec3.getX();
            double z = vec3.getZ();
            double d = maxStepDown = overrideVersion || player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_11) ? (double)(-player.getMaxUpStep()) : -0.9999999;
            while (x != 0.0 && Collisions.isEmpty(player, player.boundingBox.copy().offset(x, maxStepDown, 0.0))) {
                if (x < 0.05 && x >= -0.05) {
                    x = 0.0;
                    continue;
                }
                if (x > 0.0) {
                    x -= 0.05;
                    continue;
                }
                x += 0.05;
            }
            while (z != 0.0 && Collisions.isEmpty(player, player.boundingBox.copy().offset(0.0, maxStepDown, z))) {
                if (z < 0.05 && z >= -0.05) {
                    z = 0.0;
                    continue;
                }
                if (z > 0.0) {
                    z -= 0.05;
                    continue;
                }
                z += 0.05;
            }
            while (x != 0.0 && z != 0.0 && Collisions.isEmpty(player, player.boundingBox.copy().offset(x, maxStepDown, z))) {
                x = x < 0.05 && x >= -0.05 ? 0.0 : (x > 0.0 ? (x -= 0.05) : (x += 0.05));
                if (z < 0.05 && z >= -0.05) {
                    z = 0.0;
                    continue;
                }
                if (z > 0.0) {
                    z -= 0.05;
                    continue;
                }
                z += 0.05;
            }
            vec3 = new Vector3dm(x, vec3.getY(), z);
        }
        return vec3;
    }

    public static boolean isAboveGround(GrimPlayer player) {
        return player.lastOnGround || player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_16_2) && player.fallDistance < (double)player.getMaxUpStep() && !Collisions.isEmpty(player, player.boundingBox.copy().offset(0.0, player.fallDistance - (double)player.getMaxUpStep(), 0.0));
    }

    public static void handleInsideBlocks(GrimPlayer player) {
        if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_2)) {
            return;
        }
        double expandAmount = player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_19_4) ? 1.0E-5 : 0.001;
        SimpleCollisionBox aABB = (player.inVehicle() ? GetBoundingBox.getCollisionBoxForPlayer(player, player.x, player.y, player.z) : player.boundingBox.copy()).expand(-expandAmount);
        Location blockPos = new Location(null, aABB.minX, aABB.minY, aABB.minZ);
        Location blockPos2 = new Location(null, aABB.maxX, aABB.maxY, aABB.maxZ);
        if (CheckIfChunksLoaded.areChunksUnloadedAt(player, blockPos.getBlockX(), blockPos.getBlockY(), blockPos.getBlockZ(), blockPos2.getBlockX(), blockPos2.getBlockY(), blockPos2.getBlockZ())) {
            return;
        }
        for (int blockX = blockPos.getBlockX(); blockX <= blockPos2.getBlockX(); ++blockX) {
            for (int blockY = blockPos.getBlockY(); blockY <= blockPos2.getBlockY(); ++blockY) {
                for (int blockZ = blockPos.getBlockZ(); blockZ <= blockPos2.getBlockZ(); ++blockZ) {
                    WrappedBlockState block = player.compensatedWorld.getBlock(blockX, blockY, blockZ);
                    StateType blockType = block.getType();
                    if (blockType.isAir()) continue;
                    Collisions.onInsideBlock(player, blockType, block, blockX, blockY, blockZ, true);
                }
            }
        }
    }

    public static void onInsideBlock(GrimPlayer player, StateType blockType, WrappedBlockState block, int blockX, int blockY, int blockZ, boolean magic) {
        if (blockType == StateTypes.COBWEB) {
            player.stuckSpeedMultiplier = player.compensatedEntities.hasPotionEffect(PotionTypes.WEAVING) ? new Vector3dm(0.5, 0.25, 0.5) : new Vector3dm(0.25, (double)0.05f, 0.25);
        }
        if (blockType == StateTypes.SWEET_BERRY_BUSH && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14)) {
            player.stuckSpeedMultiplier = new Vector3dm((double)0.8f, 0.75, (double)0.8f);
        }
        if (blockType == StateTypes.POWDER_SNOW && (double)blockX == Math.floor(player.x) && (double)blockY == Math.floor(player.y) && (double)blockZ == Math.floor(player.z) && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_17)) {
            player.stuckSpeedMultiplier = new Vector3dm((double)0.9f, 1.5, (double)0.9f);
        }
        if (blockType == StateTypes.SOUL_SAND && player.getClientVersion().isOlderThan(ClientVersion.V_1_15)) {
            player.clientVelocity.setX(player.clientVelocity.getX() * 0.4);
            player.clientVelocity.setZ(player.clientVelocity.getZ() * 0.4);
        }
        if (blockType == StateTypes.LAVA && player.getClientVersion().isOlderThan(ClientVersion.V_1_16) && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14)) {
            player.wasTouchingLava = true;
        }
        if (blockType == StateTypes.BUBBLE_COLUMN && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13) && magic) {
            WrappedBlockState blockAbove = player.compensatedWorld.getBlock(blockX, blockY + 1, blockZ);
            if (player.inVehicle() && player.compensatedEntities.self.getRiding().isBoat) {
                if (!blockAbove.getType().isAir()) {
                    if (block.isDrag()) {
                        player.clientVelocity.setY(Math.max(-0.3, player.clientVelocity.getY() - 0.03));
                    } else {
                        player.clientVelocity.setY(Math.min(0.7, player.clientVelocity.getY() + 0.06));
                    }
                }
            } else if (blockAbove.getType().isAir()) {
                for (VectorData vector : player.getPossibleVelocitiesMinusKnockback()) {
                    if (block.isDrag()) {
                        vector.vector.setY(Math.max(-0.9, vector.vector.getY() - 0.03));
                        continue;
                    }
                    vector.vector.setY(Math.min(1.8, vector.vector.getY() + 0.1));
                }
            } else {
                for (VectorData vector : player.getPossibleVelocitiesMinusKnockback()) {
                    if (block.isDrag()) {
                        vector.vector.setY(Math.max(-0.3, vector.vector.getY() - 0.03));
                        continue;
                    }
                    vector.vector.setY(Math.min(0.7, vector.vector.getY() + 0.06));
                }
            }
            player.fallDistance = 0.0;
        }
        if (blockType == StateTypes.HONEY_BLOCK && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_15)) {
            if (Collisions.isSlidingDown(player.clientVelocity, player, blockX, blockY, blockZ)) {
                if (Collisions.getOldDeltaY(player, player.clientVelocity.getY()) < -0.13) {
                    double d0 = -0.05 / Collisions.getOldDeltaY(player, player.clientVelocity.getY());
                    player.clientVelocity.setX(player.clientVelocity.getX() * d0);
                    player.clientVelocity.setY(Collisions.getNewDeltaY(player, -0.05));
                    player.clientVelocity.setZ(player.clientVelocity.getZ() * d0);
                } else {
                    player.clientVelocity.setY(Collisions.getNewDeltaY(player, -0.05));
                }
            }
            player.fallDistance = 0.0;
        }
    }

    public static void applyEffectsFromBlocks(GrimPlayer player) {
        if (player.getClientVersion().isOlderThan(ClientVersion.V_1_21_2)) {
            return;
        }
        if (player.stuckSpeedMultiplier.getX() < 0.99) {
            player.uncertaintyHandler.lastStuckSpeedMultiplier.reset();
        }
        player.stuckSpeedMultiplier = new Vector3dm(1, 1, 1);
        player.finalMovementsThisTick.clear();
        Vector3d from = new Vector3d(player.lastX, player.lastY, player.lastZ);
        Vector3d to = new Vector3d(player.x, player.y, player.z);
        ClientVersion clientVersion = player.getClientVersion();
        if (clientVersion.isOlderThan(ClientVersion.V_1_21_5)) {
            player.finalMovementsThisTick.add(new GrimPlayer.Movement(from, to));
        } else if (clientVersion.isNewerThanOrEquals(ClientVersion.V_1_21_5)) {
            player.finalMovementsThisTick.addAll(player.movementThisTick);
            player.movementThisTick.clear();
            if (player.finalMovementsThisTick.isEmpty()) {
                player.finalMovementsThisTick.add(new GrimPlayer.Movement(from, to));
            } else if (player.finalMovementsThisTick.get(player.finalMovementsThisTick.size() - 1).to().distanceSquared(to) > 9.999999439624929E-11) {
                player.finalMovementsThisTick.add(new GrimPlayer.Movement(player.finalMovementsThisTick.get(player.finalMovementsThisTick.size() - 1).to(), to));
            }
        }
        Collisions.resolveBlockEffects(player, player.finalMovementsThisTick);
        if (player.stuckSpeedMultiplier.getX() < 0.9) {
            player.fallDistance = 0.0;
        }
        if (player.isFlying) {
            player.stuckSpeedMultiplier = new Vector3dm(1, 1, 1);
        }
    }

    public static void resolveBlockEffects(GrimPlayer player, Vector3d from, Vector3d to) {
        Collisions.resolveBlockEffects(player, List.of(new GrimPlayer.Movement(from, to)));
    }

    public static void resolveBlockEffects(GrimPlayer player, List<GrimPlayer.Movement> movements) {
        ClientVersion version = player.getClientVersion();
        BlockEffectsResolver resolver = version == ClientVersion.V_1_21_2 ? BlockEffectsResolverV1_21_2.INSTANCE : (version == ClientVersion.V_1_21_4 ? BlockEffectsResolverV1_21_4.INSTANCE : (version == ClientVersion.V_1_21_5 ? BlockEffectsResolverV1_21_5.INSTANCE : (version.isNewerThanOrEquals(ClientVersion.V_1_21_6) && version.isOlderThanOrEquals(ClientVersion.V_1_21_7) ? BlockEffectsResolverV1_21_6.INSTANCE : BlockEffectsResolverV1_21_10.INSTANCE)));
        resolver.applyEffectsFromBlocks(player, movements);
    }

    private static double getOldDeltaY(GrimPlayer player, double value) {
        return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_2) ? value / (double)0.98f + 0.08 : value;
    }

    private static double getNewDeltaY(GrimPlayer player, double value) {
        return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_2) ? (value - 0.08) * (double)0.98f : value;
    }

    private static boolean isSlidingDown(Vector3dm vector, GrimPlayer player, int locationX, int locationY, int locationZ) {
        if (player.onGround) {
            return false;
        }
        if (player.y > (double)locationY + 0.9375 - 1.0E-7) {
            return false;
        }
        if (Collisions.getOldDeltaY(player, vector.getY()) >= -0.08) {
            return false;
        }
        double d0 = Math.abs((double)locationX + 0.5 - player.lastX);
        double d1 = Math.abs((double)locationZ + 0.5 - player.lastZ);
        double d2 = 0.4375 + (double)(player.pose.width / 2.0f);
        return d0 + 1.0E-7 > d2 || d1 + 1.0E-7 > d2;
    }

    public static boolean checkStuckSpeed(GrimPlayer player, double expand) {
        SimpleCollisionBox aABB = GetBoundingBox.getCollisionBoxForPlayer(player, player.x, player.y, player.z).expand(expand);
        Location blockPos = new Location(null, aABB.minX, aABB.minY, aABB.minZ);
        Location blockPos2 = new Location(null, aABB.maxX, aABB.maxY, aABB.maxZ);
        if (CheckIfChunksLoaded.areChunksUnloadedAt(player, blockPos.getBlockX(), blockPos.getBlockY(), blockPos.getBlockZ(), blockPos2.getBlockX(), blockPos2.getBlockY(), blockPos2.getBlockZ())) {
            return false;
        }
        for (int i = blockPos.getBlockX(); i <= blockPos2.getBlockX(); ++i) {
            for (int j = blockPos.getBlockY(); j <= blockPos2.getBlockY(); ++j) {
                for (int k = blockPos.getBlockZ(); k <= blockPos2.getBlockZ(); ++k) {
                    WrappedBlockState block = player.compensatedWorld.getBlock(i, j, k);
                    StateType blockType = block.getType();
                    if (blockType == StateTypes.COBWEB) {
                        return true;
                    }
                    if (blockType == StateTypes.SWEET_BERRY_BUSH && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14)) {
                        return true;
                    }
                    if (blockType != StateTypes.POWDER_SNOW || (double)i != Math.floor(player.x) || (double)j != Math.floor(player.y) || (double)k != Math.floor(player.z) || !player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_17)) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean suffocatesAt(GrimPlayer player, SimpleCollisionBox playerBB) {
        int y = (int)Math.floor(playerBB.minY);
        while ((double)y < Math.ceil(playerBB.maxY)) {
            int z = (int)Math.floor(playerBB.minZ);
            while ((double)z < Math.ceil(playerBB.maxZ)) {
                int x = (int)Math.floor(playerBB.minX);
                while ((double)x < Math.ceil(playerBB.maxX)) {
                    WrappedBlockState data;
                    CollisionBox box;
                    if (Collisions.doesBlockSuffocate(player, x, y, z) && (!player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_16) || (box = CollisionData.getData((data = player.compensatedWorld.getBlock(x, y, z)).getType()).getMovementCollisionBox(player, player.getClientVersion(), data, x, y, z)).isIntersected(playerBB))) {
                        return true;
                    }
                    ++x;
                }
                ++z;
            }
            ++y;
        }
        return false;
    }

    public static boolean doesBlockSuffocate(GrimPlayer player, int x, int y, int z) {
        WrappedBlockState data = player.compensatedWorld.getBlock(x, y, z);
        StateType mat = data.getType();
        if (!mat.isSolid()) {
            return false;
        }
        if (mat == StateTypes.OBSERVER || mat == StateTypes.REDSTONE_BLOCK) {
            return player.getClientVersion().isNewerThan(ClientVersion.V_1_13_2);
        }
        if (mat == StateTypes.TNT) {
            return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14);
        }
        if (mat == StateTypes.FARMLAND) {
            return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_16);
        }
        if (mat == StateTypes.SOUL_SAND) {
            return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_16) || player.getClientVersion().isOlderThan(ClientVersion.V_1_14);
        }
        if ((mat == StateTypes.PISTON || mat == StateTypes.STICKY_PISTON) && player.getClientVersion().isOlderThan(ClientVersion.V_1_14)) {
            return false;
        }
        if (mat == StateTypes.ICE || mat == StateTypes.FROSTED_ICE) {
            return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14);
        }
        if (BlockTags.LEAVES.contains(mat) || BlockTags.GLASS_BLOCKS.contains(mat)) {
            return false;
        }
        if (mat == StateTypes.DIRT_PATH) {
            return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_16) || player.getClientVersion().isOlderThan(ClientVersion.V_1_9);
        }
        if (mat == StateTypes.BEACON) {
            return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14);
        }
        if (Materials.isSolidBlockingBlacklist(mat, player.getClientVersion())) {
            return false;
        }
        CollisionBox box = CollisionData.getData(mat).getMovementCollisionBox(player, player.getClientVersion(), data, x, y, z);
        return box.isFullBlock();
    }

    public static boolean hasMaterial(GrimPlayer player, SimpleCollisionBox checkBox, Predicate<Pair<WrappedBlockState, Vector3i>> searchingFor) {
        int minBlockX = (int)Math.floor(checkBox.minX);
        int maxBlockX = (int)Math.floor(checkBox.maxX);
        int minBlockY = (int)Math.floor(checkBox.minY);
        int maxBlockY = (int)Math.floor(checkBox.maxY);
        int minBlockZ = (int)Math.floor(checkBox.minZ);
        int maxBlockZ = (int)Math.floor(checkBox.maxZ);
        int minSection = player.compensatedWorld.getMinHeight() >> 4;
        int minBlock = minSection << 4;
        int maxBlock = player.compensatedWorld.getMaxHeight() - 1;
        int minChunkX = minBlockX >> 4;
        int maxChunkX = maxBlockX >> 4;
        int minChunkZ = minBlockZ >> 4;
        int maxChunkZ = maxBlockZ >> 4;
        int minYIterate = Math.max(minBlock, minBlockY);
        int maxYIterate = Math.min(maxBlock, maxBlockY);
        for (int currChunkZ = minChunkZ; currChunkZ <= maxChunkZ; ++currChunkZ) {
            int minZ = currChunkZ == minChunkZ ? minBlockZ & 0xF : 0;
            int maxZ = currChunkZ == maxChunkZ ? maxBlockZ & 0xF : 15;
            for (int currChunkX = minChunkX; currChunkX <= maxChunkX; ++currChunkX) {
                int minX = currChunkX == minChunkX ? minBlockX & 0xF : 0;
                int maxX = currChunkX == maxChunkX ? maxBlockX & 0xF : 15;
                int chunkXGlobalPos = currChunkX << 4;
                int chunkZGlobalPos = currChunkZ << 4;
                Column chunk = player.compensatedWorld.getChunk(currChunkX, currChunkZ);
                if (chunk == null) continue;
                BaseChunk[] sections = chunk.chunks();
                for (int y = minYIterate; y <= maxYIterate; ++y) {
                    BaseChunk section = sections[(y >> 4) - minSection];
                    if (section == null || IS_FOURTEEN && section.isEmpty()) {
                        y = (y & 0xFFFFFFF0) + 15;
                        continue;
                    }
                    for (int currZ = minZ; currZ <= maxZ; ++currZ) {
                        for (int currX = minX; currX <= maxX; ++currX) {
                            int x = currX | chunkXGlobalPos;
                            int z = currZ | chunkZGlobalPos;
                            WrappedBlockState data = section.get(CompensatedWorld.blockVersion, x & 0xF, y & 0xF, z & 0xF, false);
                            if (!searchingFor.test(new Pair<WrappedBlockState, Vector3i>(data, new Vector3i(x, y, z)))) continue;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void forEachCollisionBox(@NotNull GrimPlayer player, @NotNull SimpleCollisionBox checkBox, @NotNull @NotNull Consumer<@NotNull Vector3d> searchingFor) {
        int minBlockX = (int)Math.floor(checkBox.minX - 1.0E-7) - 1;
        int maxBlockX = (int)Math.floor(checkBox.maxX + 1.0E-7) + 1;
        int minBlockY = (int)Math.floor(checkBox.minY - 1.0E-7) - 1;
        int maxBlockY = (int)Math.floor(checkBox.maxY + 1.0E-7) + 1;
        int minBlockZ = (int)Math.floor(checkBox.minZ - 1.0E-7) - 1;
        int maxBlockZ = (int)Math.floor(checkBox.maxZ + 1.0E-7) + 1;
        int minSection = player.compensatedWorld.getMinHeight() >> 4;
        int minBlock = minSection << 4;
        int maxBlock = player.compensatedWorld.getMaxHeight() - 1;
        int minChunkX = minBlockX >> 4;
        int maxChunkX = maxBlockX >> 4;
        int minChunkZ = minBlockZ >> 4;
        int maxChunkZ = maxBlockZ >> 4;
        int minYIterate = Math.max(minBlock, minBlockY);
        int maxYIterate = Math.min(maxBlock, maxBlockY);
        for (int currChunkZ = minChunkZ; currChunkZ <= maxChunkZ; ++currChunkZ) {
            int minZ = currChunkZ == minChunkZ ? minBlockZ & 0xF : 0;
            int maxZ = currChunkZ == maxChunkZ ? maxBlockZ & 0xF : 15;
            for (int currChunkX = minChunkX; currChunkX <= maxChunkX; ++currChunkX) {
                int minX = currChunkX == minChunkX ? minBlockX & 0xF : 0;
                int maxX = currChunkX == maxChunkX ? maxBlockX & 0xF : 15;
                int chunkXGlobalPos = currChunkX << 4;
                int chunkZGlobalPos = currChunkZ << 4;
                Column chunk = player.compensatedWorld.getChunk(currChunkX, currChunkZ);
                if (chunk == null) continue;
                BaseChunk[] sections = chunk.chunks();
                for (int y = minYIterate; y <= maxYIterate; ++y) {
                    BaseChunk section = sections[(y >> 4) - minSection];
                    if (section == null || IS_FOURTEEN && section.isEmpty()) {
                        y = (y & 0xFFFFFFF0) + 15;
                        continue;
                    }
                    for (int currZ = minZ; currZ <= maxZ; ++currZ) {
                        for (int currX = minX; currX <= maxX; ++currX) {
                            CollisionBox collisionBox;
                            int x = currX | chunkXGlobalPos;
                            int z = currZ | chunkZGlobalPos;
                            WrappedBlockState data = section.get(CompensatedWorld.blockVersion, x & 0xF, y & 0xF, z & 0xF, false);
                            if (data.getGlobalId() == 0) continue;
                            int edgeCount = (x == minBlockX || x == maxBlockX ? 1 : 0) + (y == minBlockY || y == maxBlockY ? 1 : 0) + (z == minBlockZ || z == maxBlockZ ? 1 : 0);
                            StateType type = data.getType();
                            if (edgeCount == 3 || edgeCount == 1 && !Materials.isShapeExceedsCube(type) || edgeCount == 2 && type != StateTypes.PISTON_HEAD || !(collisionBox = CollisionData.getData(type).getMovementCollisionBox(player, player.getClientVersion(), data, x, y, z)).isIntersected(checkBox)) continue;
                            searchingFor.accept(new Vector3d(x, y, z));
                        }
                    }
                }
            }
        }
    }

    public static boolean onClimbable(GrimPlayer player, double x, double y, double z) {
        WrappedBlockState blockState = player.compensatedWorld.getBlock(x, y, z);
        StateType blockMaterial = blockState.getType();
        if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_11) && player.isGliding && BlockTags.CAN_GLIDE_THROUGH.contains(blockMaterial)) {
            return false;
        }
        if (blockMaterial == StateTypes.CAVE_VINES || blockMaterial == StateTypes.CAVE_VINES_PLANT) {
            return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_17);
        }
        if (player.tagManager.block(SyncedTags.CLIMBABLE).contains(blockMaterial)) {
            return true;
        }
        if (blockMaterial == StateTypes.SWEET_BERRY_BUSH && player.getClientVersion().isOlderThan(ClientVersion.V_1_14)) {
            return true;
        }
        return Collisions.trapdoorUsableAsLadder(player, x, y, z, blockState);
    }

    public static boolean trapdoorUsableAsLadder(GrimPlayer player, double x, double y, double z, WrappedBlockState blockData) {
        WrappedBlockState blockBelow;
        if (!BlockTags.TRAPDOORS.contains(blockData.getType())) {
            return false;
        }
        if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_8)) {
            return false;
        }
        if (blockData.isOpen() && (blockBelow = player.compensatedWorld.getBlock(x, y - 1.0, z)).getType() == StateTypes.LADDER) {
            return blockData.getFacing() == blockBelow.getFacing();
        }
        return false;
    }

    @Generated
    private Collisions() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /*
     * Uses 'sealed' constructs - enablewith --sealed true
     */
    public static enum Axis {
        X{

            @Override
            public double get(Vector3d vector) {
                return vector.getX();
            }

            @Override
            public int get(Vector3i vector) {
                return vector.getX();
            }

            @Override
            public double choose(double x, double y, double z) {
                return x;
            }

            @Override
            public int choose(int x, int y, int z) {
                return x;
            }

            @Override
            public Direction getPositive() {
                return Direction.EAST;
            }

            @Override
            public Direction getNegative() {
                return Direction.WEST;
            }
        }
        ,
        Y{

            @Override
            public double get(Vector3d vector) {
                return vector.getY();
            }

            @Override
            public int get(Vector3i vector) {
                return vector.getY();
            }

            @Override
            public double choose(double x, double y, double z) {
                return y;
            }

            @Override
            public int choose(int x, int y, int z) {
                return y;
            }

            @Override
            public Direction getPositive() {
                return Direction.UP;
            }

            @Override
            public Direction getNegative() {
                return Direction.DOWN;
            }
        }
        ,
        Z{

            @Override
            public double get(Vector3d vector) {
                return vector.getZ();
            }

            @Override
            public int get(Vector3i vector) {
                return vector.getZ();
            }

            @Override
            public double choose(double x, double y, double z) {
                return z;
            }

            @Override
            public int choose(int x, int y, int z) {
                return z;
            }

            @Override
            public Direction getPositive() {
                return Direction.SOUTH;
            }

            @Override
            public Direction getNegative() {
                return Direction.NORTH;
            }
        };


        public abstract double get(Vector3d var1);

        public abstract int get(Vector3i var1);

        public abstract double choose(double var1, double var3, double var5);

        public abstract int choose(int var1, int var2, int var3);

        public abstract Direction getPositive();

        public abstract Direction getNegative();
    }
}

