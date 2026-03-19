/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.math;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import lombok.Generated;

public final class VanillaMath {
    private static final float[] SIN = new float[65536];

    @Contract(pure=true)
    public static float sin(float value) {
        return SIN[(int)(value * 10430.378f) & 0xFFFF];
    }

    @Contract(pure=true)
    public static float cos(float value) {
        return SIN[(int)(value * 10430.378f + 16384.0f) & 0xFFFF];
    }

    @Generated
    private VanillaMath() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        for (int i = 0; i < SIN.length; ++i) {
            VanillaMath.SIN[i] = (float)StrictMath.sin((double)i * Math.PI * 2.0 / 65536.0);
        }
    }
}

