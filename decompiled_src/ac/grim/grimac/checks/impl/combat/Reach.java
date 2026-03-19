/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.combat;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.impl.combat.Hitboxes;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemAttackRange;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.InteractionHand;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import ac.grim.grimac.shaded.fastutil.ints.Int2ObjectMap;
import ac.grim.grimac.shaded.fastutil.ints.Int2ObjectOpenHashMap;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import ac.grim.grimac.utils.data.packetentity.PacketEntitySizeable;
import ac.grim.grimac.utils.data.packetentity.dragon.PacketEntityEnderDragonPart;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.nmsutil.ReachUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@CheckData(name="Reach", setback=10.0)
public class Reach
extends Check
implements PacketCheck {
    private static final List<EntityType> blacklisted = Arrays.asList(EntityTypes.BOAT, EntityTypes.CHEST_BOAT, EntityTypes.SHULKER);
    private static final CheckResult NONE = new CheckResult(ResultType.NONE, "");
    private final Int2ObjectMap<InteractionData> playerAttackQueue = new Int2ObjectOpenHashMap<InteractionData>();
    private boolean cancelImpossibleHits;
    private double threshold;
    private double cancelBuffer;
    private static final boolean ATTACK_RANGE_COMPONENT_EXISTS = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21_11);

    public Reach(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (!this.player.disableGrim && event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            boolean tooManyAttacks;
            ItemAttackRange startRange;
            WrapperPlayClientInteractEntity action = new WrapperPlayClientInteractEntity(event);
            if (this.player.getSetbackTeleportUtil().shouldBlockMovement()) {
                event.setCancelled(true);
                this.player.onPacketCancel();
                return;
            }
            PacketEntity entity = this.player.compensatedEntities.entityMap.get(action.getEntityId());
            if (entity == null || entity instanceof PacketEntityEnderDragonPart) {
                if (this.shouldModifyPackets() && this.player.compensatedEntities.serverPositionsMap.containsKey(action.getEntityId())) {
                    event.setCancelled(true);
                    this.player.onPacketCancel();
                }
                return;
            }
            if (entity.isDead) {
                return;
            }
            if (entity.type == EntityTypes.ARMOR_STAND && this.player.getClientVersion().isOlderThan(ClientVersion.V_1_8)) {
                return;
            }
            if (entity.type == EntityTypes.HAPPY_GHAST && this.player.getClientVersion().isOlderThan(ClientVersion.V_1_21_6)) {
                return;
            }
            if (this.player.gamemode == GameMode.CREATIVE || this.player.gamemode == GameMode.SPECTATOR) {
                return;
            }
            if (this.player.inVehicle()) {
                return;
            }
            if (entity.riding != null) {
                return;
            }
            InteractionHand hand = action.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK ? InteractionHand.MAIN_HAND : action.getHand();
            ItemStack currentStack = this.player.inventory.getItemInHand(hand);
            ItemStack startStack = this.player.inventory.getStartOfTickStack();
            boolean hasRange = false;
            float maxReach = 0.0f;
            float hitboxMargin = 0.0f;
            if (ATTACK_RANGE_COMPONENT_EXISTS && (startRange = (ItemAttackRange)startStack.getComponentOr(ComponentTypes.ATTACK_RANGE, null)) != null) {
                ItemAttackRange currentRange = currentStack.getComponentOr(ComponentTypes.ATTACK_RANGE, null);
                if (currentRange == null) {
                    hasRange = true;
                    maxReach = startRange.getMaxRange();
                    hitboxMargin = startRange.getHitboxMargin();
                } else {
                    hasRange = true;
                    maxReach = Math.min(startRange.getMaxRange(), currentRange.getMaxRange());
                    hitboxMargin = Math.min(startRange.getHitboxMargin(), currentRange.getHitboxMargin());
                }
            }
            boolean bl = tooManyAttacks = this.playerAttackQueue.size() > 10;
            if (!tooManyAttacks) {
                this.playerAttackQueue.put(action.getEntityId(), new InteractionData(this.player.x, this.player.y, this.player.z, hasRange, maxReach, hitboxMargin));
            }
            boolean knownInvalid = this.isKnownInvalid(entity, hasRange, maxReach, hitboxMargin);
            if (this.shouldModifyPackets() && this.cancelImpossibleHits && knownInvalid || tooManyAttacks) {
                event.setCancelled(true);
                this.player.onPacketCancel();
            }
        }
        if (this.isUpdate(event.getPacketType())) {
            this.tickBetterReachCheckWithAngle();
        }
    }

    private boolean isKnownInvalid(PacketEntity reachEntity, boolean hasAttackRange, float itemMaxReach, float itemHitboxMargin) {
        if ((blacklisted.contains(reachEntity.type) || !reachEntity.isLivingEntity) && reachEntity.type != EntityTypes.END_CRYSTAL) {
            return false;
        }
        if (this.player.gamemode == GameMode.CREATIVE || this.player.gamemode == GameMode.SPECTATOR) {
            return false;
        }
        if (this.player.inVehicle()) {
            return false;
        }
        if (this.cancelBuffer != 0.0) {
            CheckResult result = this.checkReach(reachEntity, this.player.x, this.player.y, this.player.z, hasAttackRange, itemMaxReach, itemHitboxMargin, true);
            return result.isFlag();
        }
        SimpleCollisionBox targetBox = this.getTargetBox(reachEntity);
        double maxReach = this.applyReachModifiers(targetBox, hasAttackRange, itemMaxReach, itemHitboxMargin, !this.player.packetStateData.didLastMovementIncludePosition);
        return ReachUtils.getMinReachToBox(this.player, targetBox) > maxReach;
    }

    private void tickBetterReachCheckWithAngle() {
        for (Int2ObjectMap.Entry entry : this.playerAttackQueue.int2ObjectEntrySet()) {
            PacketEntity reachEntity = this.player.compensatedEntities.entityMap.get(entry.getIntKey());
            if (reachEntity == null) continue;
            InteractionData interactionData = (InteractionData)entry.getValue();
            CheckResult result = this.checkReach(reachEntity, interactionData.x, interactionData.y, interactionData.z, interactionData.hasAttackRange, interactionData.maxReach, interactionData.hitboxMargin, false);
            switch (result.type().ordinal()) {
                case 0: {
                    PacketEntitySizeable sizeable;
                    String added = ", type=" + reachEntity.type.getName().getKey();
                    if (reachEntity instanceof PacketEntitySizeable) {
                        sizeable = (PacketEntitySizeable)reachEntity;
                        added = added + ", size=" + sizeable.size;
                    }
                    this.flagAndAlert(result.verbose() + added);
                    break;
                }
                case 1: {
                    PacketEntitySizeable sizeable;
                    String added = "type=" + reachEntity.type.getName().getKey();
                    if (reachEntity instanceof PacketEntitySizeable) {
                        sizeable = (PacketEntitySizeable)reachEntity;
                        added = added + ", size=" + sizeable.size;
                    }
                    this.player.checkManager.getCheck(Hitboxes.class).flagAndAlert(result.verbose() + added);
                }
            }
        }
        this.playerAttackQueue.clear();
    }

    @NotNull
    private CheckResult checkReach(PacketEntity reachEntity, double x, double y, double z, boolean hasAttackRange, float itemMaxReach, float itemHitboxMargin, boolean isPrediction) {
        SimpleCollisionBox targetBox = this.getTargetBox(reachEntity);
        double maxReach = this.applyReachModifiers(targetBox, hasAttackRange, itemMaxReach, itemHitboxMargin, !this.player.packetStateData.didLastLastMovementIncludePosition);
        double minDistance = Double.MAX_VALUE;
        List<Vector3dm> possibleLookDirs = new ArrayList<Vector3dm>(Collections.singletonList(ReachUtils.getLook(this.player, this.player.yaw, this.player.pitch)));
        if (!isPrediction) {
            possibleLookDirs.add(ReachUtils.getLook(this.player, this.player.lastYaw, this.player.pitch));
            if (this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9)) {
                possibleLookDirs.add(ReachUtils.getLook(this.player, this.player.lastYaw, this.player.lastPitch));
            }
            if (this.player.getClientVersion().isOlderThan(ClientVersion.V_1_8)) {
                possibleLookDirs = Collections.singletonList(ReachUtils.getLook(this.player, this.player.yaw, this.player.pitch));
            }
        }
        double distance = maxReach + 3.0;
        double[] possibleEyeHeights = this.player.getPossibleEyeHeights();
        Vector3dm eyePos = new Vector3dm(x, 0.0, z);
        block0: for (Vector3dm lookVec : possibleLookDirs) {
            for (double eye : possibleEyeHeights) {
                eyePos.setY(y + eye);
                Vector3dm endReachPos = eyePos.clone().add(lookVec.getX() * distance, lookVec.getY() * distance, lookVec.getZ() * distance);
                Vector3dm intercept = ReachUtils.calculateIntercept(targetBox, eyePos, endReachPos).first();
                if (ReachUtils.isVecInside(targetBox, eyePos)) {
                    minDistance = 0.0;
                    continue block0;
                }
                if (intercept == null) continue;
                minDistance = Math.min(eyePos.distance(intercept), minDistance);
            }
        }
        if (!blacklisted.contains(reachEntity.type) && reachEntity.isLivingEntity || reachEntity.type == EntityTypes.END_CRYSTAL) {
            if (minDistance == Double.MAX_VALUE) {
                this.cancelBuffer = 1.0;
                return new CheckResult(ResultType.HITBOX, "");
            }
            if (minDistance > maxReach) {
                this.cancelBuffer = 1.0;
                return new CheckResult(ResultType.REACH, String.format("%.5f", minDistance) + " blocks");
            }
            this.cancelBuffer = Math.max(0.0, this.cancelBuffer - 0.25);
        }
        return NONE;
    }

    private SimpleCollisionBox getTargetBox(PacketEntity reachEntity) {
        if (reachEntity.type == EntityTypes.END_CRYSTAL) {
            return new SimpleCollisionBox(reachEntity.trackedServerPosition.getPos().subtract(1.0, 0.0, 1.0), reachEntity.trackedServerPosition.getPos().add(1.0, 2.0, 1.0));
        }
        return reachEntity.getPossibleCollisionBoxes();
    }

    private double applyReachModifiers(SimpleCollisionBox targetBox, boolean hasAttackRange, float itemMaxReach, float itemHitboxMargin, boolean giveMovementThreshold) {
        double maxReach;
        double hitboxMargin = this.threshold;
        if (hasAttackRange) {
            maxReach = itemMaxReach;
            hitboxMargin += (double)itemHitboxMargin;
        } else {
            maxReach = this.player.compensatedEntities.self.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);
            if (this.player.getClientVersion().isOlderThan(ClientVersion.V_1_9)) {
                hitboxMargin += (double)0.1f;
            }
        }
        if (giveMovementThreshold || this.player.canSkipTicks()) {
            hitboxMargin += this.player.getMovementThreshold();
        }
        targetBox.expand(hitboxMargin);
        return maxReach;
    }

    @Override
    public void onReload(ConfigManager config) {
        this.cancelImpossibleHits = config.getBooleanElse("Reach.block-impossible-hits", true);
        this.threshold = config.getDoubleElse("Reach.threshold", 5.0E-4);
    }

    private record InteractionData(double x, double y, double z, boolean hasAttackRange, float maxReach, float hitboxMargin) {
    }

    private record CheckResult(ResultType type, String verbose) {
        public boolean isFlag() {
            return this.type != ResultType.NONE;
        }
    }

    private static enum ResultType {
        REACH,
        HITBOX,
        NONE;

    }
}

