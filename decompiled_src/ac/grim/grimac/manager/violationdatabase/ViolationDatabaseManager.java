/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.manager.violationdatabase;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.manager.init.ReloadableInitable;
import ac.grim.grimac.manager.init.start.StartableInitable;
import ac.grim.grimac.manager.violationdatabase.NoOpViolationDatabase;
import ac.grim.grimac.manager.violationdatabase.Violation;
import ac.grim.grimac.manager.violationdatabase.ViolationDatabase;
import ac.grim.grimac.manager.violationdatabase.mysql.MySQLViolationDatabase;
import ac.grim.grimac.manager.violationdatabase.postgresql.PostgresqlViolationDatabase;
import ac.grim.grimac.manager.violationdatabase.sqlite.SQLiteViolationDatabase;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.anticheat.LogUtil;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import lombok.Generated;

public class ViolationDatabaseManager
implements StartableInitable,
ReloadableInitable {
    private final GrimPlugin plugin;
    private boolean enabled = false;
    private boolean loaded = false;
    @NotNull
    private ViolationDatabase database;

    public ViolationDatabaseManager(GrimPlugin plugin) {
        this.plugin = plugin;
        this.database = NoOpViolationDatabase.INSTANCE;
    }

    @Override
    public void start() {
        this.load();
    }

    @Override
    public void reload() {
        this.load();
    }

    public void load() {
        String rawType;
        ConfigManager cfg = GrimAPI.INSTANCE.getConfigManager().getConfig();
        this.enabled = cfg.getBooleanElse("history.enabled", false);
        switch (rawType = this.enabled ? cfg.getStringElse("history.database.type", "SQLITE").toUpperCase() : "NOOP") {
            case "SQLITE": {
                if (this.database instanceof SQLiteViolationDatabase) break;
                this.database.disconnect();
                try {
                    Class.forName("org.sqlite.JDBC");
                    this.database = new SQLiteViolationDatabase(this.plugin);
                    this.database.connect();
                    this.loaded = true;
                }
                catch (ClassNotFoundException e) {
                    LogUtil.error("IMPORTANT: Could not load SQLite driver for /grim history database.\nDownload the minecraft-sqlite-jdbc mod/plugin for SQLite support, or change history.database.type\nAlternatively set history.enabled=false to remove this message if /grim history support is not desired");
                    this.database = NoOpViolationDatabase.INSTANCE;
                    this.loaded = false;
                }
                catch (SQLException e) {
                    LogUtil.error(e);
                    this.database = NoOpViolationDatabase.INSTANCE;
                    this.loaded = false;
                }
                break;
            }
            case "MYSQL": {
                MySQLViolationDatabase mysql;
                int port = cfg.getIntElse("history.database.port", 3306);
                String host = cfg.getStringElse("history.database.host", "localhost") + ":" + port;
                String db = cfg.getStringElse("history.database.database", "grimac");
                String user = cfg.getStringElse("history.database.username", "root");
                String pwd = cfg.getStringElse("history.database.password", "password");
                ViolationDatabase violationDatabase = this.database;
                if (violationDatabase instanceof MySQLViolationDatabase && (mysql = (MySQLViolationDatabase)violationDatabase).sameConfig(host, db, user, pwd)) break;
                this.database.disconnect();
                this.database = new MySQLViolationDatabase(this.plugin, host, db, user, pwd);
                try {
                    this.database.connect();
                    this.loaded = true;
                }
                catch (SQLException e) {
                    LogUtil.error(e);
                    this.database = NoOpViolationDatabase.INSTANCE;
                    this.loaded = false;
                }
                break;
            }
            case "POSTGRESQL": {
                PostgresqlViolationDatabase postgresql;
                int port = cfg.getIntElse("history.database.port", 3306);
                String host = cfg.getStringElse("history.database.host", "localhost") + ":" + port;
                String db = cfg.getStringElse("history.database.database", "grimac");
                String user = cfg.getStringElse("history.database.username", "root");
                String pwd = cfg.getStringElse("history.database.password", "password");
                ViolationDatabase violationDatabase = this.database;
                if (violationDatabase instanceof PostgresqlViolationDatabase && (postgresql = (PostgresqlViolationDatabase)violationDatabase).sameConfig(host, db, user, pwd)) break;
                this.database.disconnect();
                this.database = new PostgresqlViolationDatabase(host, db, user, pwd);
                try {
                    this.database.connect();
                    this.loaded = true;
                }
                catch (SQLException e) {
                    LogUtil.error(e);
                    this.database = NoOpViolationDatabase.INSTANCE;
                    this.loaded = false;
                }
                break;
            }
            default: {
                if (this.database instanceof NoOpViolationDatabase) break;
                this.database.disconnect();
                this.database = NoOpViolationDatabase.INSTANCE;
                this.loaded = false;
            }
        }
    }

    public void logAlert(GrimPlayer player, String verbose, String checkName, int vls) {
        String grimVersion = GrimAPI.INSTANCE.getExternalAPI().getGrimVersion();
        GrimAPI.INSTANCE.getScheduler().getAsyncScheduler().runNow(this.plugin, () -> this.database.logAlert(player, grimVersion, verbose, checkName, vls));
    }

    public int getLogCount(UUID player) {
        return this.database.getLogCount(player);
    }

    public List<Violation> getViolations(UUID player, int page, int limit) {
        return this.database.getViolations(player, page, limit);
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
    }

    @Generated
    public boolean isLoaded() {
        return this.loaded;
    }
}

