/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  lombok.Generated
 */
package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemTool;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.utils.data.tags.SyncedTag;
import ac.grim.grimac.utils.data.tags.SyncedTags;
import ac.grim.grimac.utils.enums.FluidTag;
import ac.grim.grimac.utils.inventory.EnchantmentHelper;
import ac.grim.grimac.utils.nmsutil.Materials;
import com.google.common.collect.Sets;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import lombok.Generated;

public final class BlockBreakSpeed {
    private static final Set<StateType> HARVESTABLE_TYPES_1_21_4 = Sets.newHashSet((Object[])new StateType[]{StateTypes.BELL, StateTypes.LANTERN, StateTypes.SOUL_LANTERN, StateTypes.COPPER_DOOR, StateTypes.EXPOSED_COPPER_DOOR, StateTypes.OXIDIZED_COPPER_DOOR, StateTypes.WEATHERED_COPPER_DOOR, StateTypes.WAXED_COPPER_DOOR, StateTypes.WAXED_EXPOSED_COPPER_DOOR, StateTypes.WAXED_OXIDIZED_COPPER_DOOR, StateTypes.WAXED_WEATHERED_COPPER_DOOR, StateTypes.IRON_DOOR, StateTypes.HEAVY_WEIGHTED_PRESSURE_PLATE, StateTypes.LIGHT_WEIGHTED_PRESSURE_PLATE, StateTypes.POLISHED_BLACKSTONE_PRESSURE_PLATE, StateTypes.STONE_PRESSURE_PLATE, StateTypes.BREWING_STAND, StateTypes.ENDER_CHEST});
    private static final boolean SERVER_USES_COMPONENTS_AND_RULES = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5);

    public static double getBlockDamage(GrimPlayer player, WrappedBlockState block) {
        ItemStack tool = player.inventory.getHeldItem();
        return BlockBreakSpeed.getBlockDamage(player, tool, block.getType());
    }

    public static double getBlockDamage(GrimPlayer player, ItemStack tool, StateType block) {
        ItemType toolType = tool.getType();
        if (player.gamemode == GameMode.CREATIVE) {
            if (SERVER_USES_COMPONENTS_AND_RULES && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_5)) {
                return tool.getComponent(ComponentTypes.TOOL).map(ItemTool::isCanDestroyBlocksInCreative).orElse(true) != false ? 1.0 : 0.0;
            }
            if (toolType.hasAttribute(ItemTypes.ItemAttribute.SWORD) || toolType == ItemTypes.TRIDENT || toolType == ItemTypes.DEBUG_STICK && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13) || toolType == ItemTypes.MACE && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
                return 0.0;
            }
            return 1.0;
        }
        float blockHardness = block.getHardness();
        if ((block == StateTypes.PISTON || block == StateTypes.PISTON_HEAD || block == StateTypes.STICKY_PISTON) && player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_15_2)) {
            blockHardness = 0.5f;
        }
        if (blockHardness == -1.0f) {
            return 0.0;
        }
        ToolSpeedData toolSpeedData = SERVER_USES_COMPONENTS_AND_RULES && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_20_5) ? BlockBreakSpeed.getModernToolSpeedData(player, tool, block) : BlockBreakSpeed.getLegacyToolSpeedData(player, tool, block);
        float speedMultiplier = BlockBreakSpeed.getSpeedMultiplierFromToolData(player, tool, toolSpeedData);
        boolean canHarvest = !block.isRequiresCorrectTool() || toolSpeedData.isCorrectToolForDrop || player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_4) && HARVESTABLE_TYPES_1_21_4.contains(block);
        float damage = speedMultiplier / blockHardness;
        return damage /= canHarvest ? 30.0f : 100.0f;
    }

    private static float getSpeedMultiplierFromToolData(GrimPlayer player, ItemStack tool, ToolSpeedData data) {
        OptionalInt miningFatigue;
        float speedMultiplier = data.speedMultiplier;
        if (speedMultiplier > 1.0f) {
            if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21) && PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21)) {
                speedMultiplier += (float)player.compensatedEntities.self.getAttributeValue(Attributes.MINING_EFFICIENCY);
            } else {
                int digSpeed = tool.getEnchantmentLevel(EnchantmentTypes.BLOCK_EFFICIENCY);
                if (digSpeed > 0) {
                    speedMultiplier += (float)(digSpeed * digSpeed + 1);
                }
            }
        }
        OptionalInt digSpeed = player.compensatedEntities.getPotionLevelForSelfPlayer(PotionTypes.HASTE);
        OptionalInt conduit = player.compensatedEntities.getPotionLevelForSelfPlayer(PotionTypes.CONDUIT_POWER);
        if (digSpeed.isPresent() || conduit.isPresent()) {
            int hasteLevel = Math.max(digSpeed.isEmpty() ? 0 : digSpeed.getAsInt(), conduit.isEmpty() ? 0 : conduit.getAsInt());
            speedMultiplier *= (float)(1.0 + 0.2 * (double)(hasteLevel + 1));
        }
        if ((miningFatigue = player.compensatedEntities.getPotionLevelForSelfPlayer(PotionTypes.MINING_FATIGUE)).isPresent()) {
            switch (miningFatigue.getAsInt()) {
                case 0: {
                    speedMultiplier *= 0.3f;
                    break;
                }
                case 1: {
                    speedMultiplier *= 0.09f;
                    break;
                }
                case 2: {
                    speedMultiplier *= 0.0027f;
                    break;
                }
                default: {
                    speedMultiplier *= 8.1E-4f;
                }
            }
        }
        speedMultiplier *= (float)player.compensatedEntities.self.getAttributeValue(Attributes.BLOCK_BREAK_SPEED);
        if (player.fluidOnEyes == FluidTag.WATER) {
            if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21) && PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21)) {
                speedMultiplier *= (float)player.compensatedEntities.self.getAttributeValue(Attributes.SUBMERGED_MINING_SPEED);
            } else if (EnchantmentHelper.getMaximumEnchantLevel(player.inventory, EnchantmentTypes.AQUA_AFFINITY) == 0) {
                speedMultiplier /= 5.0f;
            }
        }
        if (!player.packetStateData.packetPlayerOnGround) {
            speedMultiplier /= 5.0f;
        }
        return speedMultiplier;
    }

    private static ToolSpeedData getModernToolSpeedData(GrimPlayer player, ItemStack tool, StateType block) {
        Optional<ItemTool> toolComponentOpt = tool.getComponent(ComponentTypes.TOOL);
        float speedMultiplier = 1.0f;
        boolean isCorrectToolForDrop = false;
        if (toolComponentOpt.isPresent()) {
            ItemTool itemTool = toolComponentOpt.get();
            speedMultiplier = itemTool.getDefaultMiningSpeed();
            boolean speedFound = false;
            boolean dropsFound = false;
            for (ItemTool.Rule rule : itemTool.getRules()) {
                SyncedTag<StateType> playerTag;
                MappedEntitySet<StateType.Mapped> predicate = rule.getBlocks();
                ResourceLocation tagKey = predicate.getTagKey();
                boolean isMatch = tagKey != null ? (playerTag = player.tagManager.block(tagKey)) != null && playerTag.contains(block) || BlockTags.getByName(tagKey.getKey()).contains(block) : predicate.getEntities().contains(block.getMapped());
                if (isMatch) {
                    if (!speedFound && rule.getSpeed() != null) {
                        speedMultiplier = rule.getSpeed().floatValue();
                        speedFound = true;
                    }
                    if (!dropsFound && rule.getCorrectForDrops() != null) {
                        isCorrectToolForDrop = rule.getCorrectForDrops();
                        dropsFound = true;
                    }
                }
                if (!speedFound || !dropsFound) continue;
                break;
            }
        }
        return new ToolSpeedData(speedMultiplier, isCorrectToolForDrop);
    }

    private static ToolSpeedData getLegacyToolSpeedData(GrimPlayer player, ItemStack tool, StateType block) {
        ItemType toolType = tool.getType();
        float speedMultiplier = 1.0f;
        boolean isCorrectToolForDrop = false;
        if (toolType.hasAttribute(ItemTypes.ItemAttribute.AXE)) {
            isCorrectToolForDrop = player.tagManager.block(SyncedTags.MINEABLE_AXE).contains(block);
        } else if (toolType.hasAttribute(ItemTypes.ItemAttribute.PICKAXE)) {
            isCorrectToolForDrop = player.tagManager.block(SyncedTags.MINEABLE_PICKAXE).contains(block);
        } else if (toolType.hasAttribute(ItemTypes.ItemAttribute.SHOVEL)) {
            isCorrectToolForDrop = player.tagManager.block(SyncedTags.MINEABLE_SHOVEL).contains(block);
        } else if (toolType.hasAttribute(ItemTypes.ItemAttribute.HOE)) {
            isCorrectToolForDrop = player.tagManager.block(SyncedTags.MINEABLE_HOE).contains(block);
        }
        if (isCorrectToolForDrop) {
            int tier = 0;
            if (toolType.hasAttribute(ItemTypes.ItemAttribute.WOOD_TIER)) {
                speedMultiplier = 2.0f;
            } else if (toolType.hasAttribute(ItemTypes.ItemAttribute.STONE_TIER)) {
                speedMultiplier = 4.0f;
                tier = 1;
            } else if (toolType.hasAttribute(ItemTypes.ItemAttribute.IRON_TIER)) {
                speedMultiplier = 6.0f;
                tier = 2;
            } else if (toolType.hasAttribute(ItemTypes.ItemAttribute.DIAMOND_TIER)) {
                speedMultiplier = 8.0f;
                tier = 3;
            } else if (toolType.hasAttribute(ItemTypes.ItemAttribute.GOLD_TIER)) {
                speedMultiplier = 12.0f;
            } else if (toolType.hasAttribute(ItemTypes.ItemAttribute.NETHERITE_TIER)) {
                speedMultiplier = 9.0f;
                tier = 4;
            }
            if (tier < 3 && player.tagManager.block(SyncedTags.NEEDS_DIAMOND_TOOL).contains(block)) {
                isCorrectToolForDrop = false;
            } else if (tier < 2 && player.tagManager.block(SyncedTags.NEEDS_IRON_TOOL).contains(block)) {
                isCorrectToolForDrop = false;
            } else if (tier < 1 && player.tagManager.block(SyncedTags.NEEDS_STONE_TOOL).contains(block)) {
                isCorrectToolForDrop = false;
            }
        }
        if (toolType == ItemTypes.SHEARS) {
            isCorrectToolForDrop = true;
            if (block == StateTypes.COBWEB || Materials.isLeaves(block)) {
                speedMultiplier = 15.0f;
            } else if (BlockTags.WOOL.contains(block)) {
                speedMultiplier = 5.0f;
            } else if (block == StateTypes.VINE || block == StateTypes.GLOW_LICHEN) {
                speedMultiplier = 2.0f;
            } else {
                boolean bl = isCorrectToolForDrop = block == StateTypes.COBWEB || block == StateTypes.REDSTONE_WIRE || block == StateTypes.TRIPWIRE;
            }
        }
        if (toolType.hasAttribute(ItemTypes.ItemAttribute.SWORD)) {
            if (block == StateTypes.COBWEB) {
                speedMultiplier = 15.0f;
            } else if (player.tagManager.block(SyncedTags.SWORD_EFFICIENT).contains(block)) {
                speedMultiplier = 1.5f;
            }
            isCorrectToolForDrop = block == StateTypes.COBWEB;
        }
        return new ToolSpeedData(speedMultiplier, isCorrectToolForDrop);
    }

    @Generated
    private BlockBreakSpeed() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    record ToolSpeedData(float speedMultiplier, boolean isCorrectToolForDrop) {
    }
}

