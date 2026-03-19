/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.utils.data.MainSupportingBlockData;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import ac.grim.grimac.utils.data.packetentity.PacketEntityHorse;
import ac.grim.grimac.utils.data.packetentity.PacketEntityNautilus;
import ac.grim.grimac.utils.data.packetentity.PacketEntityStrider;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.nmsutil.Materials;
import lombok.Generated;

public final class BlockProperties {
    public static float getFrictionInfluencedSpeed(float f, GrimPlayer player) {
        if (player.lastOnGround) {
            return (float)(player.speed * (double)(0.21600002f / (f * f * f)));
        }
        if (player.inVehicle()) {
            PacketEntity riding = player.compensatedEntities.self.getRiding();
            if (riding.type == EntityTypes.PIG || riding instanceof PacketEntityNautilus || riding instanceof PacketEntityHorse) {
                return (float)(player.speed * (double)0.1f);
            }
            if (riding instanceof PacketEntityStrider) {
                PacketEntityStrider strider = (PacketEntityStrider)riding;
                if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_20)) {
                    return (float)player.speed * 0.1f;
                }
                return (float)strider.getAttributeValue(Attributes.MOVEMENT_SPEED) * (strider.isShaking ? 0.66f : 1.0f) * 0.1f;
            }
        }
        if (player.isFlying) {
            return player.flySpeed * 20.0f * (player.isSprinting ? 0.1f : 0.05f);
        }
        if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_19_4)) {
            return player.isSprinting ? 0.025999999f : 0.02f;
        }
        return player.lastSprintingForSpeed ? 0.025999999f : 0.02f;
    }

    public static StateType getOnPos(GrimPlayer player, MainSupportingBlockData mainSupportingBlockData, Vector3d playerPos) {
        if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_19_4)) {
            return BlockProperties.getOnBlock(player, playerPos.getX(), playerPos.getY(), playerPos.getZ());
        }
        Vector3i pos = BlockProperties.getOnPos(player, playerPos, mainSupportingBlockData, 0.2f);
        return player.compensatedWorld.getBlockType(pos.x, pos.y, pos.z);
    }

    public static float getFriction(GrimPlayer player, MainSupportingBlockData mainSupportingBlockData, Vector3d playerPos) {
        if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_19_4)) {
            double searchBelowAmount = 0.5000001;
            if (player.getClientVersion().isOlderThan(ClientVersion.V_1_15)) {
                searchBelowAmount = 1.0;
            }
            StateType type = player.compensatedWorld.getBlockType(playerPos.getX(), playerPos.getY() - searchBelowAmount, playerPos.getZ());
            return BlockProperties.getMaterialFriction(player, type);
        }
        StateType underPlayer = BlockProperties.getBlockPosBelowThatAffectsMyMovement(player, mainSupportingBlockData, playerPos);
        return BlockProperties.getMaterialFriction(player, underPlayer);
    }

    public static float getBlockSpeedFactor(GrimPlayer player, MainSupportingBlockData mainSupportingBlockData, Vector3d playerPos) {
        if (player.getClientVersion().isOlderThan(ClientVersion.V_1_15)) {
            return 1.0f;
        }
        if (player.isGliding || player.isFlying) {
            return 1.0f;
        }
        if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_19_4)) {
            return BlockProperties.getBlockSpeedFactorLegacy(player, playerPos);
        }
        WrappedBlockState inBlock = player.compensatedWorld.getBlock(playerPos.getX(), playerPos.getY(), playerPos.getZ());
        float inBlockSpeedFactor = BlockProperties.getBlockSpeedFactor(player, inBlock.getType());
        if (inBlockSpeedFactor != 1.0f || inBlock.getType() == StateTypes.WATER || inBlock.getType() == StateTypes.BUBBLE_COLUMN) {
            return BlockProperties.getModernVelocityMultiplier(player, inBlockSpeedFactor);
        }
        StateType underPlayer = BlockProperties.getBlockPosBelowThatAffectsMyMovement(player, mainSupportingBlockData, playerPos);
        return BlockProperties.getModernVelocityMultiplier(player, BlockProperties.getBlockSpeedFactor(player, underPlayer));
    }

    public static boolean onHoneyBlock(GrimPlayer player, MainSupportingBlockData mainSupportingBlockData, Vector3d playerPos) {
        if (player.getClientVersion().isOlderThan(ClientVersion.V_1_15)) {
            return false;
        }
        StateType inBlock = player.compensatedWorld.getBlockType(playerPos.getX(), playerPos.getY(), playerPos.getZ());
        return inBlock == StateTypes.HONEY_BLOCK || BlockProperties.getBlockPosBelowThatAffectsMyMovement(player, mainSupportingBlockData, playerPos) == StateTypes.HONEY_BLOCK;
    }

    private static StateType getBlockPosBelowThatAffectsMyMovement(GrimPlayer player, MainSupportingBlockData mainSupportingBlockData, Vector3d playerPos) {
        Vector3i pos = player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_19_4) ? new Vector3i(GrimMath.floor(playerPos.getX()), GrimMath.floor(playerPos.getY() - 0.5000001), GrimMath.floor(playerPos.getZ())) : BlockProperties.getOnPos(player, playerPos, mainSupportingBlockData, 0.500001f);
        return player.compensatedWorld.getBlockType(pos.x, pos.y, pos.z);
    }

    private static Vector3i getOnPos(GrimPlayer player, Vector3d playerPos, MainSupportingBlockData mainSupportingBlockData, float searchBelowPlayer) {
        Vector3i mainBlockPos = mainSupportingBlockData.blockPos();
        if (mainBlockPos != null) {
            StateType blockstate = player.compensatedWorld.getBlockType(mainBlockPos.x, mainBlockPos.y, mainBlockPos.z);
            boolean shouldReturn = !((double)searchBelowPlayer <= 0.5 && BlockTags.FENCES.contains(blockstate) || BlockTags.WALLS.contains(blockstate) || BlockTags.FENCE_GATES.contains(blockstate));
            return shouldReturn ? mainBlockPos.withY(GrimMath.floor(playerPos.getY() - (double)searchBelowPlayer)) : mainBlockPos;
        }
        return new Vector3i(GrimMath.floor(playerPos.getX()), GrimMath.floor(playerPos.getY() - (double)searchBelowPlayer), GrimMath.floor(playerPos.getZ()));
    }

    public static float getMaterialFriction(GrimPlayer player, StateType material) {
        float friction = 0.6f;
        if (material == StateTypes.ICE) {
            friction = 0.98f;
        }
        if (material == StateTypes.SLIME_BLOCK && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_8)) {
            friction = 0.8f;
        }
        if (material == StateTypes.HONEY_BLOCK && player.getClientVersion().isOlderThan(ClientVersion.V_1_15)) {
            friction = 0.8f;
        }
        if (material == StateTypes.PACKED_ICE) {
            friction = 0.98f;
        }
        if (material == StateTypes.FROSTED_ICE) {
            friction = 0.98f;
        }
        if (material == StateTypes.BLUE_ICE) {
            friction = 0.98f;
            if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13)) {
                friction = 0.989f;
            }
        }
        return friction;
    }

    private static StateType getOnBlock(GrimPlayer player, double x, double y, double z) {
        StateType block2;
        StateType block1 = player.compensatedWorld.getBlockType(GrimMath.floor(x), GrimMath.floor(y - (double)0.2f), GrimMath.floor(z));
        if (block1.isAir() && (Materials.isFence(block2 = player.compensatedWorld.getBlockType(GrimMath.floor(x), GrimMath.floor(y - (double)1.2f), GrimMath.floor(z))) || Materials.isWall(block2) || Materials.isGate(block2))) {
            return block2;
        }
        return block1;
    }

    private static float getBlockSpeedFactorLegacy(GrimPlayer player, Vector3d pos) {
        StateType onBlock;
        StateType block = player.compensatedWorld.getBlockType(pos.getX(), pos.getY(), pos.getZ());
        if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_16) && player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_16_1) && (onBlock = BlockProperties.getOnBlock(player, pos.getX(), pos.getY(), pos.getZ())) == StateTypes.SOUL_SAND && player.inventory.getBoots().getEnchantmentLevel(EnchantmentTypes.SOUL_SPEED) > 0) {
            return 1.0f;
        }
        float speed = BlockProperties.getBlockSpeedFactor(player, block);
        if (speed != 1.0f || block == StateTypes.SOUL_SAND || block == StateTypes.WATER || block == StateTypes.BUBBLE_COLUMN) {
            return speed;
        }
        StateType block2 = player.compensatedWorld.getBlockType(pos.getX(), pos.getY() - 0.5000001, pos.getZ());
        return BlockProperties.getBlockSpeedFactor(player, block2);
    }

    private static float getBlockSpeedFactor(GrimPlayer player, StateType type) {
        if (type == StateTypes.HONEY_BLOCK) {
            return 0.4f;
        }
        if (type == StateTypes.SOUL_SAND) {
            if (player.getClientVersion().isOlderThan(ClientVersion.V_1_21) && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_16_2) && player.inventory.getBoots().getEnchantmentLevel(EnchantmentTypes.SOUL_SPEED) > 0) {
                return 1.0f;
            }
            return 0.4f;
        }
        return 1.0f;
    }

    private static float getModernVelocityMultiplier(GrimPlayer player, float blockSpeedFactor) {
        if (player.getClientVersion().isOlderThan(ClientVersion.V_1_21)) {
            return blockSpeedFactor;
        }
        return (float)GrimMath.lerp((float)player.compensatedEntities.self.getAttributeValue(Attributes.MOVEMENT_EFFICIENCY), blockSpeedFactor, 1.0);
    }

    @Generated
    private BlockProperties() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

