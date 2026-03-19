/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.inventory;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.utils.inventory.EquipmentType;
import ac.grim.grimac.utils.inventory.inventory.AbstractContainerMenu;
import ac.grim.grimac.utils.inventory.slot.EquipmentSlot;
import ac.grim.grimac.utils.inventory.slot.ResultSlot;
import ac.grim.grimac.utils.inventory.slot.Slot;
import ac.grim.grimac.utils.lists.CorrectingPlayerInventoryStorage;
import lombok.Generated;

public class Inventory
extends AbstractContainerMenu {
    public static final int SLOT_OFFHAND = 45;
    public static final int HOTBAR_OFFSET = 36;
    public static final int ITEMS_START = 9;
    public static final int ITEMS_END = 45;
    public static final int SLOT_HELMET = 4;
    public static final int SLOT_CHESTPLATE = 5;
    public static final int SLOT_LEGGINGS = 6;
    public static final int SLOT_BOOTS = 7;
    private static final int TOTAL_SIZE = 46;
    public int selected = 0;
    private final CorrectingPlayerInventoryStorage inventoryStorage;

    public Inventory(GrimPlayer player, CorrectingPlayerInventoryStorage inventoryStorage) {
        super(player);
        int i;
        this.setPlayerInventory(this);
        this.inventoryStorage = inventoryStorage;
        this.addSlot(new ResultSlot(inventoryStorage, 0));
        for (i = 0; i < 4; ++i) {
            this.addSlot(new Slot(inventoryStorage, i));
        }
        for (i = 0; i < 4; ++i) {
            this.addSlot(new EquipmentSlot(EquipmentType.byArmorID(i), inventoryStorage, i + 4));
        }
        for (i = 0; i < 36; ++i) {
            this.addSlot(new Slot(inventoryStorage, i + 9));
        }
        this.addSlot(new Slot(inventoryStorage, 45));
    }

    public ItemStack getHelmet() {
        return this.inventoryStorage.getItem(4);
    }

    public ItemStack getChestplate() {
        return this.inventoryStorage.getItem(5);
    }

    public ItemStack getLeggings() {
        return this.inventoryStorage.getItem(6);
    }

    public ItemStack getBoots() {
        return this.inventoryStorage.getItem(7);
    }

    public ItemStack getOffhand() {
        return this.inventoryStorage.getItem(45);
    }

    public boolean hasItemType(ItemType item) {
        for (int i = 0; i < this.inventoryStorage.items.length; ++i) {
            if (this.inventoryStorage.getItem(i).getType() != item) continue;
            return true;
        }
        return false;
    }

    public ItemStack getHeldItem() {
        return this.inventoryStorage.getItem(this.selected + 36);
    }

    public void setHeldItem(ItemStack item) {
        this.inventoryStorage.setItem(this.selected + 36, item);
    }

    public ItemStack getOffhandItem() {
        return this.inventoryStorage.getItem(45);
    }

    public boolean add(ItemStack p_36055_) {
        return this.add(-1, p_36055_);
    }

    public int getFreeSlot() {
        for (int i = 0; i < this.inventoryStorage.items.length; ++i) {
            if (!this.inventoryStorage.getItem(i).isEmpty()) continue;
            return i;
        }
        return -1;
    }

    public int getSlotWithRemainingSpace(ItemStack toAdd) {
        if (this.hasRemainingSpaceForItem(this.getHeldItem(), toAdd)) {
            return this.selected;
        }
        if (this.hasRemainingSpaceForItem(this.getOffhandItem(), toAdd)) {
            return 40;
        }
        for (int i = 9; i <= 45; ++i) {
            if (!this.hasRemainingSpaceForItem(this.inventoryStorage.getItem(i), toAdd)) continue;
            return i;
        }
        return -1;
    }

    private boolean hasRemainingSpaceForItem(ItemStack one, ItemStack two) {
        return !one.isEmpty() && ItemStack.isSameItemSameTags(one, two) && one.getAmount() < one.getMaxStackSize() && one.getAmount() < this.getMaxStackSize();
    }

    private int addResource(ItemStack resource) {
        int i = this.getSlotWithRemainingSpace(resource);
        if (i == -1) {
            i = this.getFreeSlot();
        }
        return i == -1 ? resource.getAmount() : this.addResource(i, resource);
    }

    private int addResource(int slot, ItemStack stack) {
        int j;
        int i = stack.getAmount();
        ItemStack itemstack = this.inventoryStorage.getItem(slot);
        if (itemstack.isEmpty()) {
            itemstack = stack.copy();
            itemstack.setAmount(0);
            this.inventoryStorage.setItem(slot, itemstack);
        }
        if ((j = Math.min(i, itemstack.getMaxStackSize() - itemstack.getAmount())) > this.getMaxStackSize() - itemstack.getAmount()) {
            j = this.getMaxStackSize() - itemstack.getAmount();
        }
        if (j != 0) {
            i -= j;
            itemstack.grow(j);
        }
        return i;
    }

    public boolean add(int p_36041_, ItemStack p_36042_) {
        int i;
        if (p_36042_.isEmpty()) {
            return false;
        }
        if (p_36042_.isDamaged()) {
            if (p_36041_ == -1) {
                p_36041_ = this.getFreeSlot();
            }
            if (p_36041_ >= 0) {
                this.inventoryStorage.setItem(p_36041_, p_36042_.copy());
                p_36042_.setAmount(0);
                return true;
            }
            if (this.player.gamemode == GameMode.CREATIVE) {
                p_36042_.setAmount(0);
                return true;
            }
            return false;
        }
        do {
            i = p_36042_.getAmount();
            if (p_36041_ == -1) {
                p_36042_.setAmount(this.addResource(p_36042_));
                continue;
            }
            p_36042_.setAmount(this.addResource(p_36041_, p_36042_));
        } while (!p_36042_.isEmpty() && p_36042_.getAmount() < i);
        if (p_36042_.getAmount() == i && this.player.gamemode == GameMode.CREATIVE) {
            p_36042_.setAmount(0);
            return true;
        }
        return p_36042_.getAmount() < i;
    }

    @Override
    public ItemStack quickMoveStack(int slotID) {
        ItemStack original = ItemStack.EMPTY;
        Slot slot = this.getSlots().get(slotID);
        if (slot != null && slot.hasItem()) {
            int i;
            ItemStack toMove = slot.getItem();
            original = toMove.copy();
            EquipmentType equipmentslot = EquipmentType.getEquipmentSlotForItem(original);
            if (slotID == 0 ? !this.moveItemStackTo(toMove, 9, 45, true) : (slotID >= 1 && slotID < 5 ? !this.moveItemStackTo(toMove, 9, 45, false) : (slotID >= 5 && slotID < 9 ? !this.moveItemStackTo(toMove, 9, 45, false) : (equipmentslot.isArmor() && !this.getSlots().get(8 - equipmentslot.getIndex()).hasItem() ? !this.moveItemStackTo(toMove, i = 8 - equipmentslot.getIndex(), i + 1, false) : (equipmentslot == EquipmentType.OFFHAND && !this.getSlots().get(45).hasItem() ? !this.moveItemStackTo(toMove, 45, 46, false) : (slotID >= 9 && slotID < 36 ? !this.moveItemStackTo(toMove, 36, 45, false) : (slotID >= 36 && slotID < 45 ? !this.moveItemStackTo(toMove, 9, 36, false) : !this.moveItemStackTo(toMove, 9, 45, false)))))))) {
                return ItemStack.EMPTY;
            }
            if (toMove.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }
            if (toMove.getAmount() == original.getAmount()) {
                return ItemStack.EMPTY;
            }
        }
        return original;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack p_38908_, Slot p_38909_) {
        return p_38909_.inventoryStorageSlot != 0;
    }

    @Generated
    public CorrectingPlayerInventoryStorage getInventoryStorage() {
        return this.inventoryStorage;
    }
}

