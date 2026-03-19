/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.aim.processor;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.RotationCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.RotationUpdate;
import ac.grim.grimac.utils.data.Pair;
import ac.grim.grimac.utils.lists.RunningMode;
import ac.grim.grimac.utils.math.GrimMath;

public class AimProcessor
extends Check
implements RotationCheck {
    private static final int SIGNIFICANT_SAMPLES_THRESHOLD = 15;
    private static final int TOTAL_SAMPLES_THRESHOLD = 80;
    public double sensitivityX;
    public double sensitivityY;
    public double divisorX;
    public double divisorY;
    public double modeX;
    public double modeY;
    public double deltaDotsX;
    public double deltaDotsY;
    private final RunningMode xRotMode = new RunningMode(80);
    private final RunningMode yRotMode = new RunningMode(80);
    private float lastXRot;
    private float lastYRot;

    public AimProcessor(GrimPlayer playerData) {
        super(playerData);
    }

    public static double convertToSensitivity(double var13) {
        double var11 = var13 / (double)0.15f / 8.0;
        double var9 = Math.cbrt(var11);
        return (var9 - (double)0.2f) / (double)0.6f;
    }

    @Override
    public void process(RotationUpdate rotationUpdate) {
        Pair<Double, Integer> modeY;
        Pair<Double, Integer> modeX;
        rotationUpdate.setProcessor(this);
        float deltaXRot = rotationUpdate.getDeltaXRotABS();
        this.divisorX = GrimMath.gcd(deltaXRot, this.lastXRot);
        if (deltaXRot > 0.0f && deltaXRot < 5.0f && this.divisorX > GrimMath.MINIMUM_DIVISOR) {
            this.xRotMode.add(this.divisorX);
            this.lastXRot = deltaXRot;
        }
        float deltaYRot = rotationUpdate.getDeltaYRotABS();
        this.divisorY = GrimMath.gcd(deltaYRot, this.lastYRot);
        if (deltaYRot > 0.0f && deltaYRot < 5.0f && this.divisorY > GrimMath.MINIMUM_DIVISOR) {
            this.yRotMode.add(this.divisorY);
            this.lastYRot = deltaYRot;
        }
        if (this.xRotMode.size() > 15 && (modeX = this.xRotMode.getMode()).second() > 15) {
            this.modeX = modeX.first();
            this.sensitivityX = AimProcessor.convertToSensitivity(this.modeX);
        }
        if (this.yRotMode.size() > 15 && (modeY = this.yRotMode.getMode()).second() > 15) {
            this.modeY = modeY.first();
            this.sensitivityY = AimProcessor.convertToSensitivity(this.modeY);
        }
        this.deltaDotsX = (double)deltaXRot / this.modeX;
        this.deltaDotsY = (double)deltaYRot / this.modeY;
    }
}

