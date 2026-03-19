/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.data.packetentity;

import ac.grim.grimac.checks.impl.sprint.SprintD;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.attribute.ValuedAttribute;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import ac.grim.grimac.utils.inventory.EnchantmentHelper;
import ac.grim.grimac.utils.math.GrimMath;
import java.util.ArrayList;

public class PacketEntitySelf
extends PacketEntity {
    private final GrimPlayer player;
    public int opLevel;

    public PacketEntitySelf(GrimPlayer player) {
        super(player, EntityTypes.PLAYER);
        this.player = player;
    }

    public PacketEntitySelf(GrimPlayer player, PacketEntitySelf old) {
        super(player, EntityTypes.PLAYER);
        this.player = player;
        this.opLevel = old.opLevel;
        this.attributeMap.putAll(old.attributeMap);
    }

    @Override
    protected void initAttributes(GrimPlayer player) {
        super.initAttributes(player);
        if (player.getClientVersion().isOlderThan(ClientVersion.V_1_8)) {
            this.setAttribute(Attributes.STEP_HEIGHT, 0.5);
        }
        this.getAttribute(Attributes.SCALE).orElseThrow().withSetRewriter((oldValue, newValue) -> {
            if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_20_5) || newValue.equals(oldValue)) {
                return oldValue;
            }
            player.possibleEyeHeights[2][0] = 0.4 * newValue;
            player.possibleEyeHeights[2][1] = 1.62 * newValue;
            player.possibleEyeHeights[2][2] = 1.27 * newValue;
            player.possibleEyeHeights[1][0] = 1.27 * newValue;
            player.possibleEyeHeights[1][1] = 1.62 * newValue;
            player.possibleEyeHeights[1][2] = 0.4 * newValue;
            player.possibleEyeHeights[0][0] = 1.62 * newValue;
            player.possibleEyeHeights[0][1] = 1.27 * newValue;
            player.possibleEyeHeights[0][2] = 0.4 * newValue;
            return newValue;
        });
        ValuedAttribute movementSpeed = ValuedAttribute.ranged(Attributes.MOVEMENT_SPEED, 0.1f, 0.0, 1024.0);
        movementSpeed.with(new WrapperPlayServerUpdateAttributes.Property(Attributes.MOVEMENT_SPEED, (double)0.1f, new ArrayList<WrapperPlayServerUpdateAttributes.PropertyModifier>()));
        this.trackAttribute(movementSpeed);
        this.trackAttribute(ValuedAttribute.ranged(Attributes.ATTACK_DAMAGE, 2.0, 0.0, 2048.0));
        this.trackAttribute(ValuedAttribute.ranged(Attributes.ATTACK_SPEED, 4.0, 0.0, 1024.0).requiredVersion(player, ClientVersion.V_1_9));
        this.trackAttribute(ValuedAttribute.ranged(Attributes.JUMP_STRENGTH, 0.42f, 0.0, 32.0).requiredVersion(player, ClientVersion.V_1_20_5));
        this.trackAttribute(ValuedAttribute.ranged(Attributes.BLOCK_BREAK_SPEED, 1.0, 0.0, 1024.0).requiredVersion(player, ClientVersion.V_1_20_5));
        this.trackAttribute(ValuedAttribute.ranged(Attributes.MINING_EFFICIENCY, 0.0, 0.0, 1024.0).requiredVersion(player, ClientVersion.V_1_21));
        this.trackAttribute(ValuedAttribute.ranged(Attributes.SUBMERGED_MINING_SPEED, 0.2, 0.0, 20.0).requiredVersion(player, ClientVersion.V_1_21));
        this.trackAttribute(ValuedAttribute.ranged(Attributes.ENTITY_INTERACTION_RANGE, 3.0, 0.0, 64.0).requiredVersion(player, ClientVersion.V_1_20_5));
        this.trackAttribute(ValuedAttribute.ranged(Attributes.BLOCK_INTERACTION_RANGE, 4.5, 0.0, 64.0).withGetRewriter(value -> {
            if (player.gamemode == GameMode.CREATIVE && (PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_20_5) || player.getClientVersion().isOlderThan(ClientVersion.V_1_20_5))) {
                return 5.0;
            }
            return value;
        }).requiredVersion(player, ClientVersion.V_1_20_5));
        this.trackAttribute(ValuedAttribute.ranged(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.0, 0.0, 1.0).withGetRewriter(value -> {
            if (player.getClientVersion().isOlderThan(ClientVersion.V_1_8)) {
                return 0.0;
            }
            double depthStrider = EnchantmentHelper.getMaximumEnchantLevel(player.inventory, EnchantmentTypes.DEPTH_STRIDER);
            if (player.getClientVersion().isOlderThan(ClientVersion.V_1_21)) {
                return depthStrider;
            }
            if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_21)) {
                return depthStrider / 3.0;
            }
            return value;
        }).requiredVersion(player, ClientVersion.V_1_21));
        this.trackAttribute(ValuedAttribute.ranged(Attributes.MOVEMENT_EFFICIENCY, 0.0, 0.0, 1.0).requiredVersion(player, ClientVersion.V_1_21));
        this.trackAttribute(ValuedAttribute.ranged(Attributes.SNEAKING_SPEED, 0.3, 0.0, 1.0).withGetRewriter(value -> {
            if (player.getClientVersion().isOlderThan(ClientVersion.V_1_19)) {
                return 0.3f;
            }
            int swiftSneak = player.inventory.getLeggings().getEnchantmentLevel(EnchantmentTypes.SWIFT_SNEAK);
            double clamped = GrimMath.clamp(0.3f + (float)swiftSneak * 0.15f, 0.0f, 1.0f);
            if (player.getClientVersion().isOlderThan(ClientVersion.V_1_21)) {
                return clamped;
            }
            if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_21)) {
                return clamped;
            }
            return value;
        }).requiredVersion(player, ClientVersion.V_1_21));
    }

    public boolean inVehicle() {
        return this.getRiding() != null;
    }

    @Override
    public void addPotionEffect(PotionType effect, int amplifier) {
        if (effect == PotionTypes.BLINDNESS && !this.hasPotionEffect(PotionTypes.BLINDNESS)) {
            this.player.checkManager.getPostPredictionCheck(SprintD.class).startedSprintingBeforeBlind = this.player.isSprinting;
        }
        this.player.pointThreeEstimator.updatePlayerPotions(effect, amplifier);
        super.addPotionEffect(effect, amplifier);
    }

    @Override
    public void removePotionEffect(PotionType effect) {
        this.player.pointThreeEstimator.updatePlayerPotions(effect, null);
        super.removePotionEffect(effect);
    }

    @Override
    public void onFirstTransaction(boolean relative, boolean hasPos, double relX, double relY, double relZ, GrimPlayer player) {
    }

    @Override
    public void onSecondTransaction() {
    }

    @Override
    public SimpleCollisionBox getPossibleCollisionBoxes() {
        return this.player.boundingBox.copy();
    }
}

