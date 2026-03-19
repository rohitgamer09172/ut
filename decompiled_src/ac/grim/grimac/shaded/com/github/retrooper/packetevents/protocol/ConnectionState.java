/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol;

public enum ConnectionState {
    HANDSHAKING,
    STATUS,
    LOGIN,
    PLAY,
    CONFIGURATION;


    public static ConnectionState getById(int id) {
        if (id >= ConnectionState.values().length || id < 0) {
            return null;
        }
        return ConnectionState.values()[id];
    }
}

