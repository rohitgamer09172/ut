/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.checks.impl.prediction;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.api.event.events.CompletePredictionEvent;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import java.util.concurrent.atomic.AtomicInteger;

@CheckData(name="Simulation", decay=0.02)
public class OffsetHandler
extends Check
implements PostPredictionCheck {
    private static final AtomicInteger flags = new AtomicInteger(0);
    private double setbackDecayMultiplier;
    private double threshold;
    private double immediateSetbackThreshold;
    private double maxAdvantage;
    private double maxCeiling;
    private double setbackViolationThreshold;
    private double advantageGained = 0.0;

    public OffsetHandler(GrimPlayer player) {
        super(player);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onPredictionComplete(PredictionComplete predictionComplete) {
        if (!predictionComplete.isChecked()) {
            return;
        }
        double offset = predictionComplete.getOffset();
        CompletePredictionEvent completePredictionEvent = new CompletePredictionEvent(this.player, this, offset);
        GrimAPI.INSTANCE.getEventBus().post(completePredictionEvent);
        if (completePredictionEvent.isCancelled()) {
            return;
        }
        if (offset >= this.threshold || offset >= this.immediateSetbackThreshold) {
            this.advantageGained += offset;
            this.giveOffsetLenienceNextTick(offset);
            AtomicInteger atomicInteger = flags;
            synchronized (atomicInteger) {
                String humanFormattedOffset;
                int flagId = (flags.get() & 0xFF) + 1;
                if (offset < 0.001) {
                    humanFormattedOffset = String.format("%.4E", offset);
                    humanFormattedOffset = humanFormattedOffset.replace("E-0", "E-");
                } else {
                    humanFormattedOffset = String.format("%6f", offset);
                    humanFormattedOffset = humanFormattedOffset.replace("0.", ".");
                }
                String verbose = humanFormattedOffset + " /gl " + flagId;
                if (this.flag(verbose)) {
                    if (this.alert(verbose)) {
                        flags.incrementAndGet();
                        predictionComplete.setIdentifier(flagId);
                    }
                    if ((this.advantageGained >= this.maxAdvantage || offset >= this.immediateSetbackThreshold) && !this.isNoSetbackPermission() && this.violations >= this.setbackViolationThreshold) {
                        this.player.getSetbackTeleportUtil().executeViolationSetback();
                    }
                }
            }
            this.advantageGained = Math.min(this.advantageGained, this.maxCeiling);
        } else {
            this.advantageGained *= this.setbackDecayMultiplier;
        }
        this.removeOffsetLenience();
    }

    private void giveOffsetLenienceNextTick(double offset) {
        double minimizedOffset;
        this.player.uncertaintyHandler.lastHorizontalOffset = minimizedOffset = Math.min(offset, 1.0);
        this.player.uncertaintyHandler.lastVerticalOffset = minimizedOffset;
    }

    private void removeOffsetLenience() {
        this.player.uncertaintyHandler.lastHorizontalOffset = 0.0;
        this.player.uncertaintyHandler.lastVerticalOffset = 0.0;
    }

    @Override
    public void onReload(ConfigManager config) {
        this.setbackDecayMultiplier = config.getDoubleElse("Simulation.setback-decay-multiplier", 0.999);
        this.threshold = config.getDoubleElse("Simulation.threshold", 0.001);
        this.immediateSetbackThreshold = config.getDoubleElse("Simulation.immediate-setback-threshold", 0.1);
        this.maxAdvantage = config.getDoubleElse("Simulation.max-advantage", 1.0);
        this.maxCeiling = config.getDoubleElse("Simulation.max-ceiling", 4.0);
        this.setbackViolationThreshold = config.getDoubleElse("Simulation.setback-violation-threshold", 1.0);
        if (this.maxAdvantage == -1.0) {
            this.maxAdvantage = Double.MAX_VALUE;
        }
        if (this.immediateSetbackThreshold == -1.0) {
            this.immediateSetbackThreshold = Double.MAX_VALUE;
        }
    }

    public boolean doesOffsetFlag(double offset) {
        return offset >= this.threshold;
    }
}

