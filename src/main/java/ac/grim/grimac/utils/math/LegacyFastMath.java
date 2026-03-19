/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.utils.math;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.utils.math.GrimMath;
import lombok.Generated;

public final class LegacyFastMath {
    private static final float[] SIN_TABLE_FAST;

    @Contract(pure=true)
    public static float sin(float value) {
        return SIN_TABLE_FAST[(int)(value * 651.8986f) & 0xFFF];
    }

    @Contract(pure=true)
    public static float cos(float value) {
        return SIN_TABLE_FAST[(int)((value + 1.5707964f) * 651.8986f) & 0xFFF];
    }

    @Generated
    private LegacyFastMath() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        int i;
        SIN_TABLE_FAST = new float[4096];
        for (i = 0; i < 4096; ++i) {
            LegacyFastMath.SIN_TABLE_FAST[i] = (float)Math.sin(((float)i + 0.5f) / 4096.0f * ((float)Math.PI * 2));
        }
        for (i = 0; i < 360; i += 90) {
            LegacyFastMath.SIN_TABLE_FAST[(int)((float)i * 11.377778f) & 0xFFF] = (float)Math.sin(GrimMath.radians(i));
        }
    }
}

