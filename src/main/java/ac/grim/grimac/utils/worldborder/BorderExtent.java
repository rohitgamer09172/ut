/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.worldborder;

public interface BorderExtent {
    public double size();

    public double getMinX(double var1, double var3);

    public double getMaxX(double var1, double var3);

    public double getMinZ(double var1, double var3);

    public double getMaxZ(double var1, double var3);

    public BorderExtent tick();

    public BorderExtent update();
}

