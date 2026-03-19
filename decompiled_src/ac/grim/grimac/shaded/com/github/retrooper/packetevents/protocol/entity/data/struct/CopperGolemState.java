/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data.struct;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public enum CopperGolemState {
    IDLE,
    GETTING_ITEM,
    GETTING_NO_ITEM,
    DROPPING_ITEM,
    DROPPING_NO_ITEM;

    private static final CopperGolemState[] STATES;

    public static CopperGolemState read(PacketWrapper<?> wrapper) {
        return (CopperGolemState)wrapper.readEnum(STATES);
    }

    public static void write(PacketWrapper<?> wrapper, CopperGolemState state) {
        wrapper.writeEnum(state);
    }

    static {
        STATES = CopperGolemState.values();
    }
}

