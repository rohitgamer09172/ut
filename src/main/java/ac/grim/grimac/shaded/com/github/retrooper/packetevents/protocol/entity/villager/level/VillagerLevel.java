/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.villager.level;

import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public enum VillagerLevel {
    NOVICE,
    APPRENTICE,
    JOURNEYMAN,
    EXPERT,
    MASTER;

    private static final VillagerLevel[] VALUES;

    @Nullable
    public static VillagerLevel getById(int id) {
        if (id >= 1 && id <= VALUES.length) {
            return VALUES[id - 1];
        }
        return null;
    }

    public int getId() {
        return this.ordinal() + 1;
    }

    static {
        VALUES = VillagerLevel.values();
    }
}

