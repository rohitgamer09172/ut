/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.book;

public enum BookType {
    CRAFTING,
    FURNACE,
    BLAST_FURNACE,
    SMOKER;

    private static final BookType[] VALUES;

    public int getId() {
        return this.ordinal();
    }

    public static BookType getById(int id) {
        return VALUES[id];
    }

    static {
        VALUES = BookType.values();
    }
}

