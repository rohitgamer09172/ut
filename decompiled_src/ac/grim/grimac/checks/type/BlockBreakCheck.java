/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.type;

import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.utils.anticheat.update.BlockBreak;

public interface BlockBreakCheck
extends PostPredictionCheck {
    default public void onBlockBreak(BlockBreak blockBreak) {
    }

    default public void onPostFlyingBlockBreak(BlockBreak blockBreak) {
    }
}

