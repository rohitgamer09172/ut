/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.misc;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.checks.type.BlockPlaceCheck;
import ac.grim.grimac.platform.api.world.PlatformWorld;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;

public class GhostBlockMitigation
extends BlockPlaceCheck {
    private boolean allow;
    private int distance;

    public GhostBlockMitigation(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onBlockPlace(BlockPlace place) {
        if (this.allow || this.player.platformPlayer == null) {
            return;
        }
        PlatformWorld world = this.player.platformPlayer.getWorld();
        Vector3i pos = place.getPlacedBlockPos();
        Vector3i posAgainst = place.position;
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        int xAgainst = posAgainst.getX();
        int yAgainst = posAgainst.getY();
        int zAgainst = posAgainst.getZ();
        try {
            for (int i = x - this.distance; i <= x + this.distance; ++i) {
                for (int j = y - this.distance; j <= y + this.distance; ++j) {
                    for (int k = z - this.distance; k <= z + this.distance; ++k) {
                        WrappedBlockState type;
                        if (i == x && j == y && k == z || i == xAgainst && j == yAgainst && k == zAgainst || !world.isChunkLoaded(i >> 4, k >> 4) || (type = world.getBlockAt(i, j, k)).getType().isAir()) continue;
                        return;
                    }
                }
            }
            place.resync();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @Override
    public void onReload(ConfigManager config) {
        this.allow = config.getBooleanElse("exploit.allow-building-on-ghostblocks", true);
        this.distance = config.getIntElse("exploit.distance-to-check-for-ghostblocks", 2);
        if (this.distance < 2 || this.distance > 4) {
            this.distance = 2;
        }
    }
}

