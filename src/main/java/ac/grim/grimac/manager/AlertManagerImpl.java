/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package ac.grim.grimac.manager;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.GrimUser;
import ac.grim.grimac.api.alerts.AlertManager;
import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.api.config.ConfigReloadable;
import ac.grim.grimac.manager.init.start.StartableInitable;
import ac.grim.grimac.platform.api.PlatformServer;
import ac.grim.grimac.platform.api.player.PlatformPlayer;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.bukkit.entity.Player;

public final class AlertManagerImpl
implements AlertManager,
ConfigReloadable,
StartableInitable {
    @NotNull
    private static PlatformServer platformServer;

    @Override
    public void start() {
        platformServer = GrimAPI.INSTANCE.getPlatformServer();
        this.reload(GrimAPI.INSTANCE.getConfigManager().getConfig());
    }

    @Override
    public void reload(ConfigManager config) {
        this.setConsoleAlertsEnabled(config.getBooleanElse("alerts.print-to-console", true), true);
        this.setConsoleVerboseEnabled(config.getBooleanElse("verbose.print-to-console", false), true);
        AlertType.NORMAL.enableMessage = config.getStringElse("alerts-enabled", "%prefix% &fAlerts enabled");
        AlertType.NORMAL.disableMessage = config.getStringElse("alerts-disabled", "%prefix% &fAlerts disabled");
        AlertType.VERBOSE.enableMessage = config.getStringElse("verbose-enabled", "%prefix% &fVerbose enabled");
        AlertType.VERBOSE.disableMessage = config.getStringElse("verbose-disabled", "%prefix% &fVerbose disabled");
        AlertType.BRAND.enableMessage = config.getStringElse("brands-enabled", "%prefix% &fBrands enabled");
        AlertType.BRAND.disableMessage = config.getStringElse("brands-disabled", "%prefix% &fBrands disabled");
    }

    @NotNull
    private PlatformPlayer requirePlatformPlayerFromUser(@NotNull GrimUser user) {
        Objects.requireNonNull(user, "user cannot be null");
        if (!(user instanceof GrimPlayer)) {
            throw new IllegalArgumentException("AlertManager action called with non-GrimPlayer user: " + user.getName());
        }
        GrimPlayer grimPlayer = (GrimPlayer)user;
        PlatformPlayer platformPlayer = grimPlayer.platformPlayer;
        Objects.requireNonNull(platformPlayer, "AlertManager action for user " + user.getName() + " with null platformPlayer (potentially during early join)");
        return platformPlayer;
    }

    private static void sendToggleMessage(@NotNull PlatformPlayer player, boolean enabled, @NotNull AlertType type) {
        String rawMessage = type.getToggleMessage(enabled);
        if (rawMessage.isEmpty()) {
            return;
        }
        String messageWithPlaceholders = MessageUtil.replacePlaceholders(player, rawMessage);
        player.sendMessage(MessageUtil.miniMessage(messageWithPlaceholders));
    }

    @Override
    public boolean hasAlertsEnabled(@NotNull GrimUser player) {
        return this.hasAlertsEnabled(this.requirePlatformPlayerFromUser(player));
    }

    @Override
    public void setAlertsEnabled(@NotNull GrimUser player, boolean enabled, boolean silent) {
        this.setAlertsEnabled(this.requirePlatformPlayerFromUser(player), enabled, silent);
    }

    @Override
    public boolean hasVerboseEnabled(@NotNull GrimUser player) {
        return this.hasVerboseEnabled(this.requirePlatformPlayerFromUser(player));
    }

    @Override
    public void setVerboseEnabled(@NotNull GrimUser player, boolean enabled, boolean silent) {
        this.setVerboseEnabled(this.requirePlatformPlayerFromUser(player), enabled, silent);
    }

    @Override
    public boolean hasBrandsEnabled(@NotNull GrimUser player) {
        GrimPlayer grimPlayer = (GrimPlayer)player;
        if (grimPlayer.platformPlayer == null) {
            return false;
        }
        return this.hasBrandsEnabled(grimPlayer.platformPlayer);
    }

    @Override
    public void setBrandsEnabled(@NotNull GrimUser player, boolean enabled, boolean silent) {
        this.setPlayerStateAndNotify(this.requirePlatformPlayerFromUser(player), enabled, silent, AlertType.BRAND);
    }

    @Override
    public boolean hasAlertsEnabled(Player player) {
        if (player == null) {
            return false;
        }
        return this.hasAlertsEnabled(GrimAPI.INSTANCE.getPlatformPlayerFactory().getFromNativePlayerType(player));
    }

    @Override
    public void toggleAlerts(Player player) {
        if (player == null) {
            return;
        }
        this.toggleAlerts(GrimAPI.INSTANCE.getPlatformPlayerFactory().getFromNativePlayerType(player));
    }

    @Override
    public boolean hasVerboseEnabled(Player player) {
        if (player == null) {
            return false;
        }
        return this.hasVerboseEnabled(GrimAPI.INSTANCE.getPlatformPlayerFactory().getFromNativePlayerType(player));
    }

    @Override
    public void toggleVerbose(Player player) {
        if (player == null) {
            return;
        }
        this.toggleVerbose(GrimAPI.INSTANCE.getPlatformPlayerFactory().getFromNativePlayerType(player));
    }

    public void handlePlayerQuit(@Nullable PlatformPlayer platformPlayer) {
        if (platformPlayer == null) {
            return;
        }
        AlertType.NORMAL.players.remove(platformPlayer);
        AlertType.VERBOSE.players.remove(platformPlayer);
        AlertType.BRAND.players.remove(platformPlayer);
    }

    public boolean toggleConsoleAlerts() {
        return this.toggleConsoleAlerts(false);
    }

    public boolean toggleConsoleAlerts(boolean silent) {
        return this.setConsoleAlertsEnabled(!this.hasConsoleAlertsEnabled(), silent);
    }

    @Contract(value="_ -> param1")
    public boolean setConsoleAlertsEnabled(boolean enabled) {
        return this.setConsoleAlertsEnabled(enabled, false);
    }

    @Contract(value="_, _ -> param1")
    public boolean setConsoleAlertsEnabled(boolean enabled, boolean silent) {
        this.setConsoleStateAndNotify(AlertType.NORMAL, enabled, silent);
        if (!enabled) {
            this.setConsoleVerboseEnabled(false, silent);
        }
        return enabled;
    }

    @Contract(pure=true)
    public boolean hasConsoleAlertsEnabled() {
        return AlertType.NORMAL.console;
    }

    public boolean toggleConsoleVerbose() {
        return this.toggleConsoleVerbose(false);
    }

    public boolean toggleConsoleVerbose(boolean silent) {
        return this.setConsoleVerboseEnabled(!this.hasConsoleVerboseEnabled(), silent);
    }

    @Contract(value="_ -> param1")
    public boolean setConsoleVerboseEnabled(boolean enabled) {
        return this.setConsoleVerboseEnabled(enabled, false);
    }

    @Contract(value="_, _ -> param1")
    public boolean setConsoleVerboseEnabled(boolean enabled, boolean silent) {
        if (enabled) {
            this.setConsoleAlertsEnabled(true, silent);
        }
        return this.setConsoleStateAndNotify(AlertType.VERBOSE, enabled, silent);
    }

    @Contract(pure=true)
    public boolean hasConsoleVerboseEnabled() {
        return AlertType.VERBOSE.console;
    }

    public boolean toggleConsoleBrands() {
        return this.toggleConsoleBrands(false);
    }

    public boolean toggleConsoleBrands(boolean silent) {
        return this.setConsoleBrandsEnabled(!this.hasConsoleBrandsEnabled(), silent);
    }

    @Contract(value="_ -> param1")
    public boolean setConsoleBrandsEnabled(boolean enabled) {
        return this.setConsoleStateAndNotify(AlertType.BRAND, enabled, false);
    }

    @Contract(value="_, _ -> param1")
    public boolean setConsoleBrandsEnabled(boolean enabled, boolean silent) {
        return this.setConsoleStateAndNotify(AlertType.BRAND, enabled, silent);
    }

    @Contract(pure=true)
    public boolean hasConsoleBrandsEnabled() {
        return AlertType.BRAND.console;
    }

    @Contract(value="_, _, _ -> param2")
    private boolean setConsoleStateAndNotify(@NotNull AlertType type, boolean enabled, boolean silent) {
        String rawMessage;
        if (type.console != enabled && !silent && !(rawMessage = type.getToggleMessage(enabled)).isEmpty()) {
            platformServer.getConsoleSender().sendMessage(MessageUtil.miniMessage(MessageUtil.replacePlaceholders((PlatformPlayer)null, rawMessage)));
        }
        type.console = enabled;
        return enabled;
    }

    private void setPlayerStateAndNotify(@NotNull PlatformPlayer platformPlayer, boolean enabled, boolean silent, @NotNull AlertType type) {
        boolean changed;
        Objects.requireNonNull(platformPlayer, "platformPlayer cannot be null");
        boolean bl = changed = enabled ? type.players.add(platformPlayer) : type.players.remove(platformPlayer);
        if (changed && !silent) {
            AlertManagerImpl.sendToggleMessage(platformPlayer, enabled, type);
        }
    }

    public boolean toggleBrands(@NotNull PlatformPlayer player) {
        return this.toggleBrands(player, false);
    }

    public boolean toggleBrands(@NotNull PlatformPlayer player, boolean silent) {
        return this.setBrandsEnabled(player, !this.hasBrandsEnabled(player), silent);
    }

    @Contract(value="_, _ -> param2")
    public boolean setBrandsEnabled(@NotNull PlatformPlayer player, boolean enabled) {
        return this.setBrandsEnabled(player, enabled, false);
    }

    @Contract(value="_, _, _ -> param2")
    public boolean setBrandsEnabled(@NotNull PlatformPlayer player, boolean enabled, boolean silent) {
        this.setPlayerStateAndNotify(player, enabled, silent, AlertType.BRAND);
        return enabled;
    }

    @Contract(pure=true)
    public boolean hasBrandsEnabled(@NotNull PlatformPlayer player) {
        return AlertType.BRAND.players.contains(player);
    }

    public boolean toggleVerbose(@NotNull PlatformPlayer player) {
        return this.toggleVerbose(player, false);
    }

    public boolean toggleVerbose(@NotNull PlatformPlayer player, boolean silent) {
        return this.setVerboseEnabled(player, !this.hasVerboseEnabled(player), silent);
    }

    @Contract(value="_, _ -> param2")
    public boolean setVerboseEnabled(@NotNull PlatformPlayer player, boolean enabled) {
        return this.setVerboseEnabled(player, enabled, false);
    }

    @Contract(value="_, _, _ -> param2")
    public boolean setVerboseEnabled(@NotNull PlatformPlayer player, boolean enabled, boolean silent) {
        if (enabled) {
            this.setAlertsEnabled(player, true, silent);
        }
        this.setPlayerStateAndNotify(player, enabled, silent, AlertType.VERBOSE);
        return enabled;
    }

    @Contract(pure=true)
    public boolean hasVerboseEnabled(@NotNull PlatformPlayer player) {
        return AlertType.VERBOSE.players.contains(player);
    }

    public boolean toggleAlerts(@NotNull PlatformPlayer player) {
        return this.toggleAlerts(player, false);
    }

    public boolean toggleAlerts(@NotNull PlatformPlayer player, boolean silent) {
        return this.setAlertsEnabled(player, !this.hasAlertsEnabled(player), silent);
    }

    @Contract(value="_, _ -> param2")
    public boolean setAlertsEnabled(@NotNull PlatformPlayer player, boolean enabled) {
        return this.setAlertsEnabled(player, enabled, false);
    }

    @Contract(value="_, _, _ -> param2")
    public boolean setAlertsEnabled(@NotNull PlatformPlayer player, boolean enabled, boolean silent) {
        this.setPlayerStateAndNotify(player, enabled, silent, AlertType.NORMAL);
        if (!enabled) {
            this.setVerboseEnabled(player, false, silent);
        }
        return enabled;
    }

    @Contract(pure=true)
    public boolean hasAlertsEnabled(@NotNull PlatformPlayer player) {
        return AlertType.NORMAL.players.contains(player);
    }

    public Set<PlatformPlayer> sendBrand(Component component, @Nullable @Nullable Set<@Nullable PlatformPlayer> excluding) {
        return AlertType.BRAND.send(component, excluding);
    }

    public Set<PlatformPlayer> sendVerbose(Component component, @Nullable @Nullable Set<@Nullable PlatformPlayer> excluding) {
        return AlertType.VERBOSE.send(component, excluding);
    }

    public Set<PlatformPlayer> sendAlert(Component component, @Nullable @Nullable Set<@Nullable PlatformPlayer> excluding) {
        return AlertType.NORMAL.send(component, excluding);
    }

    @Contract(pure=true)
    public boolean hasVerboseListeners() {
        return AlertType.VERBOSE.hasListeners();
    }

    @Contract(pure=true)
    public boolean hasAlertListeners() {
        return AlertType.NORMAL.hasListeners();
    }

    private static enum AlertType {
        NORMAL,
        VERBOSE,
        BRAND;

        public String enableMessage;
        public String disableMessage;
        public final Set<PlatformPlayer> players = new CopyOnWriteArraySet<PlatformPlayer>();
        public boolean console;

        @Contract(pure=true)
        public boolean hasListeners() {
            return !this.players.isEmpty() || this.console;
        }

        @Contract(pure=true)
        public String getToggleMessage(boolean enabled) {
            return enabled ? this.enableMessage : this.disableMessage;
        }

        public Set<@Nullable PlatformPlayer> send(Component component, @Nullable @Nullable Set<@Nullable PlatformPlayer> excluding) {
            HashSet<PlatformPlayer> listeners = new HashSet<PlatformPlayer>(this.players);
            if (excluding != null) {
                listeners.removeAll(excluding);
            }
            for (PlatformPlayer platformPlayer : listeners) {
                platformPlayer.sendMessage(component);
            }
            if (this.console && (excluding == null || !excluding.contains(null))) {
                platformServer.getConsoleSender().sendMessage(component);
                listeners.add(null);
            }
            return listeners;
        }
    }
}

