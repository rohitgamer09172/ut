/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.event;

public enum PacketListenerPriority {
    LOWEST,
    LOW,
    NORMAL,
    HIGH,
    HIGHEST,
    MONITOR;


    public static PacketListenerPriority getById(byte id) {
        return PacketListenerPriority.values()[id];
    }

    public byte getId() {
        return (byte)this.ordinal();
    }
}

