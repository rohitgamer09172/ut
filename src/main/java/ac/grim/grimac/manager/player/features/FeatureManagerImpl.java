/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 */
package ac.grim.grimac.manager.player.features;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.api.feature.FeatureManager;
import ac.grim.grimac.api.feature.FeatureState;
import ac.grim.grimac.manager.player.features.FeatureBuilder;
import ac.grim.grimac.manager.player.features.types.ExemptElytraFeature;
import ac.grim.grimac.manager.player.features.types.ExperimentalChecksFeature;
import ac.grim.grimac.manager.player.features.types.ForceSlowMovementFeature;
import ac.grim.grimac.manager.player.features.types.ForceStuckSpeedFeature;
import ac.grim.grimac.manager.player.features.types.GrimFeature;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.utils.common.ConfigReloadObserver;
import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FeatureManagerImpl
implements FeatureManager,
ConfigReloadObserver {
    private static final Map<String, GrimFeature> FEATURES;
    private final Map<String, FeatureState> states = new HashMap<String, FeatureState>();
    private final GrimPlayer player;

    @Deprecated
    @Contract(pure=true)
    public static Map<String, GrimFeature> getFEATURES() {
        return FeatureManagerImpl.getFeatures();
    }

    @Contract(pure=true)
    public static Map<String, GrimFeature> getFeatures() {
        return FEATURES;
    }

    public FeatureManagerImpl(GrimPlayer player) {
        this.player = player;
        for (GrimFeature value : FEATURES.values()) {
            this.states.put(value.getName(), FeatureState.UNSET);
        }
    }

    @Override
    public Collection<String> getFeatureKeys() {
        return ImmutableSet.copyOf(FEATURES.keySet());
    }

    @Override
    @Nullable
    public FeatureState getFeatureState(String key) {
        return this.states.get(key);
    }

    @Override
    public boolean isFeatureEnabled(String key) {
        GrimFeature feature = FEATURES.get(key);
        if (feature == null) {
            return false;
        }
        return feature.isEnabled(this.player);
    }

    @Override
    public boolean setFeatureState(String key, FeatureState tristate) {
        GrimFeature feature = FEATURES.get(key);
        if (feature == null) {
            return false;
        }
        this.states.put(key, tristate);
        return true;
    }

    @Override
    public void reload() {
        this.onReload(GrimAPI.INSTANCE.getExternalAPI().getConfigManager());
    }

    @Override
    public void onReload(ConfigManager config) {
        for (Map.Entry<String, FeatureState> entry : this.states.entrySet()) {
            String key = entry.getKey();
            FeatureState state = entry.getValue();
            GrimFeature feature = FEATURES.get(key);
            if (feature == null) continue;
            feature.setState(this.player, config, state);
        }
    }

    static {
        FeatureBuilder builder = new FeatureBuilder();
        builder.register(new ExperimentalChecksFeature());
        builder.register(new ExemptElytraFeature());
        builder.register(new ForceStuckSpeedFeature());
        builder.register(new ForceSlowMovementFeature());
        FEATURES = builder.buildMap();
    }
}

