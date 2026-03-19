/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.scaffolding;

import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockPlaceCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;

@CheckData(name="PositionPlace", description="Placed a block against a hidden face")
public class PositionPlace
extends BlockPlaceCheck {
    public PositionPlace(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onBlockPlace(BlockPlace place) {
        boolean flag;
        if (place.material == StateTypes.SCAFFOLDING || this.player.inVehicle()) {
            return;
        }
        SimpleCollisionBox combined = this.getCombinedBox(place);
        double[] possibleEyeHeights = this.player.getPossibleEyeHeights();
        double minEyeHeight = Double.MAX_VALUE;
        double maxEyeHeight = Double.MIN_VALUE;
        for (double height : possibleEyeHeights) {
            minEyeHeight = Math.min(minEyeHeight, height);
            maxEyeHeight = Math.max(maxEyeHeight, height);
        }
        double movementThreshold = !this.player.packetStateData.didLastMovementIncludePosition || this.player.canSkipTicks() ? this.player.getMovementThreshold() : 0.0;
        SimpleCollisionBox eyePositions = new SimpleCollisionBox(this.player.x, this.player.y + minEyeHeight, this.player.z, this.player.x, this.player.y + maxEyeHeight, this.player.z);
        eyePositions.expand(movementThreshold);
        if (eyePositions.isIntersected(combined)) {
            return;
        }
        switch (place.getFace()) {
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
        if (flag && this.flagAndAlert() && this.shouldModifyPackets() && this.shouldCancel()) {
            place.resync();
        }
    }
}

