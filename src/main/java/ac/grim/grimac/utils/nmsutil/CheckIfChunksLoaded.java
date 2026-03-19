/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.player.GrimPlayer;
import lombok.Generated;

public final class CheckIfChunksLoaded {
    public static boolean areChunksUnloadedAt(GrimPlayer player, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        if (maxY < player.compensatedWorld.getMinHeight() || minY >= player.compensatedWorld.getMaxHeight()) {
            return true;
        }
        minZ >>= 4;
        maxX >>= 4;
        maxZ >>= 4;
        for (int i = minX >>= 4; i <= maxX; ++i) {
            for (int j = minZ; j <= maxZ; ++j) {
                if (player.compensatedWorld.getChunk(i, j) != null) continue;
                return true;
            }
        }
        return false;
    }

    @Generated
    private CheckIfChunksLoaded() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

