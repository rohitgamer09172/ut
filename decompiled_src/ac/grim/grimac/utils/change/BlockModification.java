/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.change;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public record BlockModification(WrappedBlockState oldBlockContents, WrappedBlockState newBlockContents, Vector3i location, int tick, Cause cause) {
    @Override
    @NotNull
    public String toString() {
        return String.format("BlockModification{location=%s, old=%s, new=%s, tick=%d, cause=%s}", new Object[]{this.location, this.oldBlockContents, this.newBlockContents, this.tick, this.cause});
    }

    public static enum Cause {
        START_DIGGING,
        APPLY_BLOCK_CHANGES,
        HANDLE_NETTY_SYNC_TRANSACTION,
        OTHER;

    }
}

