/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.manager.player.features.types;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.api.feature.FeatureState;
import ac.grim.grimac.player.GrimPlayer;

public interface GrimFeature {
    public String getName();

    public void setState(GrimPlayer var1, ConfigManager var2, FeatureState var3);

    public boolean isEnabled(GrimPlayer var1);

    public boolean isEnabledInConfig(GrimPlayer var1, ConfigManager var2);
}

