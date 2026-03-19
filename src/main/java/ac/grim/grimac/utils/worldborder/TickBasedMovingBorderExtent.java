/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.worldborder;

import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.worldborder.BorderExtent;
import ac.grim.grimac.utils.worldborder.StaticBorderExtent;

public class TickBasedMovingBorderExtent
implements BorderExtent {
    private final double from;
    private final double to;
    private final long lerpDuration;
    private long lerpProgress;
    private double size;
    private double previousSize;

    public TickBasedMovingBorderExtent(double from, double to, long durationTicks) {
        this.from = from;
        this.to = to;
        this.lerpDuration = durationTicks;
        this.lerpProgress = durationTicks;
        this.previousSize = this.size = this.calculateSize();
    }

    private double calculateSize() {
        if (this.lerpDuration <= 0L) {
            return this.to;
        }
        double progress = (double)(this.lerpDuration - this.lerpProgress) / (double)this.lerpDuration;
        return progress < 1.0 ? GrimMath.lerp(progress, this.from, this.to) : this.to;
    }

    @Override
    public double size() {
        return this.size;
    }

    @Override
    public double getMinX(double centerX, double absoluteMaxSize) {
        return GrimMath.clamp(centerX - this.previousSize / 2.0, -absoluteMaxSize, absoluteMaxSize);
    }

    @Override
    public double getMaxX(double centerX, double absoluteMaxSize) {
        return GrimMath.clamp(centerX + this.previousSize / 2.0, -absoluteMaxSize, absoluteMaxSize);
    }

    @Override
    public double getMinZ(double centerZ, double absoluteMaxSize) {
        return GrimMath.clamp(centerZ - this.previousSize / 2.0, -absoluteMaxSize, absoluteMaxSize);
    }

    @Override
    public double getMaxZ(double centerZ, double absoluteMaxSize) {
        return GrimMath.clamp(centerZ + this.previousSize / 2.0, -absoluteMaxSize, absoluteMaxSize);
    }

    @Override
    public BorderExtent tick() {
        if (this.lerpProgress > 0L) {
            --this.lerpProgress;
            this.previousSize = this.size;
            this.size = this.calculateSize();
        }
        return this.update();
    }

    @Override
    public BorderExtent update() {
        if (this.lerpProgress <= 0L) {
            return new StaticBorderExtent(this.to);
        }
        return this;
    }
}

