/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package ac.grim.grimac.manager.violationdatabase;

import ac.grim.grimac.manager.violationdatabase.DatabaseDialect;
import ac.grim.grimac.utils.anticheat.LogUtil;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import lombok.Generated;

public final class DatabaseUtils {
    public static byte[] uuidToBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    public static UUID bytesToUuid(byte[] bytes) {
        if (bytes == null || bytes.length != 16) {
            throw new IllegalArgumentException("UUID bytes must be 16 bytes long. Received: " + (String)(bytes == null ? "null" : bytes.length + " bytes"));
        }
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long msb = bb.getLong();
        long lsb = bb.getLong();
        return new UUID(msb, lsb);
    }

    public static long getOrCreateId(Connection connection, DatabaseDialect dialect, String tableName, String stringColumnName, String value) throws SQLException {
        block23: {
            String insertSql = dialect.getInsertOrIgnoreSyntax(tableName, stringColumnName);
            try (PreparedStatement insertStmt = connection.prepareStatement(insertSql);){
                insertStmt.setString(1, value);
                insertStmt.executeUpdate();
            }
            catch (SQLException e) {
                if (e.getSQLState().equals(dialect.getUniqueConstraintViolationSQLState()) && e.getErrorCode() == dialect.getUniqueConstraintViolationErrorCode()) break block23;
                LogUtil.error("Failed to insert into " + tableName + ": " + value, e);
                throw e;
            }
        }
        try (PreparedStatement selectStmt = connection.prepareStatement("SELECT id FROM " + tableName + " WHERE " + stringColumnName + " = ?");){
            selectStmt.setString(1, value);
            try (ResultSet rs = selectStmt.executeQuery();){
                if (rs.next()) {
                    long l = rs.getLong("id");
                    return l;
                }
                throw new SQLException("Failed to retrieve ID for " + value + " from " + tableName);
            }
        }
    }

    @Generated
    private DatabaseUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

