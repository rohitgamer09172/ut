/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.predictionengine.blockeffects;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;

@FunctionalInterface
public interface BlockStepVisitor {
    public boolean visit(Vector3i var1, int var2);
}

