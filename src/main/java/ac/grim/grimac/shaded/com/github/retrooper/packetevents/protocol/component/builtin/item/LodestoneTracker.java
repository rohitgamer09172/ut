/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.WorldBlockPosition;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Objects;

public class LodestoneTracker {
    @Nullable
    private WorldBlockPosition target;
    private boolean tracked;

    public LodestoneTracker(@Nullable WorldBlockPosition target, boolean tracked) {
        this.target = target;
        this.tracked = tracked;
    }

    public static LodestoneTracker read(PacketWrapper<?> wrapper) {
        WorldBlockPosition target = (WorldBlockPosition)wrapper.readOptional(PacketWrapper::readWorldBlockPosition);
        boolean tracked = wrapper.readBoolean();
        return new LodestoneTracker(target, tracked);
    }

    public static void write(PacketWrapper<?> wrapper, LodestoneTracker tracker) {
        wrapper.writeOptional(tracker.target, PacketWrapper::writeWorldBlockPosition);
        wrapper.writeBoolean(tracker.tracked);
    }

    @Nullable
    public WorldBlockPosition getTarget() {
        return this.target;
    }

    public void setTarget(@Nullable WorldBlockPosition target) {
        this.target = target;
    }

    public boolean isTracked() {
        return this.tracked;
    }

    public void setTracked(boolean tracked) {
        this.tracked = tracked;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LodestoneTracker)) {
            return false;
        }
        LodestoneTracker that = (LodestoneTracker)obj;
        if (this.tracked != that.tracked) {
            return false;
        }
        return Objects.equals(this.target, that.target);
    }

    public int hashCode() {
        return Objects.hash(this.target, this.tracked);
    }
}

