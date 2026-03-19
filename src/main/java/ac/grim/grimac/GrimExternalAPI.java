/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.entity.Player
 */
package ac.grim.grimac;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.GrimAbstractAPI;
import ac.grim.grimac.api.GrimUser;
import ac.grim.grimac.api.alerts.AlertManager;
import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.api.event.EventBus;
import ac.grim.grimac.api.event.events.GrimReloadEvent;
import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.manager.config.ConfigManagerFileImpl;
import ac.grim.grimac.manager.init.start.StartableInitable;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import ac.grim.grimac.utils.common.ConfigReloadObserver;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import lombok.Generated;
import org.bukkit.entity.Player;

public class GrimExternalAPI
implements GrimAbstractAPI,
ConfigReloadObserver,
StartableInitable {
    private final GrimAPI api;
    private final Map<String, Function<GrimUser, String>> variableReplacements = new ConcurrentHashMap<String, Function<GrimUser, String>>();
    private final Map<String, String> staticReplacements = new ConcurrentHashMap<String, String>();
    private final Map<String, Function<Object, Object>> functions = new ConcurrentHashMap<String, Function<Object, Object>>();
    private final ConfigManagerFileImpl configManagerFile = new ConfigManagerFileImpl();
    private ConfigManager configManager = null;
    private boolean started = false;

    public GrimExternalAPI(GrimAPI api) {
        this.api = api;
    }

    @Override
    @NotNull
    public EventBus getEventBus() {
        return this.api.getEventBus();
    }

    @Override
    @Nullable
    public GrimUser getGrimUser(Player player) {
        return this.getGrimUser(player.getUniqueId());
    }

    @Override
    @Nullable
    public GrimUser getGrimUser(UUID uuid) {
        return this.api.getPlayerDataManager().getPlayer(uuid);
    }

    @Override
    public void registerVariable(String string, Function<GrimUser, String> replacement) {
        if (replacement == null) {
            this.variableReplacements.remove(string);
        } else {
            this.variableReplacements.put(string, replacement);
        }
    }

    @Override
    public void registerVariable(String variable, String replacement) {
        if (replacement == null) {
            this.staticReplacements.remove(variable);
        } else {
            this.staticReplacements.put(variable, replacement);
        }
    }

    @Override
    public String getGrimVersion() {
        return this.api.getGrimPlugin().getDescription().getVersion();
    }

    @Override
    public void registerFunction(String key, Function<Object, Object> function) {
        if (function == null) {
            this.functions.remove(key);
        } else {
            this.functions.put(key, function);
        }
    }

    @Override
    public Function<Object, Object> getFunction(String key) {
        return this.functions.get(key);
    }

    @Override
    public AlertManager getAlertManager() {
        return GrimAPI.INSTANCE.getAlertManager();
    }

    @Override
    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    @Override
    public boolean hasStarted() {
        return this.started;
    }

    @Override
    public int getCurrentTick() {
        return GrimAPI.INSTANCE.getTickManager().currentTick;
    }

    @Override
    @NotNull
    public GrimPlugin getGrimPlugin(@NotNull Object o) {
        return this.api.getExtensionManager().getPlugin(o);
    }

    public void load() {
        this.reload(this.configManagerFile);
        this.api.getLoader().registerAPIService();
    }

    @Override
    public void start() {
        this.started = true;
        try {
            GrimAPI.INSTANCE.getConfigManager().start();
        }
        catch (Exception e) {
            LogUtil.error("Failed to start config manager.", e);
        }
    }

    @Override
    public void reload(ConfigManager config) {
        if (config.isLoadedAsync() && this.started) {
            GrimAPI.INSTANCE.getScheduler().getAsyncScheduler().runNow(GrimAPI.INSTANCE.getGrimPlugin(), () -> this.successfulReload(config));
        } else {
            this.successfulReload(config);
        }
    }

    @Override
    public CompletableFuture<Boolean> reloadAsync(ConfigManager config) {
        if (config.isLoadedAsync() && this.started) {
            CompletableFuture<Boolean> future = new CompletableFuture<Boolean>();
            GrimAPI.INSTANCE.getScheduler().getAsyncScheduler().runNow(GrimAPI.INSTANCE.getGrimPlugin(), () -> future.complete(this.successfulReload(config)));
            return future;
        }
        return CompletableFuture.completedFuture(this.successfulReload(config));
    }

    private boolean successfulReload(ConfigManager config) {
        try {
            config.reload();
            GrimAPI.INSTANCE.getConfigManager().load(config);
            if (this.started) {
                GrimAPI.INSTANCE.getConfigManager().start();
            }
            this.onReload(config);
            if (this.started) {
                GrimAPI.INSTANCE.getScheduler().getAsyncScheduler().runNow(GrimAPI.INSTANCE.getGrimPlugin(), () -> GrimAPI.INSTANCE.getEventBus().post(new GrimReloadEvent(true)));
            }
            return true;
        }
        catch (Exception e) {
            LogUtil.error("Failed to reload config", e);
            if (this.started) {
                GrimAPI.INSTANCE.getScheduler().getAsyncScheduler().runNow(GrimAPI.INSTANCE.getGrimPlugin(), () -> GrimAPI.INSTANCE.getEventBus().post(new GrimReloadEvent(false)));
            }
            return false;
        }
    }

    @Override
    public void onReload(ConfigManager newConfig) {
        if (newConfig == null) {
            LogUtil.warn("ConfigManager not set. Using default config file manager.");
            this.configManager = this.configManagerFile;
        } else {
            this.configManager = newConfig;
        }
        this.updateVariables();
        GrimAPI.INSTANCE.getAlertManager().reload(this.configManager);
        GrimAPI.INSTANCE.getDiscordManager().reload();
        GrimAPI.INSTANCE.getSpectateManager().reload();
        GrimAPI.INSTANCE.getViolationDatabaseManager().reload();
        if (!this.started) {
            return;
        }
        for (GrimPlayer player : GrimAPI.INSTANCE.getPlayerDataManager().getEntries()) {
            player.runSafely(() -> player.reload(this.configManager));
        }
    }

    private void updateVariables() {
        this.variableReplacements.putIfAbsent("%player%", GrimUser::getName);
        this.variableReplacements.putIfAbsent("%uuid%", user -> user.getUniqueId().toString());
        this.variableReplacements.putIfAbsent("%ping%", user -> "" + user.getTransactionPing());
        this.variableReplacements.putIfAbsent("%brand%", GrimUser::getBrand);
        this.variableReplacements.putIfAbsent("%h_sensitivity%", user -> "" + (int)Math.round(user.getHorizontalSensitivity() * 200.0));
        this.variableReplacements.putIfAbsent("%v_sensitivity%", user -> "" + (int)Math.round(user.getVerticalSensitivity() * 200.0));
        this.variableReplacements.putIfAbsent("%fast_math%", user -> "" + !user.isVanillaMath());
        this.variableReplacements.putIfAbsent("%tps%", user -> String.format("%.2f", GrimAPI.INSTANCE.getPlatformServer().getTPS()));
        this.variableReplacements.putIfAbsent("%version%", GrimUser::getVersionName);
        this.staticReplacements.put("%prefix%", MessageUtil.translateAlternateColorCodes('&', GrimAPI.INSTANCE.getConfigManager().getPrefix()));
        this.staticReplacements.putIfAbsent("%grim_version%", this.getGrimVersion());
    }

    @Generated
    public Map<String, Function<GrimUser, String>> getVariableReplacements() {
        return this.variableReplacements;
    }

    @Generated
    public Map<String, String> getStaticReplacements() {
        return this.staticReplacements;
    }
}

