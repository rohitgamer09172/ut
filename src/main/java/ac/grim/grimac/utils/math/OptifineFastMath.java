/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.math;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import lombok.Generated;

public final class OptifineFastMath {
    private static final float[] SIN = new float[4096];

    @Contract(pure=true)
    public static float sin(float value) {
        return SIN[(int)(value * 651.8986f) & 0xFFF];
    }

    @Contract(pure=true)
    public static float cos(float value) {
        return SIN[(int)(value * 651.8986f + 1024.0f) & 0xFFF];
    }

    @Contract(pure=true)
    public static float roundToFloat(double value) {
        return (float)((double)Math.round(value * 1.0E8) / 1.0E8);
    }

    @Generated
    private OptifineFastMath() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        for (int i = 0; i < 4096; ++i) {
            OptifineFastMath.SIN[i] = OptifineFastMath.roundToFloat(StrictMath.sin((double)i * Math.PI * 2.0 / 4096.0));
        }
    }
}

