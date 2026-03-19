/*
 * Decompiled with CFR 0.152.
 */
package ac.grim.grimac.manager.violationdatabase;

import ac.grim.grimac.manager.violationdatabase.Violation;
import ac.grim.grimac.player.GrimPlayer;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface ViolationDatabase {
    public void connect() throws SQLException;

    public void logAlert(GrimPlayer var1, String var2, String var3, String var4, int var5);

    public int getLogCount(UUID var1);

    public List<Violation> getViolations(UUID var1, int var2, int var3);

    public void disconnect();
}

