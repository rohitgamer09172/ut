/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.breaking;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockBreakCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.anticheat.update.BlockBreak;

@CheckData(name="AirLiquidBreak", description="Breaking a block that cannot be broken")
public class AirLiquidBreak
extends Check
implements BlockBreakCheck {
    public final boolean noFireHitbox;
    private int lastTick;
    private boolean didLastFlag;
    @NotNull
    private Vector3i lastBreakLoc;
    @NotNull
    private StateType lastBlockType;

    public AirLiquidBreak(GrimPlayer player) {
        super(player);
        this.noFireHitbox = this.player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_15_2);
        this.lastBreakLoc = new Vector3i();
        this.lastBlockType = StateTypes.AIR;
    }

    @Override
    public void onBlockBreak(BlockBreak blockBreak) {
        boolean invalid;
        if (blockBreak.action != DiggingAction.START_DIGGING && blockBreak.action != DiggingAction.FINISHED_DIGGING) {
            return;
        }
        StateType block = blockBreak.block.getType();
        int newTick = GrimAPI.INSTANCE.getTickManager().currentTick;
        if (this.lastTick == newTick && this.lastBreakLoc.equals(blockBreak.position) && !this.didLastFlag && this.lastBlockType.getHardness() == 0.0f && this.lastBlockType.getBlastResistance() == 0.0f && block == StateTypes.WATER) {
            return;
        }
        this.lastTick = newTick;
        this.lastBreakLoc = blockBreak.position;
        this.lastBlockType = block;
        boolean bl = invalid = block == StateTypes.LIGHT && !this.player.inventory.getHeldItem().is(ItemTypes.LIGHT) && !this.player.inventory.getOffHand().is(ItemTypes.LIGHT) || block.isAir() || block == StateTypes.WATER || block == StateTypes.LAVA || block == StateTypes.BUBBLE_COLUMN || block == StateTypes.MOVING_PISTON || block == StateTypes.FIRE && this.noFireHitbox || block.getHardness() == -1.0f && blockBreak.action == DiggingAction.FINISHED_DIGGING;
        if (invalid && this.flagAndAlert("block=" + block.getName() + ", type=" + String.valueOf((Object)blockBreak.action)) && this.shouldModifyPackets()) {
            this.didLastFlag = true;
            blockBreak.cancel();
        } else {
            this.didLastFlag = false;
        }
    }
}

