/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.breaking;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockBreakCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.utils.anticheat.update.BlockBreak;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;

@CheckData(name="PositionBreakA")
public class PositionBreakA
extends Check
implements BlockBreakCheck {
    public PositionBreakA(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onBlockBreak(BlockBreak blockBreak) {
        boolean flag;
        if (this.player.inVehicle() || blockBreak.action == DiggingAction.CANCELLED_DIGGING || blockBreak.block.getType() == StateTypes.REDSTONE_WIRE) {
            return;
        }
        SimpleCollisionBox combined = blockBreak.getCombinedBox();
        double[] possibleEyeHeights = this.player.getPossibleEyeHeights();
        double minEyeHeight = Double.MAX_VALUE;
        double maxEyeHeight = Double.MIN_VALUE;
        for (double height : possibleEyeHeights) {
            minEyeHeight = Math.min(minEyeHeight, height);
            maxEyeHeight = Math.max(maxEyeHeight, height);
        }
        SimpleCollisionBox eyePositions = new SimpleCollisionBox(this.player.x, this.player.y + minEyeHeight, this.player.z, this.player.x, this.player.y + maxEyeHeight, this.player.z);
        if (!this.player.packetStateData.didLastMovementIncludePosition || this.player.canSkipTicks()) {
            eyePositions.expand(this.player.getMovementThreshold());
        }
        if (eyePositions.isIntersected(combined)) {
            return;
        }
        switch (blockBreak.face) {
            case NORTH: {
                boolean bl;
                if (eyePositions.minZ > combined.minZ) {
                    bl = true;
                    break;
                }
                bl = false;
                break;
            }
            case SOUTH: {
                boolean bl;
                if (eyePositions.maxZ < combined.maxZ) {
                    bl = true;
                    break;
                }
                bl = false;
                break;
            }
            case EAST: {
                boolean bl;
                if (eyePositions.maxX < combined.maxX) {
                    bl = true;
                    break;
                }
                bl = false;
                break;
            }
            case WEST: {
                boolean bl;
                if (eyePositions.minX > combined.minX) {
                    bl = true;
                    break;
                }
                bl = false;
                break;
            }
            case UP: {
                boolean bl;
                if (eyePositions.maxY < combined.maxY) {
                    bl = true;
                    break;
                }
                bl = false;
                break;
            }
            case DOWN: {
                boolean bl;
                if (eyePositions.minY > combined.minY) {
                    bl = true;
                    break;
                }
                bl = false;
                break;
            }
            default: {
                boolean bl = flag = false;
            }
        }
        if (flag && this.flagAndAlert("action=" + String.valueOf((Object)blockBreak.action) + ", face=" + String.valueOf((Object)blockBreak.face)) && this.shouldModifyPackets()) {
            blockBreak.cancel();
        }
    }
}

