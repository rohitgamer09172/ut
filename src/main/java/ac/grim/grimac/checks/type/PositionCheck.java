/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.type;

import ac.grim.grimac.api.AbstractCheck;
import ac.grim.grimac.utils.anticheat.update.PositionUpdate;

public interface PositionCheck
extends AbstractCheck {
    default public void onPositionUpdate(PositionUpdate positionUpdate) {
    }
}

