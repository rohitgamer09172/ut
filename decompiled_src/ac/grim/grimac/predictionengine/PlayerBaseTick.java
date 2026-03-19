/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.predictionengine;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.attribute.ValuedAttribute;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import ac.grim.grimac.utils.data.packetentity.PacketEntityNautilus;
import ac.grim.grimac.utils.enums.FluidTag;
import ac.grim.grimac.utils.enums.Pose;
import ac.grim.grimac.utils.latency.CompensatedEntities;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.nmsutil.BlockProperties;
import ac.grim.grimac.utils.nmsutil.CheckIfChunksLoaded;
import ac.grim.grimac.utils.nmsutil.Collisions;
import ac.grim.grimac.utils.nmsutil.FluidTypeFlowing;
import ac.grim.grimac.utils.nmsutil.GetBoundingBox;
import java.util.Optional;
import lombok.Generated;

public final class PlayerBaseTick {
    private static final boolean SERVER_SUPPORT_ENVIRONMENT_ATTRIBUTES = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21_11);

    public static boolean canEnterPose(GrimPlayer player, Pose pose, double x, double y, double z) {
        return Collisions.isEmpty(player, PlayerBaseTick.getBoundingBoxForPose(player, pose, x, y, z).expand(-1.0E-7));
    }

    private static SimpleCollisionBox getBoundingBoxForPose(GrimPlayer player, Pose pose, double x, double y, double z) {
        float scale = (float)player.compensatedEntities.self.getAttributeValue(Attributes.SCALE);
        float width = pose.width * scale;
        float height = pose.height * scale;
        float radius = width / 2.0f;
        return new SimpleCollisionBox(x - (double)radius, y, z - (double)radius, x + (double)radius, y + (double)height, z + (double)radius, false);
    }

    public static void doBaseTick(GrimPlayer player) {
        player.baseTickAddition = new Vector3dm();
        player.baseTickWaterPushing = new Vector3dm();
        if (player.isFlying && player.isSneaking && !player.inVehicle()) {
            Vector3dm flyingShift = new Vector3dm(0.0f, player.flySpeed * -3.0f, 0.0f);
            player.baseTickAddVector(flyingShift);
            player.trackBaseTickAddition(flyingShift);
        }
        PlayerBaseTick.updateInWaterStateAndDoFluidPushing(player);
        PlayerBaseTick.updateFluidOnEyes(player);
        PlayerBaseTick.updateSwimming(player);
        if (player.wasTouchingLava) {
            player.fallDistance *= 0.5;
        }
        if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13) && player.wasTouchingWater && player.isSneaking && !player.isFlying && !player.inVehicle()) {
            Vector3dm waterPushVector = new Vector3dm(0.0f, -0.04f, 0.0f);
            player.baseTickAddVector(waterPushVector);
            player.trackBaseTickAddition(waterPushVector);
        }
        player.lastPose = player.pose;
        if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_13_2)) {
            player.isSlowMovement = player.isSneaking;
        } else {
            boolean bl = player.isSlowMovement = !player.wasFlying && !player.isSwimming && PlayerBaseTick.canEnterPose(player, Pose.CROUCHING, player.lastX, player.lastY, player.lastZ) && (player.wasSneaking || !player.isInBed && !PlayerBaseTick.canEnterPose(player, Pose.STANDING, player.lastX, player.lastY, player.lastZ)) || (player.pose == Pose.SWIMMING || !player.isGliding && player.pose == Pose.FALL_FLYING) && !player.wasTouchingWater;
            if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14) && player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_14_4)) {
                player.isSlowMovement |= player.isSneaking;
            }
        }
        if (player.inVehicle()) {
            player.isSlowMovement = false;
        }
        if (!player.inVehicle()) {
            PlayerBaseTick.moveTowardsClosestSpace(player, player.lastX - (player.boundingBox.maxX - player.boundingBox.minX) * 0.35, player.lastZ + (player.boundingBox.maxZ - player.boundingBox.minZ) * 0.35);
            PlayerBaseTick.moveTowardsClosestSpace(player, player.lastX - (player.boundingBox.maxX - player.boundingBox.minX) * 0.35, player.lastZ - (player.boundingBox.maxZ - player.boundingBox.minZ) * 0.35);
            PlayerBaseTick.moveTowardsClosestSpace(player, player.lastX + (player.boundingBox.maxX - player.boundingBox.minX) * 0.35, player.lastZ - (player.boundingBox.maxZ - player.boundingBox.minZ) * 0.35);
            PlayerBaseTick.moveTowardsClosestSpace(player, player.lastX + (player.boundingBox.maxX - player.boundingBox.minX) * 0.35, player.lastZ + (player.boundingBox.maxZ - player.boundingBox.minZ) * 0.35);
        }
        if (player.getClientVersion().isOlderThan(ClientVersion.V_1_14)) {
            PlayerBaseTick.updatePlayerSize(player);
        }
    }

    private static void updateFluidOnEyes(GrimPlayer player) {
        player.wasEyeInWater = player.fluidOnEyes == FluidTag.WATER;
        player.fluidOnEyes = null;
        double d0 = player.lastY + player.getEyeHeight() - 0.1111111119389534;
        PacketEntity riding = player.compensatedEntities.self.getRiding();
        if (riding != null && riding.isBoat && !player.vehicleData.boatUnderwater && player.boundingBox.maxY >= d0 && player.boundingBox.minY <= d0) {
            return;
        }
        double d1 = (double)((float)Math.floor(d0)) + player.compensatedWorld.getWaterFluidLevelAt(player.lastX, d0, player.lastZ);
        if (d1 > d0) {
            player.fluidOnEyes = FluidTag.WATER;
            if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_15_2)) {
                player.wasEyeInWater = true;
            }
            return;
        }
        if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_15_2)) {
            player.wasEyeInWater = false;
        }
        if ((d1 = (double)((float)Math.floor(d0)) + player.compensatedWorld.getWaterFluidLevelAt(player.lastX, d0, player.lastZ)) > d0) {
            player.fluidOnEyes = FluidTag.LAVA;
        }
    }

    private static void updateInWaterStateAndDoFluidPushing(GrimPlayer player) {
        double multiplier;
        player.fluidHeight.clear();
        PlayerBaseTick.updateInWaterStateAndDoWaterCurrentPushing(player);
        boolean fastLava = SERVER_SUPPORT_ENVIRONMENT_ATTRIBUTES && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_11) ? player.dimensionType.getAttributes().getOrDefault(EnvironmentAttributes.GAMEPLAY_FAST_LAVA).booleanValue() : player.dimensionType.isUltraWarm();
        double d = multiplier = fastLava ? 0.007 : 0.0023333333333333335;
        if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_16)) {
            player.wasTouchingLava = PlayerBaseTick.updateFluidHeightAndDoFluidPushing(player, FluidTag.LAVA, multiplier);
        } else if (player.getClientVersion().isOlderThan(ClientVersion.V_1_14)) {
            SimpleCollisionBox playerBox = player.boundingBox.copy().expand(-0.1f, -0.4f, -0.1f);
            player.wasTouchingLava = player.compensatedWorld.containsLava(playerBox);
        }
    }

    public static void updatePowderSnow(GrimPlayer player) {
        int i;
        if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_16_4)) {
            return;
        }
        ValuedAttribute playerSpeed = player.compensatedEntities.self.getAttribute(Attributes.MOVEMENT_SPEED).orElseThrow();
        Optional<WrapperPlayServerUpdateAttributes.Property> property = playerSpeed.property();
        if (property.isEmpty()) {
            return;
        }
        property.get().getModifiers().removeIf(modifier -> modifier.getUUID().equals(CompensatedEntities.SNOW_MODIFIER_UUID) || modifier.getName().getKey().equals("powder_snow"));
        playerSpeed.recalculate();
        StateType type = BlockProperties.getOnPos(player, player.mainSupportingBlockData, new Vector3d(player.x, player.y, player.z));
        if (!type.isAir() && (i = player.powderSnowFrozenTicks) > 0) {
            int ticksToFreeze = 140;
            float percentFrozen = (float)Math.min(i, ticksToFreeze) / (float)ticksToFreeze;
            float percentFrozenReducedToSpeed = -0.05f * percentFrozen;
            property.get().getModifiers().add(new WrapperPlayServerUpdateAttributes.PropertyModifier(CompensatedEntities.SNOW_MODIFIER_UUID, (double)percentFrozenReducedToSpeed, WrapperPlayServerUpdateAttributes.PropertyModifier.Operation.ADDITION));
            playerSpeed.recalculate();
        }
    }

    public static void updatePlayerPose(GrimPlayer player) {
        if (PlayerBaseTick.canEnterPose(player, Pose.SWIMMING, player.x, player.y, player.z)) {
            Pose pose = player.isGliding ? Pose.FALL_FLYING : (player.isInBed ? Pose.SLEEPING : (player.isSwimming ? Pose.SWIMMING : (player.isRiptidePose ? Pose.SPIN_ATTACK : (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) && player.getClientVersion().isOlderThan(ClientVersion.V_1_14) && player.isSneaking ? Pose.NINE_CROUCHING : (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14) && player.isSneaking && !player.isFlying ? Pose.CROUCHING : Pose.STANDING)))));
            if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14) && !player.inVehicle() && !PlayerBaseTick.canEnterPose(player, pose, player.x, player.y, player.z)) {
                pose = PlayerBaseTick.canEnterPose(player, Pose.CROUCHING, player.x, player.y, player.z) ? Pose.CROUCHING : Pose.SWIMMING;
            }
            player.pose = pose;
            player.boundingBox = PlayerBaseTick.getBoundingBoxForPose(player, player.pose, player.x, player.y, player.z);
        }
    }

    private static void updatePlayerSize(GrimPlayer player) {
        Pose pose = player.isGliding ? Pose.FALL_FLYING : (player.isInBed ? Pose.SLEEPING : (!player.isSwimming && !player.isRiptidePose ? (player.isSneaking && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) ? Pose.NINE_CROUCHING : Pose.STANDING) : Pose.SWIMMING));
        if (pose != player.pose) {
            boolean collides;
            Pose oldPose = player.pose;
            player.pose = pose;
            SimpleCollisionBox box = GetBoundingBox.getCollisionBoxForPlayer(player, player.lastX, player.lastY, player.lastZ);
            boolean bl = collides = !Collisions.isEmpty(player, box);
            if (collides) {
                player.pose = oldPose;
                return;
            }
        }
        player.boundingBox = GetBoundingBox.getCollisionBoxForPlayer(player, player.lastX, player.lastY, player.lastZ);
    }

    private static void updateSwimming(GrimPlayer player) {
        if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_12_2)) {
            player.isSwimming = false;
        } else if (player.isFlying) {
            player.isSwimming = false;
        } else if (player.inVehicle()) {
            player.isSwimming = false;
        } else if (player.isSwimming) {
            player.isSwimming = player.lastSprinting && player.wasTouchingWater;
        } else {
            boolean feetInWater = player.getClientVersion().isOlderThan(ClientVersion.V_1_17) || player.compensatedWorld.getWaterFluidLevelAt(player.lastX, player.lastY, player.lastZ) > 0.0;
            player.isSwimming = player.lastSprinting && player.wasEyeInWater && player.wasTouchingWater && feetInWater;
        }
    }

    private static void moveTowardsClosestSpace(GrimPlayer player, double xPosition, double zPosition) {
        double movementThreshold = player.getMovementThreshold();
        player.boundingBox = player.boundingBox.expand(movementThreshold, 0.0, movementThreshold);
        if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14)) {
            PlayerBaseTick.moveTowardsClosestSpaceModern(player, xPosition, zPosition);
        } else {
            PlayerBaseTick.moveTowardsClosestSpaceLegacy(player, xPosition, zPosition);
        }
        player.boundingBox = player.boundingBox.expand(-movementThreshold, 0.0, -movementThreshold);
    }

    private static void moveTowardsClosestSpaceLegacy(GrimPlayer player, double x, double z) {
        boolean suffocates;
        int floorX = GrimMath.floor(x);
        int floorZ = GrimMath.floor(z);
        int floorY = GrimMath.floor(player.lastY + 0.5);
        double d0 = x - (double)floorX;
        double d1 = z - (double)floorZ;
        if (player.isSwimming) {
            SimpleCollisionBox blockPos = new SimpleCollisionBox(floorX, floorY, floorZ, (double)floorX + 1.0, floorY + 1, (double)floorZ + 1.0, false).expand(-1.0E-7);
            suffocates = Collisions.suffocatesAt(player, blockPos);
        } else {
            boolean bl = suffocates = !PlayerBaseTick.clearAbove(player, floorX, floorY, floorZ);
        }
        if (suffocates) {
            int i = -1;
            double d2 = 9999.0;
            if (PlayerBaseTick.clearAbove(player, floorX - 1, floorY, floorZ) && d0 < d2) {
                d2 = d0;
                i = 0;
            }
            if (PlayerBaseTick.clearAbove(player, floorX + 1, floorY, floorZ) && 1.0 - d0 < d2) {
                d2 = 1.0 - d0;
                i = 1;
            }
            if (PlayerBaseTick.clearAbove(player, floorX, floorY, floorZ - 1) && d1 < d2) {
                d2 = d1;
                i = 4;
            }
            if (PlayerBaseTick.clearAbove(player, floorX, floorY, floorZ + 1) && 1.0 - d1 < d2) {
                i = 5;
            }
            if (i == 0) {
                player.uncertaintyHandler.xNegativeUncertainty -= 0.1;
                player.uncertaintyHandler.xPositiveUncertainty += 0.1;
                player.pointThreeEstimator.setPushing(true);
            }
            if (i == 1) {
                player.uncertaintyHandler.xNegativeUncertainty -= 0.1;
                player.uncertaintyHandler.xPositiveUncertainty += 0.1;
                player.pointThreeEstimator.setPushing(true);
            }
            if (i == 4) {
                player.uncertaintyHandler.zNegativeUncertainty -= 0.1;
                player.uncertaintyHandler.zPositiveUncertainty += 0.1;
                player.pointThreeEstimator.setPushing(true);
            }
            if (i == 5) {
                player.uncertaintyHandler.zNegativeUncertainty -= 0.1;
                player.uncertaintyHandler.zPositiveUncertainty += 0.1;
                player.pointThreeEstimator.setPushing(true);
            }
        }
    }

    private static void moveTowardsClosestSpaceModern(GrimPlayer player, double xPosition, double zPosition) {
        int blockZ;
        int blockX = (int)Math.floor(xPosition);
        if (!PlayerBaseTick.suffocatesAt(player, blockX, blockZ = (int)Math.floor(zPosition))) {
            return;
        }
        double relativeXMovement = xPosition - (double)blockX;
        double relativeZMovement = zPosition - (double)blockZ;
        BlockFace direction = null;
        double lowestValue = Double.MAX_VALUE;
        for (BlockFace direction2 : new BlockFace[]{BlockFace.WEST, BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH}) {
            boolean doesSuffocate;
            double d7 = direction2 == BlockFace.WEST || direction2 == BlockFace.EAST ? relativeXMovement : relativeZMovement;
            double d6 = direction2 == BlockFace.EAST || direction2 == BlockFace.SOUTH ? 1.0 - d7 : d7;
            switch (direction2) {
                case EAST: {
                    boolean bl = PlayerBaseTick.suffocatesAt(player, blockX + 1, blockZ);
                    break;
                }
                case WEST: {
                    boolean bl = PlayerBaseTick.suffocatesAt(player, blockX - 1, blockZ);
                    break;
                }
                case NORTH: {
                    boolean bl = PlayerBaseTick.suffocatesAt(player, blockX, blockZ - 1);
                    break;
                }
                default: {
                    boolean bl = doesSuffocate = PlayerBaseTick.suffocatesAt(player, blockX, blockZ + 1);
                }
            }
            if (d6 >= lowestValue || doesSuffocate) continue;
            lowestValue = d6;
            direction = direction2;
        }
        if (direction != null) {
            if (direction == BlockFace.WEST || direction == BlockFace.EAST) {
                player.uncertaintyHandler.xPositiveUncertainty += 0.15;
                player.uncertaintyHandler.xNegativeUncertainty -= 0.15;
                player.pointThreeEstimator.setPushing(true);
            } else {
                player.uncertaintyHandler.zPositiveUncertainty += 0.15;
                player.uncertaintyHandler.zNegativeUncertainty -= 0.15;
                player.pointThreeEstimator.setPushing(true);
            }
        }
    }

    public static void updateInWaterStateAndDoWaterCurrentPushing(GrimPlayer player) {
        PacketEntity riding = player.compensatedEntities.self.getRiding();
        player.wasWasTouchingWater = player.wasTouchingWater;
        boolean bl = player.wasTouchingWater = PlayerBaseTick.updateFluidHeightAndDoFluidPushing(player, FluidTag.WATER, 0.014) && (riding == null || !riding.isBoat);
        if (player.wasTouchingWater) {
            player.fallDistance = 0.0;
        }
    }

    private static boolean updateFluidHeightAndDoFluidPushing(GrimPlayer player, FluidTag tag, double multiplier) {
        if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13)) {
            return PlayerBaseTick.updateFluidHeightAndDoFluidPushingModern(player, tag, multiplier);
        }
        return PlayerBaseTick.updateFluidHeightAndDoFluidPushingLegacy(player, tag, multiplier);
    }

    private static boolean updateFluidHeightAndDoFluidPushingLegacy(GrimPlayer player, FluidTag tag, double multiplier) {
        int ceilZ;
        SimpleCollisionBox aABB = player.boundingBox.copy().expand(0.0, -0.4, 0.0).expand(-0.001);
        int floorX = GrimMath.floor(aABB.minX);
        int ceilX = GrimMath.ceil(aABB.maxX);
        int floorY = GrimMath.floor(aABB.minY);
        int ceilY = GrimMath.ceil(aABB.maxY);
        int floorZ = GrimMath.floor(aABB.minZ);
        if (CheckIfChunksLoaded.areChunksUnloadedAt(player, floorX, floorY, floorZ, ceilX, ceilY, ceilZ = GrimMath.ceil(aABB.maxZ))) {
            return false;
        }
        boolean hasPushed = false;
        Vector3dm vec3 = new Vector3dm();
        for (int x = floorX; x < ceilX; ++x) {
            for (int y = floorY; y < ceilY; ++y) {
                for (int z = floorZ; z < ceilZ; ++z) {
                    double fluidHeight = tag == FluidTag.WATER ? player.compensatedWorld.getWaterFluidLevelAt(x, y, z) : player.compensatedWorld.getLavaFluidLevelAt(x, y, z);
                    if (fluidHeight == 0.0) continue;
                    double d0 = (double)(y + 1) - fluidHeight;
                    if (player.isFlying || !((double)ceilY >= d0)) continue;
                    hasPushed = true;
                    vec3.add(FluidTypeFlowing.getFlow(player, x, y, z));
                }
            }
        }
        if (tag == FluidTag.WATER && vec3.lengthSquared() > 0.0) {
            vec3.normalize();
            vec3.multiply(multiplier);
            player.baseTickAddWaterPushing(vec3);
            player.baseTickAddVector(vec3);
        }
        return hasPushed;
    }

    private static boolean updateFluidHeightAndDoFluidPushingModern(GrimPlayer player, FluidTag tag, double multiplier) {
        int ceilZ;
        SimpleCollisionBox aABB = player.boundingBox.copy().expand(-0.001);
        int floorX = GrimMath.floor(aABB.minX);
        int ceilX = GrimMath.ceil(aABB.maxX);
        int floorY = GrimMath.floor(aABB.minY);
        int ceilY = GrimMath.ceil(aABB.maxY);
        int floorZ = GrimMath.floor(aABB.minZ);
        if (CheckIfChunksLoaded.areChunksUnloadedAt(player, floorX, floorY, floorZ, ceilX, ceilY, ceilZ = GrimMath.ceil(aABB.maxZ))) {
            return false;
        }
        double d2 = 0.0;
        boolean hasTouched = false;
        Vector3dm vec3 = new Vector3dm();
        int n7 = 0;
        for (int x = floorX; x < ceilX; ++x) {
            for (int y = floorY; y < ceilY; ++y) {
                for (int z = floorZ; z < ceilZ; ++z) {
                    double d;
                    double fluidHeight = tag == FluidTag.WATER ? player.compensatedWorld.getWaterFluidLevelAt(x, y, z) : player.compensatedWorld.getLavaFluidLevelAt(x, y, z);
                    if (player.getClientVersion().isOlderThan(ClientVersion.V_1_14)) {
                        fluidHeight = Math.min(fluidHeight, 0.8888888888888888);
                    }
                    if (fluidHeight == 0.0) continue;
                    double fluidHeightToWorld = (double)y + fluidHeight;
                    if (d < aABB.minY) continue;
                    hasTouched = true;
                    d2 = Math.max(fluidHeightToWorld - aABB.minY, d2);
                    if (player.isFlying || player.getVehicle() instanceof PacketEntityNautilus) continue;
                    Vector3dm vec32 = FluidTypeFlowing.getFlow(player, x, y, z);
                    if (d2 < 0.4) {
                        vec32 = vec32.multiply(d2);
                    }
                    vec3 = vec3.add(vec32);
                    ++n7;
                }
            }
        }
        if (vec3.lengthSquared() > 0.0) {
            if (n7 > 0) {
                vec3 = vec3.multiply(1.0 / (double)n7);
            }
            if (player.inVehicle()) {
                vec3 = vec3.normalize();
            }
            if (tag != FluidTag.LAVA || player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_16)) {
                vec3 = vec3.multiply(multiplier);
                player.baseTickAddWaterPushing(vec3);
                if (Math.abs(player.clientVelocity.getX()) < 0.003 && Math.abs(player.clientVelocity.getZ()) < 0.003 && vec3.length() < 0.0045000000000000005) {
                    vec3 = vec3.normalize().multiply(0.0045000000000000005);
                }
                player.baseTickAddVector(vec3);
            }
        }
        if (tag == FluidTag.LAVA) {
            boolean bl = player.slightlyTouchingLava = hasTouched && d2 <= 0.4;
        }
        if (tag == FluidTag.WATER) {
            player.slightlyTouchingWater = hasTouched && d2 <= 0.4;
        }
        player.fluidHeight.put(tag, d2);
        return hasTouched;
    }

    private static boolean suffocatesAt(GrimPlayer player, int x, int z) {
        SimpleCollisionBox axisAlignedBB = new SimpleCollisionBox(x, player.boundingBox.minY, z, (double)x + 1.0, player.boundingBox.maxY, (double)z + 1.0, false).expand(-1.0E-7);
        return Collisions.suffocatesAt(player, axisAlignedBB);
    }

    private static boolean clearAbove(GrimPlayer player, int x, int y, int z) {
        return !Collisions.doesBlockSuffocate(player, x, y, z) && !Collisions.doesBlockSuffocate(player, x, y + 1, z);
    }

    @Generated
    private PlayerBaseTick() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

