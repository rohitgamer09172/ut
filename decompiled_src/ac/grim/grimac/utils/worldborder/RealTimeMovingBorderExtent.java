/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.worldborder;

import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.worldborder.BorderExtent;
import ac.grim.grimac.utils.worldborder.StaticBorderExtent;

public class RealTimeMovingBorderExtent
implements BorderExtent {
    private final double from;
    private final double to;
    private final long startTime;
    private final long endTime;

    public RealTimeMovingBorderExtent(double from, double to, long durationMs) {
        this.from = from;
        this.to = to;
        this.startTime = System.currentTimeMillis();
        this.endTime = this.startTime + durationMs;
    }

    @Override
    public double size() {
        long now = System.currentTimeMillis();
        if (now >= this.endTime) {
            return this.to;
        }
        double progress = (double)(now - this.startTime) / (double)(this.endTime - this.startTime);
        return progress < 1.0 ? GrimMath.lerp(progress, this.from, this.to) : this.to;
    }

    @Override
    public double getMinX(double centerX, double absoluteMaxSize) {
        return GrimMath.clamp(centerX - this.size() / 2.0, -absoluteMaxSize, absoluteMaxSize);
    }

    @Override
    public double getMaxX(double centerX, double absoluteMaxSize) {
        return GrimMath.clamp(centerX + this.size() / 2.0, -absoluteMaxSize, absoluteMaxSize);
    }

    @Override
    public double getMinZ(double centerZ, double absoluteMaxSize) {
        return GrimMath.clamp(centerZ - this.size() / 2.0, -absoluteMaxSize, absoluteMaxSize);
    }

    @Override
    public double getMaxZ(double centerZ, double absoluteMaxSize) {
        return GrimMath.clamp(centerZ + this.size() / 2.0, -absoluteMaxSize, absoluteMaxSize);
    }

    @Override
    public BorderExtent tick() {
        return this.update();
    }

    @Override
    public BorderExtent update() {
        if (System.currentTimeMillis() >= this.endTime) {
            return new StaticBorderExtent(this.to);
        }
        return this;
    }
}

