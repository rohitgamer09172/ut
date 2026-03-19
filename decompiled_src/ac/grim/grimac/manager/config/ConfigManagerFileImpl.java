/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.manager.config;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.common.BasicReloadable;
import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.shaded.configuralize.DynamicConfig;
import ac.grim.grimac.shaded.configuralize.Language;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.utils.anticheat.LogUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.List;
import java.util.Map;

public class ConfigManagerFileImpl
implements ConfigManager,
BasicReloadable {
    private final DynamicConfig config = new DynamicConfig();
    private boolean initialized = false;

    private File getConfigFile(String path) {
        return new File(GrimAPI.INSTANCE.getGrimPlugin().getDataFolder(), path);
    }

    @Override
    public void reload() {
        GrimAPI.INSTANCE.getGrimPlugin().getDataFolder().mkdirs();
        if (!this.initialized) {
            this.initialized = true;
            this.config.addSource(GrimAPI.class, "config", this.getConfigFile("config.yml"));
            this.config.addSource(GrimAPI.class, "messages", this.getConfigFile("messages.yml"));
            this.config.addSource(GrimAPI.class, "discord", this.getConfigFile("discord.yml"));
            this.config.addSource(GrimAPI.class, "punishments", this.getConfigFile("punishments.yml"));
        }
        String languageCode = System.getProperty("user.language").toUpperCase();
        try {
            this.config.setLanguage(Language.valueOf(languageCode));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            // empty catch block
        }
        if (!this.config.isLanguageAvailable(this.config.getLanguage())) {
            String lang = languageCode.toUpperCase();
            LogUtil.info("Unknown user language " + lang + ".");
            LogUtil.info("If you fluently speak " + lang + " as well as English, see the GitHub repo to translate it!");
            this.config.setLanguage(Language.EN);
        }
        try {
            this.config.saveAllDefaults(false);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to save default config files", e);
        }
        try {
            this.config.loadAll();
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    private void upgrade() {
        File config = new File(GrimAPI.INSTANCE.getGrimPlugin().getDataFolder(), "config.yml");
        if (config.exists()) {
            try {
                String configString = new String(Files.readAllBytes(config.toPath()));
                int configVersion = configString.indexOf("config-version: ");
                if (configVersion != -1) {
                    String configStringVersion;
                    configStringVersion = configStringVersion.substring(0, !(configStringVersion = configString.substring(configVersion + "config-version: ".length())).contains("\n") ? configStringVersion.length() : configStringVersion.indexOf("\n"));
                    configStringVersion = configStringVersion.replaceAll("\\D", "");
                    configVersion = Integer.parseInt(configStringVersion);
                    configString = configString.replaceAll("config-version: " + configStringVersion, "config-version: 9");
                    Files.write(config.toPath(), configString.getBytes(), new OpenOption[0]);
                    this.upgradeModernConfig(config, configString, configVersion);
                } else {
                    this.removeLegacyTwoPointOne(config);
                }
            }
            catch (IOException e) {
                LogUtil.error("Failed to upgrade config file", e);
            }
        }
    }

    private void upgradeModernConfig(File config, String configString, int configVersion) throws IOException {
        if (configVersion < 1) {
            this.addMaxPing(config, configString);
        }
        if (configVersion < 2) {
            this.addMissingPunishments();
        }
        if (configVersion < 3) {
            this.addBaritoneCheck();
        }
        if (configVersion < 4) {
            this.newOffsetNewDiscordConf(config, configString);
        }
        if (configVersion < 5) {
            this.fixBadPacketsAndAdjustPingConfig(config, configString);
        }
        if (configVersion < 6) {
            this.addSuperDebug(config, configString);
        }
        if (configVersion < 7) {
            this.removeAlertsOnJoin(config, configString);
        }
        if (configVersion < 8) {
            this.addPacketSpamThreshold(config, configString);
        }
        if (configVersion < 9) {
            this.newOffsetHandlingAntiKB(config, configString);
        }
    }

    private void removeLegacyTwoPointOne(File config) throws IOException {
        Files.move(config.toPath(), new File(GrimAPI.INSTANCE.getGrimPlugin().getDataFolder(), "config-2.1.old.yml").toPath(), new CopyOption[0]);
    }

    private void addMaxPing(File config, String configString) throws IOException {
        configString = (String)configString + "\n\n\n# How long should players have until we keep them for timing out? Default = 2 minutes\nmax-ping: 120";
        Files.write(config.toPath(), ((String)configString).getBytes(), new OpenOption[0]);
    }

    private void addMissingPunishments() {
        File config = new File(GrimAPI.INSTANCE.getGrimPlugin().getDataFolder(), "punishments.yml");
        if (config.exists()) {
            try {
                Object configString = new String(Files.readAllBytes(config.toPath()));
                int commentIndex = ((String)configString).indexOf("  # As of 2.2.2 these are just placeholders, there are no Killaura/Aim/Autoclicker checks other than those that");
                if (commentIndex != -1) {
                    configString = ((String)configString).substring(0, commentIndex);
                    configString = (String)configString + "  Combat:\n    remove-violations-after: 300\n    checks:\n      - \"Killaura\"\n      - \"Aim\"\n    commands:\n      - \"20:40 [alert]\"\n  # As of 2.2.10, there are no AutoClicker checks and this is a placeholder. 2.3 will include AutoClicker checks.\n  Autoclicker:\n    remove-violations-after: 300\n    checks:\n      - \"Autoclicker\"\n    commands:\n      - \"20:40 [alert]\"\n";
                }
                Files.write(config.toPath(), ((String)configString).getBytes(), new OpenOption[0]);
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    private void fixBadPacketsAndAdjustPingConfig(File config, String configString) {
        try {
            configString = configString.replaceAll("max-ping: \\d+", "max-transaction-time: 60");
            Files.write(config.toPath(), configString.getBytes(), new OpenOption[0]);
        }
        catch (IOException iOException) {
            // empty catch block
        }
        File punishConfig = new File(GrimAPI.INSTANCE.getGrimPlugin().getDataFolder(), "punishments.yml");
        if (punishConfig.exists()) {
            try {
                String punishConfigString = new String(Files.readAllBytes(punishConfig.toPath()));
                punishConfigString = punishConfigString.replace("commands:", "commands:");
                Files.write(punishConfig.toPath(), punishConfigString.getBytes(), new OpenOption[0]);
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    private void addBaritoneCheck() {
        File config = new File(GrimAPI.INSTANCE.getGrimPlugin().getDataFolder(), "punishments.yml");
        if (config.exists()) {
            try {
                String configString = new String(Files.readAllBytes(config.toPath()));
                configString = configString.replace("      - \"EntityControl\"\n", "      - \"EntityControl\"\n      - \"Baritone\"\n      - \"FastBreak\"\n");
                Files.write(config.toPath(), configString.getBytes(), new OpenOption[0]);
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    private void newOffsetNewDiscordConf(File config, String configString) throws IOException {
        configString = configString.replace("threshold: 0.0001", "threshold: 0.001");
        configString = configString.replace("threshold: 0.00001", "threshold: 0.001");
        Files.write(config.toPath(), configString.getBytes(), new OpenOption[0]);
        File discordFile = new File(GrimAPI.INSTANCE.getGrimPlugin().getDataFolder(), "discord.yml");
        if (discordFile.exists()) {
            try {
                Object discordString = new String(Files.readAllBytes(discordFile.toPath()));
                discordString = (String)discordString + "\nembed-color: \"#00FFFF\"\nviolation-content:\n  - \"**Player**: %player%\"\n  - \"**Check**: %check%\"\n  - \"**Violations**: %violations%\"\n  - \"**Client Version**: %version%\"\n  - \"**Brand**: %brand%\"\n  - \"**Ping**: %ping%\"\n  - \"**TPS**: %tps%\"\n";
                Files.write(discordFile.toPath(), ((String)discordString).getBytes(), new OpenOption[0]);
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    private void addSuperDebug(File config, String configString) throws IOException {
        if (!((String)(configString = ((String)configString).replace("threshold: 0.0001", "threshold: 0.001"))).contains("experimental-checks")) {
            configString = (String)configString + "\n\n# Enables experimental checks\nexperimental-checks: false\n\n";
        }
        configString = (String)configString + "\nverbose:\n  print-to-console: false\n";
        Files.write(config.toPath(), ((String)configString).getBytes(), new OpenOption[0]);
        File messageFile = new File(GrimAPI.INSTANCE.getGrimPlugin().getDataFolder(), "messages.yml");
        if (messageFile.exists()) {
            try {
                Object messagesString = new String(Files.readAllBytes(messageFile.toPath()));
                messagesString = (String)messagesString + "\n\nupload-log: \"%prefix% &fUploaded debug to: %url%\"\nupload-log-start: \"%prefix% &fUploading log... please wait\"\nupload-log-not-found: \"%prefix% &cUnable to find that log\"\nupload-log-upload-failure: \"%prefix% &cSomething went wrong while uploading this log, see console for more info\"\n";
                Files.write(messageFile.toPath(), ((String)messagesString).getBytes(), new OpenOption[0]);
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    private void removeAlertsOnJoin(File config, String configString) throws IOException {
        configString = configString.replaceAll("  # Should players with grim\\.alerts permission automatically enable alerts on join\\?\r?\n  enable-on-join: (?:true|false)\r?\n", "");
        configString = configString.replaceAll("  # \u7ba1\u7406\u5458\u8fdb\u5165\u65f6\u662f\u5426\u81ea\u52a8\u5f00\u542f\u8b66\u544a\uff1f\r?\n  enable-on-join: (?:true|false)\r?\n", "");
        Files.write(config.toPath(), configString.getBytes(), new OpenOption[0]);
    }

    private void addPacketSpamThreshold(File config, String configString) throws IOException {
        configString = (String)configString + "\n# Grim sometimes cancels illegal packets such as with timer, after X packets in a second cancelled, when should\n# we simply kick the player? This is required as some packet limiters don't count packets cancelled by grim.\npacket-spam-threshold: 150\n";
        Files.write(config.toPath(), ((String)configString).getBytes(), new OpenOption[0]);
    }

    private void newOffsetHandlingAntiKB(File config, String configString) throws IOException {
        configString = configString.replaceAll("  # How much of an offset is \"cheating\"\r?\n  # By default this is 1e-5, which is safe and sane\r?\n  # Measured in blocks from the correct movement\r?\n  threshold: 0.001\r?\n  setbackvl: 3", "  # How much should we multiply total advantage by when the player is legit\n  setback-decay-multiplier: 0.999\n  # How large of an offset from the player's velocity should we create a violation for?\n  # Measured in blocks from the possible velocity\n  threshold: 0.001\n  # How large of a violation in a tick before the player gets immediately setback?\n  # -1 to disable\n  immediate-setback-threshold: 0.1\n  # How large of an advantage over all ticks before we start to setback?\n  # -1 to disable\n  max-advantage: 1\n  # This is to stop the player from gathering too many violations and never being able to clear them all\n  max-ceiling: 4");
        Files.write(config.toPath(), configString.getBytes(), new OpenOption[0]);
    }

    @Override
    public String getStringElse(String key, String otherwise) {
        return this.config.getStringElse(key, otherwise);
    }

    @Override
    @Nullable
    public String getString(String key) {
        return this.config.getString(key);
    }

    @Override
    public List<String> getStringList(String key) {
        return this.config.getStringList(key);
    }

    @Override
    public List<String> getStringListElse(String key, List<String> otherwise) {
        return this.config.getStringListElse(key, otherwise);
    }

    @Override
    public int getIntElse(String key, int other) {
        return this.config.getIntElse(key, other);
    }

    @Override
    public long getLongElse(String key, long otherwise) {
        return this.config.getLongElse(key, otherwise);
    }

    @Override
    public double getDoubleElse(String key, double otherwise) {
        return this.config.getDoubleElse(key, otherwise);
    }

    @Override
    public boolean getBooleanElse(String key, boolean otherwise) {
        return this.config.getBooleanElse(key, otherwise);
    }

    @Override
    public <T> T get(String key) {
        return this.config.get(key);
    }

    @Override
    @Nullable
    public <T> T getElse(String key, T otherwise) {
        return this.config.getElse(key, otherwise);
    }

    @Override
    public <K, V> Map<K, V> getMap(String key) {
        return this.config.getMap(key);
    }

    @Override
    @Nullable
    public <K, V> Map<K, V> getMapElse(String s, Map<K, V> map) {
        return this.config.getMapElse(s, map);
    }

    @Override
    @Nullable
    public <T> List<T> getList(String path) {
        return this.config.getList(path);
    }

    @Override
    @Nullable
    public <T> List<T> getListElse(String path, List<T> otherwise) {
        return this.config.getListElse(path, otherwise);
    }

    @Override
    public boolean hasLoaded() {
        return this.initialized;
    }
}

