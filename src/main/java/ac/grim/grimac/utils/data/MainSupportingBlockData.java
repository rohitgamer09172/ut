/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public record MainSupportingBlockData(@Nullable Vector3i blockPos, boolean onGround) {
    @Contract(pure=true)
    public boolean lastOnGroundAndNoBlock() {
        return this.blockPos == null && this.onGround;
    }
}

