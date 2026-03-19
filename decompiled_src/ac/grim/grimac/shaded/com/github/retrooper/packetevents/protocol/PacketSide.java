/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol;

public enum PacketSide {
    CLIENT,
    SERVER;


    public PacketSide getOpposite() {
        if (this == CLIENT) {
            return SERVER;
        }
        return CLIENT;
    }
}

