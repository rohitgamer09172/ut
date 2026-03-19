/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.utils.collisions.datatypes;

import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;

public class HexCollisionBox
extends SimpleCollisionBox {
    public HexCollisionBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX / 16.0;
        this.minY = minY / 16.0;
        this.minZ = minZ / 16.0;
        this.maxX = maxX / 16.0;
        this.maxY = maxY / 16.0;
        this.maxZ = maxZ / 16.0;
    }
}

