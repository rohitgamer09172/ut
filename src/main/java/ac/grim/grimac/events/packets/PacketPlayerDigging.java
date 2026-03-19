/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.events.packets;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.checks.impl.movement.NoSlow;
import ac.grim.grimac.events.packets.CheckManagerListener;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerAbstract;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerPriority;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.FoodProperties;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemConsumable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.InteractionHand;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUseItem;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.item.ItemBehaviour;
import ac.grim.grimac.utils.item.ItemBehaviourRegistry;

public class PacketPlayerDigging
extends PacketListenerAbstract {
    private static final boolean RELIABLE_COMPONENT_SYSTEM = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_21_4);
    private static final boolean SERVER_HAS_OFFHAND = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9);

    public PacketPlayerDigging() {
        super(PacketListenerPriority.LOW);
    }

    public static void handleUseItem(@NotNull GrimPlayer player, @NotNull InteractionHand hand) {
        ItemStack item = player.inventory.getItemInHand(hand);
        if (item == null) {
            player.packetStateData.setSlowedByUsingItem(false);
            return;
        }
        if (player.checkManager.getCompensatedCooldown().hasItem(item)) {
            player.packetStateData.setSlowedByUsingItem(false);
            return;
        }
        ItemType material = item.getType();
        if (RELIABLE_COMPONENT_SYSTEM && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_4)) {
            ItemBehaviour itemBehaviour = ItemBehaviourRegistry.getItemBehaviour(material);
            if (itemBehaviour.canUse(item, player.compensatedWorld, player, hand)) {
                player.packetStateData.setSlowedByUsingItem(true);
                player.packetStateData.itemInUseHand = hand;
            } else {
                player.packetStateData.setSlowedByUsingItem(false);
            }
            return;
        }
        ItemConsumable consumable = item.getComponentOr(ComponentTypes.CONSUMABLE, null);
        FoodProperties foodComponent = item.getComponentOr(ComponentTypes.FOOD, null);
        if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_21_2) && consumable != null && foodComponent == null) {
            player.packetStateData.setSlowedByUsingItem(true);
            player.packetStateData.itemInUseHand = hand;
        }
        if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_20_5) && foodComponent != null) {
            if (foodComponent.isCanAlwaysEat() || player.food < 20 || player.gamemode == GameMode.CREATIVE) {
                player.packetStateData.setSlowedByUsingItem(true);
                player.packetStateData.itemInUseHand = hand;
                return;
            }
            player.packetStateData.setSlowedByUsingItem(false);
        }
        if (material.hasAttribute(ItemTypes.ItemAttribute.EDIBLE) && (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_15) || player.gamemode != GameMode.CREATIVE) || material == ItemTypes.POTION || material == ItemTypes.MILK_BUCKET) {
            if (item.getType() == ItemTypes.SPLASH_POTION) {
                return;
            }
            if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_9) && item.getLegacyData() > 16384) {
                return;
            }
            if (material == ItemTypes.POTION || material == ItemTypes.MILK_BUCKET || material == ItemTypes.GOLDEN_APPLE || material == ItemTypes.ENCHANTED_GOLDEN_APPLE || material == ItemTypes.HONEY_BOTTLE || material == ItemTypes.SUSPICIOUS_STEW || material == ItemTypes.CHORUS_FRUIT) {
                player.packetStateData.setSlowedByUsingItem(true);
                player.packetStateData.itemInUseHand = hand;
                return;
            }
            if (item.getType().hasAttribute(ItemTypes.ItemAttribute.EDIBLE) && (player.platformPlayer != null && player.food < 20 || player.gamemode == GameMode.CREATIVE)) {
                player.packetStateData.setSlowedByUsingItem(true);
                player.packetStateData.itemInUseHand = hand;
                return;
            }
            player.packetStateData.setSlowedByUsingItem(false);
        }
        if (material == ItemTypes.SHIELD && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9)) {
            player.packetStateData.setSlowedByUsingItem(true);
            player.packetStateData.itemInUseHand = hand;
            return;
        }
        NBTCompound nbt = item.getNBT();
        if (material == ItemTypes.CROSSBOW && nbt != null && nbt.getBoolean("Charged")) {
            player.packetStateData.setSlowedByUsingItem(false);
            return;
        }
        if (material == ItemTypes.TRIDENT && item.getDamageValue() < item.getMaxDamage() - 1 && (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13_2) || player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_8))) {
            player.packetStateData.setSlowedByUsingItem(item.getEnchantmentLevel(EnchantmentTypes.RIPTIDE) <= 0);
            player.packetStateData.itemInUseHand = hand;
        }
        if (material == ItemTypes.BOW || material == ItemTypes.CROSSBOW) {
            player.packetStateData.setSlowedByUsingItem(false);
        }
        if (material == ItemTypes.SPYGLASS && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_17)) {
            player.packetStateData.setSlowedByUsingItem(true);
            player.packetStateData.itemInUseHand = hand;
        }
        if (material == ItemTypes.GOAT_HORN && player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_19)) {
            player.packetStateData.setSlowedByUsingItem(true);
            player.packetStateData.itemInUseHand = hand;
        }
        if (material.hasAttribute(ItemTypes.ItemAttribute.SWORD)) {
            if (player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_8)) {
                player.packetStateData.setSlowedByUsingItem(true);
            } else if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_9)) {
                player.packetStateData.setSlowedByUsingItem(false);
            }
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        GrimPlayer player;
        GrimPlayer player2;
        WrapperPlayClientPlayerDigging dig;
        if (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING && (dig = new WrapperPlayClientPlayerDigging(event)).getAction() == DiggingAction.RELEASE_USE_ITEM) {
            ItemStack hand;
            player2 = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
            if (player2 == null) {
                return;
            }
            player2.packetStateData.setSlowedByUsingItem(false);
            player2.packetStateData.slowedByUsingItemTransaction = player2.lastTransactionReceived.get();
            if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13) && (hand = player2.inventory.getItemInHand(player2.packetStateData.itemInUseHand)).getType() == ItemTypes.TRIDENT && hand.getEnchantmentLevel(EnchantmentTypes.RIPTIDE) > 0) {
                player2.packetStateData.tryingToRiptide = true;
            }
        }
        if ((WrapperPlayClientPlayerFlying.isFlying(event.getPacketType()) || event.getPacketType() == PacketType.Play.Client.CLIENT_TICK_END) && (player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser())) != null && player.packetStateData.isSlowedByUsingItem() && !player.packetStateData.lastPacketWasTeleport && !player.packetStateData.lastPacketWasOnePointSeventeenDuplicate) {
            boolean slotChanged;
            boolean bl = slotChanged = player.packetStateData.itemInUseHand != InteractionHand.OFF_HAND && player.packetStateData.getSlowedByUsingItemSlot() != player.packetStateData.lastSlotSelected;
            if (slotChanged || player.inventory.getItemInHand(player.packetStateData.itemInUseHand).isEmpty()) {
                player.packetStateData.setSlowedByUsingItem(false);
                if (slotChanged) {
                    player.checkManager.getPostPredictionCheck(NoSlow.class).didSlotChangeLastTick = true;
                }
            }
        }
        if (event.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE) {
            int slot = new WrapperPlayClientHeldItemChange(event).getSlot();
            if (slot > 8 || slot < 0) {
                return;
            }
            player2 = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
            if (player2 == null) {
                return;
            }
            CheckManagerListener.handleQueuedPlaces(player2, false, 0.0f, 0.0f, System.currentTimeMillis());
            if (player2.packetStateData.lastSlotSelected != slot) {
                if (player2.isResetItemUsageOnSlotChange() && GrimAPI.INSTANCE.getItemResetHandler().getItemUsageHand(player2.platformPlayer) == InteractionHand.MAIN_HAND) {
                    GrimAPI.INSTANCE.getItemResetHandler().resetItemUsage(player2.platformPlayer);
                }
                if (player2.canSkipTicks() && !player2.isTickingReliablyFor(3) && player2.packetStateData.itemInUseHand != InteractionHand.OFF_HAND) {
                    player2.packetStateData.setSlowedByUsingItem(false);
                }
            }
            player2.packetStateData.lastSlotSelected = slot;
        }
        if (event.getPacketType() == PacketType.Play.Client.USE_ITEM || event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT && new WrapperPlayClientPlayerBlockPlacement(event).getFace() == BlockFace.OTHER) {
            GrimPlayer player3 = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
            if (player3 == null) {
                return;
            }
            if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_8) && player3.gamemode == GameMode.SPECTATOR) {
                return;
            }
            InteractionHand hand = SERVER_HAS_OFFHAND && event.getPacketType() == PacketType.Play.Client.USE_ITEM ? new WrapperPlayClientUseItem(event).getHand() : InteractionHand.MAIN_HAND;
            player3.packetStateData.slowedByUsingItemTransaction = player3.lastTransactionReceived.get();
            if (player3.isResetItemUsageOnItemUse()) {
                GrimAPI.INSTANCE.getItemResetHandler().resetItemUsage(player3.platformPlayer);
            }
            PacketPlayerDigging.handleUseItem(player3, hand);
        }
    }
}

