/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.filter;

public enum FilterMaskType {
    PASS_THROUGH,
    FULLY_FILTERED,
    PARTIALLY_FILTERED;

    public static final FilterMaskType[] VALUES;

    public int getId() {
        return this.ordinal();
    }

    public static FilterMaskType getById(int id) {
        return VALUES[id];
    }

    static {
        VALUES = FilterMaskType.values();
    }
}

