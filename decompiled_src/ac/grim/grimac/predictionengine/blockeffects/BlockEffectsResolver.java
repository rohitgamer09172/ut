/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.predictionengine.blockeffects;

import ac.grim.grimac.player.GrimPlayer;
import java.util.List;

public interface BlockEffectsResolver {
    public void applyEffectsFromBlocks(GrimPlayer var1, List<GrimPlayer.Movement> var2);
}

