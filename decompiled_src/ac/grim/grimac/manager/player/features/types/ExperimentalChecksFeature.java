/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.manager.player.features.types;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.api.feature.FeatureState;
import ac.grim.grimac.manager.player.features.types.GrimFeature;
import ac.grim.grimac.player.GrimPlayer;

public class ExperimentalChecksFeature
implements GrimFeature {
    @Override
    public String getName() {
        return "ExperimentalChecks";
    }

    @Override
    public void setState(GrimPlayer player, ConfigManager config, FeatureState state) {
        switch (state) {
            case ENABLED: {
                player.setExperimentalChecks(true);
                break;
            }
            case DISABLED: {
                player.setExperimentalChecks(false);
                break;
            }
            default: {
                player.setExperimentalChecks(this.isEnabledInConfig(player, config));
            }
        }
    }

    @Override
    public boolean isEnabled(GrimPlayer player) {
        return player.isExperimentalChecks();
    }

    @Override
    public boolean isEnabledInConfig(GrimPlayer player, ConfigManager config) {
        return config.getBooleanElse("experimental-checks", false);
    }
}

