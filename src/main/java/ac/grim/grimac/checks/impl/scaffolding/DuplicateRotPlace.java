/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.scaffolding;

import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockPlaceCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;
import ac.grim.grimac.utils.anticheat.update.RotationUpdate;

@CheckData(name="DuplicateRotPlace", experimental=true)
public class DuplicateRotPlace
extends BlockPlaceCheck {
    private float deltaX;
    private float deltaY;
    private float lastPlacedDeltaX;
    private double lastPlacedDeltaDotsX;
    private double deltaDotsX;
    private boolean rotated = false;

    public DuplicateRotPlace(GrimPlayer player) {
        super(player);
    }

    @Override
    public void process(RotationUpdate rotationUpdate) {
        this.deltaX = rotationUpdate.getDeltaXRotABS();
        this.deltaY = rotationUpdate.getDeltaYRotABS();
        this.deltaDotsX = rotationUpdate.getProcessor().deltaDotsX;
        this.rotated = true;
    }

    @Override
    public void onPostFlyingBlockPlace(BlockPlace place) {
        if (this.rotated && !this.player.inVehicle()) {
            if (this.deltaX > 2.0f) {
                float xDiff = Math.abs(this.deltaX - this.lastPlacedDeltaX);
                double xDiffDots = Math.abs(this.deltaDotsX - this.lastPlacedDeltaDotsX);
                if ((double)xDiff < 1.0E-4) {
                    this.flagAndAlert("x=" + xDiff + " xdots=" + xDiffDots + " y=" + this.deltaY);
                } else {
                    this.reward();
                }
            } else {
                this.reward();
            }
            this.lastPlacedDeltaX = this.deltaX;
            this.lastPlacedDeltaDotsX = this.deltaDotsX;
            this.rotated = false;
        }
    }
}

