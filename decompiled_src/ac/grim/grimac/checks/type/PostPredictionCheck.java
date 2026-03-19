/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.type;

import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;

public interface PostPredictionCheck
extends PacketCheck {
    default public void onPredictionComplete(PredictionComplete predictionComplete) {
    }
}

