/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.chunks;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;

public record Column(int x, int z, BaseChunk[] chunks, int transaction) {
    public void mergeChunks(BaseChunk[] toMerge) {
        for (int i = 0; i < 16; ++i) {
            if (toMerge[i] == null) continue;
            this.chunks[i] = toMerge[i];
        }
    }
}

