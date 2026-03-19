/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.manager.violationdatabase.sqlite;

import ac.grim.grimac.manager.violationdatabase.DatabaseDialect;

public class SQLiteDialect
implements DatabaseDialect {
    @Override
    public String getUuidColumnType() {
        return "BLOB";
    }

    @Override
    public String getAutoIncrementPrimaryKeySyntax() {
        return "INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT";
    }

    @Override
    public String getInsertOrIgnoreSyntax(String tableName, String columnNames) {
        return "INSERT OR IGNORE INTO " + tableName + " (" + columnNames + ") VALUES (?)";
    }

    @Override
    public String getUniqueConstraintViolationSQLState() {
        return "23000";
    }

    @Override
    public int getUniqueConstraintViolationErrorCode() {
        return 19;
    }
}

