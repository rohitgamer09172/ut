/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Chunk
 *  org.bukkit.block.Block
 *  org.bukkit.block.data.BlockData
 */
package ac.grim.grimac.platform.bukkit.world;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.platform.api.Platform;
import ac.grim.grimac.platform.api.world.PlatformChunk;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

public class BukkitPlatformChunk
implements PlatformChunk {
    private static final Map<BlockData, Integer> blockDataToId = GrimAPI.INSTANCE.getPlatform() == Platform.FOLIA ? new ConcurrentHashMap() : new HashMap();
    private static final boolean isFlat = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13);
    @NotNull
    private final Chunk chunk;

    @Override
    public int getBlockID(int x, int y, int z) {
        Block block = this.chunk.getBlock(x, y, z);
        return isFlat ? blockDataToId.computeIfAbsent(block.getBlockData(), data -> WrappedBlockState.getByString(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), data.getAsString(false)).getGlobalId()) : BukkitPlatformChunk.getLegacyBlockID(block);
    }

    private static int getLegacyBlockID(@NotNull Block block) {
        return block.getType().getId() << 4 | block.getData();
    }

    @Generated
    public BukkitPlatformChunk(@NotNull Chunk chunk) {
        if (chunk == null) {
            throw new NullPointerException("chunk is marked non-null but is null");
        }
        this.chunk = chunk;
    }
}

