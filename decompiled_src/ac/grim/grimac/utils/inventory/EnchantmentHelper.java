/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.inventory;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentType;
import ac.grim.grimac.utils.latency.CompensatedInventory;

public class EnchantmentHelper {
    public static int getMaximumEnchantLevel(CompensatedInventory inventory, EnchantmentType enchantmentType) {
        ItemStack boots;
        ItemStack leggings;
        ItemStack chestplate;
        int maxEnchantLevel = 0;
        ItemStack helmet = inventory.getHelmet();
        if (helmet != ItemStack.EMPTY) {
            maxEnchantLevel = Math.max(maxEnchantLevel, helmet.getEnchantmentLevel(enchantmentType));
        }
        if ((chestplate = inventory.getChestplate()) != ItemStack.EMPTY) {
            maxEnchantLevel = Math.max(maxEnchantLevel, chestplate.getEnchantmentLevel(enchantmentType));
        }
        if ((leggings = inventory.getLeggings()) != ItemStack.EMPTY) {
            maxEnchantLevel = Math.max(maxEnchantLevel, leggings.getEnchantmentLevel(enchantmentType));
        }
        if ((boots = inventory.getBoots()) != ItemStack.EMPTY) {
            maxEnchantLevel = Math.max(maxEnchantLevel, boots.getEnchantmentLevel(enchantmentType));
        }
        return maxEnchantLevel;
    }
}

