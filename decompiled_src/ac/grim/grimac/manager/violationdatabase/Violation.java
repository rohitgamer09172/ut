/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.manager.violationdatabase;

import ac.grim.grimac.manager.violationdatabase.DatabaseUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record Violation(String server, UUID uuid, String checkName, String verbose, int vl, long createdAt, String grimVersion, String clientBrand, String clientVersion, String serverVersion) {
    public static List<Violation> fromResultSet(ResultSet resultSet) throws SQLException {
        ArrayList<Violation> violations = new ArrayList<Violation>();
        while (resultSet.next()) {
            String server = resultSet.getString("server_name");
            byte[] uuidBytes = resultSet.getBytes("uuid");
            UUID uuid = DatabaseUtils.bytesToUuid(uuidBytes);
            String checkName = resultSet.getString("check_name_string");
            String verbose = resultSet.getString("verbose");
            int vl = resultSet.getInt("vl");
            long createdAt = resultSet.getLong("created_at");
            String grimVersion = resultSet.getString("grim_version_string");
            String clientBrand = resultSet.getString("client_brand_string");
            String clientVersion = resultSet.getString("client_version_string");
            String serverVersion = resultSet.getString("server_version_string");
            violations.add(new Violation(server, uuid, checkName, verbose, vl, createdAt, grimVersion, clientBrand, clientVersion, serverVersion));
        }
        return violations;
    }
}

