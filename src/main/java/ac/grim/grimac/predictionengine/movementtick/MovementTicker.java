/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.viaversion.viaversion.api.Via
 *  lombok.Generated
 */
package ac.grim.grimac.predictionengine.movementtick;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.PlayerBaseTick;
import ac.grim.grimac.predictionengine.predictions.PredictionEngine;
import ac.grim.grimac.predictionengine.predictions.PredictionEngineElytra;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import ac.grim.grimac.utils.data.packetentity.PacketEntityStrider;
import ac.grim.grimac.utils.enums.FluidTag;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.nmsutil.BlockProperties;
import ac.grim.grimac.utils.nmsutil.Collisions;
import ac.grim.grimac.utils.nmsutil.EntityTypeTags;
import ac.grim.grimac.utils.nmsutil.FluidFallingAdjustedMovement;
import ac.grim.grimac.utils.nmsutil.GetBoundingBox;
import ac.grim.grimac.utils.nmsutil.MainSupportingBlockPosFinder;
import ac.grim.grimac.utils.team.EntityPredicates;
import ac.grim.grimac.utils.team.EntityTeam;
import ac.grim.grimac.utils.team.TeamHandler;
import ac.grim.grimac.utils.viaversion.ViaVersionUtil;
import com.viaversion.viaversion.api.Via;
import lombok.Generated;

public class MovementTicker {
    public final GrimPlayer player;

    public static void handleEntityCollisions(GrimPlayer player) {
        boolean hasEntityPushing;
        boolean serverSupported = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9);
        boolean bl = hasEntityPushing = !player.getClientVersion().isOlderThan(ClientVersion.V_1_9) && (serverSupported || ViaVersionUtil.isAvailable && !Via.getConfig().isPreventCollision());
        if (!hasEntityPushing) {
            return;
        }
        int possibleCollidingEntities = 0;
        int possibleRiptideEntities = 0;
        if (!player.inVehicle() && player.gamemode != GameMode.SPECTATOR) {
            SimpleCollisionBox playerBox = GetBoundingBox.getBoundingBoxFromPosAndSize(player, player.lastX, player.lastY, player.lastZ, 0.6f, 1.8f);
            playerBox.encompass(GetBoundingBox.getBoundingBoxFromPosAndSize(player, player.x, player.y, player.z, 0.6f, 1.8f).expand(player.getMovementThreshold()));
            playerBox.expand(0.2);
            TeamHandler teamHandler = player.checkManager.getPacketCheck(TeamHandler.class);
            EntityTeam playerTeam = teamHandler != null ? teamHandler.getPlayerTeam() : null;
            for (PacketEntity entity : player.compensatedEntities.entityMap.values()) {
                SimpleCollisionBox entityBox = entity.getPossibleCollisionBoxes();
                if (!playerBox.isCollided(entityBox)) continue;
                ++possibleRiptideEntities;
                if (!entity.isPushable()) continue;
                if (serverSupported) {
                    EntityTeam entityTeam;
                    EntityTeam entityTeam2 = entityTeam = teamHandler != null ? teamHandler.getEntityTeam(entity) : null;
                    if (!EntityPredicates.canBePushedBy(entityTeam, playerTeam)) continue;
                }
                ++possibleCollidingEntities;
            }
        }
        if (player.isGliding && possibleCollidingEntities > 0) {
            player.uncertaintyHandler.yNegativeUncertainty -= 0.05;
            player.uncertaintyHandler.yPositiveUncertainty += 0.05;
        }
        player.uncertaintyHandler.riptideEntities.add(possibleRiptideEntities);
        player.uncertaintyHandler.collidingEntities.add(possibleCollidingEntities);
    }

    private boolean isHorizontalCollisionSoft(Vector3dm collide) {
        double h;
        double horizontalLengthSquared = collide.getX() * collide.getX() + collide.getZ() * collide.getZ();
        if (horizontalLengthSquared < (double)1.0E-5f) {
            return false;
        }
        float xxa = (float)this.player.predictedVelocity.input.getX();
        float zza = (float)this.player.predictedVelocity.input.getZ();
        float yawInRadians = this.player.yaw * ((float)Math.PI / 180);
        double sin = this.player.trigHandler.sin(yawInRadians);
        double cos = this.player.trigHandler.cos(yawInRadians);
        double g = (double)xxa * cos - (double)zza * sin;
        double i = g * g + (h = (double)zza * cos + (double)xxa * sin) * h;
        return i >= (double)1.0E-5f && Math.acos((g * collide.getX() + h * collide.getZ()) / Math.sqrt(i * horizontalLengthSquared)) < 0.13962633907794952;
    }

    public void move(Vector3dm inputVel, Vector3dm collide) {
        boolean calculatedOnGround;
        if (this.player.stuckSpeedMultiplier.getX() < 0.99) {
            this.player.clientVelocity = new Vector3dm();
        }
        if (this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_18_2)) {
            boolean zAxis;
            boolean xAxis = !GrimMath.equal(inputVel.getX(), collide.getX());
            boolean bl = zAxis = !GrimMath.equal(inputVel.getZ(), collide.getZ());
            if (xAxis) {
                this.player.clientVelocity.setX(0);
            }
            if (zAxis) {
                this.player.clientVelocity.setZ(0);
            }
            this.player.horizontalCollision = xAxis || zAxis;
            this.player.softHorizontalCollision = this.player.horizontalCollision && this.isHorizontalCollisionSoft(collide);
        } else {
            if (inputVel.getX() != collide.getX()) {
                this.player.clientVelocity.setX(0);
            }
            if (inputVel.getZ() != collide.getZ()) {
                this.player.clientVelocity.setZ(0);
            }
            this.player.horizontalCollision = inputVel.getX() != collide.getX() || inputVel.getZ() != collide.getZ();
        }
        this.player.verticalCollision = inputVel.getY() != collide.getY();
        boolean bl = calculatedOnGround = this.player.verticalCollision && inputVel.getY() < 0.0;
        if (inputVel.getY() == -1.0E-7 && collide.getY() > -1.0E-7 && collide.getY() <= 0.0 && !this.player.inVehicle()) {
            calculatedOnGround = this.player.onGround;
        }
        this.player.clientClaimsLastOnGround = this.player.onGround;
        if (this.player.inVehicle() && this.player.clientControlledVerticalCollision && this.player.uncertaintyHandler.isStepMovement && (inputVel.getY() <= 0.0 || this.player.predictedVelocity.isSwimHop())) {
            calculatedOnGround = true;
        }
        if (this.player.inVehicle() || !this.player.exemptOnGround()) {
            this.player.onGround = calculatedOnGround;
        }
        this.player.boundingBox = GetBoundingBox.getCollisionBoxForPlayer(this.player, this.player.x, this.player.y, this.player.z);
        PacketEntity riding = this.player.compensatedEntities.self.getRiding();
        if (!(this.player.getClientVersion() == ClientVersion.V_1_21_4 || this.player.wasTouchingWater || riding != null && (riding.isBoat || riding.isHappyGhast))) {
            PlayerBaseTick.updateInWaterStateAndDoWaterCurrentPushing(this.player);
        }
        if (this.player.onGround) {
            this.player.fallDistance = 0.0;
        } else if (collide.getY() < 0.0) {
            this.player.fallDistance -= collide.getY();
            this.player.vehicleData.lastYd = collide.getY();
        }
        if (riding instanceof PacketEntityStrider) {
            Collisions.handleInsideBlocks(this.player);
        }
        this.player.mainSupportingBlockData = MainSupportingBlockPosFinder.findMainSupportingBlockPos(this.player, this.player.mainSupportingBlockData, new Vector3d(collide.getX(), collide.getY(), collide.getZ()), this.player.boundingBox, this.player.onGround);
        StateType onBlock = BlockProperties.getOnPos(this.player, this.player.mainSupportingBlockData, new Vector3d(this.player.x, this.player.y, this.player.z));
        if (inputVel.getY() != collide.getY()) {
            if (this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_8) && (onBlock == StateTypes.SLIME_BLOCK || onBlock == StateTypes.HONEY_BLOCK && this.player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_14_4))) {
                if (this.player.isSneaking) {
                    this.player.clientVelocity.setY(0);
                } else if (this.player.clientVelocity.getY() < 0.0) {
                    this.player.clientVelocity.setY(-this.player.clientVelocity.getY() * (riding != null && !riding.isLivingEntity ? 0.8 : 1.0));
                }
            } else if (BlockTags.BEDS.contains(onBlock) && this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_12)) {
                if (this.player.clientVelocity.getY() < 0.0) {
                    this.player.clientVelocity.setY(-this.player.clientVelocity.getY() * (double)0.66f * (riding != null && !riding.isLivingEntity ? 0.8 : 1.0));
                }
            } else {
                this.player.clientVelocity.setY(0);
            }
        }
        if ((collide = PredictionEngine.clampMovementToHardBorder(this.player, collide)).lengthSquared() <= 1.0E-7 && (this.player.getClientVersion().isOlderThan(ClientVersion.V_1_21_2) || inputVel.lengthSquared() - collide.lengthSquared() >= 1.0E-7)) {
            collide = new Vector3dm();
        } else if (this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_5)) {
            Vector3d from = new Vector3d(this.player.lastX, this.player.lastY, this.player.lastZ);
            Vector3d to = new Vector3d(this.player.x, this.player.y, this.player.z);
            this.player.addMovementThisTick(new GrimPlayer.Movement(from, to, new Vector3d(inputVel.getX(), inputVel.getY(), inputVel.getZ())));
        }
        this.player.predictedVelocity = new VectorData(collide.clone(), this.player.predictedVelocity.lastVector, this.player.predictedVelocity.vectorType);
        float f = BlockProperties.getBlockSpeedFactor(this.player, this.player.mainSupportingBlockData, new Vector3d(this.player.x, this.player.y, this.player.z));
        this.player.clientVelocity.multiply(f, 1.0, f);
        if (this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_2)) {
            return;
        }
        if (this.player.stuckSpeedMultiplier.getX() < 0.99) {
            this.player.uncertaintyHandler.lastStuckSpeedMultiplier.reset();
        }
        this.player.stuckSpeedMultiplier = new Vector3dm(1, 1, 1);
        if (this.player.getClientVersion().isOlderThan(ClientVersion.V_1_16)) {
            this.player.wasTouchingLava = false;
        }
        Collisions.handleInsideBlocks(this.player);
        if (this.player.stuckSpeedMultiplier.getX() < 0.9) {
            this.player.fallDistance = 0.0;
        }
        if (this.player.isFlying) {
            this.player.stuckSpeedMultiplier = new Vector3dm(1, 1, 1);
        }
    }

    public void livingEntityAIStep() {
        MovementTicker.handleEntityCollisions(this.player);
        SimpleCollisionBox oldBB = this.player.boundingBox.copy();
        if (!this.player.inVehicle()) {
            this.playerEntityTravel();
        } else {
            this.livingEntityTravel();
        }
        this.player.uncertaintyHandler.xNegativeUncertainty = 0.0;
        this.player.uncertaintyHandler.xPositiveUncertainty = 0.0;
        this.player.uncertaintyHandler.yNegativeUncertainty = 0.0;
        this.player.uncertaintyHandler.yPositiveUncertainty = 0.0;
        this.player.uncertaintyHandler.zNegativeUncertainty = 0.0;
        this.player.uncertaintyHandler.zPositiveUncertainty = 0.0;
        if (this.player.uncertaintyHandler.lastTeleportTicks.hasOccurredSince(0)) {
            this.player.uncertaintyHandler.yNegativeUncertainty -= 0.02;
        }
        if (this.player.isFlying) {
            SimpleCollisionBox playerBox = GetBoundingBox.getCollisionBoxForPlayer(this.player, this.player.lastX, this.player.lastY, this.player.lastZ);
            if (!Collisions.isEmpty(this.player, playerBox.copy().offset(0.0, 0.1, 0.0))) {
                this.player.uncertaintyHandler.yPositiveUncertainty = this.player.flySpeed * 5.0f;
            }
            if (!Collisions.isEmpty(this.player, playerBox.copy().offset(0.0, -0.1, 0.0))) {
                this.player.uncertaintyHandler.yNegativeUncertainty = this.player.flySpeed * -5.0f;
            }
        }
        if (this.player.getClientVersion().isOlderThan(ClientVersion.V_1_14) || this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_18_2)) {
            return;
        }
        oldBB.expand(-1.0E-7);
        double posX = Math.max(0.0, this.player.predictedVelocity.vector.getX()) + 1.0E-7;
        double negX = Math.min(0.0, this.player.predictedVelocity.vector.getX()) - 1.0E-7;
        double posZ = Math.max(0.0, this.player.predictedVelocity.vector.getZ()) + 1.0E-7;
        double negZ = Math.min(0.0, this.player.predictedVelocity.vector.getZ()) - 1.0E-7;
        boolean xAxisCollision = !Collisions.isEmpty(this.player, oldBB.expandMin(negX, 0.0, 0.0).expandMax(posX, 0.0, 0.0));
        boolean zAxisCollision = !Collisions.isEmpty(this.player, oldBB.expandMin(0.0, 0.0, negZ).expandMax(0.0, 0.0, posZ));
        boolean bl = zAxisCollision = zAxisCollision || this.player.actualMovement.getZ() == 0.0;
        if (zAxisCollision && xAxisCollision) {
            double playerSpeed = this.player.speed;
            if (this.player.wasTouchingWater) {
                float swimSpeed = 0.02f;
                if (this.player.depthStriderLevel > 0.0f) {
                    swimSpeed = (float)((double)swimSpeed + (this.player.speed - (double)swimSpeed) * (double)this.player.depthStriderLevel / 3.0);
                }
                playerSpeed = swimSpeed;
            } else if (this.player.wasTouchingLava) {
                playerSpeed = 0.02f;
            } else if (this.player.isGliding) {
                playerSpeed = 0.4;
                this.player.uncertaintyHandler.yNegativeUncertainty -= 0.05;
                this.player.uncertaintyHandler.yPositiveUncertainty += 0.05;
            }
            this.player.uncertaintyHandler.xNegativeUncertainty -= playerSpeed * 3.0;
            this.player.uncertaintyHandler.xPositiveUncertainty += playerSpeed * 3.0;
        }
    }

    public void playerEntityTravel() {
        if (this.player.isFlying && !this.player.inVehicle()) {
            double oldY = this.player.clientVelocity.getY();
            double oldYJumping = oldY + (double)(this.player.flySpeed * 3.0f);
            this.livingEntityTravel();
            if (this.player.predictedVelocity.isKnockback() || this.player.predictedVelocity.isTrident() || this.player.uncertaintyHandler.yPositiveUncertainty != 0.0 || this.player.uncertaintyHandler.yNegativeUncertainty != 0.0 || this.player.isGliding) {
                this.player.clientVelocity.setY(this.player.actualMovement.getY() * 0.6);
            } else if (Math.abs(oldY - this.player.actualMovement.getY()) < oldYJumping - this.player.actualMovement.getY()) {
                this.player.clientVelocity.setY(oldY * 0.6);
            } else {
                this.player.clientVelocity.setY(oldYJumping * 0.6);
            }
        } else {
            this.livingEntityTravel();
        }
    }

    public void doWaterMove(float swimSpeed, boolean isFalling, float swimFriction) {
    }

    public void doLavaMove() {
    }

    public void doNormalMove(float blockFriction) {
    }

    public void livingEntityTravel() {
        boolean isFalling;
        double playerGravity = !this.player.inVehicle() ? this.player.compensatedEntities.self.getAttributeValue(Attributes.GRAVITY) : this.player.compensatedEntities.self.getRiding().getAttributeValue(Attributes.GRAVITY);
        boolean bl = isFalling = this.player.actualMovement.getY() <= 0.0;
        if (isFalling && this.player.compensatedEntities.getSlowFallingAmplifier().isPresent()) {
            playerGravity = this.player.getClientVersion().isOlderThan(ClientVersion.V_1_20_5) ? 0.01 : Math.min(playerGravity, 0.01);
            this.player.fallDistance = 0.0;
        }
        this.player.gravity = playerGravity;
        double lavaLevel = 0.0;
        if (this.canStandOnLava()) {
            lavaLevel = this.player.compensatedWorld.getLavaFluidLevelAt(GrimMath.floor(this.player.lastX), GrimMath.floor(this.player.lastY), GrimMath.floor(this.player.lastZ));
        }
        if (this.player.wasTouchingWater && !this.player.isFlying) {
            boolean isSkeletonHorse;
            boolean bl2 = isSkeletonHorse = this.player.inVehicle() && this.player.compensatedEntities.self.getRiding().type == EntityTypes.SKELETON_HORSE && this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13);
            float swimFriction = this.player.isSprinting && this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13) ? 0.9f : (isSkeletonHorse ? 0.96f : 0.8f);
            float swimSpeed = 0.02f;
            if (this.player.getClientVersion().isOlderThan(ClientVersion.V_1_21) && this.player.depthStriderLevel > 3.0f) {
                this.player.depthStriderLevel = 3.0f;
            }
            if (!this.player.lastOnGround) {
                this.player.depthStriderLevel *= 0.5f;
            }
            if (this.player.depthStriderLevel > 0.0f) {
                float divisor = this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21) ? 1.0f : 3.0f;
                swimFriction += (0.54600006f - swimFriction) * this.player.depthStriderLevel / divisor;
                swimSpeed = (float)((double)swimSpeed + (this.player.speed - (double)swimSpeed) * (double)this.player.depthStriderLevel / (double)divisor);
            }
            if (this.player.compensatedEntities.getPotionLevelForPlayer(PotionTypes.DOLPHINS_GRACE).isPresent()) {
                swimFriction = 0.96f;
            }
            this.player.friction = swimFriction;
            this.doWaterMove(swimSpeed, isFalling, swimFriction);
            this.player.isClimbing = Collisions.onClimbable(this.player, this.player.x, this.player.y, this.player.z);
            if (this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14) && this.player.isClimbing) {
                this.player.lastWasClimbing = FluidFallingAdjustedMovement.getFluidFallingAdjustedMovement(this.player, playerGravity, isFalling, this.player.clientVelocity.clone().setY(0.1600000023841858)).getY();
            }
            this.floatInWaterWhileRidden();
        } else if (!(!this.player.wasTouchingLava || this.player.isFlying || lavaLevel > 0.0 && this.canStandOnLava())) {
            this.player.friction = 0.5f;
            this.doLavaMove();
            if (this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_16) && this.player.slightlyTouchingLava) {
                this.player.clientVelocity = this.player.clientVelocity.multiply(0.5, 0.8f, 0.5);
                this.player.clientVelocity = FluidFallingAdjustedMovement.getFluidFallingAdjustedMovement(this.player, playerGravity, isFalling, this.player.clientVelocity);
            } else {
                this.player.clientVelocity.multiply(0.5);
            }
            if (this.player.hasGravity) {
                this.player.clientVelocity.add(0.0, -playerGravity / 4.0, 0.0);
            }
        } else if (this.player.isGliding) {
            if (this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_5) && Collisions.onClimbable(this.player, this.player.lastX, this.player.lastY, this.player.lastZ)) {
                float blockFriction = BlockProperties.getFriction(this.player, this.player.mainSupportingBlockData, new Vector3d(this.player.lastX, this.player.lastY, this.player.lastZ));
                this.player.friction = this.player.lastOnGround ? blockFriction * 0.91f : 0.91f;
                this.doNormalMove(blockFriction);
                this.player.isGliding = false;
                this.player.pointThreeEstimator.updatePlayerGliding();
            } else {
                this.player.friction = 0.99f;
                if (this.player.clientVelocity.getY() > -0.5) {
                    this.player.fallDistance = 1.0;
                }
                new PredictionEngineElytra().guessBestMovement(0.0f, this.player);
            }
        } else {
            float blockFriction = BlockProperties.getFriction(this.player, this.player.mainSupportingBlockData, new Vector3d(this.player.lastX, this.player.lastY, this.player.lastZ));
            this.player.friction = this.player.lastOnGround ? blockFriction * 0.91f : 0.91f;
            this.doNormalMove(blockFriction);
        }
        Collisions.applyEffectsFromBlocks(this.player);
    }

    private void floatInWaterWhileRidden() {
        if (this.player.getClientVersion().isOlderThan(ClientVersion.V_1_21_11) || !this.player.inVehicle()) {
            return;
        }
        PacketEntity vehicle = this.player.getVehicle();
        boolean canFloatWhileRidden = EntityTypeTags.CAN_FLOAT_WHILE_RIDDEN.anyOf(vehicle.type);
        double fluidHeight = this.player.fluidHeight.getDouble((Object)FluidTag.WATER);
        if (canFloatWhileRidden && this.player.inVehicle() && fluidHeight > 0.4) {
            this.player.clientVelocity.add(0.0, 0.04f, 0.0);
        }
    }

    public boolean canStandOnLava() {
        return false;
    }

    @Generated
    public MovementTicker(GrimPlayer player) {
        this.player = player;
    }
}

