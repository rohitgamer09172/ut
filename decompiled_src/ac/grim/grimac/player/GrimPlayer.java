/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.viaversion.viaversion.api.Via
 *  com.viaversion.viaversion.api.connection.UserConnection
 *  com.viaversion.viaversion.api.protocol.Protocol
 *  com.viaversion.viaversion.api.protocol.ProtocolPathEntry
 *  com.viaversion.viaversion.api.protocol.packet.PacketTracker
 *  com.viaversion.viaversion.api.protocol.version.ProtocolVersion
 *  io.netty.channel.Channel
 *  lombok.Generated
 */
package ac.grim.grimac.player;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.AbstractCheck;
import ac.grim.grimac.api.GrimUser;
import ac.grim.grimac.api.PacketWorld;
import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.api.handler.ResyncHandler;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.impl.aim.processor.AimProcessor;
import ac.grim.grimac.checks.impl.misc.ClientBrand;
import ac.grim.grimac.checks.impl.misc.TransactionOrder;
import ac.grim.grimac.checks.impl.packetorder.PacketOrderProcessor;
import ac.grim.grimac.events.packets.CheckManagerListener;
import ac.grim.grimac.manager.ActionManager;
import ac.grim.grimac.manager.CheckManager;
import ac.grim.grimac.manager.LastInstanceManager;
import ac.grim.grimac.manager.PunishmentManager;
import ac.grim.grimac.manager.SetbackTeleportUtil;
import ac.grim.grimac.manager.player.features.FeatureManagerImpl;
import ac.grim.grimac.manager.player.handlers.DefaultResyncHandler;
import ac.grim.grimac.manager.player.handlers.NoOpResyncHandler;
import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.predictionengine.MovementCheckRunner;
import ac.grim.grimac.predictionengine.PointThreeEstimator;
import ac.grim.grimac.predictionengine.UncertaintyHandler;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.ConnectionState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemEquippable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension.DimensionType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisconnect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityTeleport;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityVelocity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPing;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;
import ac.grim.grimac.shaded.fastutil.longs.LongOpenHashSet;
import ac.grim.grimac.shaded.fastutil.longs.LongSet;
import ac.grim.grimac.shaded.fastutil.objects.Object2DoubleArrayMap;
import ac.grim.grimac.shaded.fastutil.objects.Object2DoubleMap;
import ac.grim.grimac.shaded.fastutil.objects.ObjectArrayList;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.TranslatableComponent;
import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import ac.grim.grimac.utils.anticheat.update.BlockBreak;
import ac.grim.grimac.utils.change.PlayerBlockHistory;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.BlockPlaceSnapshot;
import ac.grim.grimac.utils.data.MainSupportingBlockData;
import ac.grim.grimac.utils.data.PacketStateData;
import ac.grim.grimac.utils.data.Pair;
import ac.grim.grimac.utils.data.RotationData;
import ac.grim.grimac.utils.data.TrackerData;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.data.VehicleData;
import ac.grim.grimac.utils.data.VelocityData;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import ac.grim.grimac.utils.data.packetentity.PacketEntityHappyGhast;
import ac.grim.grimac.utils.data.packetentity.PacketEntitySelf;
import ac.grim.grimac.utils.data.tags.SyncedTags;
import ac.grim.grimac.utils.enums.FluidTag;
import ac.grim.grimac.utils.enums.Pose;
import ac.grim.grimac.utils.latency.CompensatedCameraEntity;
import ac.grim.grimac.utils.latency.CompensatedDashableEntities;
import ac.grim.grimac.utils.latency.CompensatedEntities;
import ac.grim.grimac.utils.latency.CompensatedFireworks;
import ac.grim.grimac.utils.latency.CompensatedInventory;
import ac.grim.grimac.utils.latency.CompensatedWorld;
import ac.grim.grimac.utils.latency.LatencyUtils;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Location;
import ac.grim.grimac.utils.math.TrigHandler;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.nmsutil.BlockProperties;
import ac.grim.grimac.utils.nmsutil.Collisions;
import ac.grim.grimac.utils.nmsutil.GetBoundingBox;
import ac.grim.grimac.utils.nmsutil.Materials;
import ac.grim.grimac.utils.viaversion.ViaVersionUtil;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.ProtocolPathEntry;
import com.viaversion.viaversion.api.protocol.packet.PacketTracker;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import io.netty.channel.Channel;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Generated;

public class GrimPlayer
implements GrimUser {
    public final UUID uuid;
    public final User user;
    public int entityID;
    @Nullable
    public PlatformPlayer platformPlayer;
    public final Queue<Pair<Short, Long>> transactionsSent = new ConcurrentLinkedQueue<Pair<Short, Long>>();
    public final Set<Short> didWeSendThatTrans = ConcurrentHashMap.newKeySet();
    private final AtomicInteger transactionIDCounter = new AtomicInteger(0);
    public final AtomicInteger lastTransactionSent = new AtomicInteger(0);
    public final AtomicInteger lastTransactionReceived = new AtomicInteger(0);
    public final CheckManager checkManager;
    public final ActionManager actionManager;
    public final PunishmentManager punishmentManager;
    public final MovementCheckRunner movementCheckRunner;
    public final SyncedTags tagManager;
    public Vector3dm clientVelocity = new Vector3dm();
    private PacketTracker viaPacketTracker;
    public final PacketOrderProcessor packetOrderProcessor = new PacketOrderProcessor(this);
    private long transactionPing = 0L;
    public long lastTransSent = 0L;
    public long lastTransReceived = 0L;
    private long playerClockAtLeast = System.nanoTime();
    public double lastWasClimbing = 0.0;
    public boolean canSwimHop = false;
    public int riptideSpinAttackTicks = 0;
    public int powderSnowFrozenTicks = 0;
    public boolean hasGravity = true;
    public final long joinTime = System.currentTimeMillis();
    public boolean playerEntityHasGravity = true;
    public VectorData predictedVelocity = new VectorData(new Vector3dm(), VectorData.VectorType.Normal);
    public Vector3dm actualMovement = new Vector3dm();
    public Vector3dm stuckSpeedMultiplier = new Vector3dm(1, 1, 1);
    public final UncertaintyHandler uncertaintyHandler;
    public double gravity;
    public float friction;
    public double speed;
    public Vector3d filterMojangStupidityOnMojangStupidity = new Vector3d();
    public double x;
    public double y;
    public double z;
    public double lastX;
    public double lastY;
    public double lastZ;
    public float yaw;
    public float pitch;
    public float lastYaw;
    public float lastPitch;
    public boolean onGround;
    public boolean lastOnGround;
    public boolean isSneaking;
    public boolean wasSneaking;
    public boolean isSprinting;
    public boolean lastSprinting;
    public boolean lastSprintingForSpeed;
    public boolean isFlying;
    public boolean canFly;
    public boolean wasFlying;
    public boolean isSwimming;
    public boolean wasSwimming;
    public boolean isClimbing;
    public boolean isGliding;
    public boolean wasGliding;
    public boolean isRiptidePose = false;
    public double fallDistance;
    public SimpleCollisionBox boundingBox = GetBoundingBox.getBoundingBoxFromPosAndSizeRaw(this.x, this.y, this.z, 0.6f, 1.8f);
    public Pose pose = Pose.STANDING;
    public Pose lastPose = Pose.STANDING;
    public boolean isSlowMovement = false;
    public boolean isInBed = false;
    public boolean lastInBed = false;
    public int food = 20;
    public float depthStriderLevel;
    public float sneakingSpeedMultiplier = 0.3f;
    public float flySpeed;
    public final VehicleData vehicleData = new VehicleData();
    public boolean clientClaimsLastOnGround;
    public boolean wasTouchingWater = false;
    public boolean wasWasTouchingWater = false;
    public boolean wasTouchingLava = false;
    public boolean slightlyTouchingLava = false;
    public boolean slightlyTouchingWater = false;
    public boolean wasEyeInWater = false;
    public FluidTag fluidOnEyes;
    public boolean softHorizontalCollision;
    public boolean horizontalCollision;
    public boolean verticalCollision;
    public boolean clientControlledVerticalCollision;
    public boolean couldSkipTick = false;
    public boolean skippedTickInActualMovement = false;
    public final LastInstanceManager lastInstanceManager;
    public final CompensatedFireworks fireworks;
    public final CompensatedWorld compensatedWorld;
    public final CompensatedEntities compensatedEntities;
    public final CompensatedDashableEntities dashableEntities;
    public final CompensatedInventory inventory;
    public final LatencyUtils latencyUtils = new LatencyUtils(this);
    public final PointThreeEstimator pointThreeEstimator;
    public final TrigHandler trigHandler = new TrigHandler(this);
    public final PacketStateData packetStateData = new PacketStateData();
    public Vector3dm baseTickAddition = new Vector3dm();
    public Vector3dm baseTickWaterPushing = new Vector3dm();
    public Vector3dm startTickClientVel = new Vector3dm();
    public int movementPackets = 0;
    public VelocityData firstBreadKB = null;
    public VelocityData likelyKB = null;
    public VelocityData firstBreadExplosion = null;
    public VelocityData likelyExplosions = null;
    public int minAttackSlow = 0;
    public int maxAttackSlow = 0;
    public GameMode gamemode;
    public DimensionType dimensionType;
    @Nullable
    public String worldName;
    public Vector3d bedPosition;
    public long lastBlockPlaceUseItem = 0L;
    public long lastBlockBreak = 0L;
    public final AtomicInteger cancelledPackets = new AtomicInteger(0);
    public MainSupportingBlockData mainSupportingBlockData = new MainSupportingBlockData(null, false);
    public final Object2DoubleMap<FluidTag> fluidHeight = new Object2DoubleArrayMap<FluidTag>(2);
    public final double[][] possibleEyeHeights = new double[3][];
    public int totalFlyingPacketsSent;
    public final Queue<BlockPlaceSnapshot> placeUseItemPackets = new LinkedBlockingQueue<BlockPlaceSnapshot>();
    public final Queue<BlockBreak> queuedBreaks = new LinkedBlockingQueue<BlockBreak>();
    public final PlayerBlockHistory blockHistory = new PlayerBlockHistory();
    public final ArrayDeque<RotationData> pendingRotations = new ArrayDeque();
    public final CompensatedCameraEntity cameraEntity;
    private ResyncHandler resyncHandler = GrimAPI.INSTANCE.getConfigManager().getConfig().getBooleanElse("disable-default-resync-handler", false) ? NoOpResyncHandler.INSTANCE : new DefaultResyncHandler(this);
    private final FeatureManagerImpl featureManager = new FeatureManagerImpl(this);
    public boolean serverOpenedInventoryThisTick;
    private boolean debugPacketCancel = false;
    private int spamThreshold = 100;
    private int maxTransactionTime = 60;
    private boolean ignoreDuplicatePacketRotation = false;
    private boolean experimentalChecks = false;
    private boolean cancelDuplicatePacket = true;
    private boolean exemptElytra = false;
    private boolean forceStuckSpeed = true;
    private boolean forceSlowMovement = true;
    private boolean resetItemUsageOnAttack;
    private boolean resetItemUsageOnItemUpdate;
    private boolean resetItemUsageOnSlotChange;
    private boolean resetItemUsageOnItemUse;
    public boolean noModifyPacketPermission = false;
    public boolean noSetbackPermission = false;
    public boolean disableGrim = false;
    public final ArrayDeque<Movement> movementThisTick = new ArrayDeque(8);
    public final List<Movement> finalMovementsThisTick = new ObjectArrayList<Movement>();
    public final LongSet visitedBlocks = new LongOpenHashSet();
    @Nullable
    private UserConnection viaUserConnection;
    public boolean wasLastPredictionCompleteChecked;
    public boolean isJumping;
    public boolean lastJumping;
    private final AtomicBoolean hasDisconnected = new AtomicBoolean(false);

    public GrimPlayer(@NotNull User user) {
        this.user = user;
        this.uuid = user.getUUID();
        this.fireworks = new CompensatedFireworks(this);
        this.inventory = new CompensatedInventory(this);
        this.compensatedWorld = new CompensatedWorld(this);
        this.compensatedEntities = new CompensatedEntities(this);
        this.dashableEntities = new CompensatedDashableEntities();
        this.cameraEntity = new CompensatedCameraEntity(this);
        this.lastInstanceManager = new LastInstanceManager(this);
        this.actionManager = new ActionManager(this);
        this.checkManager = new CheckManager(this);
        this.punishmentManager = new PunishmentManager(this);
        this.tagManager = new SyncedTags(this);
        this.movementCheckRunner = new MovementCheckRunner(this);
        this.uncertaintyHandler = new UncertaintyHandler(this);
        this.pointThreeEstimator = new PointThreeEstimator(this);
        if (this.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14)) {
            float scale = (float)this.compensatedEntities.self.getAttributeValue(Attributes.SCALE);
            this.possibleEyeHeights[2] = new double[]{0.4 * (double)scale, 1.62 * (double)scale, 1.27 * (double)scale};
            this.possibleEyeHeights[1] = new double[]{1.27 * (double)scale, 1.62 * (double)scale, 0.4 * (double)scale};
            this.possibleEyeHeights[0] = new double[]{1.62 * (double)scale, 1.27 * (double)scale, 0.4 * (double)scale};
        } else if (this.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9)) {
            this.possibleEyeHeights[2] = new double[]{0.4, 1.62, 1.54};
            this.possibleEyeHeights[1] = new double[]{1.54, 1.62, 0.4};
            this.possibleEyeHeights[0] = new double[]{1.62, 1.54, 0.4};
        } else {
            this.possibleEyeHeights[1] = new double[]{1.54f, 1.62f};
            this.possibleEyeHeights[0] = new double[]{1.62f, 1.54f};
        }
        this.reload();
    }

    public void onPacketCancel() {
        if (this.spamThreshold != -1 && this.cancelledPackets.incrementAndGet() > this.spamThreshold) {
            LogUtil.info("Disconnecting " + this.getName() + " for spamming invalid packets, packets cancelled within a second " + String.valueOf(this.cancelledPackets));
            this.disconnect(MessageUtil.miniMessage(MessageUtil.replacePlaceholders(this, GrimAPI.INSTANCE.getConfigManager().getDisconnectClosed())));
            this.cancelledPackets.set(0);
            if (this.debugPacketCancel) {
                try {
                    throw new Exception();
                }
                catch (Exception e) {
                    LogUtil.error("Stacktrace for onPacketCancel (debug-packet-cancel=true)", e);
                }
            }
        }
    }

    public Set<VectorData> getPossibleVelocities() {
        HashSet<VectorData> set = new HashSet<VectorData>();
        if (this.firstBreadKB != null) {
            set.add(new VectorData(this.firstBreadKB.vector.clone(), VectorData.VectorType.Knockback).returnNewModified(VectorData.VectorType.FirstBreadKnockback));
        }
        if (this.likelyKB != null) {
            set.add(new VectorData(this.likelyKB.vector.clone(), VectorData.VectorType.Knockback));
        }
        set.addAll(this.getPossibleVelocitiesMinusKnockback());
        return set;
    }

    public Set<VectorData> getPossibleVelocitiesMinusKnockback() {
        HashSet<VectorData> possibleMovements = new HashSet<VectorData>();
        possibleMovements.add(new VectorData(this.clientVelocity, VectorData.VectorType.Normal));
        if (this.canSwimHop && !this.onGround) {
            possibleMovements.add(new VectorData(this.clientVelocity.clone().setY(0.3f), VectorData.VectorType.Swimhop));
        }
        if (this.riptideSpinAttackTicks >= 0 && Collections.max(this.uncertaintyHandler.riptideEntities) > 0) {
            possibleMovements.add(new VectorData(this.clientVelocity.clone().multiply(-0.2), VectorData.VectorType.Trident));
        }
        if (this.lastWasClimbing != 0.0) {
            possibleMovements.add(new VectorData(this.clientVelocity.clone().setY(this.lastWasClimbing + this.baseTickAddition.getY()), VectorData.VectorType.Climbable));
        }
        for (VectorData data : new HashSet(possibleMovements)) {
            for (BlockFace direction : this.uncertaintyHandler.slimePistonBounces) {
                if (direction.getModX() != 0) {
                    possibleMovements.add(data.returnNewModified(data.vector.clone().setX(direction.getModX()), VectorData.VectorType.SlimePistonBounce));
                    continue;
                }
                if (direction.getModY() != 0) {
                    possibleMovements.add(data.returnNewModified(data.vector.clone().setY(direction.getModY()), VectorData.VectorType.SlimePistonBounce));
                    continue;
                }
                if (direction.getModZ() == 0) continue;
                possibleMovements.add(data.returnNewModified(data.vector.clone().setZ(direction.getModZ()), VectorData.VectorType.SlimePistonBounce));
            }
        }
        return possibleMovements;
    }

    public boolean addTransactionResponse(short id) {
        Pair<Short, Long> data = null;
        boolean hasID = false;
        int skipped = 0;
        for (Pair pair : this.transactionsSent) {
            if ((Short)pair.first() == id) {
                hasID = true;
                break;
            }
            ++skipped;
        }
        if (hasID) {
            if (this.viaPacketTracker != null) {
                this.viaPacketTracker.setIntervalPackets(this.viaPacketTracker.getIntervalPackets() - 1L);
            }
            if (skipped > 0 && System.currentTimeMillis() - this.joinTime > 5000L) {
                this.checkManager.getCheck(TransactionOrder.class).flagAndAlert("skipped: " + skipped);
            }
            while ((data = this.transactionsSent.poll()) != null) {
                this.lastTransactionReceived.incrementAndGet();
                this.lastTransReceived = System.currentTimeMillis();
                this.transactionPing = System.nanoTime() - data.second();
                this.playerClockAtLeast = data.second();
                if (data.first() != id) continue;
            }
            CheckManagerListener.handleQueuedPlaces(this, false, 0.0f, 0.0f, System.currentTimeMillis());
            CheckManagerListener.handleQueuedBreaks(this, false, 0.0f, 0.0f, System.currentTimeMillis());
            this.latencyUtils.handleNettySyncTransaction(this.lastTransactionReceived.get());
        }
        return data != null;
    }

    public void baseTickAddWaterPushing(Vector3dm vector) {
        this.baseTickWaterPushing.add(vector);
    }

    public void baseTickAddVector(Vector3dm vector) {
        this.clientVelocity.add(vector);
    }

    public void trackBaseTickAddition(Vector3dm vector) {
        this.baseTickAddition.add(vector);
    }

    public float getMaxUpStep() {
        PacketEntitySelf self = this.compensatedEntities.self;
        PacketEntity riding = self.getRiding();
        if (riding == null) {
            return (float)self.getAttributeValue(Attributes.STEP_HEIGHT);
        }
        if (riding.isBoat) {
            return 0.0f;
        }
        float value = (float)riding.getAttributeValue(Attributes.STEP_HEIGHT);
        if (riding.isHappyGhast) {
            return ((PacketEntityHappyGhast)riding).isControllingPassenger() ? Math.max(value, 1.0f) : value;
        }
        return value;
    }

    public void sendTransaction() {
        this.sendTransaction(false);
    }

    public void sendTransaction(boolean async) {
        if (this.user.getEncoderState() != ConnectionState.PLAY) {
            return;
        }
        if (this.disableGrim && (double)(System.nanoTime() - this.getPlayerClockAtLeast()) > 1.5E10) {
            return;
        }
        this.lastTransSent = System.currentTimeMillis();
        short transactionID = (short)(-1 * (this.transactionIDCounter.getAndIncrement() & Short.MAX_VALUE));
        try {
            PacketWrapper packet = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_17) ? new WrapperPlayServerPing(transactionID) : new WrapperPlayServerWindowConfirmation(0, transactionID, false);
            if (async) {
                this.runSafely(() -> {
                    this.addTransactionSend(transactionID);
                    this.user.writePacket(packet);
                });
            } else {
                this.addTransactionSend(transactionID);
                this.user.writePacket(packet);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void addTransactionSend(short id) {
        this.didWeSendThatTrans.add(id);
    }

    public double getEyeHeight() {
        return this.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) ? (double)this.pose.eyeHeight : (this.isSneaking ? (double)1.54f : (double)1.62f);
    }

    public void timedOut() {
        this.disconnect(MessageUtil.miniMessage(MessageUtil.replacePlaceholders(this, GrimAPI.INSTANCE.getConfigManager().getDisconnectTimeout())));
    }

    public void disconnect(Component reason) {
        String textReason;
        if (!this.hasDisconnected.compareAndSet(false, true)) {
            return;
        }
        if (reason instanceof TranslatableComponent) {
            TranslatableComponent translatableComponent = (TranslatableComponent)reason;
            textReason = translatableComponent.key();
        } else {
            textReason = LegacyComponentSerializer.legacySection().serialize(reason);
        }
        LogUtil.info("Disconnecting " + this.user.getProfile().getName() + " for " + MessageUtil.stripColor(textReason));
        try {
            this.user.sendPacket(new WrapperPlayServerDisconnect(reason));
        }
        catch (Exception ignored) {
            LogUtil.warn("Failed to send disconnect packet to disconnect " + this.user.getProfile().getName() + "! Disconnecting anyways.");
        }
        this.user.closeConnection();
        if (this.platformPlayer != null) {
            GrimAPI.INSTANCE.getScheduler().getEntityScheduler().execute(this.platformPlayer, GrimAPI.INSTANCE.getGrimPlugin(), () -> this.platformPlayer.kickPlayer(textReason), null, 1L);
        }
    }

    public void pollData() {
        if (this.lastTransSent != 0L && this.lastTransSent + 80L < System.currentTimeMillis()) {
            this.sendTransaction(true);
        }
        if ((double)(System.nanoTime() - this.getPlayerClockAtLeast()) > (double)this.maxTransactionTime * 1.0E9) {
            this.timedOut();
        }
        if (!GrimAPI.INSTANCE.getPlayerDataManager().shouldCheck(this.user)) {
            GrimAPI.INSTANCE.getPlayerDataManager().remove(this.user);
        }
        if (this.viaPacketTracker == null && ViaVersionUtil.isAvailable && this.uuid != null) {
            UserConnection connection = Via.getManager().getConnectionManager().getConnectedClient(this.uuid);
            this.viaPacketTracker = connection != null ? connection.getPacketTracker() : null;
            this.viaUserConnection = connection;
        }
        if (this.uuid != null && this.platformPlayer == null) {
            this.platformPlayer = GrimAPI.INSTANCE.getPlatformPlayerFactory().getFromUUID(this.uuid);
            this.updatePermissions();
        }
    }

    public void updateVelocityMovementSkipping() {
        if (!this.couldSkipTick) {
            this.couldSkipTick = this.pointThreeEstimator.determineCanSkipTick(BlockProperties.getFrictionInfluencedSpeed((float)(this.speed * (this.isSprinting ? 1.3 : 1.0)), this), this.getPossibleVelocitiesMinusKnockback());
        }
        HashSet<VectorData> knockback = new HashSet<VectorData>();
        if (this.firstBreadKB != null) {
            knockback.add(new VectorData(this.firstBreadKB.vector, VectorData.VectorType.Knockback));
        }
        if (this.likelyKB != null) {
            knockback.add(new VectorData(this.likelyKB.vector, VectorData.VectorType.Knockback));
        }
        boolean kbPointThree = this.pointThreeEstimator.determineCanSkipTick(BlockProperties.getFrictionInfluencedSpeed((float)(this.speed * (this.isSprinting ? 1.3 : 1.0)), this), knockback);
        this.checkManager.getKnockbackHandler().setPointThree(kbPointThree);
        HashSet<VectorData> explosion = new HashSet<VectorData>();
        if (this.firstBreadExplosion != null) {
            explosion.add(new VectorData(this.firstBreadExplosion.vector, VectorData.VectorType.Explosion));
        }
        if (this.likelyExplosions != null) {
            explosion.add(new VectorData(this.likelyExplosions.vector, VectorData.VectorType.Explosion));
        }
        boolean explosionPointThree = this.pointThreeEstimator.determineCanSkipTick(BlockProperties.getFrictionInfluencedSpeed((float)(this.speed * (this.isSprinting ? 1.3 : 1.0)), this), explosion);
        this.checkManager.getExplosionHandler().setPointThree(explosionPointThree);
        if (kbPointThree || explosionPointThree) {
            this.uncertaintyHandler.lastPointThree.reset();
        }
    }

    @Override
    public void updatePermissions() {
        if (this.platformPlayer == null) {
            return;
        }
        try {
            GrimAPI.INSTANCE.getScheduler().getEntityScheduler().execute(this.platformPlayer, GrimAPI.INSTANCE.getGrimPlugin(), () -> {
                this.noModifyPacketPermission = this.platformPlayer.hasPermission("grim.nomodifypacket");
                this.noSetbackPermission = this.platformPlayer.hasPermission("grim.nosetback");
                for (AbstractCheck check : this.checkManager.allChecks.values()) {
                    if (!(check instanceof Check)) continue;
                    Check c = (Check)check;
                    c.updatePermissions();
                }
            }, null, 0L);
        }
        catch (Exception e) {
            LogUtil.error("Failed to update permissions for " + this.getName() + "!", e);
        }
    }

    public boolean isPointThree() {
        return this.getClientVersion().isOlderThan(ClientVersion.V_1_18_2);
    }

    public double getMovementThreshold() {
        return this.isPointThree() ? 0.03 : 2.0E-4;
    }

    public ClientVersion getClientVersion() {
        return Objects.requireNonNullElseGet(this.user.getClientVersion(), () -> ClientVersion.getById(PacketEvents.getAPI().getServerManager().getVersion().getProtocolVersion()));
    }

    public boolean isTickingReliablyFor(int ticks) {
        return !this.canSkipTicks() || (this.inVehicle() || !this.uncertaintyHandler.lastPointThree.hasOccurredSince(ticks)) && !this.uncertaintyHandler.lastVehicleSwitch.hasOccurredSince(1);
    }

    public boolean inVehicle() {
        return this.compensatedEntities.self.inVehicle();
    }

    public PacketEntity getVehicle() {
        return this.compensatedEntities.self.riding;
    }

    public EntityType getVehicleType() {
        return this.inVehicle() ? this.getVehicle().type : null;
    }

    public double[] getPossibleEyeHeights() {
        if (this.getClientVersion().isOlderThan(ClientVersion.V_1_9)) {
            return this.isSneaking ? this.possibleEyeHeights[1] : this.possibleEyeHeights[0];
        }
        return switch (this.pose) {
            case Pose.FALL_FLYING, Pose.SPIN_ATTACK, Pose.SWIMMING -> this.possibleEyeHeights[2];
            case Pose.NINE_CROUCHING, Pose.CROUCHING -> this.possibleEyeHeights[1];
            default -> this.possibleEyeHeights[0];
        };
    }

    @Override
    public PacketWorld getPacketWorld() {
        return this.compensatedWorld;
    }

    @Override
    public int getTransactionPing() {
        return GrimMath.floor((double)this.transactionPing / 1000000.0);
    }

    @Override
    public int getKeepAlivePing() {
        if (this.platformPlayer == null) {
            return -1;
        }
        return PacketEvents.getAPI().getPlayerManager().getPing(this.platformPlayer.getNative());
    }

    public SetbackTeleportUtil getSetbackTeleportUtil() {
        return this.checkManager.getSetbackUtil();
    }

    public boolean wouldCollisionResultFlagGroundSpoof(double inputY, double collisionY) {
        boolean calculatedOnGround;
        boolean verticalCollision = inputY != collisionY;
        boolean bl = calculatedOnGround = verticalCollision && inputY < 0.0;
        if (this.exemptOnGround()) {
            return false;
        }
        if (inputY == -1.0E-7 && collisionY > -1.0E-7 && collisionY <= 0.0) {
            return false;
        }
        return calculatedOnGround != this.onGround;
    }

    public boolean exemptOnGround() {
        return this.inVehicle() || Collections.max(this.uncertaintyHandler.pistonX) != 0.0 || Collections.max(this.uncertaintyHandler.pistonY) != 0.0 || Collections.max(this.uncertaintyHandler.pistonZ) != 0.0 || this.uncertaintyHandler.isStepMovement || this.isFlying || this.compensatedEntities.self.isDead || this.isInBed || this.lastInBed || this.uncertaintyHandler.lastFlyingStatusChange.hasOccurredSince(30) || this.uncertaintyHandler.lastHardCollidingLerpingEntity.hasOccurredSince(3) || this.uncertaintyHandler.isOrWasNearGlitchyBlock;
    }

    public void handleMountVehicle(int vehicleID) {
        this.compensatedEntities.serverPlayerVehicle = vehicleID;
        TrackerData data = this.compensatedEntities.getTrackedEntity(vehicleID);
        if (data != null && PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9) && this.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) && (EntityTypes.isTypeInstanceOf(data.getEntityType(), EntityTypes.BOAT) || EntityTypes.isTypeInstanceOf(data.getEntityType(), EntityTypes.ABSTRACT_HORSE) || data.getEntityType() == EntityTypes.PIG || data.getEntityType() == EntityTypes.STRIDER || EntityTypes.isTypeInstanceOf(data.getEntityType(), EntityTypes.CAMEL) || data.getEntityType() == EntityTypes.HAPPY_GHAST || EntityTypes.isTypeInstanceOf(data.getEntityType(), EntityTypes.ABSTRACT_NAUTILUS))) {
            this.user.writePacket(new WrapperPlayServerEntityVelocity(vehicleID, new Vector3d()));
        }
        this.sendTransaction();
        this.latencyUtils.addRealTimeTask(this.lastTransactionSent.get(), () -> {
            this.vehicleData.wasVehicleSwitch = true;
        });
    }

    public int getRidingVehicleId() {
        return this.compensatedEntities.getPacketEntityID(this.compensatedEntities.self.getRiding());
    }

    public void handleDismountVehicle(PacketSendEvent event) {
        EntityType entityType = this.getVehicleType();
        this.sendTransaction();
        this.compensatedEntities.serverPlayerVehicle = null;
        event.getTasksAfterSend().add(() -> {
            int ridingId;
            TrackerData data;
            if (this.inVehicle() && (data = this.compensatedEntities.serverPositionsMap.get(ridingId = this.getRidingVehicleId())) != null) {
                this.user.writePacket(new WrapperPlayServerEntityTeleport(ridingId, new Vector3d(data.getX(), data.getY(), data.getZ()), data.getXRot(), data.getYRot(), false));
            }
        });
        this.latencyUtils.addRealTimeTask(this.lastTransactionSent.get(), () -> {
            this.vehicleData.wasVehicleSwitch = true;
            if (this.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_14) || this.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_5) && EntityTypes.MINECART == entityType) {
                this.compensatedEntities.hasSprintingAttributeEnabled = false;
            }
        });
    }

    public boolean canGlide() {
        ItemStack chestPlate = this.inventory.getChestplate();
        if (chestPlate.getType() == ItemTypes.ELYTRA && chestPlate.getDamageValue() < chestPlate.getMaxDamage() - 1) {
            return true;
        }
        if (this.getClientVersion().isOlderThan(ClientVersion.V_1_21_2) || PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_21_2)) {
            return false;
        }
        return GrimPlayer.isGlider(this.inventory.getHelmet(), EquipmentSlot.CHEST_PLATE) || GrimPlayer.isGlider(this.inventory.getChestplate(), EquipmentSlot.LEGGINGS) || GrimPlayer.isGlider(this.inventory.getLeggings(), EquipmentSlot.BOOTS) || GrimPlayer.isGlider(this.inventory.getBoots(), EquipmentSlot.OFF_HAND) || GrimPlayer.isGlider(this.inventory.getOffHand(), EquipmentSlot.HELMET);
    }

    private static boolean isGlider(ItemStack stack, EquipmentSlot slot) {
        if (!stack.hasComponent(ComponentTypes.GLIDER) || stack.canBeDepleted() && stack.getDamageValue() >= stack.getMaxDamage() - 1) {
            return false;
        }
        Optional<ItemEquippable> equippable = stack.getComponent(ComponentTypes.EQUIPPABLE);
        return equippable.isPresent() && equippable.get().getSlot() == slot;
    }

    public void resyncPose() {
        if (this.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_14) && this.platformPlayer != null) {
            this.platformPlayer.setSneaking(!this.platformPlayer.isSneaking());
        }
    }

    public boolean canPlaceGameMasterBlocks() {
        return this.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_10) || this.canUseGameMasterBlocks();
    }

    public boolean canUseGameMasterBlocks() {
        return this.gamemode == GameMode.CREATIVE && this.compensatedEntities.self.opLevel >= 2;
    }

    public boolean isInWaterOrRain() {
        return this.compensatedWorld.isRaining || Collisions.hasMaterial(this, this.boundingBox.copy().expand(0.1f), block -> Materials.isWater(CompensatedWorld.blockVersion, (WrappedBlockState)block.first()));
    }

    @Contract(pure=true)
    public boolean supportsEndTick() {
        return this.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_2) && PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2);
    }

    @Contract(pure=true)
    public boolean canSkipTicks() {
        return this.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) && !this.supportsEndTick();
    }

    @Override
    public void runSafely(Runnable runnable) {
        ChannelHelper.runInEventLoop(this.user.getChannel(), runnable);
    }

    @Override
    public int getLastTransactionReceived() {
        return this.lastTransactionReceived.get();
    }

    @Override
    public int getLastTransactionSent() {
        return this.lastTransactionSent.get();
    }

    @Override
    public void addRealTimeTask(int transaction, Runnable runnable) {
        this.latencyUtils.addRealTimeTask(transaction, runnable);
    }

    @Override
    public String getName() {
        return this.user.getName();
    }

    @Override
    public UUID getUniqueId() {
        return this.user.getProfile().getUUID();
    }

    @Override
    public String getBrand() {
        return this.checkManager.getPacketCheck(ClientBrand.class).getBrand();
    }

    @Override
    @Nullable
    public String getWorldName() {
        return this.platformPlayer != null ? this.platformPlayer.getWorld().getName() : null;
    }

    @Override
    @Nullable
    public UUID getWorldUID() {
        return this.platformPlayer != null ? this.platformPlayer.getWorld().getUID() : null;
    }

    @Override
    public String getVersionName() {
        return this.getClientVersion().getReleaseName();
    }

    @Override
    public double getHorizontalSensitivity() {
        return this.checkManager.getRotationCheck(AimProcessor.class).sensitivityX;
    }

    @Override
    public double getVerticalSensitivity() {
        return this.checkManager.getRotationCheck(AimProcessor.class).sensitivityY;
    }

    @Override
    public boolean isVanillaMath() {
        return this.trigHandler.isVanillaMath();
    }

    @Override
    public Collection<? extends AbstractCheck> getChecks() {
        return this.checkManager.allChecks.values();
    }

    public void runNettyTaskInMs(Runnable runnable, int ms) {
        ((Channel)this.user.getChannel()).eventLoop().schedule(runnable, (long)ms, TimeUnit.MILLISECONDS);
    }

    @Override
    public final void reload(ConfigManager config) {
        boolean isInternalHandler;
        this.updatePermissions();
        this.featureManager.onReload(config);
        this.debugPacketCancel = config.getBooleanElse("debug-packet-cancel", false);
        this.spamThreshold = config.getIntElse("packet-spam-threshold", 100);
        this.maxTransactionTime = GrimMath.clamp(config.getIntElse("max-transaction-time", 60), 1, 180);
        this.ignoreDuplicatePacketRotation = config.getBooleanElse("ignore-duplicate-packet-rotation", false);
        this.cancelDuplicatePacket = config.getBooleanElse("cancel-duplicate-packet", true);
        boolean shouldDisableResync = config.getBooleanElse("disable-default-resync-handler", false);
        Class<?> currentHandlerClass = this.resyncHandler.getClass();
        boolean bl = isInternalHandler = currentHandlerClass == DefaultResyncHandler.class || currentHandlerClass == NoOpResyncHandler.class;
        if (isInternalHandler) {
            if (shouldDisableResync) {
                if (currentHandlerClass != NoOpResyncHandler.class) {
                    this.resyncHandler = NoOpResyncHandler.INSTANCE;
                }
            } else if (currentHandlerClass != DefaultResyncHandler.class) {
                this.resyncHandler = new DefaultResyncHandler(this);
            }
        }
        this.resetItemUsageOnAttack = config.getBooleanElse("reset-item-usage-on-attack", true);
        this.resetItemUsageOnItemUpdate = config.getBooleanElse("reset-item-usage-on-item-update", true);
        this.resetItemUsageOnSlotChange = config.getBooleanElse("reset-item-usage-on-slot-change", true);
        this.resetItemUsageOnItemUse = config.getBooleanElse("reset-item-usage-on-item-use", true);
        for (AbstractCheck value : this.checkManager.allChecks.values()) {
            value.reload();
        }
        this.punishmentManager.reload(config);
    }

    @Override
    public void reload() {
        this.reload(GrimAPI.INSTANCE.getConfigManager().getConfig());
    }

    @Override
    public void sendMessage(String message) {
        if (this.platformPlayer != null) {
            this.platformPlayer.sendMessage(message);
        }
    }

    @Override
    public boolean hasPermission(String s) {
        return this.platformPlayer != null && this.platformPlayer.hasPermission(s);
    }

    public boolean hasPermission(String s, boolean defaultIfUnset) {
        return this.platformPlayer != null && this.platformPlayer.hasPermission(s, defaultIfUnset);
    }

    public void sendMessage(Component message) {
        if (this.platformPlayer != null) {
            this.platformPlayer.sendMessage(message);
        }
    }

    public void resyncPosition(Vector3i pos) {
        this.resyncHandler.resync(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
    }

    public void resyncPosition(Vector3i pos, int sequenceID) {
        this.resyncHandler.resyncPosition(pos.x, pos.y, pos.z, sequenceID);
    }

    public void resyncPositions(SimpleCollisionBox box) {
        this.resyncHandler.resync(GrimMath.floor(box.minX), GrimMath.floor(box.minY), GrimMath.floor(box.minZ), GrimMath.ceil(box.maxX), GrimMath.ceil(box.maxY), GrimMath.ceil(box.maxZ));
    }

    public void addMovementThisTick(Movement movement) {
        if (this.movementThisTick.size() >= 100) {
            Movement movement1 = this.movementThisTick.removeFirst();
            Movement movement2 = this.movementThisTick.removeFirst();
            Movement movement3 = new Movement(movement1.from(), movement2.to());
            this.movementThisTick.addFirst(movement3);
        }
        this.movementThisTick.add(movement);
    }

    public Location getLocation() {
        return new Location(this.platformPlayer.getWorld(), this.x, this.y, this.z, this.yaw, this.pitch);
    }

    public int getViaTranslatedClientBlockID(int blockStateId) {
        if (this.viaUserConnection == null) {
            return blockStateId;
        }
        ProtocolVersion clientVersion = this.viaUserConnection.getProtocolInfo().protocolVersion();
        ProtocolVersion serverVersion = this.viaUserConnection.getProtocolInfo().serverProtocolVersion();
        List protocolPath = Via.getManager().getProtocolManager().getProtocolPath(clientVersion, serverVersion);
        if (protocolPath == null) {
            return blockStateId;
        }
        for (int i = protocolPath.size() - 1; i >= 0; --i) {
            Protocol protocol = ((ProtocolPathEntry)protocolPath.get(i)).protocol();
            if (protocol.getMappingData() == null || protocol.getMappingData().getBlockStateMappings() == null) continue;
            blockStateId = protocol.getMappingData().getNewBlockStateId(blockStateId);
        }
        return blockStateId;
    }

    @Generated
    public long getPlayerClockAtLeast() {
        return this.playerClockAtLeast;
    }

    @Override
    @Generated
    public ResyncHandler getResyncHandler() {
        return this.resyncHandler;
    }

    @Override
    @Generated
    public void setResyncHandler(ResyncHandler resyncHandler) {
        this.resyncHandler = resyncHandler;
    }

    @Override
    @Generated
    public FeatureManagerImpl getFeatureManager() {
        return this.featureManager;
    }

    @Generated
    public boolean isIgnoreDuplicatePacketRotation() {
        return this.ignoreDuplicatePacketRotation;
    }

    @Generated
    public boolean isExperimentalChecks() {
        return this.experimentalChecks;
    }

    @Generated
    public void setExperimentalChecks(boolean experimentalChecks) {
        this.experimentalChecks = experimentalChecks;
    }

    @Generated
    public boolean isCancelDuplicatePacket() {
        return this.cancelDuplicatePacket;
    }

    @Generated
    public boolean isExemptElytra() {
        return this.exemptElytra;
    }

    @Generated
    public void setExemptElytra(boolean exemptElytra) {
        this.exemptElytra = exemptElytra;
    }

    @Generated
    public boolean isForceStuckSpeed() {
        return this.forceStuckSpeed;
    }

    @Generated
    public void setForceStuckSpeed(boolean forceStuckSpeed) {
        this.forceStuckSpeed = forceStuckSpeed;
    }

    @Generated
    public boolean isForceSlowMovement() {
        return this.forceSlowMovement;
    }

    @Generated
    public void setForceSlowMovement(boolean forceSlowMovement) {
        this.forceSlowMovement = forceSlowMovement;
    }

    @Generated
    public boolean isResetItemUsageOnAttack() {
        return this.resetItemUsageOnAttack;
    }

    @Generated
    public boolean isResetItemUsageOnItemUpdate() {
        return this.resetItemUsageOnItemUpdate;
    }

    @Generated
    public boolean isResetItemUsageOnSlotChange() {
        return this.resetItemUsageOnSlotChange;
    }

    @Generated
    public boolean isResetItemUsageOnItemUse() {
        return this.resetItemUsageOnItemUse;
    }

    public record Movement(Vector3d from, Vector3d to, Vector3d axisDependentOriginalMovement) {
        public Movement(Vector3d from, Vector3d to) {
            this(from, to, null);
        }

        public boolean axisIndependant() {
            return this.axisDependentOriginalMovement != null;
        }
    }
}

