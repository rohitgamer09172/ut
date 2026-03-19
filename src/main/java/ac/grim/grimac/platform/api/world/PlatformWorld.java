/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.platform.api.world;

import ac.grim.grimac.platform.api.world.PlatformChunk;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.UUID;

public interface PlatformWorld {
    public boolean isChunkLoaded(int var1, int var2);

    public WrappedBlockState getBlockAt(int var1, int var2, int var3);

    public String getName();

    @Nullable
    public UUID getUID();

    public PlatformChunk getChunkAt(int var1, int var2);

    public boolean isLoaded();
}

