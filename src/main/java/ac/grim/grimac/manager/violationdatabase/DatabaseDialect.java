/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.manager.violationdatabase;

public interface DatabaseDialect {
    public String getUuidColumnType();

    public String getAutoIncrementPrimaryKeySyntax();

    public String getInsertOrIgnoreSyntax(String var1, String var2);

    public String getUniqueConstraintViolationSQLState();

    public int getUniqueConstraintViolationErrorCode();
}

