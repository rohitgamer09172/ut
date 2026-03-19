/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.manager;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import ac.grim.grimac.utils.data.LastInstance;
import java.util.ArrayList;
import java.util.List;

public class LastInstanceManager
extends Check
implements PostPredictionCheck {
    private final List<LastInstance> instances = new ArrayList<LastInstance>();

    public LastInstanceManager(GrimPlayer player) {
        super(player);
    }

    public void addInstance(LastInstance instance) {
        this.instances.add(instance);
    }

    @Override
    public void onPredictionComplete(PredictionComplete predictionComplete) {
        for (LastInstance instance : this.instances) {
            instance.tick();
        }
    }
}

