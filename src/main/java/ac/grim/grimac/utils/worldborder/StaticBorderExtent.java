/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.worldborder;

import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.worldborder.BorderExtent;

public record StaticBorderExtent(double size) implements BorderExtent
{
    @Override
    public double getMinX(double centerX, double absoluteMaxSize) {
        return GrimMath.clamp(centerX - this.size / 2.0, -absoluteMaxSize, absoluteMaxSize);
    }

    @Override
    public double getMaxX(double centerX, double absoluteMaxSize) {
        return GrimMath.clamp(centerX + this.size / 2.0, -absoluteMaxSize, absoluteMaxSize);
    }

    @Override
    public double getMinZ(double centerZ, double absoluteMaxSize) {
        return GrimMath.clamp(centerZ - this.size / 2.0, -absoluteMaxSize, absoluteMaxSize);
    }

    @Override
    public double getMaxZ(double centerZ, double absoluteMaxSize) {
        return GrimMath.clamp(centerZ + this.size / 2.0, -absoluteMaxSize, absoluteMaxSize);
    }

    @Override
    public BorderExtent tick() {
        return this;
    }

    @Override
    public BorderExtent update() {
        return this;
    }
}

