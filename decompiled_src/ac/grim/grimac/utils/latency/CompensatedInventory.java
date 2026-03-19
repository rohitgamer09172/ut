/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.latency;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.InteractionHand;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCreativeInventoryAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUseItem;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenHorseWindow;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenWindow;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetPlayerInventory;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetSlot;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowItems;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;
import ac.grim.grimac.utils.inventory.EquipmentType;
import ac.grim.grimac.utils.inventory.Inventory;
import ac.grim.grimac.utils.inventory.inventory.AbstractContainerMenu;
import ac.grim.grimac.utils.inventory.inventory.MenuType;
import ac.grim.grimac.utils.inventory.inventory.NotImplementedMenu;
import ac.grim.grimac.utils.lists.CorrectingPlayerInventoryStorage;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class CompensatedInventory
extends Check
implements PacketCheck {
    private static final int PLAYER_INVENTORY_CASE = -1;
    private static final int UNSUPPORTED_INVENTORY_CASE = -2;
    public final Inventory inventory;
    public AbstractContainerMenu menu;
    public boolean isPacketInventoryActive = true;
    public boolean needResend = false;
    public int stateID = 0;
    private int openWindowID = 0;
    private int packetSendingInventorySize = -1;
    private ItemStack startOfTickStack = ItemStack.EMPTY;

    public ItemStack getStartOfTickStack() {
        return this.startOfTickStack;
    }

    public CompensatedInventory(GrimPlayer playerData) {
        super(playerData);
        CorrectingPlayerInventoryStorage storage = new CorrectingPlayerInventoryStorage(this.player, 46);
        this.inventory = new Inventory(playerData, storage);
        this.menu = this.inventory;
    }

    public int getBukkitSlot(int packetSlot) {
        if (packetSlot <= 4) {
            return -1;
        }
        if (packetSlot <= 8) {
            return 7 - packetSlot + 36;
        }
        if (packetSlot <= 35) {
            return packetSlot;
        }
        if (packetSlot <= 44) {
            return packetSlot - 36;
        }
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9) && packetSlot == 45) {
            return 40;
        }
        return -1;
    }

    private void markPlayerSlotAsChanged(int clicked) {
        if (this.openWindowID == 0) {
            this.inventory.getInventoryStorage().handleClientClaimedSlotSet(clicked);
            return;
        }
        if (this.menu instanceof NotImplementedMenu) {
            return;
        }
        int nonPlayerInvSize = this.menu.getSlots().size() - 36 + 9;
        int playerInvSlotclicked = clicked - nonPlayerInvSize;
        this.inventory.getInventoryStorage().handleClientClaimedSlotSet(playerInvSlotclicked);
    }

    public ItemStack getItemInHand(InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND ? this.getHeldItem() : this.getOffHand();
    }

    private void markServerForChangingSlot(int clicked, int windowID) {
        if (this.packetSendingInventorySize == -2) {
            return;
        }
        if (this.packetSendingInventorySize == -1 || windowID == 0) {
            this.inventory.getInventoryStorage().handleServerCorrectSlot(clicked);
            return;
        }
        int nonPlayerInvSize = this.menu.getSlots().size() - 36 + 9;
        int playerInvSlotclicked = clicked - nonPlayerInvSize;
        this.inventory.getInventoryStorage().handleServerCorrectSlot(playerInvSlotclicked);
    }

    public ItemStack getHeldItem() {
        ItemStack item = this.isPacketInventoryActive || this.player.platformPlayer == null ? this.inventory.getHeldItem() : this.player.platformPlayer.getInventory().getItemInHand();
        return item == null ? ItemStack.EMPTY : item;
    }

    public ItemStack getOffHand() {
        if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_9)) {
            return ItemStack.EMPTY;
        }
        ItemStack item = this.isPacketInventoryActive || this.player.platformPlayer == null ? this.inventory.getOffhand() : this.player.platformPlayer.getInventory().getItemInOffHand();
        return item == null ? ItemStack.EMPTY : item;
    }

    public ItemStack getHelmet() {
        ItemStack item = this.isPacketInventoryActive || this.player.platformPlayer == null ? this.inventory.getHelmet() : this.player.platformPlayer.getInventory().getHelmet();
        return item == null ? ItemStack.EMPTY : item;
    }

    public ItemStack getChestplate() {
        ItemStack item = this.isPacketInventoryActive || this.player.platformPlayer == null ? this.inventory.getChestplate() : this.player.platformPlayer.getInventory().getChestplate();
        return item == null ? ItemStack.EMPTY : item;
    }

    public ItemStack getLeggings() {
        ItemStack item = this.isPacketInventoryActive || this.player.platformPlayer == null ? this.inventory.getLeggings() : this.player.platformPlayer.getInventory().getLeggings();
        return item == null ? ItemStack.EMPTY : item;
    }

    public ItemStack getBoots() {
        ItemStack item = this.isPacketInventoryActive || this.player.platformPlayer == null ? this.inventory.getBoots() : this.player.platformPlayer.getInventory().getBoots();
        return item == null ? ItemStack.EMPTY : item;
    }

    private ItemStack getByEquipmentType(EquipmentType type) {
        return switch (type) {
            default -> throw new IncompatibleClassChangeError();
            case EquipmentType.HEAD -> this.getHelmet();
            case EquipmentType.CHEST -> this.getChestplate();
            case EquipmentType.LEGS -> this.getLeggings();
            case EquipmentType.FEET -> this.getBoots();
            case EquipmentType.OFFHAND -> this.getOffHand();
            case EquipmentType.MAINHAND -> this.getHeldItem();
        };
    }

    public boolean hasItemType(ItemType type) {
        if (this.isPacketInventoryActive || this.player.platformPlayer == null) {
            return this.inventory.hasItemType(type);
        }
        for (ItemStack itemStack : this.player.platformPlayer.getInventory().getContents()) {
            if (itemStack == null || itemStack.getType() != type) continue;
            return true;
        }
        return false;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.USE_ITEM) {
            WrapperPlayClientUseItem item = new WrapperPlayClientUseItem(event);
            ItemStack use = item.getHand() == InteractionHand.MAIN_HAND ? this.getHeldItem() : this.getOffHand();
            EquipmentType equipmentType = EquipmentType.getEquipmentSlotForItem(use);
            if (equipmentType != null) {
                int slot;
                switch (equipmentType) {
                    case HEAD: {
                        slot = 4;
                        break;
                    }
                    case CHEST: {
                        slot = 5;
                        break;
                    }
                    case LEGS: {
                        slot = 6;
                        break;
                    }
                    case FEET: {
                        slot = 7;
                        break;
                    }
                    default: {
                        return;
                    }
                }
                ItemStack currentEquippedItem = this.getByEquipmentType(equipmentType);
                if (this.player.getClientVersion().isOlderThan(ClientVersion.V_1_19_4) && !currentEquippedItem.isEmpty()) {
                    return;
                }
                int swapItemSlot = item.getHand() == InteractionHand.MAIN_HAND ? this.inventory.selected + 36 : 45;
                this.inventory.getInventoryStorage().handleClientClaimedSlotSet(swapItemSlot);
                this.inventory.getInventoryStorage().setItem(swapItemSlot, currentEquippedItem);
                this.inventory.getInventoryStorage().handleClientClaimedSlotSet(slot);
                this.inventory.getInventoryStorage().setItem(slot, use);
            }
        } else if (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING) {
            WrapperPlayClientPlayerDigging dig = new WrapperPlayClientPlayerDigging(event);
            if (this.player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_8)) {
                return;
            }
            if (dig.getAction() == DiggingAction.DROP_ITEM) {
                ItemStack heldItem = this.getHeldItem();
                if (heldItem != null) {
                    heldItem.setAmount(heldItem.getAmount() - 1);
                    if (heldItem.getAmount() <= 0) {
                        heldItem = null;
                    }
                }
                this.inventory.setHeldItem(heldItem);
                this.inventory.getInventoryStorage().handleClientClaimedSlotSet(36 + this.player.packetStateData.lastSlotSelected);
            }
            if (dig.getAction() == DiggingAction.DROP_ITEM_STACK) {
                this.inventory.setHeldItem(null);
                this.inventory.getInventoryStorage().handleClientClaimedSlotSet(36 + this.player.packetStateData.lastSlotSelected);
            }
        } else if (event.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE) {
            int slot = new WrapperPlayClientHeldItemChange(event).getSlot();
            if (slot > 8 || slot < 0) {
                return;
            }
            this.inventory.selected = slot;
        } else if (event.getPacketType() == PacketType.Play.Client.CREATIVE_INVENTORY_ACTION) {
            boolean valid;
            WrapperPlayClientCreativeInventoryAction action = new WrapperPlayClientCreativeInventoryAction(event);
            if (this.player.gamemode != GameMode.CREATIVE) {
                return;
            }
            boolean bl = action.getSlot() >= 1 && (PacketEvents.getAPI().getServerManager().getVersion().isNewerThan(ServerVersion.V_1_8) ? action.getSlot() <= 45 : action.getSlot() < 45) ? true : (valid = false);
            if (valid) {
                this.inventory.getSlot(action.getSlot()).set(action.getItemStack());
                this.inventory.getInventoryStorage().handleClientClaimedSlotSet(action.getSlot());
            }
        } else if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW && !event.isCancelled()) {
            WrapperPlayClientClickWindow click = new WrapperPlayClientClickWindow(event);
            if (click.getWindowId() != this.openWindowID) {
                return;
            }
            if (this.menu instanceof NotImplementedMenu) {
                return;
            }
            Optional<Map<Integer, ItemStack>> slots = click.getSlots();
            slots.ifPresent(integerItemStackMap -> integerItemStackMap.keySet().forEach(this::markPlayerSlotAsChanged));
            int button = click.getButton();
            int slot = click.getSlot();
            WrapperPlayClientClickWindow.WindowClickType clickType = click.getWindowClickType();
            if (slot == -1 || slot == -999 || slot < this.menu.getSlots().size()) {
                this.menu.doClick(button, slot, clickType);
            }
        } else if (event.getPacketType() == PacketType.Play.Client.CLOSE_WINDOW) {
            this.closeActiveInventory();
        } else if (event.getPacketType() == PacketType.Play.Client.CLIENT_TICK_END) {
            this.startOfTickStack = this.getHeldItem();
        }
    }

    public void markSlotAsResyncing(BlockPlace place) {
        if (place.hand == InteractionHand.MAIN_HAND) {
            this.inventory.getInventoryStorage().handleClientClaimedSlotSet(36 + this.player.packetStateData.lastSlotSelected);
        } else {
            this.inventory.getInventoryStorage().handleServerCorrectSlot(45);
        }
    }

    public void onBlockPlace(BlockPlace place) {
        if (this.player.gamemode != GameMode.CREATIVE && place.itemStack.getType() != ItemTypes.POWDER_SNOW_BUCKET) {
            this.markSlotAsResyncing(place);
            place.itemStack.setAmount(place.itemStack.getAmount() - 1);
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        PacketWrapper slot;
        PacketWrapper open;
        if (event.getPacketType() == PacketType.Play.Server.OPEN_WINDOW) {
            open = new WrapperPlayServerOpenWindow(event);
            MenuType menuType = MenuType.getMenuType(((WrapperPlayServerOpenWindow)open).getType());
            AbstractContainerMenu newMenu = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_14) ? MenuType.getMenuFromID(this.player, this.inventory, menuType) : MenuType.getMenuFromString(this.player, this.inventory, ((WrapperPlayServerOpenWindow)open).getLegacyType(), ((WrapperPlayServerOpenWindow)open).getLegacySlots(), ((WrapperPlayServerOpenWindow)open).getHorseId());
            this.packetSendingInventorySize = newMenu instanceof NotImplementedMenu ? -2 : newMenu.getSlots().size();
            this.player.latencyUtils.addRealTimeTask(this.player.lastTransactionSent.get(), () -> this.lambda$onPacketSend$1((WrapperPlayServerOpenWindow)open, newMenu));
        }
        if (event.getPacketType() == PacketType.Play.Server.OPEN_HORSE_WINDOW) {
            open = new WrapperPlayServerOpenHorseWindow(event);
            this.packetSendingInventorySize = -2;
            this.player.latencyUtils.addRealTimeTask(this.player.lastTransactionSent.get(), () -> this.lambda$onPacketSend$2((WrapperPlayServerOpenHorseWindow)open));
        }
        if (event.getPacketType() == PacketType.Play.Server.CLOSE_WINDOW) {
            this.packetSendingInventorySize = -1;
            this.player.latencyUtils.addRealTimeTask(this.player.lastTransactionSent.get(), this::closeActiveInventory);
        }
        if (event.getPacketType() == PacketType.Play.Server.WINDOW_ITEMS) {
            WrapperPlayServerWindowItems items = new WrapperPlayServerWindowItems(event);
            this.stateID = items.getStateId();
            List<ItemStack> slots = items.getItems();
            for (int i = 0; i < slots.size(); ++i) {
                this.markServerForChangingSlot(i, items.getWindowId());
            }
            int cachedPacketInvSize = this.packetSendingInventorySize;
            AtomicBoolean updatedValue = new AtomicBoolean();
            this.player.latencyUtils.addRealTimeTask(this.player.lastTransactionSent.get(), () -> {
                if (slots.size() == cachedPacketInvSize || items.getWindowId() == 0) {
                    this.isPacketInventoryActive = true;
                    updatedValue.set(true);
                }
            });
            if (items.getWindowId() == 0) {
                this.player.latencyUtils.addRealTimeTask(this.player.lastTransactionSent.get(), () -> {
                    if (!this.isPacketInventoryActive) {
                        return;
                    }
                    for (int i = 0; i < slots.size(); ++i) {
                        this.inventory.getSlot(i).set((ItemStack)slots.get(i));
                    }
                    if (items.getCarriedItem().isPresent()) {
                        this.inventory.setCarried(items.getCarriedItem().get());
                    }
                });
            } else {
                this.player.latencyUtils.addRealTimeTask(this.player.lastTransactionSent.get(), () -> {
                    if (!this.isPacketInventoryActive) {
                        return;
                    }
                    if (items.getWindowId() == this.openWindowID) {
                        for (int i = 0; i < slots.size(); ++i) {
                            this.menu.getSlot(i).set((ItemStack)slots.get(i));
                        }
                    }
                    if (items.getCarriedItem().isPresent()) {
                        this.inventory.setCarried(items.getCarriedItem().get());
                    }
                });
            }
            this.player.latencyUtils.addRealTimeTask(this.player.lastTransactionSent.get(), () -> {
                if (updatedValue.get() && !this.menu.equals(this.inventory)) {
                    this.isPacketInventoryActive = false;
                }
            });
        }
        if (event.getPacketType() == PacketType.Play.Server.SET_PLAYER_INVENTORY) {
            slot = new WrapperPlayServerSetPlayerInventory(event);
            int slotID = ((WrapperPlayServerSetPlayerInventory)slot).getSlot();
            ItemStack item = ((WrapperPlayServerSetPlayerInventory)slot).getStack();
            this.inventory.getInventoryStorage().handleServerCorrectSlot(slotID);
            this.player.latencyUtils.addRealTimeTask(this.player.lastTransactionSent.get(), () -> {
                if (!this.isPacketInventoryActive) {
                    return;
                }
                this.inventory.getSlot(slotID).set(item);
            });
        }
        if (event.getPacketType() == PacketType.Play.Server.SET_SLOT) {
            slot = new WrapperPlayServerSetSlot(event);
            int slotID = ((WrapperPlayServerSetSlot)slot).getSlot();
            int inventoryID = ((WrapperPlayServerSetSlot)slot).getWindowId();
            ItemStack item = ((WrapperPlayServerSetSlot)slot).getItem();
            if (inventoryID == -2) {
                this.inventory.getInventoryStorage().handleServerCorrectSlot(slotID);
            } else if (inventoryID == 0) {
                this.inventory.getInventoryStorage().handleServerCorrectSlot(slotID);
            } else {
                this.markServerForChangingSlot(slotID, inventoryID);
            }
            this.stateID = ((WrapperPlayServerSetSlot)slot).getStateId();
            this.player.latencyUtils.addRealTimeTask(this.player.lastTransactionSent.get(), () -> {
                if (!this.isPacketInventoryActive) {
                    return;
                }
                if (inventoryID == -1) {
                    this.inventory.setCarried(item);
                } else if (inventoryID == -2) {
                    if (this.inventory.getInventoryStorage().getSize() > slotID && slotID >= 0) {
                        this.inventory.getInventoryStorage().setItem(slotID, item);
                    }
                } else if (inventoryID == 0) {
                    if (slotID >= 0 && slotID <= 45) {
                        this.inventory.getSlot(slotID).set(item);
                    }
                } else if (inventoryID == this.openWindowID) {
                    this.menu.getSlot(slotID).set(item);
                }
            });
        }
    }

    private void closeActiveInventory() {
        this.isPacketInventoryActive = true;
        this.openWindowID = 0;
        this.menu = this.inventory;
        this.menu.setCarried(ItemStack.EMPTY);
    }

    private /* synthetic */ void lambda$onPacketSend$2(WrapperPlayServerOpenHorseWindow open) {
        this.isPacketInventoryActive = false;
        this.needResend = true;
        this.openWindowID = open.getWindowId();
    }

    private /* synthetic */ void lambda$onPacketSend$1(WrapperPlayServerOpenWindow open, AbstractContainerMenu newMenu) {
        this.openWindowID = open.getContainerId();
        this.menu = newMenu;
        this.isPacketInventoryActive = !(newMenu instanceof NotImplementedMenu);
        this.needResend = newMenu instanceof NotImplementedMenu;
    }
}

