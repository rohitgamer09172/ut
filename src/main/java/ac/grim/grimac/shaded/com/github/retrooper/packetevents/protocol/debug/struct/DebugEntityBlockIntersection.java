/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DebugEntityBlockIntersection {
    private final IntersectionType type;

    public DebugEntityBlockIntersection(IntersectionType type) {
        this.type = type;
    }

    public static DebugEntityBlockIntersection read(PacketWrapper<?> wrapper) {
        IntersectionType type = (IntersectionType)wrapper.readEnum(IntersectionType.values());
        return new DebugEntityBlockIntersection(type);
    }

    public static void write(PacketWrapper<?> wrapper, DebugEntityBlockIntersection intersection) {
        wrapper.writeEnum(intersection.type);
    }

    public IntersectionType getType() {
        return this.type;
    }

    public static enum IntersectionType {
        IN_BLOCK,
        IN_FLUID,
        IN_AIR;

    }
}

