/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  javax.annotation.Nullable
 *  lombok.Generated
 */
package ac.grim.grimac.utils.inventory.inventory;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.inventory.ClickAction;
import ac.grim.grimac.utils.inventory.Inventory;
import ac.grim.grimac.utils.inventory.slot.ResultSlot;
import ac.grim.grimac.utils.inventory.slot.Slot;
import ac.grim.grimac.utils.math.GrimMath;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;
import lombok.Generated;

public abstract class AbstractContainerMenu {
    protected final GrimPlayer player;
    private int quickcraftStatus = 0;
    private int quickcraftType = -1;
    private final Set<Slot> quickcraftSlots = Sets.newHashSet();
    private Inventory playerInventory;
    protected final List<Slot> slots = new ArrayList<Slot>();
    @NotNull
    private ItemStack carriedItem = ItemStack.EMPTY;

    public AbstractContainerMenu(GrimPlayer player, Inventory playerInventory) {
        this.player = player;
        this.playerInventory = playerInventory;
    }

    public AbstractContainerMenu(GrimPlayer player) {
        this.player = player;
    }

    public static int calculateQuickcraftHeader(int p_38948_) {
        return p_38948_ & 3;
    }

    public static int calculateQuickcraftMask(int p_38931_, int p_38932_) {
        return p_38931_ & 3 | (p_38932_ & 3) << 2;
    }

    public static int calculateQuickcraftType(int p_38929_) {
        return p_38929_ >> 2 & 3;
    }

    public static boolean canItemQuickReplace(@Nullable Slot p_38900_, ItemStack p_38901_, boolean p_38902_) {
        boolean flag;
        boolean bl = flag = p_38900_ == null || !p_38900_.hasItem();
        if (!flag && ItemStack.isSameItemSameTags(p_38901_, p_38900_.getItem())) {
            return p_38900_.getItem().getAmount() + (p_38902_ ? 0 : p_38901_.getAmount()) <= p_38901_.getMaxStackSize();
        }
        return flag;
    }

    public static void getQuickCraftSlotCount(Set<Slot> p_38923_, int p_38924_, ItemStack p_38925_, int p_38926_) {
        switch (p_38924_) {
            case 0: {
                p_38925_.setAmount(GrimMath.floor((float)p_38925_.getAmount() / (float)p_38923_.size()));
                break;
            }
            case 1: {
                p_38925_.setAmount(1);
                break;
            }
            case 2: {
                p_38925_.setAmount(p_38925_.getType().getMaxAmount());
            }
        }
        p_38925_.grow(p_38926_);
    }

    public Slot addSlot(Slot slot) {
        slot.slotListIndex = this.slots.size();
        this.slots.add(slot);
        return slot;
    }

    public void addFourRowPlayerInventory() {
        for (int slot = 9; slot < 45; ++slot) {
            this.addSlot(new Slot(this.playerInventory.getInventoryStorage(), slot));
        }
    }

    protected void resetQuickCraft() {
        this.quickcraftStatus = 0;
        this.quickcraftSlots.clear();
    }

    public boolean isValidQuickcraftType(int p_38863_) {
        if (p_38863_ == 0) {
            return true;
        }
        if (p_38863_ == 1) {
            return true;
        }
        return p_38863_ == 2 && this.player.gamemode == GameMode.CREATIVE;
    }

    public ItemStack getCarried() {
        return this.getCarriedItem();
    }

    public void setCarried(ItemStack stack) {
        this.carriedItem = stack == null ? ItemStack.EMPTY : stack;
    }

    public ItemStack getPlayerInventoryItem(int slot) {
        return this.playerInventory.getInventoryStorage().getItem(slot);
    }

    public void setPlayerInventoryItem(int slot, ItemStack stack) {
        this.playerInventory.getInventoryStorage().setItem(slot, stack);
    }

    public void doClick(int button, int slotID, WrapperPlayClientClickWindow.WindowClickType clickType) {
        block29: {
            block44: {
                block41: {
                    ItemStack hoveringItem2;
                    ItemStack hotbarKeyStack;
                    Slot hoveringSlot;
                    block43: {
                        block42: {
                            block34: {
                                ItemStack carriedItem;
                                ItemStack slotItem;
                                Slot slot;
                                block39: {
                                    block40: {
                                        ClickAction clickAction;
                                        block38: {
                                            block37: {
                                                block36: {
                                                    block35: {
                                                        block33: {
                                                            block27: {
                                                                block32: {
                                                                    ItemStack itemstack;
                                                                    block31: {
                                                                        block30: {
                                                                            block28: {
                                                                                if (clickType != WrapperPlayClientClickWindow.WindowClickType.QUICK_CRAFT) break block27;
                                                                                int i = this.quickcraftStatus;
                                                                                this.quickcraftStatus = AbstractContainerMenu.calculateQuickcraftHeader(button);
                                                                                if (i == 1 && this.quickcraftStatus == 2 || i == this.quickcraftStatus) break block28;
                                                                                this.resetQuickCraft();
                                                                                break block29;
                                                                            }
                                                                            if (!this.getCarried().isEmpty()) break block30;
                                                                            this.resetQuickCraft();
                                                                            break block29;
                                                                        }
                                                                        if (this.quickcraftStatus != 0) break block31;
                                                                        this.quickcraftType = AbstractContainerMenu.calculateQuickcraftType(button);
                                                                        if (this.isValidQuickcraftType(this.quickcraftType)) {
                                                                            this.quickcraftStatus = 1;
                                                                            this.quickcraftSlots.clear();
                                                                        } else {
                                                                            this.resetQuickCraft();
                                                                        }
                                                                        break block29;
                                                                    }
                                                                    if (this.quickcraftStatus != 1) break block32;
                                                                    if (slotID < 0) {
                                                                        return;
                                                                    }
                                                                    Slot slot2 = this.slots.get(slotID);
                                                                    if (!AbstractContainerMenu.canItemQuickReplace(slot2, itemstack = this.getCarried(), true) || !slot2.mayPlace(itemstack) || this.quickcraftType != 2 && itemstack.getAmount() <= this.quickcraftSlots.size() || !this.canDragTo(slot2)) break block29;
                                                                    this.quickcraftSlots.add(slot2);
                                                                    break block29;
                                                                }
                                                                if (this.quickcraftStatus == 2) {
                                                                    if (!this.quickcraftSlots.isEmpty()) {
                                                                        if (this.quickcraftSlots.size() == 1) {
                                                                            int l = this.quickcraftSlots.iterator().next().slotListIndex;
                                                                            this.resetQuickCraft();
                                                                            this.doClick(this.quickcraftType, l, WrapperPlayClientClickWindow.WindowClickType.PICKUP);
                                                                            return;
                                                                        }
                                                                        ItemStack itemstack3 = this.getCarried().copy();
                                                                        int j1 = this.getCarried().getAmount();
                                                                        for (Slot slot1 : this.quickcraftSlots) {
                                                                            ItemStack itemstack1 = this.getCarried();
                                                                            if (slot1 == null || !AbstractContainerMenu.canItemQuickReplace(slot1, itemstack1, true) || !slot1.mayPlace(itemstack1) || this.quickcraftType != 2 && itemstack1.getAmount() < this.quickcraftSlots.size() || !this.canDragTo(slot1)) continue;
                                                                            ItemStack itemstack2 = itemstack3.copy();
                                                                            int j = slot1.hasItem() ? slot1.getItem().getAmount() : 0;
                                                                            AbstractContainerMenu.getQuickCraftSlotCount(this.quickcraftSlots, this.quickcraftType, itemstack2, j);
                                                                            int k = Math.min(itemstack2.getMaxStackSize(), slot1.getMaxStackSize(itemstack2));
                                                                            if (itemstack2.getAmount() > k) {
                                                                                itemstack2.setAmount(k);
                                                                            }
                                                                            j1 -= itemstack2.getAmount() - j;
                                                                            slot1.set(itemstack2);
                                                                        }
                                                                        itemstack3.setAmount(j1);
                                                                        this.setCarried(itemstack3);
                                                                    }
                                                                    this.resetQuickCraft();
                                                                } else {
                                                                    this.resetQuickCraft();
                                                                }
                                                                break block29;
                                                            }
                                                            if (this.quickcraftStatus == 0) break block33;
                                                            this.resetQuickCraft();
                                                            break block29;
                                                        }
                                                        if (clickType != WrapperPlayClientClickWindow.WindowClickType.PICKUP && clickType != WrapperPlayClientClickWindow.WindowClickType.QUICK_MOVE || button != 0 && button != 1) break block34;
                                                        clickAction = ClickAction.values()[button];
                                                        if (slotID != -999) break block35;
                                                        if (this.getCarried().isEmpty()) break block29;
                                                        if (clickAction == ClickAction.PRIMARY) {
                                                            this.setCarried(ItemStack.EMPTY);
                                                        } else {
                                                            this.getCarried().split(1);
                                                        }
                                                        break block29;
                                                    }
                                                    if (clickType != WrapperPlayClientClickWindow.WindowClickType.QUICK_MOVE) break block36;
                                                    if (slotID < 0) {
                                                        return;
                                                    }
                                                    Slot stack = this.getSlot(slotID);
                                                    if (!stack.mayPickup()) {
                                                        return;
                                                    }
                                                    ItemStack itemstack9 = this.quickMoveStack(slotID);
                                                    while (!itemstack9.isEmpty() && ItemStack.isSameItemSameTags(stack.getItem(), itemstack9)) {
                                                        itemstack9 = this.quickMoveStack(slotID);
                                                    }
                                                    break block29;
                                                }
                                                if (slotID < 0) {
                                                    return;
                                                }
                                                slot = this.getSlot(slotID);
                                                slotItem = slot.getItem();
                                                carriedItem = this.getCarried();
                                                if (slot instanceof ResultSlot) {
                                                    this.player.inventory.isPacketInventoryActive = false;
                                                }
                                                if (!slotItem.isEmpty()) break block37;
                                                if (carriedItem.isEmpty()) break block29;
                                                int l2 = clickAction == ClickAction.PRIMARY ? carriedItem.getAmount() : 1;
                                                this.setCarried(slot.safeInsert(carriedItem, l2));
                                                break block29;
                                            }
                                            if (!slot.mayPickup()) break block29;
                                            if (!carriedItem.isEmpty()) break block38;
                                            int i3 = clickAction == ClickAction.PRIMARY ? slotItem.getAmount() : (slotItem.getAmount() + 1) / 2;
                                            Optional<ItemStack> optional1 = slot.tryRemove(i3, Integer.MAX_VALUE, this.player);
                                            optional1.ifPresent(p_150421_ -> {
                                                this.setCarried((ItemStack)p_150421_);
                                                slot.onTake(this.player, (ItemStack)p_150421_);
                                            });
                                            break block29;
                                        }
                                        if (!slot.mayPlace(carriedItem)) break block39;
                                        if (!ItemStack.isSameItemSameTags(slotItem, carriedItem)) break block40;
                                        int j3 = clickAction == ClickAction.PRIMARY ? carriedItem.getAmount() : 1;
                                        this.setCarried(slot.safeInsert(carriedItem, j3));
                                        break block29;
                                    }
                                    if (carriedItem.getAmount() > slot.getMaxStackSize(carriedItem)) break block29;
                                    slot.set(carriedItem);
                                    this.setCarried(slotItem);
                                    break block29;
                                }
                                if (!ItemStack.isSameItemSameTags(slotItem, carriedItem)) break block29;
                                Optional<ItemStack> optional = slot.tryRemove(slotItem.getAmount(), carriedItem.getMaxStackSize() - carriedItem.getAmount(), this.player);
                                optional.ifPresent(p_150428_ -> {
                                    carriedItem.grow(p_150428_.getAmount());
                                    slot.onTake(this.player, (ItemStack)p_150428_);
                                });
                                break block29;
                            }
                            if (clickType != WrapperPlayClientClickWindow.WindowClickType.SWAP) break block41;
                            hoveringSlot = this.slots.get(slotID);
                            if (button != 40 && (button < 0 || button >= 9)) {
                                return;
                            }
                            button = button == 40 ? 45 : button + 36;
                            hotbarKeyStack = this.getPlayerInventoryItem(button);
                            hoveringItem2 = hoveringSlot.getItem();
                            if (hotbarKeyStack.isEmpty() && hoveringItem2.isEmpty()) break block29;
                            if (!hotbarKeyStack.isEmpty()) break block42;
                            if (!hoveringSlot.mayPickup(this.player)) break block29;
                            this.setPlayerInventoryItem(button, hoveringItem2);
                            hoveringSlot.set(ItemStack.EMPTY);
                            hoveringSlot.onTake(this.player, hoveringItem2);
                            break block29;
                        }
                        if (!hoveringItem2.isEmpty()) break block43;
                        if (!hoveringSlot.mayPlace(hotbarKeyStack)) break block29;
                        int l1 = hoveringSlot.getMaxStackSize(hotbarKeyStack);
                        if (hotbarKeyStack.getAmount() > l1) {
                            hoveringSlot.set(hotbarKeyStack.split(l1));
                        } else {
                            hoveringSlot.set(hotbarKeyStack);
                            this.setPlayerInventoryItem(button, ItemStack.EMPTY);
                        }
                        break block29;
                    }
                    if (!hoveringSlot.mayPickup(this.player) || !hoveringSlot.mayPlace(hotbarKeyStack)) break block29;
                    int i2 = hoveringSlot.getMaxStackSize(hotbarKeyStack);
                    if (hotbarKeyStack.getAmount() > i2) {
                        hoveringSlot.set(hotbarKeyStack.split(i2));
                        hoveringSlot.onTake(this.player, hoveringItem2);
                        this.playerInventory.add(hoveringItem2);
                    } else {
                        hoveringSlot.set(hotbarKeyStack);
                        this.setPlayerInventoryItem(button, hoveringItem2);
                        hoveringSlot.onTake(this.player, hoveringItem2);
                    }
                    break block29;
                }
                if (clickType != WrapperPlayClientClickWindow.WindowClickType.CLONE || this.player.gamemode != GameMode.CREATIVE || slotID < 0 || !this.carriedItem.isEmpty()) break block44;
                Slot slot5 = this.getSlot(slotID);
                if (!slot5.hasItem()) break block29;
                ItemStack itemstack6 = slot5.getItem().copy();
                itemstack6.setAmount(itemstack6.getMaxStackSize());
                this.setCarried(itemstack6);
                break block29;
            }
            if (clickType == WrapperPlayClientClickWindow.WindowClickType.THROW && this.getCarried().isEmpty() && slotID >= 0) {
                Slot slot4 = this.getSlot(slotID);
                int i1 = button == 0 ? 1 : slot4.getItem().getAmount();
                ItemStack hoveringItem2 = slot4.safeTake(i1, Integer.MAX_VALUE, this.player);
            } else if (clickType == WrapperPlayClientClickWindow.WindowClickType.PICKUP_ALL && slotID >= 0) {
                Slot slot3 = this.getSlot(slotID);
                if (!(this.getCarried().isEmpty() || slot3.hasItem() && slot3.mayPickup(this.player))) {
                    int k1 = button == 0 ? 0 : this.slots.size() - 1;
                    int j2 = button == 0 ? 1 : -1;
                    for (int k2 = 0; k2 < 2; ++k2) {
                        for (int k3 = k1; k3 >= 0 && k3 < this.slots.size() && this.getCarried().getAmount() < this.getCarried().getMaxStackSize(); k3 += j2) {
                            Slot slot8 = this.slots.get(k3);
                            if (!slot8.hasItem() || !AbstractContainerMenu.canItemQuickReplace(slot8, this.getCarried(), true) || !slot8.mayPickup(this.player) || !this.canTakeItemForPickAll(this.getCarried(), slot8)) continue;
                            ItemStack itemstack12 = slot8.getItem();
                            if (k2 == 0 && itemstack12.getAmount() == itemstack12.getMaxStackSize()) continue;
                            ItemStack itemstack13 = slot8.safeTake(itemstack12.getAmount(), this.getCarried().getMaxStackSize() - this.getCarried().getAmount(), this.player);
                            this.getCarried().grow(itemstack13.getAmount());
                        }
                    }
                }
            }
        }
    }

    protected boolean moveItemStackTo(ItemStack toMove, int min, int max, boolean reverse) {
        boolean flag = false;
        int i = min;
        if (reverse) {
            i = max - 1;
        }
        if (toMove.getType().getMaxAmount() > 1) {
            while (!toMove.isEmpty() && !(!reverse ? i >= max : i < min)) {
                Slot slot = this.slots.get(i);
                ItemStack itemstack = slot.getItem();
                if (!itemstack.isEmpty() && ItemStack.isSameItemSameTags(toMove, itemstack)) {
                    int j = itemstack.getAmount() + toMove.getAmount();
                    if (j <= toMove.getMaxStackSize()) {
                        toMove.setAmount(0);
                        itemstack.setAmount(j);
                        flag = true;
                    } else if (itemstack.getAmount() < toMove.getMaxStackSize()) {
                        toMove.shrink(toMove.getMaxStackSize() - itemstack.getAmount());
                        itemstack.setAmount(toMove.getMaxStackSize());
                        flag = true;
                    }
                }
                if (reverse) {
                    --i;
                    continue;
                }
                ++i;
            }
        }
        if (!toMove.isEmpty()) {
            i = reverse ? max - 1 : min;
            while (!(!reverse ? i >= max : i < min)) {
                Slot slot1 = this.slots.get(i);
                ItemStack itemstack1 = slot1.getItem();
                if (itemstack1.isEmpty() && slot1.mayPlace(toMove)) {
                    if (toMove.getAmount() > slot1.getMaxStackSize()) {
                        slot1.set(toMove.split(slot1.getMaxStackSize()));
                    } else {
                        slot1.set(toMove.split(toMove.getAmount()));
                    }
                    flag = true;
                    break;
                }
                if (reverse) {
                    --i;
                    continue;
                }
                ++i;
            }
        }
        return flag;
    }

    public boolean canTakeItemForPickAll(ItemStack p_38908_, Slot p_38909_) {
        return true;
    }

    public ItemStack quickMoveStack(int slotID) {
        return this.slots.get(slotID).getItem();
    }

    public Slot getSlot(int slotID) {
        try {
            return this.slots.get(slotID);
        }
        catch (IndexOutOfBoundsException e) {
            LogUtil.error("Tried to get slot " + slotID + " in a container with only " + this.slots.size() + " slots, container type: " + this.getClass().getName(), e);
            throw e;
        }
    }

    public boolean canDragTo(Slot slot) {
        return true;
    }

    public int getMaxStackSize() {
        return 64;
    }

    @Generated
    protected void setPlayerInventory(Inventory playerInventory) {
        this.playerInventory = playerInventory;
    }

    @Generated
    public List<Slot> getSlots() {
        return this.slots;
    }

    @NotNull
    @Generated
    public ItemStack getCarriedItem() {
        return this.carriedItem;
    }
}

