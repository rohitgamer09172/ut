/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.registry;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public interface ItemRegistry {
    @Nullable
    public ItemType getByName(String var1);

    @Nullable
    public ItemType getById(int var1);
}

