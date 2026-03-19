/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.platform.api.player;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;

public interface PlatformInventory {
    public ItemStack getItemInHand();

    public ItemStack getItemInOffHand();

    public ItemStack getStack(int var1, int var2);

    public ItemStack getHelmet();

    public ItemStack getChestplate();

    public ItemStack getLeggings();

    public ItemStack getBoots();

    public ItemStack[] getContents();

    public String getOpenInventoryKey();
}

