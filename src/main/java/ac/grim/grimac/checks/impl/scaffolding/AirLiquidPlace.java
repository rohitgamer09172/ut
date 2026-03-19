/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.scaffolding;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockPlaceCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;
import ac.grim.grimac.utils.change.BlockModification;
import ac.grim.grimac.utils.nmsutil.Materials;

@CheckData(name="AirLiquidPlace", description="Placed a block against an invalid support")
public class AirLiquidPlace
extends BlockPlaceCheck {
    public AirLiquidPlace(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onBlockPlace(BlockPlace place) {
        if (this.player.gamemode == GameMode.CREATIVE) {
            return;
        }
        Vector3i blockPos = place.position;
        StateType placeAgainst = this.player.compensatedWorld.getBlockType(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        int currentTick = GrimAPI.INSTANCE.getTickManager().currentTick;
        Iterable<BlockModification> blockModifications = this.player.blockHistory.getRecentModifications(blockModification -> currentTick - blockModification.tick() < 2 && blockPos.equals(blockModification.location()) && (blockModification.cause() == BlockModification.Cause.START_DIGGING || blockModification.cause() == BlockModification.Cause.HANDLE_NETTY_SYNC_TRANSACTION));
        for (BlockModification blockModification2 : blockModifications) {
            StateType stateType = blockModification2.oldBlockContents().getType();
            if (stateType.isAir() || Materials.isNoPlaceLiquid(stateType)) continue;
            return;
        }
        if ((placeAgainst.isAir() || Materials.isNoPlaceLiquid(placeAgainst)) && this.flagAndAlert() && this.shouldModifyPackets() && this.shouldCancel()) {
            place.resync();
        }
    }

    @Override
    public void onReload(ConfigManager config) {
        this.cancelVL = config.getIntElse(this.getConfigName() + ".cancelVL", 0);
    }
}

