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
public enum WeatheringCopperState {
    UNAFFECTED,
    EXPOSED,
    WEATHERED,
    OXIDIZED;

    private static final WeatheringCopperState[] STATES;

    public static WeatheringCopperState read(PacketWrapper<?> wrapper) {
        return (WeatheringCopperState)wrapper.readEnum(STATES);
    }

    public static void write(PacketWrapper<?> wrapper, WeatheringCopperState state) {
        wrapper.writeEnum(state);
    }

    static {
        STATES = WeatheringCopperState.values();
    }
}

