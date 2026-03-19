/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  org.checkerframework.checker.nullness.qual.NonNull
 */
package ac.grim.grimac.shaded.incendo.cloud.bukkit.data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface ProtoItemStack {
    public @NonNull Material material();

    public boolean hasExtraData();

    public @NonNull ItemStack createItemStack(int var1, boolean var2) throws IllegalArgumentException;
}

